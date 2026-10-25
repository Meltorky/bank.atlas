package atlas.bank.integration.Image.azure;

import org.springframework.stereotype.Service;

@Service
public class AzureImageService {

    public String uploadImage(byte[] image) {

        // HTTP POST
        // POST https://aws.com/image
        // Body = image

        // AWS responds with:
        // { "url": "https://aws.com/images/abc123.jpg" }

        return "https://img.magnific.com/premium-vector/manual-signature-documents-white-background-hand-drawn-calligraphy-lettering-vector-illustration_81863-11792.jpg?semt=ais_hybrid&w=740&q=80";
    }
}
