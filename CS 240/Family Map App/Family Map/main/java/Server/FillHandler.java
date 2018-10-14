package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.sql.SQLException;

import DataAccess.database;
import Request.FillRequest;
import Response.FillResponse;
import Service.Fill;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * Fill handler, call Fill service
 */
public class FillHandler implements HttpHandler {
    private final static String Fill_DIR = "familymapserver/web/fill";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                String uriPath = exchange.getRequestURI().getPath();
                String[] uriArray = uriPath.split("/");

                //get basic info
                String username = uriArray[2];
                int generationNum = 4;
                //if user input specific generation number
                if(uriArray.length == 4) {
                    generationNum = Integer.valueOf(uriArray[3]);
                    //System.out.println(generationNum);
                }
                //if user didn't input generation number
                else {
                    generationNum = 4;
                    //System.out.println(generationNum);
                }
                //setting
                FillRequest fillRequest = new FillRequest();
                Fill fillService = new Fill();
                Gson gson = new Gson();
                FillResponse response = new FillResponse();

                //making request
                fillRequest.setUserName(username);
                fillRequest.setGenerations(generationNum);

                //send request to service and get response
                response = fillService.fill(fillRequest);

                //translate response to json
                String json = gson.toJson(response);

                //send ok sign and close, then it will send it to server
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().write(json.getBytes());
                exchange.getResponseBody().close();

                success = true;
            }
            //display 404 page
            if (!success) {
                String filePath = "familymapserver/web/HTML/404.html";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream respBody = exchange.getResponseBody();
                File file = new File(filePath);
                Files.copy(file.toPath(), respBody);
                respBody.close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(java.net.HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}

