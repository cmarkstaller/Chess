package ui;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.Resign;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WebSocketFacade extends Endpoint {

    public Session session;

    public WebSocketFacade(int port, GameHandler gameHandler) throws DeploymentException, IOException, URISyntaxException {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                switch (serverMessage.getServerMessageType()) {
                    case LOAD_GAME -> gameHandler.updateGame(new Gson().fromJson(message, LoadGame.class).getGame());
                    case NOTIFICATION -> gameHandler.printMessage(new Gson().fromJson(message, Notification.class).getMessage());
                    case ERROR -> gameHandler.printMessage(new Gson().fromJson(message, Notification.class).getMessage());
                }
            }
        });
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws Exception {
        Gson gson = new Gson();
        send(gson.toJson(new JoinPlayer(authToken, gameID, playerColor)));
    }

    public void leave(String authToken, int gameID) throws Exception {
        Gson gson = new Gson();
        send(gson.toJson(new Leave(authToken, gameID)));
    }
    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        Gson gson = new Gson();
        send(gson.toJson(new MakeMove(authToken, gameID, move)));
    }

    public void resign(String authToken, int gameID) throws Exception {
        Gson gson = new Gson();
        send(gson.toJson(new Resign(authToken, gameID)));
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}