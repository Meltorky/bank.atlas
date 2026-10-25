package atlas.bank.integration.Image.azure;

import atlas.bank.integration.Image.ImageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("azureImageAdapter")
public class AzureImageAdapter implements ImageStorage {
    @Autowired
    private AzureImageService azureImageService;

    @Override
    public String upload(byte[] imageFile) {
        return azureImageService.uploadImage(imageFile);
    }
}
