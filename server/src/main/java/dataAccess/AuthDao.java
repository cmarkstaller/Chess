package dataAccess;

import model.AuthData;

public interface AuthDao {
    public void insertAuth(AuthData authObject) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
}
