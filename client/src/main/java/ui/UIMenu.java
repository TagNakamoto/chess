package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;
public class UIMenu {
    private State state = State.SIGNEDOUT;
    private boolean running = true;
    private final Scanner scanner = new Scanner(System.in);
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
        if (numbers.length > 0) {
            try {
                commandNum = Integer.parseInt(numbers[0]);
                switch(commandNum){
                    case 1:
                        System.out.print("Registering\n");
                        state = State.SIGNEDIN;
                        postLoginLoop();
                        break;
                    case 2:
                        System.out.print("Logging in\n");
                        state = State.SIGNEDIN;
                        postLoginLoop();
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

                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. The first part of the line is not a valid number.");
            }
        } else {
            System.out.println("No numbers found in the input line.");
        }
    }

    private void postLoginExecution(){
        String line = scanner.nextLine();
        var numbers = line.split(" ");
        int commandNum;
        if (numbers.length > 0) {
            try {
                commandNum = Integer.parseInt(numbers[0]);
                switch(commandNum){
                    case 1:
                        System.out.print("Creating game\n");
                        break;
                    case 2:
                        System.out.print("Listing games\n");
                        break;
                    case 3:
                        System.out.print("Joining game\n");
                        break;
                    case 4:
                        System.out.print("Observing game\n");
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
                        System.out.print("Logging out\n");
                        state = State.SIGNEDOUT;
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
