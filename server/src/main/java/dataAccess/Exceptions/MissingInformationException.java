package dataAccess.Exceptions;

//import dataAccess.DataAccessException;

public class MissingInformationException extends dataAccess.Exceptions.DataAccessException {
    public MissingInformationException(String message) {
        super(message);
    }
}
