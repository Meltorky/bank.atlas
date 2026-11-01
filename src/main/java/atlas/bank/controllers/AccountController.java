package atlas.bank.controllers;

import atlas.bank.DTOs.AccountCreateDTO;
import atlas.bank.abstracts.AccountService;
import atlas.bank.entities.Account;
import atlas.bank.enums.AccountStatus;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody AccountCreateDTO dto) {

        Account account = accountService.createAccount(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }

    @PatchMapping("/{number}/status")
    public ResponseEntity<Account> changeAccountStatus(
            @PathVariable Long number,
            @RequestParam AccountStatus status) {

        Account account = accountService.changeAccountStatus(number, status);

        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{number}/close")
    public ResponseEntity<Account> closeAccount(
            @PathVariable Long number) {

        Account account = accountService.closeAccount(number);

        return ResponseEntity.ok(account);
    }
}
