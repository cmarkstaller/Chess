package server;
import chess.ChessGame;
import dataAccess.*;
import dataAccess.Exceptions.ColorAlreadyTakenException;
import dataAccess.Exceptions.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinPlayer;
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

        }
    }

    private void joinPlayerHandler(Session session, String message) {
        Gson gson = new Gson();
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
            sessions.addSessionToGame(joinPlayer.getGameID(), joinPlayer.getAuthString(), session);
            String username = authDao.getAuth(joinPlayer.getAuthString()).username();

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

            sendMessage(session, loadGameConstructor(joinPlayer.getGameID()));
            broadCastMessage(joinPlayer.getGameID(), joinPlayerNotification(username, joinPlayer.getTeamColor()), joinPlayer.getAuthString());
        } catch (DataAccessException e) {
            sendMessage(session, gson.toJson(new Error(e.getMessage())));
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
}

