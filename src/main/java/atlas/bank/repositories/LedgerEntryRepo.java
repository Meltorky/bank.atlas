package atlas.bank.repositories;

import atlas.bank.entities.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LedgerEntryRepo extends JpaRepository<LedgerEntry, UUID> {
}
