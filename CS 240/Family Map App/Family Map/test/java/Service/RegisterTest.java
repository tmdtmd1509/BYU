package Service;

import com.sun.media.sound.InvalidDataException;

import java.sql.SQLException;

import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.User;
import Model.Person;
import Request.FillRequest;
import Request.LoginRequest;
import Response.LoginResponse;
import Service.Register;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class RegisterTest extends database{

    private database db;
    private Register register;
    private LoginResponse expectedResponse;
    private User user;
//    private Person person;
//    private UserDAO userDAO;
//    private PersonDAO personDAO;

    @Before
    public void setUp() {
        register = new Register();
        expectedResponse = new LoginResponse();
        user = new User();
//        person = new Person();
        db =  register.getDb();
//        userDAO = new UserDAO(db);
//        personDAO = new PersonDAO(db);
    }

    @After
    public void tearDown() {
        return;
    }

    @Test
    public void validRegisterTest() throws DatabaseException {
        expectedResponse.userName = "bluedia1";
        expectedResponse.personId = "";
        expectedResponse.authToken = "";

        user.setUsername("bluedia1");
        user.setPassword("password");
        user.setEmail("g@g.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");

        LoginResponse response = register.register(user);

        assertEquals(expectedResponse.userName, response.userName);
        //assertEquals(expectedResponse.personId, register.register(user).personId);
        //assertEquals("bluedia", register.register(user).authToken);
    }

    @Test
    public void addPersonToUserTest() throws SQLException, DatabaseException {
        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId(null);

        String personId = null;
        assertEquals(null, personId);

        db.openConnection();

        personId = register.addPersonToUser(user);

        register.getDb().closeConnection(false);

        assertNotEquals(null, personId);
    }

    @Test
    public void checkInvalidInput() throws InvalidDataException {
//        try {
//        //no username
//        user.setUsername(null);
//        user.setPassword("password");
//        user.setEmail("bluedia@gmail.com");
//        user.setFirstName("Ethan");
//        user.setLastName("Hwang");
//        user.setGender("m");
//        assert(Register.checkInvalidInput(user));
//        //no password
//        user.setUsername("bluedia");
//        user.setPassword(null);
//        user.setEmail("bluedia@gmail.com");
//        user.setFirstName("Ethan");
//        user.setLastName("Hwang");
//        user.setGender("m");
//        assertFalse(Register.checkInvalidInput(user));
//        //no email
//        user.setUsername("bluedia");
//        user.setPassword("password");
//        user.setEmail(null);
//        user.setFirstName("Ethan");
//        user.setLastName("Hwang");
//        user.setGender("m");
//        assertFalse(Register.checkInvalidInput(user));
//        //no firstname
//        user.setUsername("bluedia");
//        user.setPassword("password");
//        user.setEmail("bluedia@gmail.com");
//        user.setFirstName(null);
//        user.setLastName("Hwang");
//        user.setGender("m");
//        assertFalse(Register.checkInvalidInput(user));
//        //no lastname
//        user.setUsername("bluedia");
//        user.setPassword("password");
//        user.setEmail("bluedia@gmail.com");
//        user.setFirstName("Ethan");
//        user.setLastName(null);
//        user.setGender("m");
//        assertFalse(Register.checkInvalidInput(user));
//        //no gender
//        user.setUsername("bluedia");
//        user.setPassword("password");
//        user.setEmail("bluedia@gmail.com");
//        user.setFirstName("Ethan");
//        user.setLastName("Hwang");
//        user.setGender(null);
//        assertFalse(Register.checkInvalidInput(user));
        //correct
        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID");
        assertTrue(Register.checkInvalidInput(user));
//        } catch (InvalidDataException e) {
//            e.printStackTrace();
//        }
    }
}