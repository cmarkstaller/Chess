package dataAccess.Exceptions;

public class GameDoesntExistException extends DataAccessException {
    public GameDoesntExistException(String message) {
        super(message);
    }
}
