package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDao {
    public void insertGame(GameData game) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    public void updateGame(int gameID, GameData game) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void clear() throws DataAccessException;
}
