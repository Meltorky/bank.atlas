package atlas.bank.repositories;

import atlas.bank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);
}
