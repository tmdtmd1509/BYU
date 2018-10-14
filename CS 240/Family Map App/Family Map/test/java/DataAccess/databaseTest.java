package DataAccess;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class databaseTest {
    database db;
    Connection conn = null;

    @Before
    public void setUp() throws database.DatabaseException {
    db = new database();
    }

    public void tearDown() throws SQLException {

        return;
    }

        @Test
    public void openConnectionTest() throws database.DatabaseException {
        assertEquals(null, db.getConn());
        db.openConnection();;
        assertNotEquals(null, db.getConn());
        }

    @Test
    public void getConnTest() throws database.DatabaseException {
        assertEquals(null, conn);

        db.openConnection();

        conn = db.getConn();

        assertNotEquals(null, conn);
    }

    @Test
    public void closeConnectionTest() throws database.DatabaseException, SQLException {
        db.openConnection();

        assertNotEquals(null, db.getConn());

        db.closeConnection(false);

        assertEquals(null, db.getConn());
    }
}