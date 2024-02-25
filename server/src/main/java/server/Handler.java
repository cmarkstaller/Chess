package server;
import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.Exceptions.*;
import model.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Collection;

public class Handler {
    private final AuthDao auth = new MemoryAuthDao();
    private final GameDao game = new MemoryGameDao();
    private final UserDao user = new MemoryUserDao();

    public Object clearApplication(Request req, Response res) {
        ClearService clearService = new ClearService(auth, game, user);
        Gson gson = new Gson();
        try {
            clearService.clear();
            res.status(200);
        }
        catch (dataAccess.Exceptions.DataAccessException e){
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        return("{}");
    }

    public Object register(Request req, Response res) {
        UserService userService = new UserService(auth, user);
        Gson gson = new Gson();
        UserData userData = gson.fromJson(req.body(), UserData.class);

        try {
            AuthData authData = userService.register(userData.username(), userData.password(), userData.email());
            res.status(200);
            return(gson.toJson(authData));
        }
        catch (MissingInformationException e) {
            res.status(400);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (UserExistsException e) {
            res.status(403);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }

        catch (dataAccess.Exceptions.DataAccessException e){
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    public Object login(Request req, Response res) {
        UserService userService = new UserService(auth, user);
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);

        try {
            AuthData authData = userService.login(loginRequest.username(), loginRequest.password());
            res.status(200);
            return(gson.toJson(authData));
        }
        catch (IncorrectPasswordException | UserNotFoundException e) {
            res.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (dataAccess.Exceptions.DataAccessException e) {
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }
    public Object logout(Request req, Response res) {
        UserService userService = new UserService(auth, user);
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");

        try {
            userService.logout(authToken);
            res.status(200);
        }
        catch (NotLoggedInException e) {
            res.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (dataAccess.Exceptions.DataAccessException e) {
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        return("{}");
    } 
    public Object listGames(Request req, Response res) {
        GameService gameService = new GameService(auth, game);
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");

        try {
            Collection<GameData> gameList = gameService.listGames(authToken);
            res.status(200);
            return(gson.toJson(new GameListResponseMessage(convertGameData(gameList))));
        }

        catch (NotLoggedInException e) {
            res.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }

        catch (dataAccess.Exceptions.DataAccessException e) {
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }

    private Collection<ListGamesResponse> convertGameData (Collection<GameData> gameList) {
        ArrayList<ListGamesResponse> newList = new ArrayList<>();
        for (GameData gameData : gameList) {
            int gameID = gameData.gameID();
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            String gameName = gameData.gameName();
            newList.add(new ListGamesResponse(gameID, whiteUsername, blackUsername, gameName));
        }
        return(newList);
    }

    public Object createGame(Request req, Response res) {
        GameService gameService = new GameService(auth, game);
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);

        try {
            int gameID = gameService.createGame(authToken, createGameRequest.gameName());
            res.status(200);
            return gson.toJson(new CreateGameResponse(gameID));
        }
        catch (MissingInformationException e) {
            res.status(400);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (NotLoggedInException e) {
            res.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (dataAccess.Exceptions.DataAccessException e) {
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }

    }
    public Object joinGame(Request req, Response res) {
        GameService gameService = new GameService(auth, game);
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);

        try {
            gameService.joinGame(authToken, joinGameRequest.gameID(), joinGameRequest.playerColor());
            res.status(200);
            return("{}");
        }
        catch (GameDoesntExistException e) {
            res.status(400);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (NotLoggedInException e) {
            res.status(401);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (ColorAlreadyTakenException e) {
            res.status(403);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
        catch (dataAccess.Exceptions.DataAccessException e) {
            res.status(500);
            return gson.toJson(new ResponseMessage(e.getMessage()));
        }
    }
}
