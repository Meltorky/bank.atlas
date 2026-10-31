Debit-card purchase
Example: customer purchases something for EGP 500.
1. Find the debit card.
2. Check that the card is active and not expired.
3. Check that its Checking Account is active.
4. Check that the account has enough money.
5. Deduct EGP 500 from the account.
6. Create a completed transaction.
7. Create a DEBIT LedgerEntry for EGP 500.
8. Publish a purchase-completed event.

Credit-card purchase
Example: customer purchases something for EGP 500.
1. Find the credit card.
2. Check that the card is active and not expired.
3. Calculate available credit.
4. Reject the purchase if the limit would be exceeded.
5. Increase the outstanding balance by EGP 500.
6. Create a completed transaction.
7. Publish a purchase-completed event.


PurchaseAuditListener vs PurchaseNotificationListener
PurchaseAuditListener — for the bank

Its job is to maintain an internal record of what happened.

For example:
- Customer ID: 123
- Transaction ID: abc-456
- Card type: DEBIT
- Amount: EGP 500
- Event: Purchase completed
- Timestamp: When it happened
The bank uses this information for:
- Investigating suspicious activity.
- Reviewing transaction history.
- Internal auditing.
- Troubleshooting disputes.
Think of it as the bank's internal evidence.

PurchaseNotificationListener — for the customer

Its job is to communicate with the customer.

For example, after a successful purchase:
Your debit card ending in 1234 was used for EGP 500. Your purchase was successful.

It could eventually:
- Send an SMS.
- Send an email.
- Send a push notification.
- Notify the customer through the banking application.
Think of it as customer communication.
2. Why do we need two listeners?
Imagine tomorrow you want to add another feature: detecting suspicious purchases.

You could create:

FraudDetectionListener

Now the same purchase event has three independent listeners:
1. Audit listener → records the purchase.
2. Notification listener → informs the customer.
3. Fraud detection listener → analyzes the purchase.
The purchase service doesn't need to change!

That's the beauty of Observer.
