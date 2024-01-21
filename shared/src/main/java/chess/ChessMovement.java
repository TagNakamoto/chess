package chess;

import java.util.ArrayList;
import java.util.Collection;

//Make class of ChessMovement, create subclasses for each piece type, piecemoves method in this class
public class ChessMovement {
    protected ChessBoard board;
    protected  ChessPosition position;
    protected ChessGame.TeamColor teamColor;
    public ChessMovement(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
    public Boolean hasPiece(ChessPosition nextMove){
        return (board.getPiece(nextMove) != null);
    }
    public Boolean isBounds(ChessPosition nextMove){
        if((nextMove.getColumn() > 7) || (nextMove.getColumn() < 0)){
            return false;
        }
        else if((nextMove.getRow() > 7) || (nextMove.getRow() < 0)){
            return false;
        }
        else{
            return true;
        }
    }
    public Boolean isSameTeam(ChessPosition nextMove){
        ChessGame.TeamColor moveColor = board.getPiece(nextMove).getTeamColor();
        return moveColor == teamColor;
    }
    //Functions: isSameTeam
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