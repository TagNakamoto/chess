package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class UIChessBoard {
    private static final int BUFFER_SIZE = 3;
    private static final String SPACER = " ";
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        printBoard(out);
    }

    private static void setLightGrayBorder(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void drawTopBottom(PrintStream out){
        setLightGrayBorder(out);
        String[] letters = {"h", "g", "f", "e", "d", "c", "b", "a"};
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

    private static void printBlackSquare(PrintStream out, String c){
        setDarkGreenBG(out);
        printSquare(out, c);
    }

    private static void printWhiteSquare(PrintStream out, String c){
        setWhiteBG(out);
        printSquare(out, c);
    }
    private static void printBoard(PrintStream out){
        out.print(SET_TEXT_BOLD);

        drawTopBottom(out);
        setDarkGrayBorder(out);
        for(int i=1; i<=8; i++){
            setDarkGrayBorder(out);
            out.print("\n");
            printGraySquare(out, String.valueOf(i));
            for(int j=1; j<=4; j++){
                if(i%2 == 1){
                    printWhiteSquare(out, SPACER);
                    printBlackSquare(out, SPACER);
                }
                else{
                    printBlackSquare(out, SPACER);
                    printWhiteSquare(out, SPACER);
                }
            }
            printGraySquare(out, String.valueOf(i));
            setDarkGrayBorder(out);
        }
        setDarkGrayBorder(out);
        out.print("\n");
        drawTopBottom(out);
    }
}
