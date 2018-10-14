package Service3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import DataAccess.database;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Response.RegisterResponse;
import Service.Clear;
import Service.Login;
import Service.Register;

import static org.junit.Assert.*;

public class LoginTest {
    private database db;
    private Login login;
    private LoginResponse loginResponse;
    private LoginRequest loginRequest;
    private Register register;
    private Clear clear;

    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;

    private User registerRequest;
    private LoginResponse registerResponse;
    @Before
    public void setUp() throws database.DatabaseException, SQLException {
        login = new Login();
        loginResponse = new LoginResponse();
        loginRequest = new LoginRequest();
        registerRequest = new User();
        registerResponse = new LoginResponse();
        register = new Register();
        clear = new Clear();
        db = login.getDb();
//
//        db.openConnection();
//
//        userDAO = new UserDAO(db);
//        personDAO = new PersonDAO(db);
//        eventDAO = new EventDAO(db);
//        authTokenDAO = new AuthTokenDAO(db);
//
//        userDAO.clear();
//        personDAO.clear();
//        eventDAO.clear();
//        authTokenDAO.clear();

    }

    @After
    public void tearDown() throws SQLException {
        return;
    }

    @Test
    public void loginTest() throws database.DatabaseException {
        clear.clear();

        String username = "qwer";
        String password = "password";

        registerRequest.setUsername("qwer");
        registerRequest.setPassword("password");
        registerRequest.setEmail("g@g.com");
        registerRequest.setFirstName("Ethan");
        registerRequest.setLastName("Hwang");
        registerRequest.setGender("m");

        registerResponse = register.register(registerRequest);

        loginRequest.setUserName(username);
        loginRequest.setPassword(password);

        db.openConnection();

        loginResponse = login.login(loginRequest);

        assertEquals(username, loginResponse.userName);
        //assertEquals(registerResponse.authToken, loginResponse.authToken);
        //assertEquals(registerResponse.personId, loginResponse.personId);
        clear.clear();
    }
}