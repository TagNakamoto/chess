package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    int insertGame(String gameName);
    void clear();
    void addObserver(int gameID, String username) throws DataAccessException;
    void addPlayer(String playerColor, int gameID, String playerName) throws DataAccessException;
    ArrayList<GameData> listGames();
    
}
