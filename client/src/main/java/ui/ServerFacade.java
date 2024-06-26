package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collection;

public class ServerFacade {
    private final int port;
    private String authToken;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void register(String username, String password, String email) throws IOException, URISyntaxException, ClientExceptionWrapper {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        //var body = new UserData(username, password, email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(new UserData(username, password, email));
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                myHappyFunction(http);
                return;
            default:
                mySadFunction(http);
        }
    }

    public void login(String username, String password) throws IOException, URISyntaxException, ClientExceptionWrapper {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        //var body = new LoginRequest(username, password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(new LoginRequest(username, password));
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                myHappyFunction(http);
                return;
            default:
                mySadFunction(http);
        }
    }

    public void logout() throws IOException, URISyntaxException, ClientExceptionWrapper {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);


        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                return;
            default:
                mySadFunction(http);
        }
    }

    public int createGame(String gameName) throws IOException, URISyntaxException, ClientExceptionWrapper {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Write out the body
        //var body = new CreateGameRequest(gameName);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(new CreateGameRequest(gameName));
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    int gameID = new Gson().fromJson(inputStreamReader, CreateGameResponse.class).gameID();
                    return (gameID);
                }

            default:
                mySadFunction(http);
                return(0);
        }
    }

    // Spark.get("/game", handler::listGames);
    public Collection<ListGamesResponse> listGames() throws IOException, URISyntaxException, ClientExceptionWrapper {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Collection<ListGamesResponse> games = new Gson().fromJson(inputStreamReader, GameListResponseMessage.class).games();
                    return (games);
                }
            default:
                mySadFunction(http);
                return(null);
        }
    }

    public void joinGame(ChessGame.TeamColor teamColor, int gameID) throws URISyntaxException, IOException, ClientExceptionWrapper {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", authToken);

        // Write out the body
        //var body = new JoinGameRequest(teamColor, gameID);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(new JoinGameRequest(teamColor, gameID));
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                return;
            default:
                mySadFunction(http);
        }
    }
    public String getAuthToken() {
        return(this.authToken);
    }

    private void myHappyFunction(HttpURLConnection http) throws IOException {
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData authData = new Gson().fromJson(inputStreamReader, AuthData.class);
            authToken = authData.authToken();
        }
    }

    private void mySadFunction(HttpURLConnection http) throws IOException, ClientExceptionWrapper {
        try (InputStream errorStream = http.getErrorStream()) {
            InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
            ResponseMessage errorResponse = new Gson().fromJson(errorStreamReader, ResponseMessage.class);

            throw new ClientExceptionWrapper(errorResponse.message());
        }
    }
}
