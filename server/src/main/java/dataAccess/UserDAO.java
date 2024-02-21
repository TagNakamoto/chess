package dataAccess;

import model.UserData;

public interface UserDAO {
    void insertUser(UserData u) throws DataAccessException;
    UserData getUser(String username);
    void clear();

}
