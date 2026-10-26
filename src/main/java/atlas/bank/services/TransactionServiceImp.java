package atlas.bank.services;

import atlas.bank.DTOs.TransactionRequestDTO;
import atlas.bank.entities.Account;
import atlas.bank.entities.LedgerEntry;
import atlas.bank.entities.Transaction;
import atlas.bank.enums.AccountStatus;
import atlas.bank.enums.LedgerEntryType;
import atlas.bank.enums.TransactionStatus;
import atlas.bank.enums.TransactionType;
import atlas.bank.repositories.AccountRepo;
import atlas.bank.repositories.LedgerEntryRepo;
import atlas.bank.repositories.TransactionRepo;
import atlas.bank.shared.httpRequests.CustomResponseException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImp {

    private final TransactionRepo transactionRepo;
    private final LedgerEntryRepo ledgerEntryRepo;
    private final AccountRepo accountRepo;

    public TransactionServiceImp(
            TransactionRepo transactionRepo,
            LedgerEntryRepo ledgerEntryRepo,
            AccountRepo accountRepo) {

        this.transactionRepo = transactionRepo;
        this.ledgerEntryRepo = ledgerEntryRepo;
        this.accountRepo = accountRepo;
    }


    // =========================
    // Deposit
    // =========================

    @Transactional
    public Transaction deposit(TransactionRequestDTO dto) {

        validateAmount(dto.amount());

        Account account = getAccountForUpdate(dto.accountNumber());

        checkActive(account);

        account.setBalance(
                account.getBalance().add(dto.amount())
        );

        Transaction transaction = createTransaction(
                dto.amount(),
                account,
                TransactionType.DEPOSIT
        );

        createLedgerEntry(
                dto.amount(),
                LedgerEntryType.CREDIT,
                account,
                transaction
        );

        accountRepo.save(account);

        return transactionRepo.save(transaction);
    }


    // =========================
    // Withdrawal
    // =========================

    @Transactional
    public Transaction withdraw(TransactionRequestDTO dto) {

        validateAmount(dto.amount());

        Account account = getAccountForUpdate(dto.accountNumber());

        checkActive(account);

        checkSufficientBalance(account, dto.amount());

        account.setBalance(
                account.getBalance().subtract(dto.amount())
        );

        Transaction transaction = createTransaction(
                dto.amount(),
                account,
                TransactionType.WITHDRAWAL
        );

        createLedgerEntry(
                dto.amount(),
                LedgerEntryType.DEBIT,
                account,
                transaction
        );

        accountRepo.save(account);

        return transactionRepo.save(transaction);
    }


    // =========================
    // Internal Transfer
    // =========================

    @Transactional
    public Transaction internalTransfer(TransactionRequestDTO dto) {

        validateAmount(dto.amount());

        if (dto.accountNumber().equals(dto.destinationAccountNumber())) {
            throw new IllegalArgumentException(
                    "Source and destination accounts cannot be the same!"
            );
        }

        Account sourceAccount =
                getAccountForUpdate(dto.accountNumber());

        Account destinationAccount =
                getAccountForUpdate(dto.destinationAccountNumber());

        checkActive(sourceAccount);
        checkActive(destinationAccount);

        checkSameCurrency(sourceAccount, destinationAccount);

        checkSufficientBalance(
                sourceAccount,
                dto.amount()
        );

        // Remove money from source
        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(dto.amount())
        );

        // Add money to destination
        destinationAccount.setBalance(
                destinationAccount.getBalance().add(dto.amount())
        );

        Transaction transaction = createTransaction(
                dto.amount(),
                sourceAccount,
                TransactionType.INTERNAL_TRANSFER
        );

        // Source → DEBIT
        createLedgerEntry(
                dto.amount(),
                LedgerEntryType.DEBIT,
                sourceAccount,
                transaction
        );

        // Destination → CREDIT
        createLedgerEntry(
                dto.amount(),
                LedgerEntryType.CREDIT,
                destinationAccount,
                transaction
        );

        accountRepo.save(sourceAccount);
        accountRepo.save(destinationAccount);

        return transactionRepo.save(transaction);
    }


    // =========================
    // External Transfer
    // =========================

    @Transactional
    public Transaction externalTransfer(TransactionRequestDTO dto) {

        validateAmount(dto.amount());

        Account account =
                getAccountForUpdate(dto.accountNumber());

        checkActive(account);

        checkSufficientBalance(
                account,
                dto.amount()
        );

        account.setBalance(
                account.getBalance().subtract(dto.amount())
        );

        Transaction transaction = createTransaction(
                dto.amount(),
                account,
                TransactionType.EXTERNAL_TRANSFER
        );

        createLedgerEntry(
                dto.amount(),
                LedgerEntryType.DEBIT,
                account,
                transaction
        );

        accountRepo.save(account);

        return transactionRepo.save(transaction);
    }


    // =========================
    // Helpers
    // =========================

    private Account getAccountForUpdate(Long number) {

        return accountRepo.findById(number)
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Account not found!"
                        )
                );
    }


    private void checkActive(Account account) {

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Account is " + account.getStatus()
            );
        }
    }


    private void checkSufficientBalance(
            Account account,
            BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {

            throw new IllegalStateException(
                    "Insufficient balance!"
            );
        }
    }


    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero!"
            );
        }
    }


    private void checkSameCurrency(
            Account source,
            Account destination) {

        if (source.getCurrency() != destination.getCurrency()) {

            throw new IllegalArgumentException(
                    "Accounts must have the same currency!"
            );
        }
    }


    private Transaction createTransaction(
            BigDecimal amount,
            Account account,
            TransactionType type) {

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setCurrency(account.getCurrency());
        transaction.setTransactionType(type);
        transaction.setTransactionStatus(
                TransactionStatus.COMPLETED
        );

        return transaction;
    }


    private void createLedgerEntry(
            BigDecimal amount,
            LedgerEntryType entryType,
            Account account,
            Transaction transaction) {

        LedgerEntry entry = new LedgerEntry();

        entry.setAmount(amount);
        entry.setEntryType(entryType);
        entry.setAccount(account);
        entry.setTransaction(transaction);

        ledgerEntryRepo.save(entry);
    }
}