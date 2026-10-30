package atlas.bank.controllers;

import atlas.bank.DTOs.CardCreateDTO;
import atlas.bank.DTOs.CardResponseDTO;
import atlas.bank.abstracts.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(
            @Valid @RequestBody CardCreateDTO dto) {

        CardResponseDTO response = cardService.createCard(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}