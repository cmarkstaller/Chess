package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import model.GameData;

import java.util.*;

public class MemoryGameDao implements GameDao {
    private int index = 0;
    public int indexID() {
        ++index;
        return(index);
    }
    public HashMap<Integer, GameData> hashMap = new HashMap<>();
    public void insertGame(GameData game) throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.containsKey(game.gameID())) throw new dataAccess.Exceptions.DataAccessException("Game already exists in hashmap");
        hashMap.put(game.gameID(), game);
    }
    public GameData getGame(int gameID) throws GameDoesntExistException {
        if (!hashMap.containsKey(gameID)) throw new GameDoesntExistException("bad game ID error");
        return(hashMap.get(gameID));
    }
    public void deleteGame(int gameID) throws dataAccess.Exceptions.DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new dataAccess.Exceptions.DataAccessException("Game not in hashMap error");
        hashMap.remove(gameID);
    }
    public void updateGame(int gameID, GameData game) throws GameDoesntExistException {
        if (!hashMap.containsKey(gameID)) throw new GameDoesntExistException("error");
        hashMap.replace(gameID, game);
    }
    public Collection<GameData> listGames() throws dataAccess.Exceptions.DataAccessException {
        return(new ArrayList<>(hashMap.values()));
    }

    public void clear() throws dataAccess.Exceptions.DataAccessException {
        hashMap.clear();
    }

    public int size() {
        return(hashMap.size());
    }
}
