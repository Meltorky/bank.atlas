package atlas.bank.factories;

import atlas.bank.entities.Account;
import atlas.bank.entities.Card;
import atlas.bank.entities.CreditCard;
import atlas.bank.entities.Customer;
import atlas.bank.entities.DebitCard;
import atlas.bank.enums.CardStatus;
import atlas.bank.enums.CardType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class CardFactory {

    public Card createCard(
            CardType cardType,
            Customer customer,
            Account account,
            BigDecimal creditLimit) {

        return switch (cardType) {
            case DEBIT -> createDebitCard(customer, account);
            case CREDIT -> createCreditCard(customer, creditLimit);
        };
    }

    private DebitCard createDebitCard(
            Customer customer,
            Account account) {

        DebitCard card = new DebitCard();
        initializeCard(card, customer);

        card.setAccount(account);

        return card;
    }

    private CreditCard createCreditCard(
            Customer customer,
            BigDecimal creditLimit) {

        CreditCard card = new CreditCard();
        initializeCard(card, customer);

        card.setCreditLimit(creditLimit);
        card.setOutstandingBalance(BigDecimal.ZERO);

        return card;
    }

    private void initializeCard(Card card, Customer customer) {
        card.setCardReference("CARD-" + UUID.randomUUID());
        card.setCustomer(customer);
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(LocalDate.now().plusYears(5));
    }
}