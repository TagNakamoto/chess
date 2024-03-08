package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PieceDeserializer implements JsonDeserializer<ChessPiece> {

    private static final HashMap<String, ChessPiece> pieceMap = new HashMap<>();

    static {
        pieceMap.put("K", new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        pieceMap.put("Q", new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        pieceMap.put("B", new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        pieceMap.put("N", new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        pieceMap.put("R", new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        pieceMap.put("P", new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        pieceMap.put("k", new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        pieceMap.put("q", new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        pieceMap.put("b", new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        pieceMap.put("n", new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        pieceMap.put("r", new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        pieceMap.put("p", new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
    }

    @Override
    public ChessPiece deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String pieceLetter = jsonObject.get("a").getAsString();
        return pieceMap.get(pieceLetter);
    }
}
