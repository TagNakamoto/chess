package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserTests {
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
    public void normalInsertUserTest(){
        UserDAO userDAO = new SQLUserDAO();
        try{
            UserData newUser = new UserData("Normal user", "password", "email");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            userDAO.insertUser(newUser);
            UserData retrievedUser = userDAO.getUser(newUser.username());

            assertEquals(newUser.username(), retrievedUser.username());
            assertTrue(encoder.matches(newUser.password(), retrievedUser.password()));
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void noPasswordInsertUserTest(){
        UserDAO userDAO = new SQLUserDAO();

        UserData newUser = new UserData("user no password", null, "email");

        assertThrows(DataAccessException.class, ()-> userDAO.insertUser(newUser));

    }

    @Test
    public void normalGetUserTest(){
        UserDAO userDAO = new SQLUserDAO();
        try{
            UserData newUser = new UserData("Normal user", "password", "email");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            userDAO.insertUser(newUser);
            UserData retrievedUser = userDAO.getUser(newUser.username());

            assertEquals(newUser.username(), retrievedUser.username());
            assertTrue(encoder.matches(newUser.password(), retrievedUser.password()));
            assertNull(retrievedUser.email());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void userDNEGetUserTest(){
        UserDAO userDAO = new SQLUserDAO();
        UserData userNotInDatabase = new UserData("Not in system", "not a real password", "email");
        try{
        assertNull(userDAO.getUser(userNotInDatabase.username()));
        }
        catch(DataAccessException ex){
        fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void emptyTableTest(){
        UserDAO userDAO = new SQLUserDAO();
        try {
            assertTrue(userDAO.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void notEmptyTableTest() {
        UserDAO userDAO = new SQLUserDAO();
        try {
            UserData newUser = new UserData("Normal user", "password", "email");

            userDAO.insertUser(newUser);
            assertFalse(userDAO.isEmpty());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }
}
