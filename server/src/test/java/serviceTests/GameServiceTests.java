package serviceTests;

import chess.ChessGame;
import dataAccess.AuthDao;
import dataAccess.Exceptions.*;
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
    void positiveCreateGame() throws DataAccessException, NotLoggedInException, GameDoesntExistException{
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

// Positive Join Game Test
   @Test
   void positiveJoinGame() throws DataAccessException, NotLoggedInException, GameDoesntExistException, ColorAlreadyTakenException {
       AuthDao testAuthDao = new MemoryAuthDao();
       GameDao testGameDao = new MemoryGameDao();

       testAuthDao.insertAuth(new AuthData("MyAuthToken", "MyUsername"));
       testGameDao.insertGame(new GameData(1234, "whitey", null, "superGAme", new ChessGame()));

       GameService gameService = new GameService(testAuthDao, testGameDao);

       gameService.joinGame("MyAuthToken", 1234, ChessGame.TeamColor.BLACK);

       String actual = testGameDao.getGame(1234).blackUsername();
       String expected = "MyUsername";

       Assertions.assertEquals(actual, expected);
   }

// negative join game test
    @Test
    void negativeJoinTest() throws DataAccessException, NotLoggedInException, GameDoesntExistException {
        AuthDao testAuthDao = new MemoryAuthDao();
        GameDao testGameDao = new MemoryGameDao();

        testAuthDao.insertAuth(new AuthData("MyAuthToken", "MyUsername"));
        testGameDao.insertGame(new GameData(1234, "whitey", "yeet", "superGame", new ChessGame()));

        GameService gameService = new GameService(testAuthDao, testGameDao);

        Assertions.assertThrows(ColorAlreadyTakenException.class, () -> gameService.joinGame("MyAuthToken", 1234, ChessGame.TeamColor.BLACK));
    }
}
