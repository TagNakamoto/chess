package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class UIChessBoard {
    private static final int BUFFER_SIZE = 3;
    private static final String SPACER = " ";
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        ChessBoard board = new ChessBoard();
        printWhiteBoard(out, board.boardToStringArray());
        setDarkGrayBorder(out);
        out.print('\n');
        printBlackBoard(out, board.boardToStringArray());
    }

    private static void setLightGrayBorder(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void drawTopBottom(PrintStream out){
        setLightGrayBorder(out);
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};//{"h", "g", "f", "e", "d", "c", "b", "a"};
        out.print(SPACER.repeat(BUFFER_SIZE));
        for(String let: letters){
            printGraySquare(out, let);
        }
        out.print(SPACER.repeat(BUFFER_SIZE));

    }

    private static void setDarkGrayBorder(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setDarkGreenBG(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
    }
    private static void setWhiteBG(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void printSquare(PrintStream out, String c){
        out.print(SPACER + c + SPACER);
    }
    private static void printGraySquare(PrintStream out, String c){
        setLightGrayBorder(out);
        printSquare(out, c);
    }
    private static void setRedLetters(PrintStream out){
        out.print(SET_TEXT_COLOR_MAGENTA);
    }
    private static void setBlueLetters(PrintStream out){
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void printBlackSquare(PrintStream out, String c){
        setDarkGreenBG(out);
        if(c.charAt(0)>='A' && c.charAt(0) <='Z'){
            setRedLetters(out);
        }
        else if(c.charAt(0)>='a' && c.charAt(0) <='z'){
            setBlueLetters(out);
        }
        printSquare(out, c);
    }

    private static void printWhiteSquare(PrintStream out, String c){
        setWhiteBG(out);
        if(c.charAt(0)>='A' && c.charAt(0) <='Z'){
            setRedLetters(out);
        }
        else if(c.charAt(0)>='a' && c.charAt(0) <='z'){
            setBlueLetters(out);
        }
        printSquare(out, c);
    }
    private static void printWhiteBoard(PrintStream out, String[][] piecesLetter){
        out.print(SET_TEXT_BOLD);
        drawTopBottom(out);
        setDarkGrayBorder(out);
        for(int i=8; i>=1; i--){
            setDarkGrayBorder(out);
            out.print("\n");
            printGraySquare(out, String.valueOf(i));
            for(int j=1; j<=4; j++){
                if(i%2 == 1){
                    printBlackSquare(out, piecesLetter[i-1][(j-1)*2]);
                    printWhiteSquare(out, piecesLetter[i-1][(j-1)*2+1]);
                }
                else{
                    printWhiteSquare(out, piecesLetter[i-1][(j-1)*2]);
                    printBlackSquare(out, piecesLetter[i-1][(j-1)*2+1]);
                }
            }
            printGraySquare(out, String.valueOf(i));
            setDarkGrayBorder(out);
        }
        setDarkGrayBorder(out);
        out.print("\n");
        drawTopBottom(out);
        setDarkGrayBorder(out);
        out.print("\n");
    }

    private static void printBlackBoard(PrintStream out, String[][] piecesLetter){
        out.print(SET_TEXT_BOLD);
        drawTopBottom(out);
        setDarkGrayBorder(out);
        for(int i = 1; i <= 8; i++){  // Iterate from 1 to 8 (backwards)
            setDarkGrayBorder(out);
            out.print("\n");
            printGraySquare(out, String.valueOf(i));
            for(int j = 4; j >= 1; j--){  // Iterate from 4 to 1 (backwards)
                if(i % 2 == 1){

                    printWhiteSquare(out, piecesLetter[i - 1][(j - 1) * 2 + 1]);
                    printBlackSquare(out, piecesLetter[i - 1][(j - 1) * 2]);
                }
                else{
                    printBlackSquare(out, piecesLetter[i - 1][(j - 1) * 2 + 1]);
                    printWhiteSquare(out, piecesLetter[i - 1][(j - 1) * 2]);
                }
            }
            printGraySquare(out, String.valueOf(i));
            setDarkGrayBorder(out);
        }
        setDarkGrayBorder(out);
        out.print("\n");
        drawTopBottom(out);
        setDarkGrayBorder(out);
        out.print("\n");
    }
}
