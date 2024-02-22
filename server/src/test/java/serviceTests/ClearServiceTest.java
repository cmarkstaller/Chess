package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearServiceTest {
    @Test
    void clear() throws DataAccessException {
        AuthDao testAuthDao = new MemoryAuthDao();
        GameDao testGameDao = new MemoryGameDao();
        UserDao testUserDao = new MemoryUserDao();

        testAuthDao.insertAuth(new AuthData("122334", "Usernamestring"));
        testGameDao.insertGame(new GameData(1234, "White user", "Black user", "boringGame", new ChessGame()));
        testUserDao.insertUser(new UserData("Usernamestring", "passwordstring", "emailstring"));

        ClearService testClear = new ClearService(testAuthDao, testGameDao, testUserDao);
        testClear.clear();
    }
}
