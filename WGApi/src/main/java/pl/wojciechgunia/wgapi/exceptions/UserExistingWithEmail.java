package pl.wojciechgunia.wgapi.exceptions;

public class UserExistingWithEmail extends RuntimeException {
    public UserExistingWithEmail(String message) {
        super(message);
    }
}
