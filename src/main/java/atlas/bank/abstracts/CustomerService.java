package atlas.bank.abstracts;

import atlas.bank.DTOs.CustomerCreateDTO;
import atlas.bank.DTOs.CustomerUpdateDTO;
import atlas.bank.entities.Customer;

import java.util.UUID;

public interface CustomerService {
    Customer createCustomer(CustomerCreateDTO dto);
    Customer editCustomer(UUID id, CustomerUpdateDTO dto);
}
