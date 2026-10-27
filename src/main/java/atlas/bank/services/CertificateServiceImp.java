package atlas.bank.services;

import atlas.bank.DTOs.CertificateCreateDTO;
import atlas.bank.entities.Account;
import atlas.bank.entities.Certificate;
import atlas.bank.entities.Customer;
import atlas.bank.enums.AccountStatus;
import atlas.bank.enums.AccountType;
import atlas.bank.repositories.AccountRepo;
import atlas.bank.repositories.CertificateRepo;
import atlas.bank.repositories.CustomerRepo;
import atlas.bank.shared.httpRequests.CustomResponseException;
import atlas.bank.strategy.ProfitCalculation.ProfitCalculationStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class CertificateServiceImp {

    private final ProfitCalculationStrategy profitCalculationStrategy;
    private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.01");

    private static final int TERM_YEARS = 3;

    private final CertificateRepo certificateRepo;
    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    public CertificateServiceImp(
            CertificateRepo certificateRepo,
            CustomerRepo customerRepo,
            AccountRepo accountRepo,
            @Qualifier("certificateProfitStrategy")
            ProfitCalculationStrategy profitCalculationStrategy) {

        this.certificateRepo = certificateRepo;
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.profitCalculationStrategy = profitCalculationStrategy;
    }

    @Transactional
    public Certificate createCertificate(CertificateCreateDTO dto) {

        Customer customer = customerRepo.findById(dto.customerId())
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Customer not found!"
                        )
                );

        Account checkingAccount = accountRepo.findById(
                        dto.checkingAccountNumber())
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Checking account not found!"
                        )
                );

        validateCheckingAccount(checkingAccount, customer);

        if (checkingAccount.getBalance()
                .compareTo(dto.principal()) < 0) {

            throw new IllegalStateException(
                    "Insufficient balance!"
            );
        }

        checkingAccount.setBalance(
                checkingAccount.getBalance()
                        .subtract(dto.principal())
        );

        LocalDate startDate = LocalDate.now();

        Certificate certificate = new Certificate();

        certificate.setPrincipal(dto.principal());
        certificate.setMonthlyRate(MONTHLY_RATE);
        certificate.setStartDate(startDate);
        certificate.setMaturityDate(
                startDate.plusYears(TERM_YEARS)
        );
        certificate.setCustomer(customer);
        certificate.setCheckingAccount(checkingAccount);

        accountRepo.save(checkingAccount);

        return certificateRepo.save(certificate);
    }

    // Helpers

    private void validateCheckingAccount(
            Account account,
            Customer customer) {

        if (!Objects.equals(account.getCustomer().getId(), customer.getId())) {
            throw new IllegalStateException(
                    "This account does not belong to the customer!"
            );
        }

        if (account.getAccountType() != AccountType.CHECKING) {

            throw new IllegalStateException(
                    "Certificate must be linked to a checking account!"
            );
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Checking account must be active!"
            );
        }
    }
}