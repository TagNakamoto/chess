package chess;

import java.util.ArrayList;
import java.util.Collection;

//Make class of ChessMovement, create subclasses for each piece type, piecemoves method in this class
public class ChessMovement {
    public ChessMovement(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        teamColor = board.getPiece(position).getTeamColor();
    }
    protected ChessBoard board;
    protected  ChessPosition position;
    protected ChessGame.TeamColor teamColor;

    public boolean noPiece(ChessPosition nextMove){
        return (board.getPiece(nextMove) == null);
    }
    public Boolean isSameTeam(ChessPosition nextMove){
        ChessGame.TeamColor moveColor = board.getPiece(nextMove).getTeamColor();
        return moveColor == teamColor;
    }
    protected void generateMovesInDirection(int rowIncrement, int colIncrement, ArrayList<ChessMove> moves) {
        int startRow = position.getRow();
        int startCol = position.getColumn();

        int i = 1;
        do {
            int newRow = startRow + i * rowIncrement;
            int newCol = startCol + i * colIncrement;

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessMove tempMove = new ChessMove(position, newPosition);

                if (noPiece(newPosition)) {
                    moves.add(tempMove);
                } else if (!isSameTeam(newPosition)) {
                    moves.add(tempMove);
                    break; // Stop if a piece is encountered, and it's not the same team, add to list
                } else {
                    break; // Stop if a piece is encountered (same team or not), do not add to list
                }

                i++;
            } else {
                break; // Stop if out of bounds
            }
        } while (true);
    }
}
class Bishop extends ChessMovement {
    public Bishop(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        generateMovesInDirection(1, 1, moves);  // Right Up
        generateMovesInDirection(-1, 1, moves); // Right Down
        generateMovesInDirection(1, -1, moves); // Left Up
        generateMovesInDirection(-1, -1, moves);// Left Down

        return moves;
    }
}

class Rook extends ChessMovement {
    public Rook(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        generateMovesInDirection(1, 0, moves);  // Up
        generateMovesInDirection(-1, 0, moves); // Down
        generateMovesInDirection(0, -1, moves); // Left
        generateMovesInDirection(0, 1, moves);// Right

        return moves;
    }
}

class Queen extends ChessMovement {
    public Queen(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        //Bishop moves
        generateMovesInDirection(1, 1, moves);  // Right Up
        generateMovesInDirection(-1, 1, moves); // Right Down
        generateMovesInDirection(1, -1, moves); // Left Up
        generateMovesInDirection(-1, -1, moves);// Left Down

        //Rook moves
        generateMovesInDirection(1, 0, moves);  // Up
        generateMovesInDirection(-1, 0, moves); // Down
        generateMovesInDirection(0, -1, moves); // Left
        generateMovesInDirection(0, 1, moves);// Right

        return moves;
    }
}

