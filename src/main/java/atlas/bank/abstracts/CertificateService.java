package atlas.bank.abstracts;

import atlas.bank.DTOs.CertificateCreateDTO;
import atlas.bank.entities.Certificate;
import org.springframework.transaction.annotation.Transactional;

public interface CertificateService {
    @Transactional
    Certificate createCertificate(CertificateCreateDTO dto);
}
