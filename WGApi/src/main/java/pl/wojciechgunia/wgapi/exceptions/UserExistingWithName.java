package pl.wojciechgunia.wgapi.exceptions;

public class UserExistingWithName extends RuntimeException {
    public UserExistingWithName(String message) {
        super(message);
    }
}
