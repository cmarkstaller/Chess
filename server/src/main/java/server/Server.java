package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/connect", WebSocketHandler.class);

        Spark.init();

        Handler handler = new Handler();
        //WebSocketHandler wsHandler = new WebSocketHandler();

        Spark.delete("/db", handler::clearApplication);
        Spark.post("/user", handler::register);
        Spark.post("/session", handler::login);
        Spark.delete("/session", handler::logout);
        Spark.get("/game", handler::listGames);
        Spark.post("/game", handler::createGame);
        Spark.put("/game", handler::joinGame);

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
