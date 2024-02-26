package dataAccess;

import model.AuthData;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO{
    private static final Map<String, String> authTokens = new HashMap<>();
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException{
        if(auth.username() == null || auth.authToken()==null){
            throw new DataAccessException("bad request");
        }
        else {
            authTokens.put(auth.authToken(), auth.username());
        }
    }
    @Override
    public void clear() throws DataAccessException{
        authTokens.clear();
    }
    @Override
    public boolean isEmpty(){
        return authTokens.isEmpty();
    }
    @Override
    public void deleteAuth(AuthData authData){
        authTokens.remove(authData.authToken(), authData.username());
    }
    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException{
        String username = authTokens.get(authToken);

        if(username !=null) {
            return new AuthData(authToken, authTokens.get(authToken));
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public String toString() {
        return "MemoryAuthDAO{}";
    }

}
