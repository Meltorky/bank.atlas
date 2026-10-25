package atlas.bank.integration.Image.aws;

import org.springframework.stereotype.Service;

@Service
public class AwsImageService {

    public String uploadImage(byte[] image) {

        // HTTP POST
        // POST https://aws.com/image
        // Body = image

        // AWS responds with:
        // { "url": "https://aws.com/images/abc123.jpg" }

        return "https://img.magnific.com/premium-vector/flat-man-driver-license-plastic-card-template-id-card-vector-illustration_589019-3455.jpg";
    }
}
