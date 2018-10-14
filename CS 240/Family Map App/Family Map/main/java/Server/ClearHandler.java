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
import Response.ClearResponse;
import Service.Clear;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * Clear handler, call Clear service
 */
public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                //setting
                Clear clearService = new Clear();
                ClearResponse response;
                Gson gson = new Gson();

                //call service and get response
                response = clearService.clear();

                //translate response to json
                String json = gson.toJson(response);

                //send ok sign and close, then it will send it to server
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().write(json.getBytes());
                exchange.getResponseBody().close();

                success = true;
                System.out.println("clear success");
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

