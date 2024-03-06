package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class DBGameDao implements GameDao {

    public int indexID() {
        return 0;
    }

    public void insertGame(GameData game) throws DataAccessException {

    }

    public GameData getGame(int gameID) throws GameDoesntExistException {
        return null;
    }

    public void updateGame(int gameID, GameData game) throws GameDoesntExistException {

    }

    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    public void clear() throws DataAccessException {

    }

    public int size() {
        return 0;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database");
        }
    }
}
