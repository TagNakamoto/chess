import chess.*;
import server.Server;
import ui.UIMenu;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

        UIMenu menu = new UIMenu();
        menu.run();
        server.stop();
    }
}