package atlas.bank.entities;

import atlas.bank.enums.CardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "debit_card")
@Getter
@Setter
@NoArgsConstructor
public class DebitCard extends Card {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

    @Override
    public CardType getCardType() {
        return CardType.DEBIT;
    }
}