package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;


public interface AuthDao {
    public Map<String, AuthData> hashMap = new HashMap<>();
    public AuthData createAuth(String Username) throws Exception;
    public getAuth();
    public deleteAuth();

}
