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
public class ChessPiece implements Cloneable{
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
        //System.out.println(myPosition);
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        //use switch statement to switch into relevant subclass, return what piecemoves in subclass returns
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        switch (piece.type) {
            case BISHOP:
//                System.out.println("Bishop");
                Bishop Bishop = new Bishop(board, myPosition);
                moves = Bishop.pieceMoves();
                break;
            case ROOK:
//                System.out.println("Rook");
                Rook Rook = new Rook(board, myPosition);
                moves = Rook.pieceMoves();
                break;
            case QUEEN:
//                System.out.println("Queen");
                Queen Queen = new Queen(board, myPosition);
                moves = Queen.pieceMoves();
                break;
            case KNIGHT:
//                System.out.println("Knight");
                Knight Knight = new Knight(board, myPosition);
                moves = Knight.pieceMoves();
                break;
            case KING:
//                System.out.println("King");
                King King = new King(board, myPosition);
                moves = King.pieceMoves();
                break;
            case PAWN:
//                System.out.println("King");
                if(piece.pieceColor == ChessGame.TeamColor.BLACK){
                    BlackPawn BlackPawn = new BlackPawn(board, myPosition);
                    moves = BlackPawn.pieceMoves();
                }
                else if(piece.pieceColor == ChessGame.TeamColor.WHITE){
                    WhitePawn WhitePawn = new WhitePawn(board, myPosition);
                    moves = WhitePawn.pieceMoves();
                }
                break;
        }



        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type + ", " +
                '}';
    }
    @Override
    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}



