package dataAccess;
import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import model.AuthData;

import java.sql.SQLException;

public class DBAuthDao implements AuthDao {
    public DBAuthDao() throws dataAccess.DataAccessException, DataAccessException {
        configureDatabase();
    }

    public void insertAuth(AuthData authObject) throws DataAccessException {
        
    }

    public AuthData getAuth(String authToken) throws NotLoggedInException {
        return null;
    }

    public void deleteAuth(String authToken) throws NotLoggedInException {

    }

    public void clear() throws DataAccessException {

    }

    public int size() {
        return 0;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException, dataAccess.DataAccessException {
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
