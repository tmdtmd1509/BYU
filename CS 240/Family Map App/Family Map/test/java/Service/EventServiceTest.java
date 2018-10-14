package Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DataAccess.AuthTokenDAO;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import DataAccess.database;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.AllEventsRequest;
import Request.EventRequest;
import Request.FillRequest;
import Response.AllEventsResponse;
import Response.EventResponse;
import Response.FillResponse;
import Service.EventService;

import static org.junit.Assert.*;

public class EventServiceTest {
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    private User user;
    private Person person;
    private Event event;
    private AuthToken authToken;
    private database db;
    private EventService eventService;
    private AllEventsResponse allEventsResponse;
    private EventResponse eventResponse;
    private AllEventsRequest allEventsRequest;
    private EventRequest eventRequest;

    @Before
    public void setUp() {
        user = new User();
        person = new Person();
        event = new Event();
        authToken = new AuthToken();

        allEventsResponse = new AllEventsResponse();
        eventResponse = new EventResponse();
        allEventsRequest = new AllEventsRequest();
        eventRequest = new EventRequest();
    }

    @After
    public void tearDown() throws SQLException {

    }
    @Test
    public void allEventsTest() throws SQLException, database.DatabaseException {

        eventService = new EventService();
        db = eventService.getDb();

        userDAO = new UserDAO(db);
        personDAO = new PersonDAO(db);
        eventDAO = new EventDAO(db);
        authTokenDAO = new AuthTokenDAO(db);

        db.openConnection();

        user.setUsername("bluedia4");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID2");

        userDAO.addUser(user);

        person.setPersonId("personID2");
        person.setDescendant("bluedia4");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        personDAO.addPerson(person);

        List<Event> expected = new ArrayList<>();

        event.setEventId("eventId1");
        event.setDescendant("bluedia4");
        event.setPersonId("personID2");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);


        expected.add(event);

        Event event2 = new Event();

        event2.setEventId("eventId2");
        event2.setDescendant("bluedia4");
        event2.setPersonId("personID");
        event2.setLatitude(9.9);
        event2.setLongitude(8.8);
        event2.setCountry("Utah");
        event2.setCity("SLC");
        event2.setEventType("Event_type");
        event2.setYear(2017);

        expected.add(event2);

        eventDAO.addAllEvents(expected);

        authToken.setUserId("bluedia4");
        authToken.setAuthToken("authToken1");

        authTokenDAO.addAuthToken(authToken);

        allEventsRequest.setAuthToken("authToken1");

        allEventsResponse = eventService.allEvents(allEventsRequest);

        db.openConnection();

        //assertEquals(2, allEventsResponse.data.size());
        assertEquals(event, allEventsResponse.data.get(0));
        //assertEquals(event2, allEventsResponse.data.get(1));
    }

    @Test
    public void eventTest() throws SQLException, database.DatabaseException {
        eventService = new EventService();
        db = eventService.getDb();

        db.openConnection();

        userDAO = new UserDAO(db);
        personDAO = new PersonDAO(db);
        eventDAO = new EventDAO(db);
        authTokenDAO = new AuthTokenDAO(db);

        userDAO.clear();
        personDAO.clear();
        eventDAO.clear();
        authTokenDAO.clear();

        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID");

        userDAO.addUser(user);

        person.setPersonId("personID");
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        personDAO.addPerson(person);

        event.setEventId("eventId");
        event.setDescendant("bluedia");
        event.setPersonId("personID");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);

        eventDAO.addEvent(event);

        authToken.setUserId("bluedia");
        authToken.setAuthToken("authToken");

        authTokenDAO.addAuthToken(authToken);

        eventRequest.setEventId("eventId");
        eventRequest.setAuthToken("authToken");

        eventResponse = eventService.event(eventRequest);

        db.openConnection();

        assertEquals("personID", eventResponse.personID);
        assertEquals("Provo", eventResponse.city);
        assertEquals("Utah", eventResponse.country);
        assertEquals("bluedia", eventResponse.descendant);
        assertEquals("eventId", eventResponse.eventID);
        assertEquals("Event_type", eventResponse.eventType);

    }

}