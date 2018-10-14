package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import Model.AuthToken;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    private database db;
    private AuthTokenDAO authTokenDAO;
    private AuthToken authToken;

    @Before
    public void setUp() throws database.DatabaseException {
        db = new database();
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
    public void generateAuthTokendTest() {

        assertEquals(null, authToken.getAuthToken());

        authToken = authTokenDAO.generateAuthToken("bluedia");

        assertNotEquals(null, authToken.getAuthToken());
        assertNotEquals(null, authToken.getUsername());
    }

    @Test
    public void addAuthTokenTest() throws SQLException {
        //generate and set
        authToken = authTokenDAO.generateAuthToken("bluedia");

        //add
        authTokenDAO.addAuthToken(authToken);

        //check auth token exist
        assertTrue(authTokenDAO.checkAuthToken(authToken.getAuthToken()));
    }

    @Test
    public void checkAuthTokenTest() throws SQLException {
        //generate and set
        authToken = authTokenDAO.generateAuthToken("bluedia");
        //add
        authTokenDAO.addAuthToken(authToken);

        //check auth token exist
        assertTrue(authTokenDAO.checkAuthToken(authToken.getAuthToken()));
        assertFalse(authTokenDAO.checkAuthToken("wrong authToken"));

    }

    @Test
    public void clear() throws SQLException {
        //generate and set
        authToken = authTokenDAO.generateAuthToken("bluedia");
        //add
        authTokenDAO.addAuthToken(authToken);

        assertTrue(authTokenDAO.checkAuthToken(authToken.getAuthToken()));

        authTokenDAO.clear();

        assertFalse(authTokenDAO.checkAuthToken(authToken.getAuthToken()));
    }
}