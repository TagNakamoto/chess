package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    int insertGame(String gameName) throws DataAccessException;
    void clear() throws DataAccessException;
    void addObserver(int gameID, String username) throws DataAccessException;
    void addPlayer(String playerColor, int gameID, String playerName) throws DataAccessException;
    HashSet<GameData> listGames() throws DataAccessException;
    
}
