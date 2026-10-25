package atlas.bank.repositories;

import atlas.bank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
}
