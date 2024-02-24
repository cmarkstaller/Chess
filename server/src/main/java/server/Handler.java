package server;
import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.Exceptions.IncorrectPasswordException;
import dataAccess.Exceptions.MissingInformationException;
import dataAccess.Exceptions.NotLoggedInException;
import dataAccess.Exceptions.UserExistsException;
import model.AuthData;
import model.LoginRequest;
import model.ResponseMessage;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

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
        catch (dataAccess.Exceptions.IncorrectPasswordException e) {
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
        //GameService gameService = new GameService(auth, game);
        return("");
    }
    public Object createGame(Request req, Response res) {return "";}
    public Object joinGame(Request req, Response res) {return "";}
}
