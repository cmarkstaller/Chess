package server;
import com.google.gson.Gson;
import dataAccess.*;
import model.ResponseMessage;
import service.ClearService;
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
        return("");
    }
}
