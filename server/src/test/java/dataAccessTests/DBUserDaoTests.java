package dataAccessTests;

import dataAccess.AuthDao;
import dataAccess.DBAuthDao;
import dataAccess.DBUserDao;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.Exceptions.UserExistsException;
import dataAccess.UserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DBUserDaoTests {
    UserDao userDao = new DBUserDao();

    @BeforeEach
    void before() throws DataAccessException {
        userDao.clear();
    }
    @Test
    void positiveInsertUser() throws DataAccessException {

        userDao.insertUser(new UserData("MyUser", "myPassword", "myEmail"));

        UserData actual = userDao.getUser("MyUser");
        UserData expected = new UserData("MyUser", "myPassword", "myEmail");
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void negativeInsertUser() throws DataAccessException {
        userDao.insertUser(new UserData("MyUsername", "MyPassword", "MyEmail"));
        Assertions.assertThrows(UserExistsException.class, () -> userDao.insertUser(new UserData("MyUsername", "MyPassword", "MyEmail")));
    }

    @Test
    void positiveGetUser() throws DataAccessException {
        userDao.insertUser(new UserData("MyUser", "myPassword", "myEmail"));

        UserData actual = userDao.getUser("MyUser");
        UserData expected = new UserData("MyUser", "myPassword", "myEmail");
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void negativeGetUser() throws DataAccessException {
        Assertions.assertNull(userDao.getUser("myUsername"));
    }

    @Test
    void positiveClear() throws DataAccessException {
        userDao.insertUser(new UserData("MyUser", "myPassword", "myEmail"));
        userDao.clear();
        Assertions.assertNull(userDao.getUser("MyUser"));
    }
}
