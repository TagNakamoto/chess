package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO users = new MemoryUserDAO();
    private final AuthDAO auths = new MemoryAuthDAO();
    public UserService(){}
    public AuthData register(UserData user) throws DataAccessException{
        String username = user.username();
        if(users.getUser(username) ==null){
            users.insertUser(user);
            AuthData tempAuth = createAuth(username);
            auths.insertAuth(tempAuth);
            return tempAuth;
        }
        else{
            throw new DataAccessException("Error: already taken");
        }

    }
    public void clear() throws DataAccessException{
        users.clear();
        auths.clear();
    }
    public AuthData login(UserData user) throws DataAccessException{
        String username = user.username();
        UserData correctInfo = users.getUser(username);

        if(user.email()!=null){
            throw new DataAccessException("Error: email not required for this operation");
        }

        if(correctInfo == null || !user.password().equals(correctInfo.password())){
            throw new DataAccessException("Error: unauthorized");
        }
        else {
            AuthData tempAuth = createAuth(username);
            auths.insertAuth(tempAuth);
            return tempAuth;
        }

    }
    public void logout(String authToken) throws DataAccessException{
        AuthData authData = auths.getAuthFromToken(authToken);
        if(authData != null){
            auths.deleteAuth(authData);
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
    }
    private AuthData createAuth(String username){
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
