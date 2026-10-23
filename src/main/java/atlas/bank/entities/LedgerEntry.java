package atlas.bank.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ledgerEntity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerEntry {
}
