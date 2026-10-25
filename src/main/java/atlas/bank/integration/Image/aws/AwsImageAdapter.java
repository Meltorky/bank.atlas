package atlas.bank.integration.Image.aws;

import atlas.bank.integration.Image.ImageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("awsImageAdapter")
public class AwsImageAdapter implements ImageStorage {

    @Autowired
    private AwsImageService awsImageService;

    @Override
    public String upload(byte[] imageFile) {
        return awsImageService.uploadImage(imageFile);
    }
}
