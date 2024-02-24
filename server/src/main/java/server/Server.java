package server;

import spark.*;

public class Server {
    private static Handler handler = new Handler();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> handler.registerHandler(req, res));
        Spark.delete("/db",(req, res) -> handler.clearHandler(req, res));
        Spark.post("/session", (req, res) ->handler.loginHandler(req, res));
        Spark.delete("/session", (req, res) -> handler.logoutHandler(req, res));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
