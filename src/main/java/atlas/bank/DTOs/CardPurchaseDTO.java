package atlas.bank.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CardPurchaseDTO(
        @NotNull
        @Positive
        BigDecimal amount
) {}