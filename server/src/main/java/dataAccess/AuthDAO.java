package dataAccess;


import model.AuthData;

public interface AuthDAO {
    void insertAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String username);
    void clear() throws DataAccessException;
    boolean isEmpty();
}
