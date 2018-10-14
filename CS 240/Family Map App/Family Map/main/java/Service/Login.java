package Service;

import com.sun.media.sound.InvalidDataException;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Response.LoginResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;

/**
 * login service class
 * get the request from the handler
 * usually get the data from the DataAccess package and resend the data to server
 */
public class  Login {
    private database db = new database();
    public database getDb() { return db; }
    private UserDAO userDAO = new UserDAO(db);
    private AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);

    /**
     * get the request and do the login service, might throw some exceptions
     * @param request
     * @return response
     * @throws InvalidDataException
     * @throws InternalException
     */
    public LoginResponse login (LoginRequest request) throws InternalException {
        LoginResponse response = new LoginResponse();

        try {
            System.out.println("Login service started");
            //open databas
            db.openConnection();

            //get request
            String username = request.getUserName();
            String password = request.getPassword();
            //find if user exist
            User user = userDAO.findUser(username);
            if (user != null) {
                //check password
                if (user.getPassword().equals(password)) {
                    //generate authToken
                    AuthToken authToken = authTokenDAO.generateAuthToken(username);
                    authTokenDAO.addAuthToken(authToken);
                    //make response
                    response.userName = username;
                    response.authToken = authToken.getAuthToken();
                    response.personId = user.getPersonId();
                } else {
                    throw new InvalidDataException();
                }
            } else {
                throw new InvalidDataException();
            }
            //close database
            db.closeConnection(true);

        }catch (SQLException e) {
            e.printStackTrace();
            response.message = "SQL Error";
        } catch (InvalidDataException e) {
            e.printStackTrace();
            response.message = "wrong userName or password";
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
        System.out.println("login service finished");
        return response;
    }
}
