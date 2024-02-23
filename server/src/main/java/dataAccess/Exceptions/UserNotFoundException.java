package dataAccess.Exceptions;

import dataAccess.Exceptions.DataAccessException;

public class UserNotFoundException extends DataAccessException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
