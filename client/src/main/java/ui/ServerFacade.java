package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.LoginRequest;
import model.ResponseMessage;
import model.UserData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {
    private final int port;
    private String authToken;

    public ServerFacade(int port) {
        this.port = port;
    }

    public String register(String username, String password, String email) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = new UserData(username, password, email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    AuthData authData = new Gson().fromJson(inputStreamReader, AuthData.class);
                    authToken = authData.authToken();
                    return ("Success");
                }
            default:
                try (InputStream errorStream = http.getErrorStream()) {
                    InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
                    ResponseMessage errorResponse = new Gson().fromJson(errorStreamReader, ResponseMessage.class);

                    return(errorResponse.message());
                }
        }
    }

    public String login(String username, String password) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = new LoginRequest(username, password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        int responseCode = http.getResponseCode();
        switch(responseCode) {
            case 200:
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    AuthData authData = new Gson().fromJson(inputStreamReader, AuthData.class);
                    authToken = authData.authToken();
                    return ("Success");
                }
            default:
                try (InputStream errorStream = http.getErrorStream()) {
                    InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
                    ResponseMessage errorResponse = new Gson().fromJson(errorStreamReader, ResponseMessage.class);

                    return(errorResponse.message());
                }
        }
    }

    public String logout() throws Exception {
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
                return ("Success");
            default:
                try (InputStream errorStream = http.getErrorStream()) {
                    InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
                    ResponseMessage errorResponse = new Gson().fromJson(errorStreamReader, ResponseMessage.class);

                    return(errorResponse.message());
                }
        }
    }


}
