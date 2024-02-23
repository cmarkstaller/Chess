package dataAccess.Exceptions;

public class NotLoggedInException extends Exception{
    public NotLoggedInException(String message) {
        super(message);
    }
}
