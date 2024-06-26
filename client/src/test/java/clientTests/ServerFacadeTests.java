package clientTests;

import chess.ChessGame;
import ui.ClientExceptionWrapper;
import model.ListGamesResponse;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static int port;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void reset() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.connect();

        int response = http.getResponseCode();
        if (response != 200) {
            throw new RuntimeException("Coudn't clear database");
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void positiveRegister() {
        Assertions.assertDoesNotThrow(() -> facade.register("myUsername", "myPassword", "myEmail"));
    }

    @ Test
    public void negativeRegister() throws IOException, URISyntaxException, ClientExceptionWrapper {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertThrows(ClientExceptionWrapper.class, () -> facade.register("myUsername", "myPassword", "myEmail"));
    }

    @Test
    public void positiveLogin() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertDoesNotThrow(() -> facade.login("myUsername", "myPassword"));
    }

    @Test
    public void negativeLogin() throws Exception {
        Assertions.assertThrows(ClientExceptionWrapper.class, () -> facade.login("myUsername", "myPassword"));
    }

    @Test
    public void positiveLogout() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void negativeLogout() {
        Assertions.assertThrows(ClientExceptionWrapper.class, () -> facade.logout());
    }

    @Test
    public void positiveCreateGame() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertEquals(1, facade.createGame("maGame"));
    }

    @Test
    public void negativeCreateGame() throws Exception {
        Assertions.assertThrows(ClientExceptionWrapper.class, () -> facade.createGame("maGame"));
    }

    @Test
    public void positiveListGames() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        facade.createGame("maGame");
        facade.createGame("maOtherGame");

        ArrayList<ListGamesResponse> expected = new ArrayList<ListGamesResponse>();
        expected.add(new ListGamesResponse(1, null, null, "maGame"));
        expected.add(new ListGamesResponse(2, null, null, "maOtherGame"));

        Assertions.assertEquals(expected, facade.listGames());
    }

    @Test
    public void negativeListGames() throws Exception {
        Assertions.assertThrows(ClientExceptionWrapper.class, () -> facade.listGames());
    }

    @Test
    public void positiveJoinGame() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        facade.createGame("maGame");
        facade.joinGame(ChessGame.TeamColor.BLACK, 1);

        ArrayList<ListGamesResponse> expected = new ArrayList<ListGamesResponse>();
        expected.add(new ListGamesResponse(1, null, "myUsername", "maGame"));

        Assertions.assertEquals(expected, facade.listGames());
    }

    @Test
    public void negativeJoinGame() throws Exception {
        Assertions.assertThrows(ClientExceptionWrapper.class, () -> facade.listGames());
    }
}
