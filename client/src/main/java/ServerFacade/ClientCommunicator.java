package ServerFacade;
import chess.ChessPiece;
import chess.PieceDeserializer;
import chess.PieceSerializer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

//Does the HTTP writing and communication
public class ClientCommunicator {

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(ChessPiece.class, new PieceSerializer())
            .registerTypeAdapter(ChessPiece.class, new PieceDeserializer())
            .create();

    public Object doPost(String urlString, String authToken, Object bodyObj) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        return addHeadersAndBody(authToken, bodyObj, connection);
    }



    public Object doPut(String urlString, String authToken, Object bodyObj) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(false);

        return addHeadersAndBody(authToken, bodyObj, connection);
    }

    public Object doGet(String urlString, String authToken) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);

        if(authToken != null) {
            connection.addRequestProperty("Accept", "text/html");
            connection.addRequestProperty("authorization", authToken);
        }

        return objectFromResponse(connection);
    }

    public boolean doDelete(String urlString, String authToken) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(false);

        if(authToken != null) {
            connection.addRequestProperty("Accept", "text/html");
            connection.addRequestProperty("authorization", authToken);
        }

        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    private Object objectFromResponse(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
            StringBuilder builder = new StringBuilder();
            String line;
            //get the response body
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //builder is now the response body text
            //return the deserialized object
            return bodyDecoder(builder.toString());

        } else {
            InputStream responseBody = connection.getErrorStream();
            if (responseBody != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    String errorResponse = responseBuilder.toString();
                    System.out.println("Error response from server: " + errorResponse);
                    // Process the error response as needed
                }
            } else {
                System.out.println("Error response body is empty");
            }
        }
        return null;
    }
    private Object addHeadersAndBody(String authToken, Object bodyObj, HttpURLConnection connection) throws IOException {
        if(authToken != null) {
            connection.addRequestProperty("Accept", "text/html");
            connection.addRequestProperty("authorization", authToken);
        }

        try(OutputStream requestBody = connection.getOutputStream()) {
            //write request body to OutputStream
            byte[] jsonBytes = gson.toJson(bodyObj).getBytes();
            requestBody.write(jsonBytes);
        }
        return objectFromResponse(connection);
    }
    private Object bodyDecoder(String s){
        JsonObject json = JsonParser.parseString(s).getAsJsonObject();

        if(json.isJsonObject()){
            try {
                if(json.has("authToken")){
                    return gson.fromJson(json, AuthData.class);
                }
                else if (json.has("username")) {
                    return gson.fromJson(json, UserData.class);

                } else if (json.has("gameID")) {
                    return gson.fromJson(json, GameData.class);
                }
            }
            catch (JsonSyntaxException e){
                //That's fine
            }
        }
        else if (json.isJsonArray()){
            try{
                Type listType = (new TypeToken<ArrayList<GameData>>(){}).getType();
                return gson.fromJson(json, listType);
            }
            catch (JsonSyntaxException e){
                //That's fine
            }
        }
        return null;
    }

}
