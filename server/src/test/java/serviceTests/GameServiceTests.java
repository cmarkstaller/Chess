package serviceTests;

import chess.ChessGame;
import dataAccess.AuthDao;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.GameDao;
import dataAccess.MemoryAuthDao;
import dataAccess.MemoryGameDao;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.ArrayList;
import java.util.Collection;

public class GameServiceTests {
// positive list Games test
    @Test
    void positiveListGames() throws DataAccessException, NotLoggedInException {
        AuthDao testAuthDao = new MemoryAuthDao();
        GameDao testGameDao = new MemoryGameDao();

        GameService gameService = new GameService(testAuthDao, testGameDao);

        ChessGame game1 = new ChessGame();

        testGameDao.insertGame(new GameData(1234567890, "WhiteUser", "BlackUser", "AwesomeGame", game1));
        testAuthDao.insertAuth(new AuthData("myauthtoken", "username"));

        Collection<GameData> actual = gameService.listGames("myauthtoken");

        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(new GameData(1234567890, "WhiteUser", "BlackUser", "AwesomeGame", game1));

        Assertions.assertEquals(actual, expected);
    }

// negative list games test
    @Test
    void negativeListGames() throws DataAccessException {
        AuthDao testAuthDao = new MemoryAuthDao();
        GameDao testGameDao = new MemoryGameDao();

        GameService gameService = new GameService(testAuthDao, testGameDao);

        Assertions.assertThrows(NotLoggedInException.class, () -> gameService.listGames("randomAuth"));
    }
// positive Create Game test
    @Test
    void positiveCreateGame() throws DataAccessException, NotLoggedInException{
        AuthDao testAuthDao = new MemoryAuthDao();
        GameDao testGameDao = new MemoryGameDao();

        GameService gameService = new GameService(testAuthDao, testGameDao);
        testAuthDao.insertAuth(new AuthData("MyAuthToken", "MyUsername"));

        int gameID = gameService.createGame("MyAuthToken", "MyGameName");

        String actual = testGameDao.getGame(gameID).gameName();
        String expected = "MyGameName";

        Assertions.assertEquals(actual, expected);
    }

// Negative Create Game Test
    @Test
    void negativeCreateGame() {
        AuthDao testAuthDao = new MemoryAuthDao();
        GameDao testGameDao = new MemoryGameDao();

        GameService gameService = new GameService(testAuthDao, testGameDao);

        Assertions.assertThrows(NotLoggedInException.class, () -> gameService.createGame("randomAuth", "My Game Name"));
    }
}
