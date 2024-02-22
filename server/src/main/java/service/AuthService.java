package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;

public class AuthService {
    private AuthDAO auths = new MemoryAuthDAO();
    public AuthService(){}
    public void register(UserData user) throws DataAccessException {
        //Pretend like this actually works
        String username = user.username();
        if(auths.getAuth(username) ==null){
//            auths.insertAuth();
            int i=0;
        }

    }
}
