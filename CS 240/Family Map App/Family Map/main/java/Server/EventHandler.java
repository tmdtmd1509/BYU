package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.database;
import Request.AllEventsRequest;
import Request.EventRequest;
import Response.AllEventsResponse;
import Response.EventResponse;
import Service.EventService;
import sun.net.www.protocol.http.HttpURLConnection;

public class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    database db = new database();
                    AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);

                    //open database
                    db.openConnection();
                    System.out.println(authToken);

                    //check if authToken exist
                    if(authTokenDAO.checkAuthToken(authToken)) {
                        db.closeConnection(true);

                        String uriPath = exchange.getRequestURI().getPath();
                        //distinguish by /
                        String[] uriArray = uriPath.split("/");
                        String personId = "";
                        //person
                        if(uriArray.length == 3) {
                            personId = uriArray[2];
                            //setting
                            EventRequest eventRequest = new EventRequest();
                            EventService eventService = new EventService();
                            Gson gson = new Gson();
                            EventResponse eventResponse = new EventResponse();

                            //making request
                            eventRequest.setEventId(personId);

                            //send request to service and get response
                            eventResponse = eventService.event(eventRequest);

                            //translate response to json
                            String json = gson.toJson(eventResponse);

                            //send ok sign and close, then it will send it to server
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            exchange.getResponseBody().write(json.getBytes());
                            exchange.getResponseBody().close();

                            success = true;
                        }
                        //people
                        else {
                            //setting
                            AllEventsRequest allEventsRequest = new AllEventsRequest();
                            EventService eventService = new EventService();
                            Gson gson = new Gson();
                            AllEventsResponse allEventsResponse = new AllEventsResponse();

                            //making request
                            allEventsRequest.setAuthToken(authToken);

                            //send request to service and get response
                            allEventsResponse = eventService.allEvents(allEventsRequest);

                            //translate response to json
                            String json = gson.toJson(allEventsResponse);

                            //send ok sign and close, then it will send it to server
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            exchange.getResponseBody().write(json.getBytes());
                            exchange.getResponseBody().close();

                            success = true;
                        }
                    }
                }
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
        } catch (database.DatabaseException | SQLException e) {
            e.printStackTrace();
        }
    }
}


