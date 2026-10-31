package atlas.bank.listeners;

import atlas.bank.events.CardPurchaseCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PurchaseAuditListener {

    private static final Logger log =
            LoggerFactory.getLogger(PurchaseAuditListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePurchase(CardPurchaseCompletedEvent event) {

        log.info(
                "Card purchase completed. Transaction ID: {}, " +
                        "Customer ID: {}, Card Type: {}, Amount: {} {}",
                event.transactionId(),
                event.customerId(),
                event.cardType(),
                event.amount(),
                event.currency()
        );
    }
}