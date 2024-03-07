package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.HashSet;

public class GameService {
    static {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private static final AuthDAO auths = new SQLAuthDAO();
    private static final GameDAO games = new SQLGameDAO();
    public GameService(){}
    public int createGame(String gameName, String authToken) throws DataAccessException{
        AuthData authInMem = auths.getAuthFromToken(authToken);
        if (authInMem == null) {
            throw new DataAccessException("Error: unauthorized");
        } else {
            return games.insertGame(gameName);
        }
    }
    public void joinGame(String playerColor, int gameID, String authToken) throws DataAccessException{
        AuthData user = auths.getAuthFromToken(authToken);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        else{
            if(playerColor==null){
                games.addObserver(gameID, user.username());
            }
            else if (!playerColor.isEmpty()) {
                games.addPlayer(playerColor, gameID, user.username());
            }
            else {
                games.addObserver(gameID, user.username());
            }
        }
    }
    public HashSet<GameData> listGames(String authToken) throws DataAccessException{
        AuthData user = auths.getAuthFromToken(authToken);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        else{
            return games.listGames();
        }
    }
}
