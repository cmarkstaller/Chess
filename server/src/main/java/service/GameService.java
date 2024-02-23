package service;

import chess.ChessGame;
import dataAccess.AuthDao;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.GameDao;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final AuthDao auth;
    private final GameDao game;
    private int nextID;
    public GameService(AuthDao auth, GameDao game) {
        this.auth = auth;
        this.game = game;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException, NotLoggedInException {
        auth.getAuth(authToken);
        return(game.listGames());
    }

    public int createGame(String authToken, String gameName) throws NotLoggedInException, DataAccessException {
        auth.getAuth(authToken);
        GameData newGame = new GameData(game.indexID(), null, null, gameName, new ChessGame());
        game.insertGame(newGame);
        return(newGame.gameID());
    }


}
