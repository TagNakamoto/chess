package server;

import spark.*;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private static Handler handler = new Handler();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> handler.registerHandler(req, res));
        Spark.delete("/db",(req, res) -> handler.clearHandler(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
