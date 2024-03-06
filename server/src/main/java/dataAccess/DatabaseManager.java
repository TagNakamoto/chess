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
            String statement = "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(60) NOT NULL PRIMARY KEY," +
                    "password VARCHAR(60) NOT NULL," +
                    "email VARCHAR(60) NOT NULL" +
                    ");";
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    private static void createGamesTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
            String statement = "CREATE TABLE IF NOT EXISTS games (" +
                    "gameID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                    "whiteUsername VARCHAR(60)," +
                    "blackUsername VARCHAR(60)," +
                    "gameName VARCHAR(60) NOT NULL," +
                    "game VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (whiteUsername) REFERENCES users(username)," +
                    "FOREIGN KEY (blackUsername) REFERENCES users(username)" +
                    ");";
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static void createAuthsTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
            String statement = "CREATE TABLE IF NOT EXISTS auths (" +
                    "authToken VARCHAR(60) NOT NULL PRIMARY KEY," +
                    "username VARCHAR(60) NOT NULL," +
                    "FOREIGN KEY (username) REFERENCES users(username)" +
                    ");";
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void createObserversTable() throws DataAccessException {
        try (Connection conn = getConnection()) {
            String statement = "CREATE TABLE IF NOT EXISTS observers (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                    "gameID INTEGER NOT NULL," +
                    "username VARCHAR(60) NOT NULL," +
                    "FOREIGN KEY (gameID) REFERENCES games(gameID)," +
                    "FOREIGN KEY (username) REFERENCES users(username)" +
                    ");";
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


}
