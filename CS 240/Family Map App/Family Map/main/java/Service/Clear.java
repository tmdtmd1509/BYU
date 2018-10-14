package Service;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Person;
import Response.ClearResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;

/**
 * clear service class
 * get the request from the server and handle the request
 * usually get the data from the DataAccess package and resend the data to server
 */
public class Clear {
    private database db = new database();
    public database getDb() { return db; }
    public void setDb(database DB) { db = DB; }
    private UserDAO userDAO = new UserDAO(db);
    private PersonDAO personDAO = new PersonDAO(db);
    private EventDAO eventDAO = new EventDAO(db);
    private AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);

    /**
     * get the request for clear
     */
    public ClearResponse clear() throws InternalException {
        ClearResponse response = new ClearResponse();
        try {
            System.out.println("clear service started");
            //open database
            db.openConnection();

            //clear every DAO
            userDAO.clear();
            personDAO.clear();
            eventDAO.clear();
            authTokenDAO.clear();

            //make response
            response.message = "Clear succeeded.";

            //close database
            db.closeConnection(true);

            //make response
            response.message = "Clear finished";

        }catch (SQLException e) {
            e.printStackTrace();
            response.message = "SQL Error";
        } catch (database.DatabaseException e) {
            e.printStackTrace();
            response.message = "Database Error";
        } finally {
            if(db.getConn() != null) {
                try {
                    db.closeConnection(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
}
