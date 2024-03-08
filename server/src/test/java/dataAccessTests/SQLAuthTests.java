package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {

    @BeforeEach
    public void setup(){
        clearTest();
    }

    @Test
    public void clearTest() {
        UserDAO userDAO = new SQLUserDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        GameDAO gameDAO = new SQLGameDAO();
        try{
            gameDAO.clear();
            authDAO.clear();
            userDAO.clear();
            assertTrue(userDAO.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void normalInsertAuthTest(){
        AuthDAO authDAO = new SQLAuthDAO();
        UserDAO userDAO = new SQLUserDAO();
        try{
            AuthData newAuth = new AuthData("Pretend authToken", "normal username");
            UserData newUser = new UserData("normal username", "password", "email");

            userDAO.insertUser(newUser);

            authDAO.insertAuth(newAuth);
            AuthData retrievedAuth = authDAO.getAuthFromToken(newAuth.authToken());

            assertEquals(newUser.username(), retrievedAuth.username());
            assertEquals(newAuth.authToken(), retrievedAuth.authToken());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void noAuthTokenInsertUserTest(){
        AuthDAO authDAO = new SQLAuthDAO();

        assertThrows(DataAccessException.class, ()-> authDAO.getAuthFromToken("Invalid AuthToken"));

    }

    @Test
    public void emptyTableTest(){
        AuthDAO authDAO = new SQLAuthDAO();
        try {
            assertTrue(authDAO.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void notEmptyTableTest() {
        AuthDAO authDAO = new SQLAuthDAO();
        UserDAO userDAO = new SQLUserDAO();
        try {
            AuthData newAuth = new AuthData("Pretend authToken", "normal username");
            UserData newUser = new UserData("normal username", "password", "email");

            userDAO.insertUser(newUser);

            authDAO.insertAuth(newAuth);
            assertFalse(authDAO.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void normalDeleteAuth() {
        AuthDAO authDAO = new SQLAuthDAO();
        UserDAO userDAO = new SQLUserDAO();
        try {
            AuthData newAuth = new AuthData("Pretend authToken", "normal username");
            UserData newUser = new UserData("normal username", "password", "email");

            userDAO.insertUser(newUser);
            authDAO.insertAuth(newAuth);
            authDAO.deleteAuth(newAuth);
            assertTrue(authDAO.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void noAuthDeleteAuth() {
        AuthDAO authDAO = new SQLAuthDAO();

        AuthData newAuth = new AuthData("Pretend authToken", "normal username");
        assertThrows(DataAccessException.class, ()-> authDAO.deleteAuth(newAuth));

    }
}
