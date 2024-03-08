package dataAccess;

import chess.ChessGame;
import chess.ChessPiece;
import chess.PieceDeserializer;
import chess.PieceSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SQLGameDAO implements GameDAO {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ChessPiece.class, new PieceSerializer())
            .registerTypeAdapter(ChessPiece.class, new PieceDeserializer())
            .create();
    @Override
    public int insertGame(String gameName) throws DataAccessException{
        String statement = "INSERT INTO games (gameName, game) values (?, ?);";
        if(gameName==null){
            throw new DataAccessException("Error: bad request");
        }
        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, gameName);
            ChessGame game = new ChessGame();
            String gameString = gson.toJson(game);
            stmt.setString(2, gameString);
            if (stmt.executeUpdate() != 1) {
                throw new DataAccessException("Error: Could not insert into auth database");
            } else {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    return generatedKeys.getInt(1); // ID of the inserted game
                } catch (SQLException ex) {
                    throw new DataAccessException(ex.getMessage());
                }
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public void clear() throws DataAccessException {
        try {
            String statement = "DELETE FROM observers";
            try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
                stmt.executeUpdate();
            }

            statement = "DELETE FROM games";
            try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public void addObserver(int gameID, String username) throws DataAccessException{
        String statement = "INSERT INTO observers (gameID, username) values (?, ?);";
        if(gameID==0 || username==null){
            throw new DataAccessException("Error: bad request");
        }
        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
            stmt.setInt(1, gameID);
            stmt.setString(2, username);
            if (stmt.executeUpdate() != 1) {
                throw new DataAccessException("Error: Could not insert into auth database");
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public void addPlayer(String playerColor, int gameID, String playerName) throws DataAccessException {
        String statement = "UPDATE games " +
                "SET %s = ? " +  // Placeholder for column name
                "WHERE gameID = ?";
        if (gameID == 0 || playerName == null || playerColor == null) {
            throw new DataAccessException("Error: bad request");
        }

        String columnName;
        String currentWhiteUsername = "";
        String currentBlackUsername = "";
        String queryStatement = "SELECT whiteUsername, blackUsername FROM games WHERE gameID=(?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(queryStatement)){
            stmt.setInt(1, gameID);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                currentWhiteUsername = rs.getString(1);
                currentBlackUsername = rs.getString(2);
            }
            if (playerColor.equals("WHITE") && currentWhiteUsername == null ) {
                columnName = "whiteUsername";
            } else if (playerColor.equals("BLACK") && currentBlackUsername == null) {
                columnName = "blackUsername";
            } else {
                throw new DataAccessException("Error: already taken");
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }

        statement = String.format(statement, columnName);

        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, gameID);

            if (stmt.executeUpdate() != 1) {
                throw new DataAccessException("Error: Could not update game record");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        HashSet<GameData> gamesList = new HashSet<>();
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameString = rs.getString("game");
                ChessGame gameObject = gson.fromJson(gameString, ChessGame.class);
                gamesList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, gameObject));
            }
            return gamesList;
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

}
