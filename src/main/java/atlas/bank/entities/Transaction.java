package atlas.bank.entities;

import atlas.bank.enums.Currency;
import atlas.bank.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency = Currency.EGP;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @OneToMany(mappedBy = "transaction")
    private List<LedgerEntry> ledgerEntryList = new ArrayList<>();
}
