package pl.wojciechgunia.wgapi.exceptions;

public class FtpConnectionException extends RuntimeException {
    public FtpConnectionException(String message) {
        super(message);
    }

    public FtpConnectionException(Throwable cause) {
        super(cause);
    }
}
