package dataAccess;
import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.Exceptions.UserExistsException;
import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DBAuthDao implements AuthDao {

    public void insertAuth(AuthData authObject) throws DataAccessException {
        if(this.getAuth(authObject.authToken()) != null) {
            throw new UserExistsException("User already exists error");
        }
        configureDatabase();
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authObject.authToken(), authObject.username());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        configureDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM AuthData WHERE authToken=?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                return new AuthData(authToken, username);
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if (this.getAuth(authToken) == null) {
            throw new NotLoggedInException("you aren't logged in mate");
        }
        configureDatabase();
        var statement = "DELETE FROM AuthData WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public void clear() throws DataAccessException {
        configureDatabase();
        var statement = "TRUNCATE AuthData";
        executeUpdate(statement);
    }

    @Override
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
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
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
