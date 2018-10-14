package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Model.AuthToken;
import Model.Event;
import Model.Person;

import static org.junit.Assert.*;

public class EventDAOTest {
    private EventDAO eventDAO;
    private Event event;
    private database db;
    private Person person;
    private PersonDAO personDAO;

    @Before
    public void setUp() throws database.DatabaseException {
        event = new Event();
        db = new database();
        eventDAO = new EventDAO(db);
        person = new Person();
        personDAO = new PersonDAO(db);
        db.openConnection();

    }

    @After
    public void tearDown() throws SQLException {
        db.closeConnection(false);

        return;
    }

    @Test
    public void generateEventIdTest() {

        assertEquals(null, event.getEventId());

        event = eventDAO.generateEventId(event);

        assertNotEquals(null, event.getEventId());
    }

    @Test
    public void addEventTest() throws SQLException {
        //full input
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

        assertEquals(event.getEventId(), eventDAO.findEvent("eventIdTest").getEventId());
        assertEquals(event.getCity(), eventDAO.findEvent("eventIdTest").getCity());
        assertEquals(event.getDescendant(), eventDAO.findEvent("eventIdTest").getDescendant());
        assertEquals(event.getCountry(), eventDAO.findEvent("eventIdTest").getCountry());
        assertEquals(event, eventDAO.findEvent("eventIdTest"));
    }

    @Test
    public void addEvnetTest2() throws SQLException {
        //some empty input(can be null)
        event = eventDAO.generateEventId(event);
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(0);
        event.setLongitude(0);
        event.setCountry("");
        event.setCity("");
        event.setEventType("");
        event.setYear(0);

        eventDAO.addEvent(event);

        assertEquals(event, eventDAO.findEvent(event.getEventId()));
    }

    @Test
    public void addAllEventTest() throws SQLException {
        List<Event> expected = new ArrayList<>();

        event = eventDAO.generateEventId(event);
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);


        expected.add(event);

        Event event2 = new Event();

        event2 = eventDAO.generateEventId(event2);
        event2.setDescendant("bluedia2");
        event2.setPersonId("personId");
        event2.setLatitude(11.11);
        event2.setLongitude(10.10);
        event2.setCountry("Utah");
        event2.setCity("Provo");
        event2.setEventType("Event_type");
        event2.setYear(2018);

        expected.add(event2);

        eventDAO.addAllEvents(expected);

        assertEquals(event.getEventId(), eventDAO.findEvent(event.getEventId()).getEventId());
        assertEquals(event.getCity(), eventDAO.findEvent(event.getEventId()).getCity());
        assertEquals(event.getDescendant(), eventDAO.findEvent(event.getEventId()).getDescendant());
        assertEquals(event.getCountry(), eventDAO.findEvent(event.getEventId()).getCountry());

        assertEquals(event2.getEventId(), eventDAO.findEvent(event2.getEventId()).getEventId());
        assertEquals(event2.getCity(), eventDAO.findEvent(event2.getEventId()).getCity());
        assertEquals(event2.getDescendant(), eventDAO.findEvent(event2.getEventId()).getDescendant());
        assertEquals(event2.getCountry(), eventDAO.findEvent(event2.getEventId()).getCountry());
    }

    @Test
    public void findEventTest() throws SQLException {
        event = eventDAO.generateEventId(event);
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);

        //add
        eventDAO.addEvent(event);

        //test find correct personId
        assertEquals(event.getEventId(), eventDAO.findEvent(event.getEventId()).getEventId());
        assertEquals(event.getCity(), eventDAO.findEvent(event.getEventId()).getCity());
        assertEquals(event.getDescendant(), eventDAO.findEvent(event.getEventId()).getDescendant());
        assertEquals(event.getCountry(), eventDAO.findEvent(event.getEventId()).getCountry());

        //test find incorrect personId
        assertNotEquals(event.getEventId(), eventDAO.findEvent("wrongID"));
        assertNotEquals(event.getCity(), eventDAO.findEvent("wrongID"));
        assertNotEquals(event.getDescendant(), eventDAO.findEvent("wrongID"));
        assertNotEquals(event.getCountry(), eventDAO.findEvent("wrongID"));
    }

    @Test
    public void findEventByPersonTest() throws SQLException {
        event = eventDAO.generateEventId(event);
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);

        //add
        eventDAO.addEvent(event);

        //make another event have same personID
        Event event2 = new Event();

        event2 = eventDAO.generateEventId(event2);
        event2.setDescendant("bluedia");
        event2.setPersonId("personId");
        event2.setLatitude(11.11);
        event2.setLongitude(10.10);
        event2.setCountry("Utah");
        event2.setCity("Provo");
        event2.setEventType("Event_type");
        event2.setYear(2018);

        //add
        eventDAO.addEvent(event2);

        //make expectedList
        List<Event> expectedList = new ArrayList<>();
        expectedList.add(event);
        expectedList.add(event2);

        assertEquals(expectedList.size(), eventDAO.findEventByPerson("personId").size());
        //assertEquals(expectedList.get(1), eventDAO.findEventByPerson("personId").get(1));
        //assertEquals(expectedList.get(0), eventDAO.findEventByPerson("personId").get(0));
    }

    @Test
    public void getFamilyEvents() throws SQLException {
        List<Event> events = new ArrayList<Event>();
        //add person
        person.setPersonId("personId");
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");
        //add
        personDAO.addPerson(person);

        //add event for person
        event.setEventId("eventId");
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);
        //add
        eventDAO.addEvent(event);

        events.add(event);

        assertEquals(events.get(0).getEventId(), eventDAO.getFamilyEvents("bluedia").get(0).getEventId());
        assertEquals(events.get(0).getCountry(), eventDAO.getFamilyEvents("bluedia").get(0).getCountry());
        assertEquals(events.get(0).getDescendant(), eventDAO.getFamilyEvents("bluedia").get(0).getDescendant());
    }

    @Test
    public void clear() throws SQLException {
        eventDAO.generateEventId(event);
        event.setDescendant("bluedia");
        event.setPersonId("personId");
        event.setLatitude(11.11);
        event.setLongitude(10.10);
        event.setCountry("Utah");
        event.setCity("Provo");
        event.setEventType("Event_type");
        event.setYear(2018);

        //add
        eventDAO.addEvent(event);

        assertEquals(event.getEventId(), eventDAO.findEvent(event.getEventId()).getEventId());
        assertEquals(event.getCity(), eventDAO.findEvent(event.getEventId()).getCity());
        assertEquals(event.getDescendant(), eventDAO.findEvent(event.getEventId()).getDescendant());
        assertEquals(event.getCountry(), eventDAO.findEvent(event.getEventId()).getCountry());

        eventDAO.clear();

        assertEquals(null, eventDAO.findEvent(event.getEventId()));
    }
}