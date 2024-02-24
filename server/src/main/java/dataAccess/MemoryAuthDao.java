package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDao implements AuthDao {
    public Map<String, AuthData> hashMap = new HashMap<>();
    public void insertAuth(AuthData authObject) throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.containsKey(authObject.authToken())) throw new dataAccess.Exceptions.DataAccessException("Duplicated Key");
        hashMap.put(authObject.authToken(), authObject);
    }
    public AuthData getAuth(String authToken) throws NotLoggedInException {
        if (!hashMap.containsKey(authToken)) throw new NotLoggedInException("ur not logged in");
        return(hashMap.get(authToken));
    }
    public void deleteAuth(String authToken) throws NotLoggedInException {
        if (!hashMap.containsKey(authToken)) throw new NotLoggedInException("Still not logged in");
        hashMap.remove(authToken);
    }
    public void clear() throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("Error: No AuthData to clear");
        hashMap.clear();
    }

    public int size() {
        return(hashMap.size());
    }
}
