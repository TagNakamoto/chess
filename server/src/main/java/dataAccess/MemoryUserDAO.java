package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements UserDAO{
    private static Map<String, UserData> users = new HashMap<>();
    @Override
    public void insertUser(UserData u){
        users.put(u.username(), u);
    }
    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
    @Override
    public void clear(){
        users.clear();
    }

}
