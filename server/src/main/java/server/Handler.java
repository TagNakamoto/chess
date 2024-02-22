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
            res.status(500);
            return ex.getMessage();
        }

    }
    public String clearHandler (Request req, Response res){
        userServer.clear();
        res.status(200);
        return "";
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
