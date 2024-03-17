package ServerFacade;

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
        return (ArrayList<GameData>) communicator.doGet(urlString + "/game", authToken);
    }

    public int facadeCreateGame(String authToken, String gameName) throws IOException, URISyntaxException {
        return ((GameData) communicator.doPost(urlString+"/game", authToken, gameName)).gameID();
    }

    public void facadeJoinGame(String authToken, GameData gameData) throws IOException, URISyntaxException {
        communicator.doPut(urlString+"/game", authToken, gameData);
    }
}
