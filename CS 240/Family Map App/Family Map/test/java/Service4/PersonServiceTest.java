package Service4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import DataAccess.database;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.PeopleRequest;
import Request.PersonRequest;
import Response.LoginResponse;
import Response.PeopleResponse;
import Response.PersonResponse;
import Service.Clear;
import Service.PersonService;
import Service.Register;

import static org.junit.Assert.*;

public class PersonServiceTest {

    private database db;
    private database db2;
    private PersonService personService;
    private PersonRequest personRequest;
    private PersonResponse personResponse;
    private PeopleRequest peopleRequest;
    private PeopleResponse peopleResponse;
    private LoginResponse loginResponse;
    private Clear clear;

    private User user;
    private Person person;
    private PersonDAO personDAO;
    private Register register;
    private AuthTokenDAO authTokenDAO;

    @Before
    public void setUp() throws database.DatabaseException {
        personService = new PersonService();
        register = new Register();
        clear = new Clear();
        personResponse = new PersonResponse();
        personRequest = new PersonRequest();
        peopleRequest = new PeopleRequest();
        peopleResponse = new PeopleResponse();
        db = personService.getDb();
        db2 = register.getDb();
        user = new User();
        person = new Person();
        personDAO = new PersonDAO(db);
        authTokenDAO = new AuthTokenDAO(db2);
    }

    @After
    public void tearDown() throws SQLException {
        return;
    }
    @Test
    public void person() throws SQLException, database.DatabaseException {
        clear.clear();

        user.setUsername("bluedia4");
        user.setPassword("password");
        user.setEmail("g@g.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");

        loginResponse = register.register(user);
        personRequest.setAuthToken(loginResponse.authToken);
        personRequest.setPersonId(loginResponse.personId);

        personResponse = personService.person(personRequest);

        assertEquals("Ethan", personResponse.firstName);
        assertEquals("Hwang", personResponse.lastName);
        assertEquals("m", personResponse.gender);
        assertEquals("bluedia4", personResponse.descendant);

        clear.clear();
    }

    @Test
    public void people() {
        clear.clear();

        user.setUsername("bluedia4");
        user.setPassword("password");
        user.setEmail("g@g.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");

        loginResponse = register.register(user);
        peopleRequest.setAuthToken(loginResponse.authToken);

        peopleResponse = personService.people(peopleRequest);

        assertNotEquals(null, peopleResponse.data.size());
        assertEquals("bluedia4", peopleResponse.data.get(0).getDescendant());
        assertEquals("Ethan", peopleResponse.data.get(0).getFirstName());

        clear.clear();
    }

    @Test
    public void checkAuthToken() {
        assertFalse(personService.checkAuthToken("wrong authToken"));
        //String token = authTokenDAO.generateAuthToken("bluedia").getAuthToken();
        //assertTrue(personService.checkAuthToken(token));
    }
}