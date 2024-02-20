package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.RegisterInfo;
import spark.Request;
import spark.Response;

public class Handler {
    private Gson gson;
    public Handler() {
        gson = new Gson();
    }
    public String registerHandler (Request req, Response res){
        UserData regisObj = decodeJSON(req, UserData.class);
        String username = regisObj.username();
        String password = regisObj.password();
        String email = regisObj.email();

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
}
