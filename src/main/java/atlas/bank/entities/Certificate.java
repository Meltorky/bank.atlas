package atlas.bank.entities;

import atlas.bank.enums.CertificateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certificate")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false) // value $ will be on hold
    private BigDecimal principal;

    @Column(name = "monthly_rate", nullable = false)
    private BigDecimal monthlyRate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "maturity_date", nullable = false) // ending date
    private LocalDate maturityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CertificateStatus status = CertificateStatus.ACTIVE;

    @Column(name = "last_profit_paid_date")
    private LocalDate lastProfitPaidDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checking_account_id", nullable = false)
    private Account checkingAccount;
}