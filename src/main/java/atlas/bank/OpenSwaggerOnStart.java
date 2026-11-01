package atlas.bank;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OpenSwaggerOnStart {

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            new ProcessBuilder(
                    "rundll32", "url.dll,FileProtocolHandler",
                    "http://localhost:8080/swagger-ui/index.html"
            ).start();
        } catch (Exception e) {
            System.out.println("Could not open browser: " + e.getMessage());
        }
    }
}