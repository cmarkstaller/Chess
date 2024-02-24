package dataAccess.Exceptions;

public class IncorrectPasswordException extends DataAccessException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}