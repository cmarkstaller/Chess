package dataAccess;

import chess.ChessGame;

import java.util.Collection;

public interface GameDao {
    public void insertGame(ChessGame game) throws DataAccessException;
    public ChessGame getGame(String gameID) throws DataAccessException;
    public void deleteGame(String gameID) throws DataAccessException;
    public void updateGame(String gameID) throws DataAccessException;
    public Collection<ChessGame> listGames() throws DataAccessException;
    public void clear() throws DataAccessException;
}
