package atlas.bank.DTOs;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CustomerUpdateDTO(

        @NotBlank(message = "First name is required")
        @Size(max = 50)
        String firstName,

        @NotBlank(message = "Middle name is required")
        @Size(max = 250)
        String middleName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50)
        String lastName,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^(?:(?:\\+20|0020)1[0125]\\d{8}|01[0125]\\d{8})$",
                message = "Enter a valid Egyptian mobile number"
        )
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "National ID is required")
        @Pattern(
                regexp = "^\\d{14}$",
                message = "Egyptian national ID must contain exactly 14 digits"
        )
        String nationalId,

        byte[] nationalIdCopy,

        byte[] signatureCopy

) {
    @AssertTrue(message = "Customer must be at least 18 years old")
    public boolean isAtLeast18() {
        return dateOfBirth != null
                && !dateOfBirth.isAfter(LocalDate.now().minusYears(18));
    }
}
