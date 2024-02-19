package server;

import spark.*;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private DataBase db = new DataBase();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        Spark.post("/register", (req, res) -> {
            Handler registerHandler = new Handler();
            return registerHandler.registerHandler(req, res);
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
