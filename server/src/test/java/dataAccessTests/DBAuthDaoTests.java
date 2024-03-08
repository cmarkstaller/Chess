package dataAccessTests;

import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.Exceptions.UserExistsException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;

import javax.xml.crypto.Data;

public class DBAuthDaoTests {
    AuthDao authDao = new DBAuthDao();

    @BeforeEach
    void before() throws DataAccessException  {
        authDao.clear();
    }
    @Test
    void positiveInsertAuth() throws DataAccessException {

        authDao.insertAuth(new AuthData("MyToken", "myusername"));

        AuthData actual = authDao.getAuth("MyToken");
        AuthData expected = new AuthData("MyToken", "myusername");
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void negativeInsertAuth() throws DataAccessException {

        authDao.insertAuth(new AuthData("myToken", "superUser"));

        Assertions.assertThrows(UserExistsException.class, () -> authDao.insertAuth(new AuthData("myToken", "superUser")));
    }

    @Test
    void positiveGetAuth() throws DataAccessException {
        authDao.insertAuth(new AuthData("MyToken", "myusername"));

        AuthData actual = authDao.getAuth("MyToken");
        AuthData expected = new AuthData("MyToken", "myusername");
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void negativeGetAuth() throws DataAccessException {
        Assertions.assertNull(authDao.getAuth("yup"));
    }

    @Test
    void positiveDeleteAuth() throws DataAccessException {
        authDao.insertAuth(new AuthData("MyToken", "myusername"));
        authDao.deleteAuth("MyToken");

        Assertions.assertNull(authDao.getAuth("MyToken"));
    }

    @Test
    void negativeDeleteAuth() throws DataAccessException {
        Assertions.assertThrows(NotLoggedInException.class, () -> authDao.deleteAuth("myAuthToken"));
    }

    @Test
    void positiveClearAuth() throws DataAccessException {
        authDao.insertAuth(new AuthData("otherToken", "OtherUser"));
        authDao.clear();
        Assertions.assertNull(authDao.getAuth("otherToken"));
    }
}
