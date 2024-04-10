package server;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.*;
import dataAccess.Exceptions.ColorAlreadyTakenException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.GameDoesntExistException;
import dataAccess.Exceptions.UserNotFoundException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
    private final GameDao gameDao = new DBGameDao();
    private final UserDao userDao = new DBUserDao();
    private final AuthDao authDao = new DBAuthDao();
    private WebSocketSessions sessions = new WebSocketSessions();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);

        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> this.joinPlayerHandler(session, message);
            case JOIN_OBSERVER -> this.joinObserverHandler(session, message);
            case MAKE_MOVE -> this.makeMoveHandler(session, message);

        }
    }

    private void joinPlayerHandler(Session session, String message) {
        Gson gson = new Gson();
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);

            // Bad authToken
            if (authDao.getAuth(joinPlayer.getAuthString()) == null) {
                throw new UserNotFoundException("User doesn't exist");
            }

            String username = authDao.getAuth(joinPlayer.getAuthString()).username();

            // Bad Game ID
            if (gameDao.getGame(joinPlayer.getGameID()) == null) {
                throw new GameDoesntExistException("Game Doesn't Exist Error");
            }

            // Player joins wrong color / without color
            if (joinPlayer.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (!Objects.equals(gameDao.getGame(joinPlayer.getGameID()).whiteUsername(), username)) {
                    throw new ColorAlreadyTakenException("Join Player Wrong Team Error");
                }
            }
            else if (joinPlayer.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (!Objects.equals(gameDao.getGame(joinPlayer.getGameID()).blackUsername(), username)) {
                    throw new ColorAlreadyTakenException("Join Player Wrong Team Error");
                }
            }

            sessions.addSessionToGame(joinPlayer.getGameID(), joinPlayer.getAuthString(), session);
            sendMessage(session, loadGameConstructor(joinPlayer.getGameID()));
            broadCastMessage(joinPlayer.getGameID(), joinPlayerNotification(username, joinPlayer.getTeamColor()), joinPlayer.getAuthString());
        } catch (DataAccessException e) {
            sendMessage(session, gson.toJson(new Error(e.getMessage())));
        }
    }

    private void joinObserverHandler(Session session, String message) {
        Gson gson = new Gson();
        try {
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);

            // Bad authToken
            if (authDao.getAuth(joinObserver.getAuthString()) == null) {
                throw new UserNotFoundException("User doesn't exist");
            }

            String username = authDao.getAuth(joinObserver.getAuthString()).username();

            // Bad Game ID
            if (gameDao.getGame(joinObserver.getGameID()) == null) {
                throw new GameDoesntExistException("Game Doesn't Exist Error");
            }

            sessions.addSessionToGame(joinObserver.getGameID(), joinObserver.getAuthString(), session);
            String load = loadGameConstructor(joinObserver.getGameID());
            this.sendMessage(session, load);
            //sendMessage(session, loadGameConstructor(joinObserver.getGameID()));
            broadCastMessage(joinObserver.getGameID(), joinObserverNotification(username), joinObserver.getAuthString());
        } catch (DataAccessException e) {
            sendMessage(session, gson.toJson(new Error(e.getMessage())));
        }
    }

    private void makeMoveHandler(Session session, String message) {
        Gson gson = new Gson();
        try {
            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
            String username = authDao.getAuth(makeMove.getAuthString()).username();

            ChessMove move = makeMove.getChessMove();

            GameData gameData = gameDao.getGame(makeMove.getGameID());
            ChessGame chessGame = gameData.game();

            if (getTeamColor(makeMove.getGameID(), username) == null) {
                throw new InvalidMoveException("Can't make move as observer error");
            }

            // Tries to move wrong piece
            if (chessGame.getTeamTurn() != getTeamColor(makeMove.getGameID(), username)) {
                throw new InvalidMoveException("Not your piece error");
            }
            if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE) ||
                    chessGame.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                    chessGame.isInStalemate(ChessGame.TeamColor.WHITE) ||
                    chessGame.isInStalemate(ChessGame.TeamColor.BLACK)
            ){
                throw new InvalidMoveException("Ur Done error");
            }

            chessGame.makeMove(move);

            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
            gameDao.updateGame(makeMove.getGameID(), newGameData);

            sendMessage(session, loadGameConstructor(makeMove.getGameID()));
            broadCastMessage(makeMove.getGameID(), loadGameConstructor(makeMove.getGameID()), makeMove.getAuthString());

            broadCastMessage(makeMove.getGameID(), makeMoveNotification(makeMove.getChessMove()), makeMove.getAuthString());
        } catch (DataAccessException | InvalidMoveException e) {
            sendMessage(session, gson.toJson(new Error("You've got mail, error")));
        }
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadCastMessage(int gameID, String message, String exceptThisAuthToken) {
        Map<String, Session> map = sessions.getSessionsForGame(gameID);
        for (String key : map.keySet()) {
            if (!Objects.equals(key, exceptThisAuthToken)) {
                sendMessage(map.get(key), message);
            }
        }
    }

    private String loadGameConstructor(int gameID) throws DataAccessException {
        Gson gson = new Gson();
        return(gson.toJson(new LoadGame(gameDao.getGame(gameID).game())));
    }

    private ChessGame.TeamColor getTeamColor(int gameID, String username) throws DataAccessException {
        if (Objects.equals(gameDao.getGame(gameID).whiteUsername(), username)) {
            return(ChessGame.TeamColor.WHITE);
        }
        else if (Objects.equals(gameDao.getGame(gameID).blackUsername(), username)) {
            return (ChessGame.TeamColor.BLACK);
        }
        else {
            return null;
        }
    }

    private String joinPlayerNotification(String username, ChessGame.TeamColor teamColor) {
        String message;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            message = username + "has joined as white";
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            message = username + "has joined as black";
        }

        else {
            message = username + "has joined";
        }
        Gson gson = new Gson();
        return(gson.toJson(new Notification(message)));
    }

    private String joinObserverNotification(String username) {
        String message = username + "has joined";

        Gson gson = new Gson();
        return(gson.toJson(new Notification(message)));
    }

    private String makeMoveNotification(ChessMove chessMove) {
        String message = chessMove.toString();

        Gson gson = new Gson();
        return(gson.toJson(new Notification(message)));
    }
}

