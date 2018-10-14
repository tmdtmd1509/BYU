package Service;

import com.sun.media.sound.InvalidDataException;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Model.Person;
import Request.FillRequest;
import Request.LoginRequest;
import Response.LoginResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;

/**
 * register service class
 * use login service and fill service also
 * get the request from the handler
 * usually get the data from the DataAccess package and resend the data to server
 */
public class Register {
    private database db = new database();
    public database getDb() { return db; }
    private UserDAO userDAO = new UserDAO(db);
    private PersonDAO personDAO = new PersonDAO(db);
    private AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);

    /**
     * get the request and do the register service, might throw some exceptions
     *
     * @param request
     * @return
     * @throws InvalidDataException
     * @throws UsernameAlreadyTakenException
     * @throws InternalError
     */
    public LoginResponse register(User request) throws InternalException {
        LoginResponse loginResponse = new LoginResponse();

        //check if input is null
        try {
            checkInvalidInput(request);
        }
        catch (InvalidDataException e) {
            System.out.println("You missed some data");
        }
        try {
            //open database
            db.openConnection();

            //get request
            String username = request.getUsername();
            String personId;
            //check if username already taken
            if (userDAO.findUser(username) == null) {
                //link generated personId with user
                personId = addPersonToUser(request);
                request.setPersonId(personId);
                //start register
                userDAO.addUser(request);
            } else {
                throw new UsernameAlreadyTakenException();
            }
            //close db
            db.closeConnection(true);

            //login start
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserName(username);
            loginRequest.setPassword(request.getPassword());

            Login loginService = new Login();

            //make response
            loginResponse = loginService.login(loginRequest);
            loginResponse.personId = personId;
            loginResponse.userName = username;
            loginResponse.message = null;

            //fill start
            FillRequest fillRequest = new FillRequest();
            fillRequest.setUserName(username);
            fillRequest.setGenerations(4);

            Fill fillService = new Fill();
            fillService.fill(fillRequest);

        } catch (Register.UsernameAlreadyTakenException e) {
            e.printStackTrace();
            loginResponse.message = "This Username is Already Taken";
            System.out.println("error1");
            return loginResponse;

        } catch (SQLException e) {
            e.printStackTrace();
            loginResponse.message = "SQL or Database Error";
            //System.out.println("error2");
            return loginResponse;


        } catch (database.DatabaseException e) {
            e.printStackTrace();
        } finally {
            if (db.getConn() != null) {
                //System.out.println("closed con");
                try {
                    db.closeConnection(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("register service finished");
        return loginResponse;
    }

    /**
     * link person ID and user
     * @param user
     * @return
     * @throws SQLException
     */
    public String addPersonToUser(User user) throws SQLException {
        Person person = new Person();

        //generate personId
        person = personDAO.generatePersonId(person);
        //set person info
        person.setDescendant(user.getUsername());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());
        person.setGender(user.getGender());

        personDAO.addPerson(person);

        return person.getPersonId();
    }

    /**
     * check invalid input from user
     * @param request
     * @throws InvalidDataException
     */
    public static boolean checkInvalidInput(User request) throws InvalidDataException {
        boolean success = true;
        if(request.getUsername() == null || request.getEmail() == null ||
                request.getFirstName() == null || request.getPassword() == null ||
                request.getLastName() == null || request.getGender() == null) {
            throw new InvalidDataException();
        }
        return success;
    }

    private static class UsernameAlreadyTakenException extends Exception { }
}
