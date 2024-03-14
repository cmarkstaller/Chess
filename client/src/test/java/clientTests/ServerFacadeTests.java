package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;


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
    public void positiveRegister() throws Exception {
        Assertions.assertEquals("Success", facade.register("myUsername", "myPassword", "myEmail"));
    }

    @Test
    public void negativeRegister() throws Exception {
        // User already exists error;
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertEquals("User already exists error", facade.register("myUsername", "myPassword", "myEmail"));

        // Missing information error;
        reset();
        Assertions.assertEquals("Error: bad request", facade.register("myUsername", null, "myEmail"));
    }

    @Test
    public void positiveLogin() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertEquals("Success", facade.login("myUsername", "myPassword"));
    }

    @Test
    public void negativeLogin() throws Exception {
        Assertions.assertEquals("User not found error", facade.login("myUsername", "myPassword"));
    }

    @Test
    public void positiveLogout() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertEquals("Success", facade.logout());
    }

    @Test
    public void negativeLogout() throws Exception {
        Assertions.assertEquals("you are not logged in error", facade.logout());
    }

    @Test
    public void positiveCreateGame() throws Exception {
        facade.register("myUsername", "myPassword", "myEmail");
        Assertions.assertEquals("1", facade.createGame("maGame"));
    }

    @ Test
    public void negativeCreateGame() throws Exception {
        Assertions.assertEquals("you are not logged in error", facade.createGame("maGame"));
    }

}
