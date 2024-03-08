package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    @Override
    public void insertUser(UserData u) throws DataAccessException{
        String statement = "INSERT INTO users (username, password, email) values (?, ?, ?);";
        if(u.password()==null){
            throw new DataAccessException("Error: bad request");
        }
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(statement)){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(u.password());

            stmt.setString(1, u.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, u.email());

            if(stmt.executeUpdate() != 1){
                throw new DataAccessException("Error: Could not insert into user database");
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT password FROM users where username=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)){
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String hashedPassword = rs.getString(1);
                return new UserData(username, hashedPassword, null);
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
    public void clear() throws DataAccessException{
        String statement = "DELETE FROM users;";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    @Override
    public boolean isEmpty() throws DataAccessException {
      String statement = "SELECT COUNT(*) FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)){
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

}
