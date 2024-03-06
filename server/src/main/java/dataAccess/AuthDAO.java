package dataAccess;


import model.AuthData;

public interface AuthDAO {
    void insertAuth(AuthData auth) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean isEmpty() throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;
    AuthData getAuthFromToken(String authToken) throws DataAccessException;
}
