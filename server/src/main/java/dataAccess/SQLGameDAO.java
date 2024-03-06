package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class SQLGameDAO implements GameDAO {
    @Override
    public int insertGame(String gameName){
//        GameData tempGame = new GameData(numGames, null,null, gameName, new ChessGame());
//        games.put(numGames, tempGame);
//        int tempNum = numGames;
//        numGames++;
//        return tempNum;
    }
    @Override
    public void clear(){
//        games.clear();
//        observers.clear();
//        numGames = 1;
    }
    @Override
    public void addObserver(int gameID, String username) throws DataAccessException{
//        if(!games.containsKey(gameID)){
//            throw new DataAccessException("Error: bad request");
//        }
//        else {
//            observers.merge(gameID, new HashSet<>(Collections.singletonList(username)), (existingSet, unused) -> {
//                existingSet.add(username);
//                return existingSet;
//            });
//        }
    }
    @Override
    public void addPlayer(String playerColor, int gameID, String playerName) throws DataAccessException{
//        if(!games.containsKey(gameID)){
//            throw new DataAccessException("Error: bad request");
//        }
//        else{
//            GameData updateGame = games.get(gameID);
//            if(playerColor.equals("WHITE") && updateGame.whiteUsername() == null){
//                games.put(gameID, new GameData(gameID, playerName, updateGame.blackUsername(),
//                        updateGame.gameName(), updateGame.game()));
//            }
//            else if(playerColor.equals("BLACK") && updateGame.blackUsername() == null){
//                games.put(gameID, new GameData(gameID, updateGame.whiteUsername(), playerName,
//                        updateGame.gameName(), updateGame.game()));
//            }
//            else if(playerColor.equals("WHITE") || playerColor.equals("BLACK")){
//                throw new DataAccessException("Error: already taken");
//            }
//            else{
//                throw new DataAccessException("Error: bad request");
//            }
//        }
    }

    @Override
    public HashSet<GameData> listGames(){
//        HashSet<GameData> gamesList = new HashSet<>();
//        Set<Integer> gameIDs = games.keySet();
//        for(Integer gameID : gameIDs){
//            gamesList.add(games.get(gameID));
//        }
//        return gamesList;
    }
}
