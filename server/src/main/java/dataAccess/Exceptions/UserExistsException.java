package dataAccess.Exceptions;

import dataAccess.Exceptions.DataAccessException;

public class UserExistsException extends DataAccessException {
    public UserExistsException(String message) {
        super(message);
    }
}
