
package atlas.bank.services;

import atlas.bank.DTOs.AccountCreateDTO;
import atlas.bank.abstracts.AccountService;
import atlas.bank.entities.Account;
import atlas.bank.entities.Customer;
import atlas.bank.repositories.AccountRepo;
import atlas.bank.repositories.CustomerRepo;
import atlas.bank.enums.AccountStatus;
import atlas.bank.shared.helpers.AccountNumberGenerator;
import atlas.bank.shared.httpRequests.CustomResponseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;

    public AccountServiceImp(
            AccountRepo accountRepo,
            CustomerRepo customerRepo,
            AccountNumberGenerator accountNumberGenerator) {

        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    // create account

    @Transactional
    public Account createAccount(AccountCreateDTO dto) {

        Customer customer = customerRepo.findById(dto.customerId())
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Customer not found!"
                        )
                );

        Account account = new Account();

        account.setAccountType(dto.accountType());
        account.setCurrency(dto.currency());
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE
        );

        return accountRepo.save(account);
    }

    // Change account status

    @Transactional
    public Account changeAccountStatus(Long number, AccountStatus status) {

        Account account = getAccountById(number);
        account.setStatus(status);
        return accountRepo.save(account);
    }

    // Close account

    @Transactional
    public Account closeAccount(Long number) {

        Account account = getActiveAccount(number);

        account.setStatus(AccountStatus.CLOSED);

        return accountRepo.save(account);
    }

    // Private helpers

    private Account getActiveAccount(Long number) {

        Account account = getAccountById(number);

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Your account is already " + account.getStatus().toString() + "!"
            );
        }

        return account;
    }

    private Account getAccountById(Long number){

        return accountRepo.findById(number)
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Account not found!"
                        )
                );
    }
}
