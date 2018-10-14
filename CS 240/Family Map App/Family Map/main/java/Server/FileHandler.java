package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import sun.net.www.protocol.http.HttpURLConnection;

/**
 * display default homepage
 */
public class FileHandler implements HttpHandler {
    private final static String File_ROOT_DIR = "familymapserver/web";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                String uriPath = exchange.getRequestURI().getPath();
                System.out.println("url path: " + uriPath);
                if(uriPath.length() == 0 || uriPath.equals("/")) {
                    uriPath = "/index.html";
                }
                String filePath = File_ROOT_DIR + uriPath;
                System.out.println(" " + filePath);
                File file = new File(filePath);
                //check the file
                if(file.exists() && file.canRead()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.close();
                    success = true;
                }
            }
            //display 404 page
            if(!success) {
                String filePath = File_ROOT_DIR +"/HTML/404.html";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream respBody = exchange.getResponseBody();
                File file = new File(filePath);
                Files.copy(file.toPath(), respBody);
                respBody.close();
            }
        }catch (IOException e) {
            exchange.sendResponseHeaders(java.net.HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
        }
    }
}
