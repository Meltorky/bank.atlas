package atlas.bank.controllers;

import atlas.bank.DTOs.CardPurchaseDTO;
import atlas.bank.DTOs.CardPurchaseResponseDTO;
import atlas.bank.abstracts.CardPurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
public class CardPurchaseController {

    private final CardPurchaseService cardPurchaseService;

    public CardPurchaseController(CardPurchaseService cardPurchaseService) {
        this.cardPurchaseService = cardPurchaseService;
    }

    @PostMapping("/debit/{cardId}/purchases")
    public ResponseEntity<CardPurchaseResponseDTO> debitPurchase(
            @PathVariable UUID cardId,
            @Valid @RequestBody CardPurchaseDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardPurchaseService.purchaseWithDebitCard(cardId, dto));
    }

    @PostMapping("/credit/{cardId}/purchases")
    public ResponseEntity<CardPurchaseResponseDTO> creditPurchase(
            @PathVariable UUID cardId,
            @Valid @RequestBody CardPurchaseDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardPurchaseService.purchaseWithCreditCard(cardId, dto));
    }
}