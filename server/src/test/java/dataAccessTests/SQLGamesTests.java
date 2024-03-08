package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class SQLGamesTests {

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
    public void normalInsertGame(){
        GameDAO gameDAO = new SQLGameDAO();
        String gameName = "This is a game name";
        try{
            int gameID = gameDAO.insertGame(gameName);
            assertTrue(gameID>0);
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void noNameInsertGame(){
        GameDAO gameDAO = new SQLGameDAO();
        assertThrows(DataAccessException.class, ()->gameDAO.insertGame(null));
    }

    @Test
    public void normalAddObserverTest(){
        UserDAO userDAO = new SQLUserDAO();
        GameDAO gameDAO = new SQLGameDAO();
        String gameName = "This is a game name";
        try{
            UserData newObserver = new UserData("observer", "password", "email");
            userDAO.insertUser(newObserver);
            int gameID = gameDAO.insertGame(gameName);
            gameDAO.addObserver(gameID, newObserver.username());
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }

    @Test
    public void userDNEAddObserverTest(){
        GameDAO gameDAO = new SQLGameDAO();
        String gameName = "This is a game name";
        try{
            int gameID = gameDAO.insertGame(gameName);
            assertThrows(DataAccessException.class, ()-> gameDAO.addObserver(gameID, "username DNE"));
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }
    }
}
