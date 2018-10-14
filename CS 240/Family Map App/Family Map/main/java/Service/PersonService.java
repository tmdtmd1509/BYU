package Service;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Person;
import Model.User;
import Request.PeopleRequest;
import Request.PersonRequest;
import Response.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;

/**
 * person service class
 * get the request from the handler
 * usually get the data from the DataAccess package and resend the data to server
 */
public class PersonService {
    private database db = new database();
    public database getDb() { return db; }
    private UserDAO userDAO = new UserDAO(db);
    private PersonDAO personDAO = new PersonDAO(db);
    private AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);

    /**
     * This method is for the specific person data. person/[personId]
     * @param request
     * @return response
     * @throws InvalidAuthTokenException
     * @throws InvalidPersonIdException
     * @throws RequestedPersonNotBelongException
     * @throws InternalException
     */
    public PersonResponse person(PersonRequest request) throws InternalException {
        PersonResponse response = new PersonResponse();
        try {
            System.out.println("Person service started");
            //open database
            db.openConnection();

            //get request
            String authToken = request.getAuthToken();
            String personId = request.getPersonId();

            //find person by personId
            Person person = personDAO.findPerson(personId);
            if (person != null) {
                //see if decendant is the user
                User user = userDAO.findUserByToken(authToken);
                if (person.getDescendant().equals(user.getUsername())) {
                    //make response
                    response.descendant = person.getDescendant();
                    response.personId = person.getPersonId();
                    response.firstName = person.getFirstName();
                    response.lastName = person.getLastName();
                    response.gender = person.getGender();
                    response.father = person.getFather();
                    response.mother = person.getMother();
                    response.spouse = person.getSpouse();
                } else {
                    throw new RequestedPersonNotBelongException();
                }
            } else {
                throw new InvalidPersonIdException();
            }
            //close databse
            db.closeConnection(true);

        } catch (PersonService.InvalidPersonIdException e) {
            e.printStackTrace();
            response.message = "Invalid PersonId";
        } catch (PersonService.RequestedPersonNotBelongException e) {
            e.printStackTrace();
            response.message = "Requested Person is not belong to this user";
        } catch (SQLException e) {
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
        System.out.println("Person service finished");
        return response;
    }

    /**
     * this method is for the whole people of current user
     * @param request
     * @return
     * @throws InvalidAuthTokenException
     * @throws InternalException
     */
    public PeopleResponse people(PeopleRequest request) throws InternalException {
        PeopleResponse response = new PeopleResponse();
        try {
            System.out.println("People service started");
            //open database
            db.openConnection();

            //get request
            String authToken = request.getAuthToken();
            //find person by personId
            User user = userDAO.findUserByToken(authToken);
            //see if decendant is the user
            String userName = user.getUsername();
            response.data = personDAO.getFamily(userName);

            //close database
            db.closeConnection(true);

        }  catch (SQLException e) {
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
        System.out.println("people service finished");
        return response;
    }

    /**
     * check authToekn
     * @param authToken
     * @return
     * @throws InternalException
     */
    public boolean checkAuthToken(String authToken) throws InternalException {
        boolean success = false;
        try {
            //open database
            db.openConnection();
            //check auth Token
            success = authTokenDAO.checkAuthToken(authToken);
            //close databse
            db.closeConnection(true);

        } catch (SQLException | database.DatabaseException e) {
            e.printStackTrace();
        }
        return success;
    }

    private class InvalidAuthTokenException extends Exception { }
    private class InvalidPersonIdException extends Exception { }
    private class RequestedPersonNotBelongException extends Exception { }
}
