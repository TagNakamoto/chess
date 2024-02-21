package dataAccess;

import model.UserData;

import java.util.Map;


public class MemoryUserDAO implements UserDAO{
    private Map<String, UserData> users;
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
