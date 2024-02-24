package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    private static final Map<String, AuthData> authTokens = new HashMap<>();
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException{
        if(auth.username() == null || auth.authToken()==null){
            throw new DataAccessException("bad request");
        }
        else {
            authTokens.put(auth.username(), auth);
        }
    }
    @Override
    public AuthData getAuth(String username){
        return authTokens.get(username);
    }
    @Override
    public void clear() throws DataAccessException{
        authTokens.clear();
    }
    @Override
    public boolean isEmpty(){
        return authTokens.isEmpty();
    }
}
