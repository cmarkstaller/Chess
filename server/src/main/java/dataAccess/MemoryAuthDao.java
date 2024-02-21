package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDao implements AuthDao {
    public Map<String, AuthData> hashMap = new HashMap<>();
    public void insertAuth(AuthData authObject) throws DataAccessException {
        if (hashMap.containsKey(authObject.authToken())) throw new DataAccessException("Duplicated Key");
        hashMap.put(authObject.authToken(), authObject);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!hashMap.containsKey(authToken)) throw new DataAccessException("authData not in hashMap");
        return(hashMap.get(authToken));
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!hashMap.containsKey(authToken)) throw new DataAccessException("authData not in hashMap");
        hashMap.remove(authToken);
    }
    public void clear() throws DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("hashMap has no entries to clear");
        hashMap.clear();
    }
}
