package dataAccessTests;

import dataAccess.AuthDao;
import dataAccess.DBAuthDao;
import dataAccess.Exceptions.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DBAuthDaoTests {
    @Test
    void positiveInsertAuth() throws DataAccessException {
        AuthDao authDao = new DBAuthDao();

        authDao.clear();

        authDao.insertAuth(new AuthData("MyToken", "myusername"));

        AuthData actual = authDao.getAuth("MyToken");
        AuthData expected = new AuthData("MyToken", "myusername");
        Assertions.assertEquals(actual, expected);
    }
}
