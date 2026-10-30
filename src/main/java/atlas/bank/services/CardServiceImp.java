package atlas.bank.services;

import atlas.bank.DTOs.CardCreateDTO;
import atlas.bank.DTOs.CardResponseDTO;
import atlas.bank.abstracts.CardService;
import atlas.bank.entities.Account;
import atlas.bank.entities.Card;
import atlas.bank.entities.Customer;
import atlas.bank.enums.AccountStatus;
import atlas.bank.enums.AccountType;
import atlas.bank.enums.CardType;
import atlas.bank.factories.CardFactory;
import atlas.bank.repositories.AccountRepo;
import atlas.bank.repositories.CardRepo;
import atlas.bank.repositories.CustomerRepo;
import atlas.bank.shared.httpRequests.CustomResponseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CardServiceImp implements CardService {

    private final CardRepo cardRepo;
    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final CardFactory cardFactory;

    public CardServiceImp(
            CardRepo cardRepo,
            CustomerRepo customerRepo,
            AccountRepo accountRepo,
            CardFactory cardFactory) {

        this.cardRepo = cardRepo;
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.cardFactory = cardFactory;
    }

    @Override
    @Transactional
    public CardResponseDTO createCard(CardCreateDTO dto) {

        Customer customer = customerRepo.findById(dto.customerId())
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Customer not found!"
                        )
                );

        Account account = null;

        if (dto.cardType() == CardType.DEBIT) {
            if (dto.checkingAccountNumber() == null) {
                throw new IllegalArgumentException(
                        "Checking account number is required for a debit card!"
                );
            }

            account = accountRepo.findById(dto.checkingAccountNumber())
                    .orElseThrow(() ->
                            CustomResponseException.resourceNotFound(
                                    "Checking account not found!"
                            )
                    );

            if (!account.getCustomer().getId().equals(customer.getId())) {
                throw new IllegalArgumentException(
                        "The account does not belong to this customer!"
                );
            }

            if (account.getAccountType() != AccountType.CHECKING) {
                throw new IllegalArgumentException(
                        "A debit card must be linked to a checking account!"
                );
            }

            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new IllegalArgumentException(
                        "The checking account is not active!"
                );
            }
        }

        if (dto.cardType() == CardType.CREDIT) {
            if (dto.creditLimit() == null
                    || dto.creditLimit().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "A positive credit limit is required!"
                );
            }
        }

        Card card = cardFactory.createCard(
                dto.cardType(),
                customer,
                account,
                dto.creditLimit()
        );

        Card savedCard = cardRepo.save(card);

        return toResponse(savedCard);
    }

    private CardResponseDTO toResponse(Card card) {

        Long linkedAccountNumber = null;
        BigDecimal creditLimit = null;
        BigDecimal outstandingBalance = null;

        if (card instanceof atlas.bank.entities.DebitCard debitCard) {
            linkedAccountNumber = debitCard.getAccount().getNumber();
        }

        if (card instanceof atlas.bank.entities.CreditCard creditCard) {
            creditLimit = creditCard.getCreditLimit();
            outstandingBalance = creditCard.getOutstandingBalance();
        }

        return new CardResponseDTO(
                card.getId(),
                card.getCardReference(),
                card.getCardType(),
                card.getStatus(),
                card.getExpiryDate(),
                card.getCustomer().getId(),
                linkedAccountNumber,
                creditLimit,
                outstandingBalance
        );
    }
}