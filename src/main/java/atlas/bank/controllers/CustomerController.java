package atlas.bank.controllers;

import atlas.bank.DTOs.CustomerCreateDTO;
import atlas.bank.DTOs.CustomerUpdateDTO;
import atlas.bank.abstracts.CustomerService;
import atlas.bank.entities.Customer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vi/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody CustomerCreateDTO dto) {

        Customer customer = customerService.createCustomer(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customer);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Customer> editCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerUpdateDTO dto) {

        Customer customer = customerService.editCustomer(id, dto);

        return ResponseEntity.ok(customer);
    }
}
