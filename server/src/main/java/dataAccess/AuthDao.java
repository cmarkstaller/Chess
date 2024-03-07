package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import model.AuthData;

public interface AuthDao {
    public void insertAuth(AuthData authObject) throws dataAccess.Exceptions.DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
    public int size();
}
