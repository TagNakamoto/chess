package chess;

import java.util.ArrayList;
import java.util.Collection;

//Make class of ChessMovement, create subclasses for each piece type, piecemoves method in this class
public class ChessMovement {
    protected ChessBoard board;
    protected  ChessPosition position;
    public ChessMovement(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
}
class Bishop extends ChessMovement {
    public Bishop(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition tempPosition = new ChessPosition(1,8);
        ChessMove tempMove = new ChessMove(position, tempPosition);
        moves.add(tempMove);
        return moves;
    }
}