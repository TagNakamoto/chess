package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType { //data type
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) { //TOUGH ONE, need board
        ChessPiece piece = board.getPiece(myPosition);
        //use switch statement to switch into relevant subclass, return what pieceMoves in subclass returns
        Collection<ChessMove> moves = new ArrayList<>();
        switch (piece.type) {
            case BISHOP:
//                System.out.println("Bishop");
                Bishop bishop = new Bishop(board, myPosition);
                moves = bishop.pieceMoves();
                break;
            case ROOK:
//                System.out.println("Rook");
                Rook rook = new Rook(board, myPosition);
                moves = rook.pieceMoves();
                break;
            case QUEEN:
//                System.out.println("Queen");
                Queen queen = new Queen(board, myPosition);
                moves = queen.pieceMoves();
                break;
            case KNIGHT:
//                System.out.println("Knight");
                Knight knight = new Knight(board, myPosition);
                moves = knight.pieceMoves();
                break;
            case KING:
//                System.out.println("King");
                King king = new King(board, myPosition);
                moves = king.pieceMoves();
                break;
            case PAWN:
//                System.out.println("King");
                if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
                    BlackPawn blackPawn = new BlackPawn(board, myPosition);
                    moves = blackPawn.pieceMoves();
                } else if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
                    WhitePawn whitePawn = new WhitePawn(board, myPosition);
                    moves = whitePawn.pieceMoves();
                }
                break;
        }


        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return switch (type) {
            case KING -> pieceColor == ChessGame.TeamColor.WHITE ? "K" : "k";
            case QUEEN -> pieceColor == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case ROOK -> pieceColor == ChessGame.TeamColor.WHITE ? "R" : "r";
            case BISHOP -> pieceColor == ChessGame.TeamColor.WHITE ? "B" : "b";
            case KNIGHT -> pieceColor == ChessGame.TeamColor.WHITE ? "N" : "n";
            case PAWN -> pieceColor == ChessGame.TeamColor.WHITE ? "P" : "p";
        };

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}



