package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import model.GameData;

import java.util.Collection;

public interface GameDao {
    public int indexID();
    public void insertGame(GameData game) throws dataAccess.Exceptions.DataAccessException;
    public GameData getGame(int gameID) throws GameDoesntExistException;
    public void updateGame(int gameID, GameData game) throws GameDoesntExistException;
    public Collection<GameData> listGames() throws dataAccess.Exceptions.DataAccessException;
    public void clear() throws DataAccessException;
    public int size();
}
