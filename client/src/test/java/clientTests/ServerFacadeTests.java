package clientTests;

import ServerFacade.ServerFacade;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static final String urlString = "http://localhost:8080";
    private static final ServerFacade serverFacade = new ServerFacade(urlString);

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clearing() throws Exception{
        serverFacade.facadeClear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void normalRegister() throws Exception{
        UserData regisObj =new UserData("registerUsername", "registerPassword", "registerEmail");
        AuthData authData =  serverFacade.facadeRegister(regisObj);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void doubleRegister(){
        UserData regisObj =new UserData("registerUsername", "registerPassword", "registerEmail");
        try {
            serverFacade.facadeRegister(regisObj);
            assertNull(serverFacade.facadeRegister(regisObj));
        }
        catch (Exception ex){
            fail("Unexpected exception:" + ex.getMessage());
        }

    }

    @Test
    public void normalLogin() throws Exception{
        UserData regisObj =new UserData("loginUsername", "loginPassword", "loginEmail");
        serverFacade.facadeRegister(regisObj);
        UserData loginObj =new UserData("loginUsername", "loginPassword", null);
        AuthData authData = serverFacade.facadeLogin(loginObj);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void loginDNE() throws Exception{
        UserData loginObj =new UserData("loginUsername", "loginPassword", null);
        AuthData authData = serverFacade.facadeLogin(loginObj);
        assertNull(authData);
    }

    @Test
    public void normalLogout() throws Exception{
        UserData regisObj =new UserData("loginUsername", "loginPassword", "loginEmail");
        serverFacade.facadeRegister(regisObj);
        UserData loginObj =new UserData("loginUsername", "loginPassword", null);
        AuthData authData = serverFacade.facadeLogin(loginObj);
        assertTrue(serverFacade.facadeLogout(authData.authToken()));
    }

    @Test
    public void nullLogout() throws Exception{
        assertFalse(serverFacade.facadeLogout(null));
    }

}
