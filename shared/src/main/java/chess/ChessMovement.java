package chess;

import java.util.*;

//Make class of ChessMovement, create subclasses for each piece type, pieceMoves method in this class
public class ChessMovement {
    public ChessMovement(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        teamColor = board.getPiece(position).getTeamColor();
    }

    protected ChessBoard board;
    protected ChessPosition position;
    protected ChessGame.TeamColor teamColor;

    public boolean noPiece(ChessPosition nextMove) {
        return (board.getPiece(nextMove) == null);
    }

    public Boolean isSameTeam(ChessPosition nextMove) {
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

    protected void generateArrayMoves(int[][] increments, ArrayList<ChessMove> moves) {
        int startRow = position.getRow();
        int startCol = position.getColumn();

        for (int[] row : increments) {
            int rowIncrement = row[0];
            int columnIncrement = row[1];
            int newRow = startRow + rowIncrement;
            int newCol = startCol + columnIncrement;
            if ((newRow > 0 && newRow < 9) && (newCol > 0 && newCol < 9)) {
                ChessPosition nextPosition = new ChessPosition(newRow, newCol);
                ChessMove tempMove = new ChessMove(position, nextPosition);
                if (noPiece(nextPosition) || !isSameTeam(nextPosition)) {
                    moves.add(tempMove);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMovement that = (ChessMovement) o;
        return Objects.deepEquals(board, that.board) && Objects.equals(position, that.position) && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, teamColor);
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

class Knight extends ChessMovement {
    public Knight(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] arrayLShape = {
                {2, 1},
                {2, -1},
                {1, 2},
                {-1, 2},
                {-2, 1},
                {-2, -1},
                {-1, -2},
                {1, -2}
        };
        generateArrayMoves(arrayLShape, moves);
        return moves;
    }
}

class King extends ChessMovement {
    public King(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int[][] arrayKingBox = {
                {1, 0}, //up
                {1, 1}, //upRight
                {0, 1}, //Right
                {-1, 1}, //RightDown
                {-1, 0}, //Down
                {-1, -1}, //LeftDown
                {0, -1}, //Left
                {1, -1} //LeftUp
        };
        generateArrayMoves(arrayKingBox, moves);

        return moves;
    }
}

class BlackPawn extends ChessMovement {
    public BlackPawn(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();

        ChessPiece.PieceType[] promotionArray = {ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN};
        ChessPosition firstSquare = new ChessPosition(startRow - 1, startCol);
        ChessPosition rightDown = null;
        ChessPosition leftDown = null;
        boolean enemyLeftDown = false;
        boolean enemyRightDown = false;
        if (startCol < 8) {
            rightDown = new ChessPosition(startRow - 1, startCol + 1);
            enemyRightDown = (!noPiece(rightDown) && !isSameTeam(rightDown));
        }
        if (startCol > 1) {
            leftDown = new ChessPosition(startRow - 1, startCol - 1);
            enemyLeftDown = (!noPiece(leftDown) && !isSameTeam(leftDown));
        }

        if (startRow == 7) {
            ChessPosition secondSquare = new ChessPosition(startRow - 2, startCol);
            if (noPiece(firstSquare) && noPiece(secondSquare)) {
                moves.add(new ChessMove(position, secondSquare));
            }
        }


        if (startRow == 2) {
            for (ChessPiece.PieceType promotionPiece : promotionArray) {
                if (noPiece(firstSquare)) {
                    moves.add(new ChessMove(position, firstSquare, promotionPiece));
                }
                if (enemyRightDown) {
                    moves.add(new ChessMove(position, rightDown, promotionPiece));
                }
                if (enemyLeftDown) {
                    moves.add(new ChessMove(position, leftDown, promotionPiece));
                }
            }

        } else {
            if (noPiece(firstSquare)) {
                moves.add(new ChessMove(position, firstSquare));
            }
            if (enemyRightDown) {
                moves.add(new ChessMove(position, rightDown));
            }
            if (enemyLeftDown) {
                moves.add(new ChessMove(position, leftDown));
            }
        }
        return moves;
    }
}

class WhitePawn extends ChessMovement {
    public WhitePawn(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessPiece.PieceType[] promotionArray = {ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN};
        ChessPosition firstSquare = new ChessPosition(startRow + 1, startCol);
        ChessPosition rightUp = null;
        ChessPosition leftUp = null;
        boolean enemyLeftUp = false;
        boolean enemyRightUp = false;
        if (startCol < 8) {
            rightUp = new ChessPosition(startRow + 1, startCol + 1);
            enemyRightUp = (!noPiece(rightUp) && !isSameTeam(rightUp));
        }
        if (startCol > 1) {
            leftUp = new ChessPosition(startRow + 1, startCol - 1);
            enemyLeftUp = (!noPiece(leftUp) && !isSameTeam(leftUp));
        }


        if (startRow == 2) {
            ChessPosition secondSquare = new ChessPosition(startRow + 2, startCol);
            if (noPiece(firstSquare) && noPiece(secondSquare)) {
                moves.add(new ChessMove(position, secondSquare));
            }
        }

        if (startRow == 7) {
            for (ChessPiece.PieceType promotionPiece : promotionArray) {
                if (noPiece(firstSquare)) {
                    moves.add(new ChessMove(position, firstSquare, promotionPiece));
                }
                if (enemyRightUp) {
                    moves.add(new ChessMove(position, rightUp, promotionPiece));
                }
                if (enemyLeftUp) {
                    moves.add(new ChessMove(position, leftUp, promotionPiece));
                }
            }

        } else {
            if (noPiece(firstSquare)) {
                moves.add(new ChessMove(position, firstSquare));
            }
            if (enemyRightUp) {
                moves.add(new ChessMove(position, rightUp));
            }
            if (enemyLeftUp) {
                moves.add(new ChessMove(position, leftUp));
            }
        }
        return moves;
    }

}
