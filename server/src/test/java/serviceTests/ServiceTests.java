package serviceTests;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
public class ServiceTests {

    @BeforeEach
    public void clear(){
        try {
            UserService userService = new UserService();
            userService.clear();
        }
        catch(DataAccessException ex){
             fail("Unexpected exception:" + ex.getMessage());
        }
    }
    @Test
    public void normalRegisterTest(){
        UserService service = new UserService();
        UserData normalUser = new UserData("taho", "password123", "email@u.com");
        try {
            AuthData auth = service.register(normalUser);
            MemoryUserDAO users = new MemoryUserDAO();
            assertEquals(auth.username(), "taho");
            assertNotNull(auth.authToken());
            assertEquals(users.getUser(normalUser.username()),normalUser);

        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void noUserDataRegisterTest(){
        UserService service = new UserService();
        UserData emptyUser = new UserData(null, null, null);
        assertThrows(DataAccessException.class, ()-> service.register(emptyUser));
    }

    @Test
    public void clearDataTest(){
        MemoryUserDAO users = new MemoryUserDAO();
        MemoryAuthDAO auths = new MemoryAuthDAO();
        UserService service = new UserService();

        try {
            users.insertUser(new UserData("me", "password", "email@email.com"));
            auths.insertAuth(new AuthData("authToken", "me"));
            service.clear();
            assertTrue(users.isEmpty());
            assertTrue(auths.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void normalLogin(){
        UserService service = new UserService();
        normalRegisterTest();
        try {
            AuthData loginObject = service.login(new UserData("taho", "password123", null));
            assertEquals("taho", loginObject.username());
            assertNotNull(loginObject.authToken());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }

    }

    @Test
    void wrongPasswordLogin(){
        UserService service = new UserService();
        UserData wrongPass = new UserData("taho", "password", null);
        assertThrows(DataAccessException.class, ()-> service.login(wrongPass));
    }

    @Test
    void normalLogout(){
        UserService service = new UserService();
        normalRegisterTest();
        try {
            AuthData loginObject = service.login(new UserData("taho", "password123", null));
            service.logout(loginObject.authToken());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    void wrongAuthTokenLogout(){
        UserService service = new UserService();
        normalRegisterTest();
        try {
            service.login(new UserData("taho", "password123", null));
            assertThrows(DataAccessException.class, ()-> service.logout("Definitely not an authToken"));
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    void normalCreateGame () {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        normalRegisterTest();
        try {
            AuthData user = userService.login(new UserData("taho", "password123", null));
            int gameNumber = gameService.createGame("Test Game", user.authToken());
            assertEquals(gameNumber, 1);
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    void notAuthCreateGame(){
        GameService gameService = new GameService();
        normalRegisterTest();

        assertThrows(DataAccessException.class, ()
                -> gameService.createGame("Test Game", "Not an authToken"));
    }

    @Test
    void normalJoinGame() {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        normalCreateGame();

        try {
            AuthData user = userService.login(new UserData("taho", "password123", null));
            int gameNumber = gameService.createGame("Test Game", user.authToken());
            gameService.joinGame("WHITE", gameNumber, user.authToken());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }

    }

    @Test
    void wrongNumberJoinGame() {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        normalCreateGame();

        try {
            AuthData user = userService.login(new UserData("taho", "password123", null));
            gameService.createGame("Test Game", user.authToken());
            assertThrows(DataAccessException.class, ()
                    ->gameService.joinGame("WHITE", 5, user.authToken()));
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    void normalListGames() {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        normalRegisterTest();
        try {
            AuthData user = userService.login(new UserData("taho", "password123", null));
            gameService.createGame("Test Game 1", user.authToken());
            gameService.createGame("Test Game 2", user.authToken());
            gameService.createGame("Test Game 3", user.authToken());

            HashSet<GameData> games = gameService.listGames(user.authToken());
            assertEquals(games.size(), 3);
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }

    }

    @Test
    void notAuthListGames() {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        normalRegisterTest();
        try {
            AuthData user = userService.login(new UserData("taho", "password123", null));
            gameService.createGame("Test Game 1", user.authToken());
            gameService.createGame("Test Game 2", user.authToken());
            gameService.createGame("Test Game 3", user.authToken());

            userService.logout(user.authToken());

            assertThrows(DataAccessException.class, () ->gameService.listGames(user.authToken()));
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }
}
