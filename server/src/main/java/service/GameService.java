package service;

import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.AuthDao;
import dataAccess.Exceptions.*;
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

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (auth.getAuth(authToken) == null) {
            throw new NotLoggedInException("you are not logged in error");
        }
        auth.getAuth(authToken);
        return(game.listGames());
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (authToken == null || gameName == null) throw new MissingInformationException("Error: bad request");

        if (auth.getAuth(authToken) == null) {
            throw new NotLoggedInException("you are not logged in error");
        }
        auth.getAuth(authToken);

        ChessGame starterGame = new ChessGame();
        starterGame.getBoard().resetBoard();
        GameData newGame = new GameData(game.indexID(), null, null, gameName, starterGame);
        return(game.insertGame(newGame));
    }

    public void joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws DataAccessException {
        if (auth.getAuth(authToken) == null) {
            throw new NotLoggedInException("you are not logged in error");
        }
        String userName = auth.getAuth(authToken).username();
        if (game.getGame(gameID) == null) {
            throw new GameDoesntExistException("bad game ID error");
        }

        GameData myGame = game.getGame(gameID);
        if (color != null) {
            switch (color) {
                case BLACK:
                    if (colorTaken(myGame, ChessGame.TeamColor.BLACK)) throw new ColorAlreadyTakenException("Color taken error");
                    GameData updatedBlackGame = new GameData(gameID, myGame.whiteUsername(), userName, myGame.gameName(), myGame.game());
                    game.updateGame(gameID, updatedBlackGame);
                    break;

                case WHITE:
                    if (colorTaken(myGame, ChessGame.TeamColor.WHITE)) throw new ColorAlreadyTakenException("Color taken error");
                    GameData updatedWhiteGame = new GameData(gameID, userName, myGame.blackUsername(), myGame.gameName(), myGame.game());
                    game.updateGame(gameID, updatedWhiteGame);
                    break;
            }
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
