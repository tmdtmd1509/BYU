package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.AuthToken;
import Model.User;

import static org.junit.Assert.*;

public class UserDAOTest {

    private UserDAO userDAO;
    private User user;
    private database db;
    private AuthTokenDAO authTokenDAO;
    private AuthToken authToken;

    @Before
    public void setUp() throws database.DatabaseException {
        user = new User();
        db = new database();
        userDAO = new UserDAO(db);
        authTokenDAO = new AuthTokenDAO(db);
        authToken = new AuthToken();
        db.openConnection();

    }

    @After
    public void tearDown() throws SQLException {
        db.closeConnection(false);

        return;
    }


    @Test
    public void addUserTest() throws SQLException, database.DatabaseException {
        //correct case
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
    }

    @Test
    public void addAllUserTest() throws SQLException {
        List<User> expected = new ArrayList<>();

        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID");

        expected.add(user);

        User user2 = new User();

        user2.setUsername("bluedia2");
        user2.setPassword("password");
        user2.setEmail("bluedia@gmail.com");
        user2.setFirstName("Ethan2");
        user2.setLastName("Hwang");
        user2.setGender("m");
        user2.setPersonId("personID");

        expected.add(user2);

        userDAO.addAllUser(expected);

        assertEquals(user, userDAO.findUser("bluedia"));
        assertEquals(user2, userDAO.findUser("bluedia2"));

        userDAO.clear();
    }

    @Test
    public void findUserTest() throws SQLException {
        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID");

        //add
        userDAO.addUser(user);

        //test find correct name
        assertEquals(user, userDAO.findUser("bluedia"));

        //test find incorrect name
        assertNotEquals(user, userDAO.findUser("bluedi1"));

        userDAO.clear();
    }

    @Test
    public void findUserByTokenTest() throws SQLException {
        user.setUsername("bluedia");
        user.setPassword("password");
        user.setEmail("bluedia@gmail.com");
        user.setFirstName("Ethan");
        user.setLastName("Hwang");
        user.setGender("m");
        user.setPersonId("personID");

        //add
        userDAO.addUser(user);

        authToken = authTokenDAO.generateAuthToken("bluedia");
        authTokenDAO.addAuthToken(authToken);
        String token = authToken.getAuthToken();

        assertEquals(user, userDAO.findUserByToken(token));
    }

    @Test
    public void clear() throws SQLException {
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

        userDAO.clear();

        assertNotEquals(user, userDAO.findUser("bluedia"));
        assertEquals(null, userDAO.findUser("bluedia"));
    }
}