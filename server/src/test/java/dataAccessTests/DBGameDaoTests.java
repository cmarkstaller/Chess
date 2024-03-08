package dataAccessTests;

import chess.ChessGame;
import dataAccess.DBGameDao;
import dataAccess.DBUserDao;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import dataAccess.Exceptions.UserExistsException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DBGameDaoTests {
    GameDao gameDao = new DBGameDao();

    @BeforeEach
    void before() throws DataAccessException {
        gameDao.clear();
    }

    @Test
    void positiveInsertGame() throws DataAccessException {
        int gameID = gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        String actual = gameDao.getGame(gameID).gameName();
        String expected = "superGame";
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void negativeInsertGame() throws DataAccessException {
        int gameID = gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        Assertions.assertNotEquals(0, gameDao.getGame(gameID).gameID());
    }

    @Test
    void positiveGetGame() throws DataAccessException {
        int gameID = gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        String actual = gameDao.getGame(gameID).gameName();
        String expected = "superGame";
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void negativeGetGame() throws DataAccessException {
        int gameID = gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        Assertions.assertNull(gameDao.getGame(-1));
    }

    @Test
    void positiveUpdateGame() throws DataAccessException {
        int gameID = gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        String before = gameDao.getGame(gameID).whiteUsername();

        gameDao.updateGame(gameID, new GameData(0, "Whittey", null, "superGame", new ChessGame()));
        String after = gameDao.getGame(gameID).whiteUsername();

        Assertions.assertNotEquals(before, after);
    }

    @Test
    void negativeUpdateGame() throws DataAccessException {
        Assertions.assertThrows(GameDoesntExistException.class, () -> gameDao.updateGame(2, new GameData(0, null, null, "superGame", new ChessGame())));
    }

    @Test
    void positiveListGames() throws DataAccessException {
        gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        gameDao.insertGame(new GameData(0, null, null, "superDupergame", new ChessGame()));
        gameDao.insertGame(new GameData(0, null, null, "epicgame", new ChessGame()));

        Assertions.assertEquals(gameDao.listGames().size(), 3);
    }

    @Test
    void negativeListGames() throws DataAccessException {
        Assertions.assertEquals(gameDao.listGames(), new ArrayList<>());
    }

    @Test
    void positiveClear() throws DataAccessException {
        int gameID = gameDao.insertGame(new GameData(0, null, null, "superGame", new ChessGame()));
        gameDao.clear();
        Assertions.assertNull(gameDao.getGame(gameID));
    }
//    @Test
//    void positiveInsertUser() throws DataAccessException {
//
//        userDao.insertUser(new UserData("MyUser", "myPassword", "myEmail"));
//
//        UserData actual = userDao.getUser("MyUser");
//        UserData expected = new UserData("MyUser", "myPassword", "myEmail");
//        Assertions.assertEquals(actual, expected);
//    }
//
//    @Test
//    void negativeInsertUser() throws DataAccessException {
//        userDao.insertUser(new UserData("MyUsername", "MyPassword", "MyEmail"));
//        Assertions.assertThrows(UserExistsException.class, () -> userDao.insertUser(new UserData("MyUsername", "MyPassword", "MyEmail")));
//    }
//
//    @Test
//    void positiveGetUser() throws DataAccessException {
//        userDao.insertUser(new UserData("MyUser", "myPassword", "myEmail"));
//
//        UserData actual = userDao.getUser("MyUser");
//        UserData expected = new UserData("MyUser", "myPassword", "myEmail");
//        Assertions.assertEquals(actual, expected);
//    }
//
//    @Test
//    void negativeGetUser() throws DataAccessException {
//        Assertions.assertNull(userDao.getUser("myUsername"));
//    }
//
//    @Test
//    void positiveClear() throws DataAccessException {
//        userDao.insertUser(new UserData("MyUser", "myPassword", "myEmail"));
//        userDao.clear();
//        Assertions.assertNull(userDao.getUser("MyUser"));
//    }
}
