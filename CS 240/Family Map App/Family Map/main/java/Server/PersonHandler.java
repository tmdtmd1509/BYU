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
import Request.PeopleRequest;
import Request.PersonRequest;
import Response.PeopleResponse;
import Response.PersonResponse;
import Service.PersonService;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * Person handler, call person service and people service
 */
public class PersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                //setting
                PersonRequest personRequest = new PersonRequest();
                PersonService personService = new PersonService();
                Gson gson = new Gson();
                PersonResponse personResponse = new PersonResponse();
                PeopleRequest peopleRequest = new PeopleRequest();
                PeopleResponse peopleResponse = new PeopleResponse();
                String json = null;

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    //check if authToken exist
                    if(personService.checkAuthToken(authToken)) {
                        String uriPath = exchange.getRequestURI().getPath();
                        String[] uriArray = uriPath.split("/");

                        //person
                        if(uriArray.length == 3) {
                            String personId = uriArray[2];
                            //making request
                            personRequest.setPersonId(personId);
                            //send request to service and get response
                            personResponse = personService.person(personRequest);
                            //translate response to json
                            json = gson.toJson(personResponse);
                        }
                        //people
                        else {
                            //making request
                            peopleRequest.setAuthToken(authToken);
                            //send request to service and get response
                            peopleResponse = personService.people(peopleRequest);
                            //translate response to json
                            json = gson.toJson(peopleResponse);
                        }
                        //send ok sign and close, then it will send it to server
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseBody().write(json.getBytes());
                        exchange.getResponseBody().close();

                        success = true;
                    }
                    else {
                        peopleResponse.message = "Invalid Token";
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
        }
    }
}

