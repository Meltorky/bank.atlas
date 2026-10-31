package atlas.bank.events;

import atlas.bank.enums.CardType;
import atlas.bank.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record CardPurchaseCompletedEvent(
        UUID transactionId,
        UUID customerId,
        CardType cardType,
        BigDecimal amount,
        Currency currency
) {}