package Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import DataAccess.database;
import Model.Event;
import Model.Person;
import Model.User;
import Request.FillRequest;
import Request.LoadRequest;
import Response.FillResponse;
import Response.LoadResponse;
import Service.Clear;
import Service.Fill;

import static org.junit.Assert.*;

public class FillTest {
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;

    private User user;
    private Clear clear;
    private database db;
    private Fill fillService;
    private FillResponse fillResponse;
    private FillRequest fillRequest;

    @Before
    public void setUp() throws database.DatabaseException, SQLException {
        user = new User();

        fillService = new Fill();
        db = fillService.getDb();

        db.openConnection();

        userDAO = new UserDAO(db);
        personDAO = new PersonDAO(db);
        eventDAO = new EventDAO(db);
        authTokenDAO = new AuthTokenDAO(db);

        userDAO.clear();
        personDAO.clear();
        eventDAO.clear();
        authTokenDAO.clear();

        fillResponse = new FillResponse();
        fillRequest = new FillRequest();
    }

    @After
    public void tearDown() throws SQLException {
        userDAO.clear();
        personDAO.clear();
        eventDAO.clear();
        authTokenDAO.clear();
    }
    @Test
    public void fillTest() throws SQLException, database.DatabaseException {

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

        //case 1
        fillRequest.setUserName("bluedia");
        fillRequest.setGenerations(4);

        fillResponse = fillService.fill(fillRequest);

        db.openConnection();

        assertEquals(31, fillResponse.personNum);

        //case2 different name and generation
        fillRequest.setUserName("bluedia");
        fillRequest.setGenerations(3);

        fillResponse = fillService.fill(fillRequest);

        db.openConnection();

        assertEquals(15, fillResponse.personNum);

        //case3 wrong name
        fillRequest.setUserName("bluedia2");
        fillRequest.setGenerations(3);

        fillResponse = fillService.fill(fillRequest);

        db.openConnection();

        assertNotEquals(15, fillResponse.personNum);

    }
}