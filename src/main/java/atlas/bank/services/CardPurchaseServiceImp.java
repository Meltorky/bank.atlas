package atlas.bank.services;

import atlas.bank.DTOs.CardPurchaseDTO;
import atlas.bank.DTOs.CardPurchaseResponseDTO;
import atlas.bank.entities.*;
import atlas.bank.enums.AccountStatus;
import atlas.bank.enums.AccountType;
import atlas.bank.enums.CardStatus;
import atlas.bank.enums.CardType;
import atlas.bank.enums.Currency;
import atlas.bank.enums.LedgerEntryType;
import atlas.bank.enums.TransactionStatus;
import atlas.bank.enums.TransactionType;
import atlas.bank.events.CardPurchaseCompletedEvent;
import atlas.bank.repositories.AccountRepo;
import atlas.bank.repositories.CreditCardRepo;
import atlas.bank.repositories.DebitCardRepo;
import atlas.bank.repositories.LedgerEntryRepo;
import atlas.bank.repositories.TransactionRepo;
import atlas.bank.abstracts.CardPurchaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class CardPurchaseServiceImp implements CardPurchaseService {

    private final DebitCardRepo debitCardRepo;
    private final CreditCardRepo creditCardRepo;
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final LedgerEntryRepo ledgerEntryRepo;
    private final ApplicationEventPublisher eventPublisher;

    public CardPurchaseServiceImp(
            DebitCardRepo debitCardRepo,
            CreditCardRepo creditCardRepo,
            AccountRepo accountRepo,
            TransactionRepo transactionRepo,
            LedgerEntryRepo ledgerEntryRepo,
            ApplicationEventPublisher eventPublisher
    ) {
        this.debitCardRepo = debitCardRepo;
        this.creditCardRepo = creditCardRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.ledgerEntryRepo = ledgerEntryRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CardPurchaseResponseDTO purchaseWithDebitCard(
            UUID cardId,
            CardPurchaseDTO dto
    ) {
        // 1. Find and lock the debit card
        DebitCard card = debitCardRepo.findById(cardId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Debit card not found"));

        validateCard(card.getStatus(), card.getExpiryDate());

        // 2. Find and lock the linked account
        Account account = accountRepo.findById(
                card.getAccount().getNumber()
        ).orElseThrow(() ->
                new EntityNotFoundException("Account not found"));

        // 3. Validate the account
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }

        if (account.getAccountType() != AccountType.CHECKING) {
            throw new IllegalStateException(
                    "Debit card must be linked to a checking account");
        }

        // 4. Check available balance
        BigDecimal amount = dto.amount();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient account balance");
        }

        // 5. Deduct money from the account
        account.setBalance(account.getBalance().subtract(amount));
        accountRepo.save(account);

        // 6. Create the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(account.getCurrency());
        transaction.setTransactionType(TransactionType.DEBIT_CARD_PURCHASE);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        transaction = transactionRepo.save(transaction);

        // 7. Create the ledger entry
        LedgerEntry entry = new LedgerEntry();
        entry.setAmount(amount);
        entry.setEntryType(LedgerEntryType.DEBIT);
        entry.setAccount(account);
        entry.setTransaction(transaction);

        ledgerEntryRepo.save(entry);

        // 8. Publish the event
        publishPurchaseEvent(
                transaction,
                card.getCustomer().getId(),
                CardType.DEBIT,
                account.getCurrency()
        );

        return mapResponse(transaction, CardType.DEBIT);
    }

    @Override
    @Transactional
    public CardPurchaseResponseDTO purchaseWithCreditCard(
            UUID cardId,
            CardPurchaseDTO dto
    ) {
        // 1. Find and lock the credit card
        CreditCard card = creditCardRepo.findById(cardId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Credit card not found"));

        validateCard(card.getStatus(), card.getExpiryDate());

        // 2. Credit card purchases currently use EGP
        BigDecimal amount = dto.amount();

        // 3. Calculate available credit
        BigDecimal availableCredit =
                card.getCreditLimit().subtract(card.getOutstandingBalance());

        if (availableCredit.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient available credit");
        }

        // 4. Increase outstanding balance
        card.setOutstandingBalance(
                card.getOutstandingBalance().add(amount)
        );

        creditCardRepo.save(card);

        // 5. Create the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(Currency.EGP);
        transaction.setTransactionType(TransactionType.CREDIT_CARD_PURCHASE);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        transaction = transactionRepo.save(transaction);

        // 6. Publish the event
        publishPurchaseEvent(
                transaction,
                card.getCustomer().getId(),
                CardType.CREDIT,
                Currency.EGP
        );

        return mapResponse(transaction, CardType.CREDIT);
    }

    private void validateCard(CardStatus status, LocalDate expiryDate) {
        if (status != CardStatus.ACTIVE) {
            throw new IllegalStateException("Card is not active");
        }

        if (expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Card has expired");
        }
    }

    private void publishPurchaseEvent(
            Transaction transaction,
            UUID customerId,
            CardType cardType,
            Currency currency
    ) {
        CardPurchaseCompletedEvent event =
                new CardPurchaseCompletedEvent(
                        transaction.getId(),
                        customerId,
                        cardType,
                        transaction.getAmount(),
                        currency
                );

        eventPublisher.publishEvent(event);
    }

    private CardPurchaseResponseDTO mapResponse(
            Transaction transaction,
            CardType cardType
    ) {
        return new CardPurchaseResponseDTO(
                transaction.getId(),
                cardType,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionStatus(),
                transaction.getDateTime()
        );
    }
}