package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DBGameDao implements GameDao {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
              `gameID` INT NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    DaoHelper helper = new DaoHelper();
    public int indexID() {
        return 0;
    }

    @Override
    public int insertGame(GameData game) throws DataAccessException {
        helper.configureDatabase(createStatements);
        var statement = "INSERT INTO GameData (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
        //game.game().getBoard().resetBoard();
        String maGson = new Gson().toJson(game.game());
        return(helper.executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), maGson));
    }

    public GameData getGame(int gameID) throws DataAccessException  {
        helper.configureDatabase(createStatements);
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM GameData WHERE gameID=?";
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, gameID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String chessGame = rs.getString("chessGame");
                return new GameData(gameID, whiteUsername, blackUsername, gameName, new Gson().fromJson(chessGame, ChessGame.class));
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void updateGame(int gameID, GameData game) throws DataAccessException {
        if (this.getGame(gameID) == null) {
            throw new GameDoesntExistException("Game doesn't exist error");
        }
        helper.configureDatabase(createStatements);
        var statement = "UPDATE GameData SET whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE gameID = ?";
        helper.executeUpdate(statement, game.whiteUsername(), game.blackUsername(), new Gson().toJson(game.game()), gameID);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM GameData";
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery() ;
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame game = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                result.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
            }


        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void clear() throws DataAccessException {
        helper.configureDatabase(createStatements);
        var statement = "TRUNCATE GameData";
        helper.executeUpdate(statement);
    }

    public int size() {
        return 0;
    }
}
