package atlas.bank.repositories;

import atlas.bank.entities.Account;
import atlas.bank.enums.AccountStatus;
import atlas.bank.enums.AccountType;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@NullMarked
@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Account> findByAccountTypeAndStatus(
            AccountType accountType,
            AccountStatus status
    );
}