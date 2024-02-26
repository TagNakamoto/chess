package dataAccess;


import model.AuthData;

public interface AuthDAO {
    void insertAuth(AuthData auth) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean isEmpty();
    void deleteAuth(AuthData authData) ;
    AuthData getAuthFromToken(String authToken) throws DataAccessException;
}
