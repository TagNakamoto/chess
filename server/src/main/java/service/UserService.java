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

        return null;
    }
    public void clear(){
        users.clear();
        auths.clear();
    }
    private AuthData createAuth(String username){
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
