package dataAccess;

import model.GameData;

import java.util.*;

public class MemoryGameDao implements GameDao {
    public HashMap<Integer, GameData> hashMap = new HashMap<>();
    public void insertGame(GameData game) throws DataAccessException {
        if (hashMap.containsKey(game.gameID())) throw new DataAccessException("Game already exists in hashmap");
        hashMap.put(game.gameID(), game);
    }
    public GameData getGame(int gameID) throws DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new DataAccessException("Game not in hashMap");
        return(hashMap.get(gameID));
    }
    public void deleteGame(int gameID) throws DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new DataAccessException("Game not in hashMap");
        hashMap.remove(gameID);
    }
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        if (!hashMap.containsKey(gameID)) throw new DataAccessException("Game not in hashMap");
        hashMap.replace(gameID, game);
    }
    public Collection<GameData> listGames() throws DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("No games in hashMap");
        return(new ArrayList<>(hashMap.values()));
    }

    public void clear() throws DataAccessException {
        if (hashMap.isEmpty()) throw new DataAccessException("No games in hashMap");
        hashMap.clear();
    }

    public int size() {
        return(hashMap.size());
    }
}
