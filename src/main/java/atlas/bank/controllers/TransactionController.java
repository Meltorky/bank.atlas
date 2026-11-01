package atlas.bank.controllers;

import atlas.bank.DTOs.TransactionRequestDTO;
import atlas.bank.abstracts.TransactionService;
import atlas.bank.entities.Transaction;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @Valid @RequestBody TransactionRequestDTO dto) {

        Transaction transaction = transactionService.deposit(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @Valid @RequestBody TransactionRequestDTO dto) {

        Transaction transaction = transactionService.withdraw(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    @PostMapping("/internal-transfer")
    public ResponseEntity<Transaction> internalTransfer(
            @Valid @RequestBody TransactionRequestDTO dto) {

        Transaction transaction = transactionService.internalTransfer(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    @PostMapping("/external-transfer")
    public ResponseEntity<Transaction> externalTransfer(
            @Valid @RequestBody TransactionRequestDTO dto) {

        Transaction transaction = transactionService.externalTransfer(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
}
