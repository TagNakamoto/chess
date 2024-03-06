package dataAccess;

import model.AuthData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException{
        String statement = "INSERT INTO auths (authToken, username) values (?, ?);";
        if(auth.authToken()==null){
            throw new DataAccessException("Error: bad request");
        }
        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)){
            stmt.setString(1, auth.authToken());
            stmt.setString(2, auth.username());

            if(stmt.executeUpdate() != 1){
                throw new DataAccessException("Error: Could not insert into auth database");
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public void clear() throws DataAccessException{
        String statement = "DELETE FROM auths;";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)){
            stmt.executeUpdate();
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public boolean isEmpty() throws DataAccessException {
        String statement = "SELECT COUNT(*) FROM auths";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                return count==0;
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
        return false;
    }
    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        String statement = "DELETE FROM auths WHERE authToken=?;";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)){
            stmt.setString(1, authData.authToken());
            stmt.executeUpdate();
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException{
        String statement = "SELECT username FROM auths where authToken=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(statement)){
            stmt.setString(1, authToken);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return new AuthData(authToken, rs.getString(1));
            }
            else{
                return null;
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return "SQLAuthDAO{}";
    }
}
