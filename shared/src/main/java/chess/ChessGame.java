package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard myBoard;
    private TeamColor turnColor;
    public ChessGame() {
        turnColor = TeamColor.WHITE;
        myBoard = new ChessBoard();
        myBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = myBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        TeamColor currTeam = piece.getTeamColor();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(myBoard, startPosition);
        Iterator<ChessMove> iterator = possibleMoves.iterator();

        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            ChessPosition endSquare = move.getEndPosition();
            ChessPiece capturedPiece = myBoard.getPiece(endSquare); // Store the piece being captured

            // Temporarily make the move
            if(move.getPromotionPiece() == null) {
                myBoard.addPiece(endSquare, myBoard.getPiece(startPosition));
            }
            else{
                ChessPiece tempPiece = new ChessPiece(currTeam, move.getPromotionPiece());
                myBoard.addPiece(endSquare, tempPiece);
            }
            myBoard.removePiece(startPosition);

            // Check if the player's king is in check after making the move
            if (isInCheck(currTeam)) {
                // Move puts the king in check, so remove it from possible moves
                iterator.remove();
            }

            // Undo the temporary move
            myBoard.addPiece(startPosition, myBoard.getPiece(endSquare));
            myBoard.removePiece(endSquare);
            if (capturedPiece != null) {
                myBoard.addPiece(endSquare, capturedPiece); // Restore the captured piece if any
            }
        }

        return possibleMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        myBoard.addPiece(move.getEndPosition(), myBoard.getPiece(move.getStartPosition()));
        myBoard.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++){
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece piece = myBoard.getPiece(currPosition);
                if(piece == null){
                    continue;
                }
                Collection<ChessMove> moves = piece.pieceMoves(myBoard, currPosition);
                for (ChessMove move : moves){
                    if(move.getEndPosition().equals(kingPosition))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

    public ChessPosition findKing(TeamColor teamColor){
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                ChessPosition tempPosition = new ChessPosition(i,j);
                ChessPiece tempPiece =myBoard.getPiece(tempPosition);
                if(tempPiece == null) {
                }
                else if(tempPiece.getPieceType() == ChessPiece.PieceType.KING && (tempPiece.getTeamColor()==teamColor)) {
                    return tempPosition;
                }
            }
        }
        throw new RuntimeException("King not found");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(myBoard, chessGame.myBoard) && turnColor == chessGame.turnColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myBoard, turnColor);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "myBoard=" + myBoard +
                ", turnColor=" + turnColor +
                '}';
    }
}
