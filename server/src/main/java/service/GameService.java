package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    private static final UserDAO users = new MemoryUserDAO();
    private static final AuthDAO auths = new MemoryAuthDAO();
    private static final GameDAO games = new MemoryGameDAO();
    public GameService(){}
    public int createGame(String gameName, String authToken) throws DataAccessException{
        AuthData authInMem = auths.getAuthFromToken(authToken);
        if(authInMem != null){
            return games.insertGame(gameName);
        }
        else{
            throw new DataAccessException("Error: unauthorized");
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
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException{
        AuthData user = auths.getAuthFromToken(authToken);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        else{
            return games.listGames();
        }
    }
}
