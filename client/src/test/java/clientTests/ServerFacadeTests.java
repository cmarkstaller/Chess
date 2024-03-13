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
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void positiveRegister() {
        facade.register("")
        Assertions.assertTrue(true);
    }

}
