package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class SQLUserTests {
    @Test
    public void clearTest() {
        UserDAO userDAO = new SQLUserDAO();
        try{
            userDAO.clear();
        }
        catch(DataAccessException ex){
            fail("Unexpected exception:" + ex.getMessage());
        }

    }
}
