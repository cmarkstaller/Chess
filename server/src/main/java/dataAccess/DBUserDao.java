package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UserExistsException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DBUserDao implements UserDao {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    DaoHelper helper = new DaoHelper();
    public void insertUser(UserData user) throws DataAccessException {
        if (this.getUser(user.username()) != null) {
            throw new UserExistsException("User already exists exception");
        }
        helper.configureDatabase(createStatements);
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
        helper.executeUpdate(statement, user.username(), user.password(), user.email());
    }

    public UserData getUser(String userName) throws DataAccessException {
        helper.configureDatabase(createStatements);
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, email FROM UserData WHERE username=?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, userName);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                String email = rs.getString("email");

                return new UserData(userName, password, email);
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void clear() throws DataAccessException {
        helper.configureDatabase(createStatements);
        var statement = "TRUNCATE UserData";
        helper.executeUpdate(statement);
    }

    public int size() {
        return 0;
    }
}
