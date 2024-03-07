package service;

import dataAccess.AuthDao;
import dataAccess.Exceptions.*;
import dataAccess.UserDao;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

        String hashedPassword = new BCryptPasswordEncoder().encode(password);

        user.insertUser(new UserData(userName, hashedPassword, email));
        AuthData authentication = authDataGenerator(userName);
        auth.insertAuth(authentication);
        return(authentication);
    }

    public AuthData login(String userName, String password) throws DataAccessException {
        UserData userObject = user.getUser(userName);
        if (userObject == null) {
            throw new UserNotFoundException("User not found error");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        if (!encoder.matches(password, userObject.password())) throw new IncorrectPasswordException("bad password error");
        AuthData authentication = authDataGenerator(userName);
        auth.insertAuth(authentication);
        return(authentication);
    }

    public void logout(String authToken) throws DataAccessException {
        auth.deleteAuth(authToken);
    }

    private AuthData authDataGenerator(String userName) {
        String authToken = UUID.randomUUID().toString();
        return(new AuthData(authToken, userName));
    }
}
