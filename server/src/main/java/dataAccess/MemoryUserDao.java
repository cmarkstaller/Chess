package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UserExistsException;
import dataAccess.Exceptions.UserNotFoundException;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {
    public HashMap<String, UserData> hashMap = new HashMap<>();
    public void insertUser(UserData user) throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.containsKey(user.username())) throw new UserExistsException("");
        hashMap.put(user.username(), user);
    }
    public UserData getUser(String userName) throws dataAccess.Exceptions.DataAccessException {
        if (!hashMap.containsKey(userName)) throw new UserNotFoundException("");
        return(hashMap.get(userName));
    }
    public void deleteUser(String userName) throws dataAccess.Exceptions.DataAccessException {
        if (!hashMap.containsKey(userName)) throw new dataAccess.Exceptions.DataAccessException("User not in hashMap");
        hashMap.remove(userName);
    }
    public void clear() throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("hashMap is empty");
        hashMap.clear();
    }

    public int size() {
        return(hashMap.size());
    }
}