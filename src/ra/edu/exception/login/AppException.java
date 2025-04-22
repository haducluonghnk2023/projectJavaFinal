package ra.edu.exception.login;

public class AppException extends RuntimeException  {
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}