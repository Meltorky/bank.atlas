package atlas.bank.DTOs;

import atlas.bank.enums.CardType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CardCreateDTO(

        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Card type is required")
        CardType cardType,

        Long checkingAccountNumber,

        @Positive(message = "Credit limit must be greater than zero")
        BigDecimal creditLimit

) {}