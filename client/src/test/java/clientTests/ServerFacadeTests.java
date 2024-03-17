package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.UIMenu;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        UIMenu menu = new UIMenu();
        menu.run();

        Assertions.assertTrue(true);
    }

}
