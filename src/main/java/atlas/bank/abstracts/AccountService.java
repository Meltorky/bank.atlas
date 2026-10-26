package atlas.bank.abstracts;

import atlas.bank.DTOs.AccountCreateDTO;
import atlas.bank.entities.Account;
import atlas.bank.enums.AccountStatus;

public interface AccountService {

    Account createAccount(AccountCreateDTO dto);

    Account changeAccountStatus(Long number, AccountStatus status);

    Account closeAccount(Long number);

}
