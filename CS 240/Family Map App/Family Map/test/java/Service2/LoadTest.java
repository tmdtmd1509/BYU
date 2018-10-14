package Service2;

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
import Request.LoadRequest;
import Response.LoadResponse;
import Service.Load;

import static org.junit.Assert.*;

public class LoadTest {
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    private User user;
    private database db;
    private Person person;
    private Event event;
    private Load loadService;
    private LoadResponse loadResponse;
    private LoadRequest loadRequest;

    @Before
    public void setUp() throws database.DatabaseException, SQLException {
        user = new User();
        person = new Person();
        event = new Event();

        loadService = new Load();
        db = loadService.getDb();

        db.openConnection();

        userDAO = new UserDAO(db);
        personDAO = new PersonDAO(db);
        eventDAO = new EventDAO(db);
        authTokenDAO = new AuthTokenDAO(db);


        userDAO.clear();
        personDAO.clear();
        eventDAO.clear();
        authTokenDAO.clear();


        loadResponse = new LoadResponse();
        loadRequest = new LoadRequest();
    }

    @After
    public void tearDown() throws SQLException {
        userDAO.clear();
        personDAO.clear();
        eventDAO.clear();
        authTokenDAO.clear();

        return;
    }

    @Test
    public void loadTest() throws SQLException, database.DatabaseException {
        //users
        List<User> users = new ArrayList<>();

        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("person");

        users.add(user);

        User user2 = new User();

        user2.setUsername("bluedia2");
        user2.setPassword("password");
        user2.setEmail("bluedia@gmail.com");
        user2.setFirstName("Ethan2");
        user2.setLastName("Hwang");
        user2.setGender("m");
        user2.setPersonId("person2");

        users.add(user2);

        userDAO.addAllUser(users);

        loadRequest.setUsers(users);

        //persons
        List<Person> persons = new ArrayList<>();

        person.setPersonId("person");
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        persons.add(person);

        Person person2 = new Person();

        person2.setPersonId("person2");
        person2.setDescendant("bluedia2");
        person2.setFirstName("Ethan2");
        person2.setLastName("Hwang");
        person2.setGender("m");
        person2.setFather("Father");
        person2.setMother("Mother");
        person2.setSpouse("Spouse");

        persons.add(person2);

        personDAO.addAllPerson(persons);

        loadRequest.setPersons(persons);

        //events
        List<Event> events = new ArrayList<>();

        event.setEventId("eventId");
        event.setDescendant("bluedia");
        event.setPersonId("person");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);


        events.add(event);

        Event event2 = new Event();

        event2.setEventId("eventId2");
        event2.setDescendant("bluedia2");
        event2.setPersonId("person2");
        event2.setLatitude(11.11);
        event2.setLongitude(10.10);
        event2.setCountry("Utah");
        event2.setCity("Provo");
        event2.setEventType("Event_type");
        event2.setYear(2018);

        events.add(event2);

        eventDAO.addAllEvents(events);

        loadRequest.setEvents(events);

        loadResponse = loadService.load(loadRequest);

        db.openConnection();

        assertEquals(2, loadResponse.userNum);
        assertEquals(2, loadResponse.eventNum);
        assertEquals(2, loadResponse.personNum);

        assertEquals(user, userDAO.findUser("bluedia"));
        assertEquals(user2, userDAO.findUser("bluedia2"));
        assertEquals(person, personDAO.findPerson("person"));
        assertEquals(person2, personDAO.findPerson("person2"));
        assertEquals(event, eventDAO.findEvent("eventId"));
        assertEquals(event2, eventDAO.findEvent("eventId2"));
    }
}