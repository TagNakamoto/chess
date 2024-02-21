package dataAccess;

import model.AuthData;

import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    private Map<String, AuthData> authTokens;
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException{
        authTokens.put(auth.username(), auth);
    }
    @Override
    public AuthData getAuth(String username){
        return authTokens.get(username);
    }
    @Override
    public void clear(){

    }
}
