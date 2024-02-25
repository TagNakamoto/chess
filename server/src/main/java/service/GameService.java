package service;

import dataAccess.*;
import model.AuthData;

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
            if(playerColor.isEmpty()){
                games.addObserver(gameID, user.username());
            }
            else{
                games.addPlayer(playerColor, gameID, user.username());
            }
        }
    }

}
