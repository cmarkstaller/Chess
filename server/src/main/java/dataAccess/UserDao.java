package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.UserData;

public interface UserDao {
    public void insertUser(UserData user) throws dataAccess.Exceptions.DataAccessException;
    public UserData getUser(String userName) throws dataAccess.Exceptions.DataAccessException;
    public void clear() throws DataAccessException;
    public int size();
}

