package ui;

import ServerFacade.ServerFacade;
import dataAccess.JoinRequest;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;
public class UIMenu {
    private State state = State.SIGNEDOUT;
    private boolean running = true;
    private final Scanner scanner = new Scanner(System.in);
    private final String serverUrl = "http://localhost:8080";
    private final ServerFacade serverFacade = new ServerFacade(serverUrl);
    private String authToken = null;

    public void run(){
        clearConsole();
        while(running) {
            printPreLogin();
            preLoginExecution();
        }
    }

    private void printPreLogin(){
        String menuString = """

                Welcome to the Login Menu. Please enter the number of one of the following commands:
                1. Register
                2. Login
                3. Help
                4. Quit
                """;

        System.out.print(menuString);
        System.out.print(">>> ");
    }

    private void clearConsole(){
        System.out.print(ERASE_SCREEN);
    }

    private void printPostLogin(){
        String menuString = """

                Welcome to the Chess Game Menu. Please enter the number of one of the following commands:
                1. Create Game
                2. List Games
                3. Join Game
                4. Observe Game
                5. Help
                6. Logout
                """;

        System.out.print(menuString);
        System.out.print(">>> ");
    }

    private void preLoginExecution(){
        String line = scanner.nextLine();
        var numbers = line.split(" ");
        int commandNum;
        String username;
        String password;
        if (numbers.length > 0) {
            try {
                commandNum = Integer.parseInt(numbers[0]);
                switch(commandNum){
                    case 1:
                        System.out.print("Please enter your username:");
                        username = scanner.nextLine();
                        System.out.print("Please enter your password:");
                        password = scanner.nextLine();
                        System.out.print("Please enter your email:");
                        String email = scanner.nextLine();
                        UserData userRegister = new UserData(username, password, email);
                        try {
                            authToken = serverFacade.facadeRegister(userRegister).authToken();
                            state = State.SIGNEDIN;
                            postLoginLoop();
                        }
                        catch (Exception ex){
                            System.out.print("Sorry, an error was encountered");
                            System.out.print(ex.getMessage());
                        }

                        break;
                    case 2:
                        System.out.print("Logging in\nPlease enter your username:\n");
                        username = scanner.nextLine();
                        System.out.print("Please enter your password:\n");
                        password = scanner.nextLine();
                        UserData userLogin = new UserData(username, password, null);
                        try {
                            authToken = serverFacade.facadeLogin(userLogin).authToken();
                            state = State.SIGNEDIN;
                            postLoginLoop();
                        }
                        catch (Exception ex){
                            System.out.print("Sorry, an error was encountered");
                            System.out.print(ex.getMessage());
                        }
                        break;
                    case 3:
                        System.out.print("""
                                
                                Enter the corresponding number to do the following:
                                1. Register for an account with a username, password, and email address
                                2. Log into an account you made previously with username and password
                                3. See this help page again
                                4. Quit the chess program
                                """);
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        break;
                    case 4:
                        System.out.print("Exiting Chess Program\n");
                        running = false;
                        break;
                    case 14:
                        System.out.print("You were not supposed to find this easter egg\n");
                        serverFacade.facadeClear();

                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. The first part of the line is not a valid number.");
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No numbers found in the input line.");
        }
    }

    private void postLoginExecution(){
        String line = scanner.nextLine();
        var numbers = line.split(" ");
        int commandNum;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (numbers.length > 0) {
            try {
                commandNum = Integer.parseInt(numbers[0]);
                switch(commandNum){
                    case 1:
                        System.out.print("Enter the name of your new game:\n");
                        String gameName = scanner.nextLine();
                        GameData newGame = new GameData(0, null, null, gameName, null);
                        try {
                            int gameID = serverFacade.facadeCreateGame(authToken, newGame);
                            System.out.print("New game created with ID of " + gameID + '\n');
                        }
                        catch (Exception ex){
                            System.out.print("Sorry, an error was encountered\n");
                            System.out.print(ex.getMessage());
                        }
                        break;
                    case 2:
                        System.out.print("Listing games:\n");
                        try {
                            ArrayList<GameData> gamesList = serverFacade.facadeGetGames(authToken);
                            for (GameData game : gamesList){
                                System.out.print('\n' + game.toString());
                            }
                        }
                        catch (Exception ex){
                            System.out.print("Sorry, an error was encountered\n");
                            System.out.print(ex.getMessage());
                        }
                        break;
                    case 3:
                        System.out.print("Enter the game ID number of the game you would like to join:\n");
                        int gameID = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter the color you would like to join as (BLACK/WHITE):\n");
                        String playerColor = scanner.nextLine();
                        JoinRequest joinRequest = new JoinRequest(playerColor,gameID);
                        UIChessBoard.printStartWhiteBlack(out);
                        try {
                            serverFacade.facadeJoinGame(authToken, joinRequest);
                            System.out.print("Joined game " + gameID +  " as " + playerColor + '\n');

                        }
                        catch (Exception ex){
                            System.out.print("Sorry, an error was encountered\n");
                            System.out.print(ex.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Enter the game ID number of the game you would like to join:\n");
                        gameID = Integer.parseInt(scanner.nextLine());
                        joinRequest = new JoinRequest(null,gameID);

                        try {
                            serverFacade.facadeJoinGame(authToken, joinRequest);
                            System.out.print("Observing game " + gameID + '\n');
                            UIChessBoard.printStartWhiteBlack(out);
                        }
                        catch (Exception ex){
                            System.out.print("Sorry, an error was encountered\n");
                            System.out.print(ex.getMessage());
                        }
                        break;
                    case 5:
                        System.out.print("""
                                
                               Enter the corresponding number to do the following:
                               1. Create a new chess game with a custom name
                               2. List all of the ongoing games
                               3. Join a game by supplying the game ID and your desired color
                               4. Observe a game by supplying the game ID
                               5. See this help page again
                               6. Log out and return to the pre-login menu
                               """);
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        break;
                    case 6:
                        try {
                        serverFacade.facadeLogout(authToken);
                        System.out.print("Logging out\n");
                        state = State.SIGNEDOUT;
                        authToken=null;
                        }
                        catch (Exception ex){
                        System.out.print("Sorry, an error was encountered\n");
                        System.out.print(ex.getMessage());
                        }
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. The first part of the line is not a valid number.\n");
            }
        } else {
            System.out.println("No numbers found in the input line.\n");
        }
    }

    private void postLoginLoop(){
        while(state == State.SIGNEDIN){
            printPostLogin();
            postLoginExecution();
        }
    }
}
