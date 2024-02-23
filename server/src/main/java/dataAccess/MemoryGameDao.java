package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import model.GameData;

import java.util.*;

public class MemoryGameDao implements GameDao {
    public HashMap<Integer, GameData> hashMap = new HashMap<>();
    public void insertGame(GameData game) throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.containsKey(game.gameID())) throw new dataAccess.Exceptions.DataAccessException("Game already exists in hashmap");
        hashMap.put(game.gameID(), game);
    }
    public GameData getGame(int gameID) throws dataAccess.Exceptions.DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new dataAccess.Exceptions.DataAccessException("Game not in hashMap");
        return(hashMap.get(gameID));
    }
    public void deleteGame(int gameID) throws dataAccess.Exceptions.DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new dataAccess.Exceptions.DataAccessException("Game not in hashMap");
        hashMap.remove(gameID);
    }
    public void updateGame(int gameID, GameData game) throws dataAccess.Exceptions.DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new dataAccess.Exceptions.DataAccessException("Game not in hashMap");
        hashMap.replace(gameID, game);
    }
    public Collection<GameData> listGames() throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.isEmpty()) throw new dataAccess.Exceptions.DataAccessException("No games in hashMap");
        return(new ArrayList<>(hashMap.values()));
    }

    public void clear() throws dataAccess.Exceptions.DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("No games in hashMap");
        hashMap.clear();
    }

    public int size() {
        return(hashMap.size());
    }
}
