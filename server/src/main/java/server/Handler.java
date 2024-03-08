package server;

import chess.ChessPiece;
import chess.PieceDeserializer;
import chess.PieceSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import java.util.HashSet;
import java.util.Objects;

public class Handler {
    private final Gson gson;
    private static final UserService userService = new UserService();
    private static final GameService gameService = new GameService();
    public Handler() {
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(ChessPiece.class, new PieceSerializer())
                .registerTypeAdapter(ChessPiece.class, new PieceDeserializer())
                .create();
    }
    public String registerHandler (Request req, Response res){
        UserData regisObj = decodeBodyJSON(req, UserData.class);
        try{
            String body = encodeJSON(userService.register(regisObj));
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
            userService.clear();
            res.status(200);
            return "";
        }
        catch (DataAccessException ex){
            res.status(500);
            return encodeJSON(ex);
        }


    }
    public String loginHandler (Request req, Response res){
        UserData loginObj = decodeBodyJSON(req, UserData.class);
        try{
            String body = encodeJSON(userService.login(loginObj));
            res.status(200);
            return body;
        }
        catch (DataAccessException ex){
            return encodeJSON(errorCatcher(ex,res));
        }
    }

    public String logoutHandler(Request req, Response res){
        String authToken = req.headers("authorization");
        try{
            userService.logout(authToken);
            res.status(200);
            return "";
        }
        catch (DataAccessException ex) {
            return encodeJSON(errorCatcher(ex,res));
        }
    }

    public String createGameHandler(Request req, Response res){
        String authToken = req.headers("authorization");
        GameData gameNameContainer = decodeBodyJSON(req, GameData.class);
        String gameName = gameNameContainer.gameName();
        if(gameName == null){
            res.status(400);
            Message errorMessage = new Message("Error: bad request");
            return encodeJSON(errorMessage);
        }
        else{
            try{
                int gameID = gameService.createGame(gameName, authToken);
                res.status(200);
                return encodeJSON(new GameData(gameID, null, null, null, null));
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
    }

    public String joinGameHandler(Request req, Response res){
        String authToken = req.headers("authorization");
        JoinRequest joinRequest = decodeBodyJSON(req, JoinRequest.class);
        String playerColor = joinRequest.playerColor();
        int gameID = joinRequest.gameID();
        if(gameID ==0){
            res.status(400);
            return  encodeJSON(new Message("Error: bad request"));
        }
        else{
            try {
                gameService.joinGame(playerColor, gameID, authToken);
                res.status(200);
                return "";
            }
            catch (DataAccessException ex){
                String errorString = ex.getMessage();
                Message messageObject = new Message(errorString);
                switch (errorString) {
                    case "Error: unauthorized" -> res.status(401);
                    case "Error: bad request" -> res.status(400);
                    case "Error: already taken" -> res.status(403);
                    default -> res.status(500);
                }

                return encodeJSON(messageObject);
            }
        }
    }

    public String listGamesHandler(Request req, Response res){
        String authToken = req.headers("authorization");
        HashSet<GameData> gamesList;
        try{
            gamesList = gameService.listGames(authToken);
            return encodeJSON(new GamesList(gamesList));
        }
        catch (DataAccessException ex){
            return encodeJSON(errorCatcher(ex,res));
        }
    }

    private <T> T decodeBodyJSON(Request req, Class<T> clazz) {
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

    private Message errorCatcher(DataAccessException ex, Response res){
        String errorString = ex.getMessage();
        Message messageObject = new Message(errorString);
        if(errorString.equals("Error: unauthorized")){
            res.status(401);
        }
        else {
            res.status(500);
        }
        return messageObject;
    }
    @Override
    public String toString() {
        return "Handler{" +
                "gson=" + gson +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Handler handler = (Handler) o;
        return Objects.equals(gson, handler.gson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gson);
    }
}
