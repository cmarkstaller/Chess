package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {
    public HashMap<String, UserData> hashMap = new HashMap<>();
    public void insertUser(UserData user) throws DataAccessException {
        if (hashMap.containsKey(user.username())) throw new DataAccessException("User already in hashMap");
        hashMap.put(user.username(), user);
    }
    public UserData getUser(String userName) throws DataAccessException {
        if (!hashMap.containsKey(userName)) throw new DataAccessException("User not in hashMap");
        return(hashMap.get(userName));
    }
    public void deleteUser(String userName) throws DataAccessException {
        if (!hashMap.containsKey(userName)) throw new DataAccessException("User not in hashMap");
        hashMap.remove(userName);
    }
    public void clear() throws DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("hashMap is empty");
        hashMap.clear();
    }
}
