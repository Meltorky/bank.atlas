package atlas.bank.controllers;

import atlas.bank.DTOs.CertificateCreateDTO;
import atlas.bank.abstracts.CertificateService;
import atlas.bank.entities.Certificate;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/create")
    public ResponseEntity<Certificate> createCertificate(
            @Valid @RequestBody CertificateCreateDTO dto) {

        Certificate certificate = certificateService.createCertificate(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificate);
    }
}