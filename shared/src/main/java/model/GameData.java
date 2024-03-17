package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public String toString() {
        return String.format("Game Name = %s\n" +
                        "Game ID =%d\n" +
                        "White Username = %s\n" +
                        "Black Username = %s\n"
                        ,gameName, gameID, whiteUsername, blackUsername);
    }
}
