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
    private boolean blackARookUnmoved;
    private boolean blackHRookUnmoved;
    private boolean whiteARookUnmoved;
    private boolean whiteHRookUnmoved;
    private boolean whiteKingUnmoved;
    private boolean blackKingUnmoved;
    private ChessMove lastMove;
    public ChessGame() {
        turnColor = TeamColor.WHITE;
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        blackARookUnmoved = true;
        blackHRookUnmoved = true;
        whiteARookUnmoved = true;
        whiteHRookUnmoved = true;
        whiteKingUnmoved = true;
        blackKingUnmoved = true;
        lastMove = null;
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
     * startPositionF
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
        if(piece.getPieceType() == ChessPiece.PieceType.KING) {
            possibleMoves.addAll(castleMoves(currTeam));
        }
        else if(piece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove !=null){
            ChessPosition endPosition = lastMove.getEndPosition();
            if(myBoard.getPiece(endPosition) != null) {
                if (myBoard.getPiece(endPosition).getPieceType() == ChessPiece.PieceType.PAWN && Math.abs(endPosition.getRow() - lastMove.getStartPosition().getRow()) == 2) {
                    if (Math.abs(lastMove.getEndPosition().getColumn() - startPosition.getColumn()) == 1 && endPosition.getRow() == startPosition.getRow()) {
                        ChessPosition captureSquare;
                        if (currTeam == TeamColor.BLACK) {
                            captureSquare = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn());
                        } else {
                            captureSquare = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn());
                        }
                        ChessMove enPassant = new ChessMove(startPosition, captureSquare);
                        makeEnPassant(enPassant);
                        if (!isInCheck(currTeam)) {
                            possibleMoves.add(enPassant);
                        }
                        undoEnPassant(enPassant);
                    }
                }
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
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = myBoard.getPiece(startPosition);
        ChessPiece.PieceType promoted = move.getPromotionPiece();
        boolean validMove = validMoves(startPosition).contains(move);
        if(castleMoves(turnColor).contains(move)){
            makeCastleMove(move);
        }
        else if(validMove && (piece.getTeamColor()==turnColor)) {

            //If rook is moved or taken, change boolean to false
            if(((startPosition.getRow()==8)&&(startPosition.getColumn()==8))||(((endPosition.getRow()==8)&&(endPosition.getColumn()==8))))
                blackHRookUnmoved = false;
            if(((startPosition.getRow()==8)&&(startPosition.getColumn()==1))||(((endPosition.getRow()==8)&&(endPosition.getColumn()==1))))
                blackARookUnmoved = false;
            if(((startPosition.getRow()==1)&&(startPosition.getColumn()==8))||(((endPosition.getRow()==1)&&(endPosition.getColumn()==8))))
                whiteHRookUnmoved = false;
            if(((startPosition.getRow()==1)&&(startPosition.getColumn()==1))||(((endPosition.getRow()==1)&&(endPosition.getColumn()==1))))
                whiteARookUnmoved = false;

            //If king is moved change boolean to false
            if(startPosition.getRow()==1 && startPosition.getColumn()==5)
                whiteKingUnmoved = false;
            if(startPosition.getRow()==8 && startPosition.getColumn()==5)
                blackKingUnmoved = false;

            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && myBoard.getPiece(endPosition) == null && endPosition.getColumn()!=startPosition.getColumn()) {
                makeEnPassant(move);
            }
            else if(promoted ==null) {
                myBoard.addPiece(endPosition, myBoard.getPiece(startPosition));
                myBoard.removePiece(move.getStartPosition());
            }
            else{
                myBoard.addPiece(endPosition, new ChessPiece(myBoard.getPiece(startPosition).getTeamColor(), promoted));
                myBoard.removePiece(move.getStartPosition());
            }
            if(turnColor==TeamColor.BLACK){
                setTeamTurn(TeamColor.WHITE);
            }
            else{
                setTeamTurn(TeamColor.BLACK);
            }
        }
        else{
            throw new InvalidMoveException("Move not valid");
        }
        lastMove = move;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        if(kingPosition == null){
            return false;
        }
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
        if(isInCheck(teamColor)){
            return hasNoMoves(teamColor);
        }
        else {
            return false;
        }
    }

    private boolean hasNoMoves(TeamColor teamColor) {
        for (int i=1; i<9; i++) { //goes through all squares, if piece is of team, see valid moves, if not empty, return false, otherwise return true
            for (int j = 1; j < 9; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = myBoard.getPiece(tempPosition);
                if (tempPiece != null) {
                    TeamColor pieceColor = tempPiece.getTeamColor();
                    if (pieceColor == teamColor) {
                        if (!validMoves(tempPosition).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        else {
            return hasNoMoves(teamColor);
        }
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
                if(tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && (tempPiece.getTeamColor()==teamColor)) {
                    return tempPosition;
                }
            }
        }
        return null;
    }

    private Collection<ChessMove> castleMoves(TeamColor teamColor){
        if (turnColor == TeamColor.BLACK) {
            ChessPosition blackKing = findKing(TeamColor.BLACK);
            if (blackKing != null && (blackKing.getColumn() != 5 || blackKing.getRow() != 8)) {
                blackKingUnmoved = false;
            }
        } else if (turnColor == TeamColor.WHITE) {
            ChessPosition whiteKing = findKing(TeamColor.WHITE);
            if (whiteKing != null && (whiteKing.getColumn() != 5 || whiteKing.getRow() != 1)) {
                whiteKingUnmoved = false;
            }
        }
        ArrayList<ChessMove> castleMoves = new ArrayList<>();
        if(teamColor == TeamColor.BLACK){
            if(blackKingUnmoved && !isInCheck(teamColor)){
                ChessPosition kingSquare = new ChessPosition(8,5);
                ChessPiece blackKing = myBoard.getPiece(kingSquare);
                if(blackHRookUnmoved){
                    ChessPosition f8 = new ChessPosition(8,6);
                    ChessPosition g8 = new ChessPosition(8,7);
                    generateValidCastles(teamColor, castleMoves, kingSquare, blackKing, f8, g8);
                }
                if(blackARookUnmoved){
                    ChessPosition d8 = new ChessPosition(8,4);
                    ChessPosition c8 = new ChessPosition(8,3);
                    generateValidCastles(teamColor, castleMoves, kingSquare, blackKing, d8, c8);
                }
            }
        }
        else{
            if(whiteKingUnmoved && !isInCheck(teamColor)){
                ChessPosition kingSquare = new ChessPosition(1,5);
                ChessPiece whiteKing = myBoard.getPiece(kingSquare);
                if(whiteHRookUnmoved){
                    ChessPosition f1 = new ChessPosition(1,6);
                    ChessPosition g1 = new ChessPosition(1,7);
                    generateValidCastles(teamColor, castleMoves, kingSquare, whiteKing, f1, g1);
                }
                if(whiteARookUnmoved){
                    ChessPosition d1 = new ChessPosition(1,4);
                    ChessPosition c1 = new ChessPosition(1,3);
                    generateValidCastles(teamColor, castleMoves, kingSquare, whiteKing, d1, c1);
                }
            }
        }
        return castleMoves;
    }

    private void generateValidCastles(TeamColor teamColor, ArrayList<ChessMove> castleMoves, ChessPosition kingSquare, ChessPiece blackKing, ChessPosition f8, ChessPosition g8) {
        boolean canCastle = (myBoard.getPiece(f8) == null && myBoard.getPiece(g8) ==null);
        if(canCastle){
            myBoard.addPiece(f8, blackKing);
            myBoard.removePiece(kingSquare);
            if(isInCheck(teamColor))
                canCastle = false;
            myBoard.addPiece(g8, blackKing);
            myBoard.removePiece(f8);
            if(isInCheck(teamColor))
                canCastle = false;
            if(canCastle){
                ChessMove tempMove = new ChessMove(kingSquare, g8);
                castleMoves.add(tempMove);
            }
            myBoard.addPiece(kingSquare,blackKing);
            myBoard.removePiece(g8);
        }
    }

    private void makeCastleMove(ChessMove move){
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece king = myBoard.getPiece(startPosition);
        int increment;
        if(startPosition.getColumn()>endPosition.getColumn()){
            increment= -1;
        }
        else {
            increment = 1;
        }
        ChessPosition rookSquare;
        if(increment==-1)
            rookSquare = new ChessPosition(endPosition.getRow(),1);
        else
            rookSquare = new ChessPosition(endPosition.getRow(),8);
        ChessPiece rook = myBoard.getPiece(rookSquare);
        ChessPosition newRookSquare = new ChessPosition(startPosition.getRow(), startPosition.getColumn()+increment);
        myBoard.addPiece(newRookSquare,rook);
        myBoard.removePiece(rookSquare);
        myBoard.addPiece(endPosition,king);
        myBoard.removePiece(startPosition);

    }
    private void makeEnPassant(ChessMove move){
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPosition capturedPiece;
        ChessPiece piece = myBoard.getPiece(startPosition);
        ChessGame.TeamColor teamColor = getTeamTurn();
        if(teamColor == TeamColor.BLACK) {
               capturedPiece =new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
        }
        else{
            capturedPiece =new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        }
        myBoard.addPiece(endPosition,piece);
        myBoard.removePiece(startPosition);
        myBoard.removePiece(capturedPiece);
    }

    private void undoEnPassant(ChessMove passingMove){
        ChessPosition startPosition = passingMove.getStartPosition();
        ChessPosition endPosition = passingMove.getEndPosition();
        ChessPiece piece = myBoard.getPiece(endPosition);
        ChessPosition passerPosition;
        ChessGame.TeamColor passerColor;
        if(turnColor == TeamColor.WHITE){
            passerPosition = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
            passerColor = TeamColor.BLACK;
        }
        else{
            passerPosition = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
            passerColor = TeamColor.WHITE;
        }
        myBoard.addPiece(startPosition,piece);
        myBoard.addPiece(passerPosition, new ChessPiece(passerColor, ChessPiece.PieceType.PAWN));
        myBoard.removePiece(endPosition);
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
                "myBoard=\n" + myBoard +
                ", turnColor=" + turnColor +
                '}';
    }
}
