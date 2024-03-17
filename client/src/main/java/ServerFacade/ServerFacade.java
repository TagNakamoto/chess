package ServerFacade;

import dataAccess.JoinRequest;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

//Represents the server for the client, convert json string
public class ServerFacade {
    private final String urlString;
    private final ClientCommunicator communicator = new ClientCommunicator();
    public ServerFacade(String urlString) {
        this.urlString = urlString;
    }
    public void facadeClear() throws IOException, URISyntaxException {
        communicator.doDelete(urlString+"/db", null);
    }

    public AuthData facadeRegister(UserData u) throws IOException, URISyntaxException {
        return (AuthData) communicator.doPost(urlString+"/user", null, u);
    }

    public AuthData facadeLogin(UserData u) throws IOException, URISyntaxException {
        return (AuthData) communicator.doPost(urlString+"/session", null, u);
    }

    public void facadeLogout(String authToken) throws IOException, URISyntaxException {
        communicator.doDelete(urlString+"/session", authToken);
    }

    public ArrayList<GameData> facadeGetGames(String authToken) throws IOException, URISyntaxException {
        Object gamesList = communicator.doGet(urlString + "/game", authToken);
        if (gamesList instanceof ArrayList<?>) {
            @SuppressWarnings("unchecked")
            ArrayList<GameData> typedGamesList = (ArrayList<GameData>) gamesList;
            return typedGamesList;
        }
        return null;
    }

    public int facadeCreateGame(String authToken, GameData gameName) throws IOException, URISyntaxException {
        return ((GameData) communicator.doPost(urlString+"/game", authToken, gameName)).gameID();
    }

    public void facadeJoinGame(String authToken, JoinRequest joinRequest) throws IOException, URISyntaxException {
        communicator.doPut(urlString+"/game", authToken, joinRequest);
    }
}
