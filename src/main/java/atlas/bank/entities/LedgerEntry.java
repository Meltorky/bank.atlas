package atlas.bank.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ledger_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerEntry {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonProperty("customerId")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonProperty("transactionId")
    private Transaction transaction;

    // overload the get methods
    public UUID getCustomer() {return customer.getId();}
    public UUID getTransaction() {return transaction.getId();}
}
