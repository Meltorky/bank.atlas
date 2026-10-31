package atlas.bank.DTOs;

import atlas.bank.enums.CardType;
import atlas.bank.enums.Currency;
import atlas.bank.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CardPurchaseResponseDTO(
        UUID transactionId,
        CardType cardType,
        BigDecimal amount,
        Currency currency,
        TransactionStatus status,
        LocalDateTime dateTime
) {}