package dataAccess;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.Exceptions.UserExistsException;
import model.AuthData;

public class DBAuthDao implements AuthDao {
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

    DaoHelper helper = new DaoHelper();
    public void insertAuth(AuthData authObject) throws DataAccessException {
        if(this.getAuth(authObject.authToken()) != null) {
            throw new UserExistsException("User already exists error");
        }
        helper.configureDatabase(createStatements);
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        helper.executeUpdate(statement, authObject.authToken(), authObject.username());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        helper.configureDatabase(createStatements);
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
        helper.configureDatabase(createStatements);
        var statement = "DELETE FROM AuthData WHERE authToken=?";
        helper.executeUpdate(statement, authToken);
    }

    public void clear() throws DataAccessException {
        helper.configureDatabase(createStatements);
        var statement = "TRUNCATE AuthData";
        helper.executeUpdate(statement);
    }

    @Override
    public int size() {
        return 0;
    }
}
