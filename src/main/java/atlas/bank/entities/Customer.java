package atlas.bank.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "national_id", nullable = false, unique = true,length = 100)
    private String nationalId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", nullable = false, length = 250)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone_number", unique = true,nullable = false, length = 25)
    private String phoneNumber;

    @Column(name = "email", unique = true,nullable = false, length = 100)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "national_id_copy_url", nullable = false)
    private String nationalIdCopy;

    @Column(name = "signature_copy_url", nullable = false)
    private String signatureCopy ;
}

// id [https://img.magnific.com/premium-vector/flat-man-driver-license-plastic-card-template-id-card-vector-illustration_589019-3455.jpg]
// sig[https://img.magnific.com/premium-vector/manual-signature-documents-white-background-hand-drawn-calligraphy-lettering-vector-illustration_81863-11792.jpg?semt=ais_hybrid&w=740&q=80]