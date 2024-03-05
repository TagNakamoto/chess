package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            createUsersTable();
            createAuthsTable();
            createGamesTable();
            createObserversTable();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static void createUsersTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
            var statement = "CREATE TABLE IF NOT EXISTS users" +
                    "(" +
                    "username varchar(60) not null primary key," +
                    "password varchar(60) not null," +
                    "email varchar(60) not null" +
                    ");";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    static void createGamesTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
            var statement = "CREATE TABLE IF NOT EXISTS games" +
                    "(" +
                    "gameID integer not null primary key auto increment," +
                    "whiteUsername varchar(60)," +
                    "blackUsername varchar(60)," +
                    "gameName varchar(60) not null," +
                    "game varchar(255) not null ," +
                    "foreign key(whiteUsername) references users(username)," +
                    "foreign key(blackUsername) references users(username)" +
                    ");";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    static void createAuthsTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
            var statement = "CREATE TABLE IF NOT EXISTS auths" +
                    "(" +
                    "authToken varchar(60) not null primary key," +
                    "username varchar(60) not null," +
                    "foreign key(username) references users(username)" +
                    ");";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    static void createObserversTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
        var statement = "CREATE TABLE IF NOT EXISTS observers" +
                "(" +
                "id integer not null primary key auto increment," +
                "gameID integer not null," +
                "username varchar(60) not null," +
                "foreign key(gameID) references games(gameID)," +
                "foreign key(username) references users(username)" +
                ");";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }
        catch (SQLException ex){
        throw new DataAccessException(ex.getMessage());
    }
}


}
