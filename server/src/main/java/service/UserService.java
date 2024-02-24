package service;

import dataAccess.AuthDao;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.IncorrectPasswordException;
import dataAccess.Exceptions.MissingInformationException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.UserDao;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final AuthDao auth;
    private final UserDao user;

    public UserService(AuthDao auth, UserDao user) {
        this.auth = auth;
        this.user = user;
    }

    public AuthData register(String userName, String password, String email) throws DataAccessException {
        if (userName == null || password == null || email == null) throw new MissingInformationException("Error: bad request");
        user.insertUser(new UserData(userName, password, email));
        AuthData authentication = authDataGenerator(userName);
        auth.insertAuth(authentication);
        return(authentication);
    }

    public AuthData login(String userName, String password) throws DataAccessException, IncorrectPasswordException {
        UserData userObject = user.getUser(userName);
        if (!Objects.equals(userObject.password(), password)) throw new IncorrectPasswordException("bad password");
        AuthData authentication = authDataGenerator(userName);
        auth.insertAuth(authentication);
        return(authentication);
    }

    public void logout(String authToken) throws NotLoggedInException {
        auth.deleteAuth(authToken);
    }

    private AuthData authDataGenerator(String userName) {
        String authToken = UUID.randomUUID().toString();
        return(new AuthData(authToken, userName));
    }
}
