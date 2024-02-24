package service;

import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.AuthDao;
import dataAccess.Exceptions.ColorAlreadyTakenException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
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

    public void joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws NotLoggedInException, GameDoesntExistException, ColorAlreadyTakenException {
        String userName = auth.getAuth(authToken).username();
        GameData myGame = game.getGame(gameID);
        switch(color){
            case BLACK:
                if (colorTaken(myGame, ChessGame.TeamColor.BLACK)) throw new ColorAlreadyTakenException("");
                GameData updatedBlackGame = new GameData(gameID, myGame.whiteUsername(), userName, myGame.gameName(), myGame.game());
                game.updateGame(gameID, updatedBlackGame);
                break;

            case WHITE:
                if (colorTaken(myGame, ChessGame.TeamColor.WHITE)) throw new ColorAlreadyTakenException("");
                GameData updatedWhiteGame = new GameData(gameID, userName, myGame.blackUsername(), myGame.gameName(), myGame.game());
                game.updateGame(gameID, updatedWhiteGame);
                break;

            default:
                break;
        }
    }

    public boolean colorTaken(GameData game, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.BLACK) {
            return game.blackUsername() != null;
        }
        else {
            return game.whiteUsername() != null;
        }
    }

}
