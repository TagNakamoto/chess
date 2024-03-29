package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserService {
    static {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private static final UserDAO users = new SQLUserDAO();
    private static final AuthDAO auths = new SQLAuthDAO();
    private static final GameDAO games = new SQLGameDAO();
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
        games.clear();
        auths.clear();
        users.clear();
    }
    public AuthData login(UserData user) throws DataAccessException{
        String username = user.username();
        UserData correctInfo = users.getUser(username);

        if(user.email()!=null){
            throw new DataAccessException("Error: email not required for this operation");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(user.password()==null){
            throw new DataAccessException("Error: unauthorized");
        }
        else if(correctInfo == null || !encoder.matches(user.password(), correctInfo.password())){
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

    @Override
    public String toString() {
        return "UserService{}";
    }
}
