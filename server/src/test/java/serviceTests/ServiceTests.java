package serviceTests;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

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
}
