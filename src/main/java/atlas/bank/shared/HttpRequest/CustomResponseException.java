package atlas.bank.shared.HttpRequest;

import lombok.Getter;

@Getter
public class CustomResponseException extends RuntimeException {

    private final int code;

    public CustomResponseException(int code, String message) {
        this.code = code;
        super(message);
    }


    public static CustomResponseException resourceNotFound101(String message) {
        return new CustomResponseException(404, message);
    }

    public static CustomResponseException resourceMisMatch101(String message) {
        return new CustomResponseException(407, message);
    }

}
