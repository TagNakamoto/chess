package chess;

import com.google.gson.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PieceSerializer implements JsonSerializer<ChessPiece> {
    private static final HashMap<ChessPiece, String> letterMap = new HashMap<>();
    static {
        letterMap.put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING), "K");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN), "Q");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP), "B");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT), "N");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK), "R");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN), "P");

        letterMap.put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING), "k");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN), "q");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP), "b");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT), "n");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK), "r");
        letterMap.put(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN), "p");
    }
    @Override
    public JsonElement serialize(ChessPiece piece, Type ChessPiece, JsonSerializationContext context){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("a", letterMap.get(piece));
        return jsonObject;
    }
}
