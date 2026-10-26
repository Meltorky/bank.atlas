package atlas.bank.services;

import atlas.bank.DTOs.CustomerCreateDTO;
import atlas.bank.DTOs.CustomerUpdateDTO;
import atlas.bank.entities.Customer;
import atlas.bank.integration.Image.ImageStorage;
import atlas.bank.repositories.CustomerRepo;
import atlas.bank.shared.HttpRequest.CustomResponseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImp {

    private final CustomerRepo customerRepo;
    private final ImageStorage imageStorageAws;
    private final ImageStorage imageStorageAzure;

    public CustomerServiceImp(
            CustomerRepo customerRepo,
            @Qualifier("awsImageAdapter") ImageStorage imageStorageAws,
            @Qualifier("azureImageAdapter") ImageStorage imageStorageAzure) {

        this.customerRepo = customerRepo;
        this.imageStorageAws = imageStorageAws;
        this.imageStorageAzure = imageStorageAzure;
    }

    // create customer
    public Customer createCustomer(CustomerCreateDTO dto){

        if(customerRepo.existsByEmail(dto.email()) == true)
            throw CustomResponseException.resourceDuplicateData("Email is already registered, choose another one!");

        if(customerRepo.existsByPhoneNumber(dto.phoneNumber()) == true)
            throw CustomResponseException.resourceDuplicateData("Phone Number is already registered, choose another one!");

        Customer newCustomer = new Customer();
        newCustomer.setFirstName(dto.firstName());
        newCustomer.setMiddleName(dto.middleName());
        newCustomer.setLastName(dto.lastName());
        newCustomer.setEmail(dto.email());
        newCustomer.setAddress(dto.address());
        newCustomer.setDateOfBirth(dto.dateOfBirth());
        newCustomer.setNationalId(dto.nationalId());
        newCustomer.setNationalIdCopy(imageStorageAws.upload(dto.nationalIdCopy()));
        newCustomer.setSignatureCopy(imageStorageAzure.upload(dto.signatureCopy()));

        return customerRepo.save(newCustomer);
    }

    // edit customer
    public Customer editCustomer(UUID id, CustomerUpdateDTO dto) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Customer not found!"
                        )
                );

        if (customerRepo.existsByEmailAndIdNot(dto.email(), id))
            throw CustomResponseException.resourceDuplicateData(
                    "Email is already registered, choose another one!"
            );

        if (customerRepo.existsByPhoneNumberAndIdNot(dto.phoneNumber(), id))
            throw CustomResponseException.resourceDuplicateData(
                    "Phone Number is already registered, choose another one!"
            );

        customer.setFirstName(dto.firstName());
        customer.setMiddleName(dto.middleName());
        customer.setLastName(dto.lastName());
        customer.setPhoneNumber(dto.phoneNumber());
        customer.setEmail(dto.email());
        customer.setAddress(dto.address());
        customer.setDateOfBirth(dto.dateOfBirth());
        customer.setNationalId(dto.nationalId());

        if (dto.nationalIdCopy() != null) {
            customer.setNationalIdCopy(
                    imageStorageAws.upload(dto.nationalIdCopy())
            );
        }

        if (dto.signatureCopy() != null) {
            customer.setSignatureCopy(
                    imageStorageAzure.upload(dto.signatureCopy())
            );
        }

        return customerRepo.save(customer);
    }


    // delete customer
    public void deleteCustomer(UUID id) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() ->
                        CustomResponseException.resourceNotFound(
                                "Customer not found!"
                        )
                );

        customerRepo.delete(customer);
    }
}
