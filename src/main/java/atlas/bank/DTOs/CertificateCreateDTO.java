package atlas.bank.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CertificateCreateDTO(

        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Checking account number is required")
        Long checkingAccountNumber,

        @NotNull(message = "Principal is required")
        @Positive(message = "Principal must be greater than zero")
        BigDecimal principal

) {}