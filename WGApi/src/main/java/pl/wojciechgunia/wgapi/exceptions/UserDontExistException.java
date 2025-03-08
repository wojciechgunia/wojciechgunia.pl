package pl.wojciechgunia.wgapi.exceptions;

public class UserDontExistException extends RuntimeException {
  public UserDontExistException(String message) {
    super(message);
  }
}
