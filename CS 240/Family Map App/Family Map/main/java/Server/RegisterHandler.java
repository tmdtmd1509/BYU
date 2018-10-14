package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

import Model.User;
import Response.LoginResponse;
import Service.Register;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * Register handler, call register service
 * request use User since register request is same as User
 * response use Login response since it use same response
 */
public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                //Gson setting
                Gson gson = new Gson();
                User request = new User();
                Register registerService = new Register();
                LoginResponse response = new LoginResponse();

                //get request
                InputStream inputStream = exchange.getRequestBody();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                //translate json to request
                request = gson.fromJson(inputStreamReader, User.class);

                //send request to service and get response
                response = registerService.register(request);

                //close stream
                inputStream.close();
                inputStreamReader.close();

                //translate response to json
                String json = gson.toJson(response);

                //send ok sign and close, then it will send it to server
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().write(json.getBytes());
                exchange.getResponseBody().close();

                success = true;
                //System.out.println("register success");
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

