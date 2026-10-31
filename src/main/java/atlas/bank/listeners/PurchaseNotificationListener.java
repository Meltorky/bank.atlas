package atlas.bank.listeners;

import atlas.bank.events.CardPurchaseCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PurchaseNotificationListener {

    private static final Logger log =
            LoggerFactory.getLogger(PurchaseNotificationListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePurchase(CardPurchaseCompletedEvent event) {

        log.info(
                "Preparing purchase confirmation for customer {}. " +
                        "Amount: {} {}",
                event.customerId(),
                event.amount(),
                event.currency()
        );
    }
}