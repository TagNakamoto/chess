package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class Handler {
    private Gson gson;
    private static UserService userServer = new UserService();
    public Handler() {
        gson = new Gson();
    }
    public String registerHandler (Request req, Response res){
        UserData regisObj = decodeJSON(req, UserData.class);
        try{
            String body = encodeJSON(userServer.register(regisObj));
            res.status(200);
            return body;
        }
        catch (DataAccessException ex){
            String errorString = ex.getMessage();
            Message messageObject = new Message(errorString);
            if(errorString.equals("Error: bad request")){
                res.status(400);
            }
            else if(errorString.equals("Error: already taken")){
                res.status(403);
            }
            else {
                res.status(500);
            }

            return encodeJSON(messageObject);
        }

    }
    public String clearHandler (Request req, Response res){
        try {
            userServer.clear();
            res.status(200);
            return "";
        }
        catch (DataAccessException ex){
            res.status(500);
            return encodeJSON(ex);
        }


    }
    public String loginHandler (Request req, Response res){
        UserData loginObj = decodeJSON(req, UserData.class);
        try{
            String body = encodeJSON(userServer.login(loginObj));
            res.status(200);
            return body;
        }
        catch (DataAccessException ex){
            String errorString = ex.getMessage();
            Message messageObject = new Message(errorString);
            if(errorString.equals("Error: unauthorized")){
                res.status(401);
            }
            else {
                res.status(500);
            }

            return encodeJSON(messageObject);
        }
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
    private String encodeJSON (Object object){
        return gson.toJson(object);
    }
    private AuthData createAuth(String username){
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
