package atlas.bank.repositories;

import atlas.bank.entities.CreditCard;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface CreditCardRepo extends JpaRepository<CreditCard, UUID> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CreditCard> findById(UUID id);
}