package atlas.bank.services;

import atlas.bank.entities.Account;
import atlas.bank.entities.Certificate;
import atlas.bank.entities.LedgerEntry;
import atlas.bank.entities.Transaction;
import atlas.bank.enums.AccountStatus;
import atlas.bank.enums.AccountType;
import atlas.bank.enums.CertificateStatus;
import atlas.bank.enums.LedgerEntryType;
import atlas.bank.enums.TransactionStatus;
import atlas.bank.enums.TransactionType;
import atlas.bank.repositories.AccountRepo;
import atlas.bank.repositories.CertificateRepo;
import atlas.bank.repositories.LedgerEntryRepo;
import atlas.bank.repositories.TransactionRepo;
import atlas.bank.strategy.ProfitCalculation.ProfitCalculationStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;

@Service
public class MonthlyProfitServiceImp {

    private final AccountRepo accountRepo;
    private final CertificateRepo certificateRepo;
    private final TransactionRepo transactionRepo;
    private final LedgerEntryRepo ledgerEntryRepo;

    private final ProfitCalculationStrategy savingsProfitStrategy;
    private final ProfitCalculationStrategy certificateProfitStrategy;

    public MonthlyProfitServiceImp(
            AccountRepo accountRepo,
            CertificateRepo certificateRepo,
            TransactionRepo transactionRepo,
            LedgerEntryRepo ledgerEntryRepo,
            @Qualifier("savingsProfitStrategy")
            ProfitCalculationStrategy savingsProfitStrategy,
            @Qualifier("certificateProfitStrategy")
            ProfitCalculationStrategy certificateProfitStrategy) {

        this.accountRepo = accountRepo;
        this.certificateRepo = certificateRepo;
        this.transactionRepo = transactionRepo;
        this.ledgerEntryRepo = ledgerEntryRepo;
        this.savingsProfitStrategy = savingsProfitStrategy;
        this.certificateProfitStrategy = certificateProfitStrategy;
    }

    @Transactional
    public void processMonthlyProfits() {

        YearMonth month = YearMonth.now(ZoneId.of("Africa/Cairo"));
        LocalDate monthStart = month.atDay(1);

        processSavingsProfits(month);

        for (Certificate certificate :
                certificateRepo.findByStatus(CertificateStatus.ACTIVE)) {

            // Don't pay in the same calendar month the certificate starts.
            if (!certificate.getStartDate().isBefore(monthStart)) {
                continue;
            }

            // Don't pay twice for the same month.
            if (alreadyPaidThisMonth(
                    certificate.getLastProfitPaidDate(), month)) {
                continue;
            }

            // Skip certificates whose maturity date has passed.
            if (certificate.getMaturityDate().isBefore(monthStart)) {
                continue;
            }

            Account checkingAccount = accountRepo.findById(
                            certificate.getCheckingAccount().getNumber())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Certificate checking account not found!"
                            )
                    );

            if (checkingAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new IllegalStateException(
                        "Cannot pay certificate profit: checking account is not active!"
                );
            }

            BigDecimal profit = certificateProfitStrategy.calculateProfit(
                    certificate.getPrincipal()
            );

            checkingAccount.setBalance(
                    checkingAccount.getBalance().add(profit)
            );

            accountRepo.save(checkingAccount);

            saveProfitRecords(
                    checkingAccount,
                    profit,
                    TransactionType.CERTIFICATE_PROFIT
            );

            certificate.setLastProfitPaidDate(month.atEndOfMonth());
            certificateRepo.save(certificate);
        }
    }

    private void processSavingsProfits(YearMonth month) {

        for (Account account : accountRepo
                .findByAccountTypeAndStatus(
                        AccountType.SAVING,
                        AccountStatus.ACTIVE)) {

            if (alreadyPaidThisMonth(
                    account.getLastProfitPaidDate(), month)) {
                continue;
            }

            BigDecimal profit = savingsProfitStrategy.calculateProfit(
                    account.getBalance()
            );

            if (profit.compareTo(BigDecimal.ZERO) <= 0) {
                account.setLastProfitPaidDate(month.atEndOfMonth());
                accountRepo.save(account);
                continue;
            }

            account.setBalance(account.getBalance().add(profit));
            account.setLastProfitPaidDate(month.atEndOfMonth());

            accountRepo.save(account);

            saveProfitRecords(
                    account,
                    profit,
                    TransactionType.SAVINGS_PROFIT
            );
        }
    }

    private boolean alreadyPaidThisMonth(
            LocalDate lastPaidDate,
            YearMonth month) {

        return lastPaidDate != null
                && !YearMonth.from(lastPaidDate).isBefore(month);
    }

    private void saveProfitRecords(
            Account account,
            BigDecimal profit,
            TransactionType transactionType) {

        Transaction transaction = new Transaction();
        transaction.setAmount(profit);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setCurrency(account.getCurrency());
        transaction.setTransactionType(transactionType);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        // Save the transaction first.
        transaction = transactionRepo.save(transaction);

        LedgerEntry entry = new LedgerEntry();
        entry.setAccount(account);
        entry.setTransaction(transaction);
        entry.setAmount(profit);
        entry.setEntryType(LedgerEntryType.CREDIT);

        ledgerEntryRepo.save(entry);
    }
}