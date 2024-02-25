package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{
    private static int numGames = 1;
    private static final Map<Integer, GameData> games = new HashMap<>();
    private static final Map<Integer, Set<String>> observers = new HashMap<>();
    @Override
    public int insertGame(String gameName){
        GameData tempGame = new GameData(numGames, null,null, gameName, new ChessGame());
        games.put(numGames, tempGame);
        int tempNum = numGames;
        numGames++;
        return tempNum;
    }
    @Override
    public void clear(){
        games.clear();
        observers.clear();
        numGames = 1;
    }
    @Override
    public void addObserver(int gameID, String username) throws DataAccessException{
        if(!games.containsKey(gameID)){
            throw new DataAccessException("Error: bad request");
        }
        else {
            observers.merge(gameID, new HashSet<>(Collections.singletonList(username)), (existingSet, unused) -> {
                existingSet.add(username);
                return existingSet;
            });
        }
    }
    @Override
    public void addPlayer(String playerColor, int gameID, String playerName) throws DataAccessException{
        if(!games.containsKey(gameID)){
            throw new DataAccessException("Error: bad request");
        }
        else{
            GameData updateGame = games.get(gameID);
            if(playerColor.equals("White") && updateGame.whiteUsername() == null){
                games.put(gameID, new GameData(gameID, playerName, updateGame.blackUsername(),
                        updateGame.gameName(), updateGame.game()));
            }
            else if(playerColor.equals("Black") && updateGame.blackUsername() == null){
                games.put(gameID, new GameData(gameID, updateGame.whiteUsername(), playerName,
                        updateGame.gameName(), updateGame.game()));
            }
            else if(playerColor.equals("White") || playerColor.equals("Black")){
                throw new DataAccessException("Error: already taken");
            }
            else{
                throw new DataAccessException("Error: bad request");
            }
        }
    }
}
