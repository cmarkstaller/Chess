package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class UserServiceTests {

// User Tests
// Positive Registration test
    @Test
    void positiveRegister() throws DataAccessException {
        AuthDao testAuthDao = new MemoryAuthDao();
        UserDao testUserDao = new MemoryUserDao();

        UserService testRegister = new UserService(testAuthDao, testUserDao);

        // Calculate expected and actual values for AuthDao
        AuthData expectedAuth = testRegister.register("MyUsername", "MyPassword", "MyEmail");
        AuthData actualAuth = testAuthDao.getAuth(expectedAuth.authToken());

        // Calculate expected and actual values for UserDao
        UserData expectedUser = new UserData("MyUsername", "MyPassword", "MyEmail");
        UserData actualUser = testUserDao.getUser("MyUsername");

        Assertions.assertEquals(expectedAuth, actualAuth);
        Assertions.assertEquals(expectedUser, actualUser);
    }

// Negative Registration test
    @Test
    void negativeRegister() throws DataAccessException {
        AuthDao testAuthDao = new MemoryAuthDao();
        UserDao testUserDao = new MemoryUserDao();

        UserService testRegister = new UserService(testAuthDao, testUserDao);

        try {
            testRegister.register("MyUsername", "MyPassword", "MyEmail");
            Assertions.assertThrows(UserExistsException.class, () -> testRegister.register("MyUsername", "MyOtherPassword", "MyJunkEmail"));
        } catch (UserExistsException ignored) {}
    }

// Positive Login test
    @Test
    void positiveLogin() throws DataAccessException, IncorrectPasswordException {
        AuthDao testAuthDao = new MemoryAuthDao();
        UserDao testUserDao = new MemoryUserDao();

        testUserDao.insertUser(new UserData("MyUsername", "MyPassword", "MyEmail"));

        UserService userService = new UserService(testAuthDao, testUserDao);
        AuthData expected = userService.login("MyUsername", "MyPassword");
        AuthData actual = testAuthDao.getAuth(expected.authToken());

        Assertions.assertEquals(expected, actual);
    }
}
