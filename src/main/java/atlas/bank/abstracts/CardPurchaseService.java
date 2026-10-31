package atlas.bank.abstracts;

import atlas.bank.DTOs.CardPurchaseDTO;
import atlas.bank.DTOs.CardPurchaseResponseDTO;

import java.util.UUID;

public interface CardPurchaseService {

    CardPurchaseResponseDTO purchaseWithDebitCard(
            UUID cardId,
            CardPurchaseDTO dto
    );

    CardPurchaseResponseDTO purchaseWithCreditCard(
            UUID cardId,
            CardPurchaseDTO dto
    );
}
