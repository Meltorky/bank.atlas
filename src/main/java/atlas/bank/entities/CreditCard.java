package atlas.bank.entities;

import atlas.bank.enums.CardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_card")
@Getter
@Setter
@NoArgsConstructor
public class CreditCard extends Card {

    @Column(name = "credit_limit", nullable = false)
    private BigDecimal creditLimit;

    @Column(name = "outstanding_balance", nullable = false)
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Override
    public CardType getCardType() {
        return CardType.CREDIT;
    }
}