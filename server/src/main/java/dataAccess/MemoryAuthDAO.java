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
            authTokens.put(auth.username(), auth.authToken());
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
        authTokens.remove(authData.username(), authData.authToken());
    }
    @Override
    public AuthData getAuthFromToken(String authToken){
        String username = "";

        for (Map.Entry<String, String> entry : authTokens.entrySet()) {
            if (authToken.equals(entry.getValue())) {
                username = entry.getKey();
            }
        }

        if(!Objects.equals(username, "")) {
            return new AuthData(authToken, username);
        }
        return null;
    }
}
