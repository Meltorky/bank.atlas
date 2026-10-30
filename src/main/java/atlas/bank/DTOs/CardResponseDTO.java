package atlas.bank.DTOs;

import atlas.bank.enums.CardStatus;
import atlas.bank.enums.CardType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardResponseDTO(
        UUID id,
        String cardReference,
        CardType cardType,
        CardStatus status,
        LocalDate expiryDate,
        UUID customerId,
        Long linkedAccountNumber,
        BigDecimal creditLimit,
        BigDecimal outstandingBalance
) {}