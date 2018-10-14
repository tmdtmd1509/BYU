package Service;

import com.sun.media.sound.InvalidDataException;

import java.sql.SQLException;

import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Request.LoadRequest;
import Response.LoadResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;
/**
 * load service class
 * get the request from the handler
 * usually get the data from the DataAccess package and resend the data to server
 */
public class Load {
    private database db = new database();
    public database getDb() { return db; }
    public void setDb(database DB) { db = DB; }
    private UserDAO userDAO = new UserDAO(db);
    private PersonDAO personDAO = new PersonDAO(db);
    private EventDAO eventDAO = new EventDAO(db);

    /**
     * load
     * @param request
     * @return
     * @throws InternalException
     */
    public LoadResponse load(LoadRequest request) throws InternalException {
        LoadResponse response = new LoadResponse();
        try {
            System.out.println("Load service started");
            //open database
            db.openConnection();
            //clear
            userDAO.clear();
            personDAO.clear();
            eventDAO.clear();
            //load
            userDAO.addAllUser(request.getUsers());
            personDAO.addAllPerson(request.getPersons());
            eventDAO.addAllEvents(request.getEvents());
            //make response
            response.userNum = request.getUsers().size();
            response.personNum = request.getPersons().size();
            response.eventNum = request.getEvents().size();
            response.message = "Successfully added "+request.getUsers().size()+" users, "+
                    request.getPersons().size()+" persons and "+ request.getEvents().size()+" events"+
                    " to the database.";

            //close database
            db.closeConnection(true);

        } catch (database.DatabaseException e) {
            e.printStackTrace();
            response.message = "Database Error";
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = "SQL Error";
        }
        finally {
            if(db.getConn() != null) {
                try {
                    db.closeConnection(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Load service finished");
        return response;
    }
}
