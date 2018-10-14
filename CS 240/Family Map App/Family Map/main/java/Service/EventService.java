package Service;

import java.sql.SQLException;
import java.util.List;

import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.AllEventsRequest;
import Request.EventRequest;
import Response.AllEventsResponse;
import Response.EventResponse;
import Response.PersonResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;

/**
 * event service class
 * get the request from the handler
 * usually get the data from the DataAccess package and resend the data to server
 * mainly has two method, for single Event and for all the Events
 */
public class EventService {
    private database db = new database();
    public database getDb() { return db; }
    private UserDAO userDAO = new UserDAO(db);
    private EventDAO eventDAO = new EventDAO(db);

    /**
     * this method is for one event information
     * @param request
     * @return
     * @throws InternalException
     */
    public EventResponse event(EventRequest request) throws InternalException {
        EventResponse response = new EventResponse();
        try {
            System.out.println("event service started");
            //open database
            db.openConnection();

            //get request
            String authToken = request.getAuthToken();
            String eventId = request.getEventId();

            //find event by eventId
            Event event = eventDAO.findEvent(eventId);
            if (event != null) {
                //see if event exist
                User user = userDAO.findUserByToken(authToken);
                //see if decendant is the user
                if (event.getDescendant().equals(user.getUsername())) {
                    //make response
                    response.descendant = event.getDescendant();
                    response.eventID = event.getEventId();
                    response.personID = event.getPersonId();
                    response.latitude = event.getLatitude();
                    response.longitude = event.getLongitude();
                    response.country = event.getCountry();
                    response.city = event.getCity();
                    response.eventType = event.getEventType();
                    response.year = event.getYear();
                } else {
                    throw new RequestedEventNotBelongException();
                }
            } else {
                throw new InvalidEventIdException();
            }
            //close database
            db.closeConnection(true);
        } catch (EventService.RequestedEventNotBelongException e) {
            e.printStackTrace();
            response.message = "Requested Event is not belong to this user";
        } catch (EventService.InvalidEventIdException e) {
            e.printStackTrace();
            response.message = "Invalid eventId Error";
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = "SQL or Database Error";
        } catch (database.DatabaseException e) {
            e.printStackTrace();
        } finally {
            if(db.getConn() != null) {
                try {
                    db.closeConnection(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("event service finished");
        return response;
    }

    /**
     * this method is for all the events for all family members of the current user
     * @param request
     * @return response    all the events
     * @throws InvalidAuthTokenException
     * @throws InternalException
     */
    public AllEventsResponse allEvents(AllEventsRequest request) throws InternalException {
        AllEventsResponse response = new AllEventsResponse();

        try {
            System.out.println("All Events service started");
            //open database
            db.openConnection();

            String authToken = request.getAuthToken();
            //find person by personId
            User user = userDAO.findUserByToken(authToken);
            String userName = user.getUsername();
            //make response
            response.data = eventDAO.getFamilyEvents(userName);
            //close database
            db.closeConnection(true);

        } catch (SQLException e) {
            e.printStackTrace();
            response.message = "Database or SQL Error";
        } catch (database.DatabaseException e) {
            e.printStackTrace();
        } finally {
            if(db.getConn() != null) {
                try {
                    db.closeConnection(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("All Events service finished");
        return response;
    }

    private class InvalidAuthTokenException extends Exception { }
    private static class InvalidEventIdException extends Exception { }
    private static class RequestedEventNotBelongException extends Exception { }
}
