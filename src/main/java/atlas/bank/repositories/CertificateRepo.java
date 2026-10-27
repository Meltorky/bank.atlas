package atlas.bank.repositories;

import atlas.bank.entities.Certificate;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@NullMarked
@Repository
public interface CertificateRepo extends JpaRepository<Certificate, UUID> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Certificate> findById(UUID id);
}