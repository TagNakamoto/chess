package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLUserTests {
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
}
