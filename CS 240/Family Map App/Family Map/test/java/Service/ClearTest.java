package Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import DataAccess.database;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Clear;

import static org.junit.Assert.*;

public class ClearTest {

    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private User user;
    private database db;
    private Person person;
    private Event event;
    private AuthToken authToken;
    private Clear clearService;

    @Before
    public void setUp() throws database.DatabaseException {
        user = new User();
        person = new Person();
        event = new Event();

        clearService = new Clear();

        db = clearService.getDb();
        authToken = new AuthToken();
        db.openConnection();
        clearService.clear();

        db.openConnection();
        userDAO = new UserDAO(db);
        personDAO = new PersonDAO(db);
        eventDAO = new EventDAO(db);

    }

    @After
    public void tearDown() throws SQLException {
        clearService.clear();
        return;
    }

    @Test
    public void clearTest() throws SQLException, database.DatabaseException {
        //user
        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID");

        //add
        userDAO.addUser(user);

        assertEquals(user, userDAO.findUser("bluedia"));

        clearService.clear();

        db.openConnection();
        assertEquals(null, userDAO.findUser("bluedia"));


        //person
        person.setPersonId("personId");
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        personDAO.addPerson(person);

        assertEquals(person, personDAO.findPerson(person.getPersonId()));

        clearService.clear();

        db.openConnection();
        assertEquals(null, personDAO.findPerson("personId"));

        //event
        event.setEventId("eventIdTest");
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);

        eventDAO.addEvent(event);

        assertEquals(event, eventDAO.findEvent("eventIdTest"));

        clearService.clear();

        db.openConnection();
        assertEquals(null,eventDAO.findEvent("eventIdTest"));
    }
}