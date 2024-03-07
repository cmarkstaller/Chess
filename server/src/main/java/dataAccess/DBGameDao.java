package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS UserData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(chessGame)
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
