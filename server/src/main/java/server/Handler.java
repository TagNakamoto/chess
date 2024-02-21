package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class Handler {
    private Gson gson;
    private UserDAO userDAO = new MemoryUserDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();
    public Handler() {
        gson = new Gson();
    }
    public String registerHandler (Request req, Response res){
        UserData regisObj = decodeJSON(req, UserData.class);
        String username = regisObj.username();


        if(userDAO.getUser(username) !=null){
            return "User already exists";
        }
        else{
            try {
                userDAO.insertUser(regisObj);
                authDAO.insertAuth(createAuth(username));
            }
            catch(DataAccessException ex){
                return "400" + ex.getMessage();
            }
        }



        return " ";
    }
    private <T> T decodeJSON (Request req, Class<T> clazz) {
        try {
            String body = req.body();
            return gson.fromJson(body,clazz);
        }
        catch (JsonSyntaxException ex){
            throw  new IllegalArgumentException("Invalid JSON format: " + ex.getMessage());
        }
    }
    private AuthData createAuth(String username){
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
