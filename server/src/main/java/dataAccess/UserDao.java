package dataAccess;

import model.UserData;

public interface UserDao {
    public void insertUser(UserData user) throws DataAccessException;
    public UserData getUser(String userName) throws DataAccessException;
    public void deleteUser(String userName) throws DataAccessException;
    public void updateUser(String userName) throws DataAccessException;
    public void clear() throws DataAccessException;
}

