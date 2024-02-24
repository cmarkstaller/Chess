package dataAccess.Exceptions;

public class GameDoesntExistException extends Exception {
    public GameDoesntExistException(String message) {
        super(message);
    }
}
