package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDao {
    public int indexID();
    public void insertGame(GameData game) throws dataAccess.Exceptions.DataAccessException;
    public GameData getGame(int gameID) throws dataAccess.Exceptions.DataAccessException;
    public void deleteGame(int gameID) throws dataAccess.Exceptions.DataAccessException;
    public void updateGame(int gameID, GameData game) throws dataAccess.Exceptions.DataAccessException;
    public Collection<GameData> listGames() throws dataAccess.Exceptions.DataAccessException;
    public void clear() throws DataAccessException;
    public int size();
}
