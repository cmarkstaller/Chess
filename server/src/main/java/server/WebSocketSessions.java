package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;

public class WebSocketSessions {
    private Map<Integer, Map<String, Session>> sessionMap;

    public WebSocketSessions() {
        sessionMap = new HashMap<Integer, Map<String, Session>>();
    }

    public void addSessionToGame(int gameID, String authToken, Session session) {
        if (!sessionMap.containsKey(gameID)) {
            sessionMap.put(gameID, new HashMap<String, Session>());

        }
        sessionMap.get(gameID).put(authToken, session);
    }
    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        sessionMap.get(gameID).remove(authToken);
    }

    public Map<String, Session>getSessionsForGame(int gameID) {
        return(sessionMap.get(gameID));
    }
}
