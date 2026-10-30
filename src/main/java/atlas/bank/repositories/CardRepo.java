package atlas.bank.repositories;

import atlas.bank.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRepo extends JpaRepository<Card, UUID> {
}