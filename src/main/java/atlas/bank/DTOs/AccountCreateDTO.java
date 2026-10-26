
package atlas.bank.DTOs;

import atlas.bank.enums.AccountType;
import atlas.bank.enums.Currency;
import jakarta.validation.constraints.NotNull;

public record AccountCreateDTO(

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Currency is required")
        Currency currency,

        @NotNull(message = "Customer ID is required")
        java.util.UUID customerId

) {
}
