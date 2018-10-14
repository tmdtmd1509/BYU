package Service;

import com.sun.media.sound.InvalidDataException;

import java.sql.SQLException;
import java.util.Random;

import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import RandomFile.RandomLocation;
import Request.FillRequest;
import Response.FillResponse;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import DataAccess.database;
import RandomFile.RandomFileReader;
/**
 * fill service class
 * generate family
 * get the request from the server and handle the request
 * usually get the data from the DataAccess package and resend the data to server
 */
public class Fill {
    private int generationCount = 0;
    private int totalEventNum = 0;
    private database db = new database();
    public database getDb() { return db; }
    private UserDAO userDAO = new UserDAO(db);
    private PersonDAO personDAO = new PersonDAO(db);
    private EventDAO eventDAO = new EventDAO(db);

    /**
     * get the request and generate family
     * @param request
     * @return
     * @throws InternalException
     */
    public FillResponse fill(FillRequest request) throws InternalException {
        FillResponse response = new FillResponse();
        try {
            System.out.println("fill service started");
            //open db
            db.openConnection();
            //get request
            int generationNum = request.getGenerations();
            String userName = request.getUserName();

            //find total people number need to add
            int personNum = 0;
            for(int i = 0; i < generationNum+1; i++) {
                personNum += (int) Math.pow(2, i);
            }
            //see if user exist
            User user = userDAO.findUser(userName);
            if (user != null) {
                //check generation number
                if(generationNum > 0) {
                    //generate start
                    //use recursion
                    generateFamily(personNum, generationNum, user.getUsername(), user.getPersonId());
                }
                else {
                    throw new InvalidDataException();
                }
            } else {
                throw new InvalidUsernameException();
            }

            //make response
            response.message = "Successfully added "+ personNum + " persons and "+ totalEventNum + " events to the database.";
            response.personNum = personNum;
            response.eventNum = totalEventNum;

        } catch (SQLException e) {
            e.printStackTrace();
            response.message = "SQL Error";
        } catch (InvalidUsernameException e) {
            e.printStackTrace();
            response.message = "Invalid Username Error";
        } catch (InvalidDataException e) {
            e.printStackTrace();
            response.message = "Invalid generation number Error";
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
        System.out.println("fill service finished");
        return response;
    }

    /**
     * generate famliy see if generation 0 is user input
     * call recursion method
     * @param personNum
     * @param generationNum
     * @param userName
     * @param personId
     * @throws SQLException
     * @throws database.DatabaseException
     */
    private void generateFamily(int personNum, int generationNum, String userName, String personId) throws SQLException, database.DatabaseException {
        if(personDAO.getFamily(userName).size() != personNum) {
            //if you fill from nothing
            if(personDAO.getFamily(userName).size() == 1) {
                //make parents recursion
                makeParents(generationNum, personNum, userName, personId);
                //create user's event
                makeEventForPerson(userName, personId, 0);
            }
            //if you load something and add more
            else {
                if (personDAO.getFamily(userName).size() != 1) {
                    personDAO.deleteRelated(userName);
                    eventDAO.deleteRelated(userName);

                    Person person = new Person();

                    //generate personId
                    person = personDAO.generatePersonId(person);
                    User user1 = userDAO.findUser(userName);
                    //set person info
                    person.setDescendant(user1.getUsername());
                    person.setFirstName(user1.getFirstName());
                    person.setLastName(user1.getLastName());
                    person.setGender(user1.getGender());

                    personDAO.addPerson(person);
                    generateFamily(personNum, generationNum, userName, person.getPersonId());
                }
            }
        }
    }

    /**
     * make parents
     * make mother, father. set spouse and make event
     * @param generationNum
     * @param personNum
     * @param userName
     * @param personId
     * @throws SQLException
     * @throws database.DatabaseException
     */
    private void makeParents(int generationNum, int personNum, String userName, String personId) throws SQLException, database.DatabaseException {
        //make father
        Person father = makeFather(userName);
        System.out.println("make father");
        //make mother
        Person mother = makeMother(userName);
        System.out.println("make mother");
        //set as a spouse to each other
        setSpouse(userName, father.getPersonId(), mother.getPersonId());
        System.out.println("married");

        //edit person's data
        db.openConnection();
        personDAO.editPerson(personId, father.getPersonId(), mother.getPersonId());
        db.closeConnection(true);

        generationCount++;

        db.openConnection();
        if(personDAO.getFamily(userName).size() != personNum) {
            db.closeConnection(true);
            if (generationCount != generationNum) {
                makeParents(generationNum, personNum, userName, father.getPersonId());
                makeParents(generationNum, personNum, userName, mother.getPersonId());
                generationCount--;
            } else {
                generationCount--;
            }
        }
    }

    /**
     * make father
     * @param userName
     * @return
     * @throws SQLException
     * @throws database.DatabaseException
     */
    private Person makeFather(String userName) throws SQLException, database.DatabaseException {
        db.openConnection();

        Person person = new Person();
        RandomFileReader randomFileReader = new RandomFileReader();
        //get random father's name
        String father = randomFileReader.getRandomMaleName();
        //get same last name as user
        String surname = userDAO.findUser(userName).getLastName();


        //set person information
        person.setDescendant(userName);
        person = personDAO.generatePersonId(person);
        person.setFirstName(father);
        person.setLastName(surname);
        person.setGender("m");

        db.closeConnection(true);
        db.openConnection();
        //add person
        personDAO.addPerson(person);

        db.closeConnection(true);
        return person;
    }

    /**
     * make mother
     * @param userName
     * @return
     * @throws SQLException
     * @throws database.DatabaseException
     */
    private Person makeMother(String userName) throws SQLException, database.DatabaseException {
        Person person = new Person();
        RandomFileReader randomFileReader = new RandomFileReader();
        //get random mother's name
        String mother = randomFileReader.getRandomFemaleName();
        //get random mother's surname
        String surname = randomFileReader.getRandomSurname();

        db.openConnection();

        //set person information
        person.setDescendant(userName);
        person = personDAO.generatePersonId(person);
        person.setFirstName(mother);
        person.setLastName(surname);
        person.setGender("f");

        db.closeConnection(true);
        db.openConnection();

        //add person
        personDAO.addPerson(person);

        db.closeConnection(true);

        return person;
    }

    /**
     * set father and mother as spouse
     * @param userName
     * @param maleId
     * @param femaleId
     * @throws SQLException
     * @throws database.DatabaseException
     */
    private void setSpouse(String userName, String maleId, String femaleId) throws SQLException, database.DatabaseException {
        db.openConnection();
        //set each other as a spouse
        personDAO.setSpouse(maleId, femaleId);
        personDAO.setSpouse(femaleId, maleId);

        db.closeConnection(true);

        //to check if events are making from setSpouse or it is for the user
        int spouseMarriageYear = 1;
        //make event for a male
        spouseMarriageYear = makeEventForPerson(userName, maleId, spouseMarriageYear);
        //make event for a female
        makeEventForPerson(userName, femaleId, spouseMarriageYear);

    }

    private int makeEventForPerson(String userName, String personId, int spouseMarriageYear) throws SQLException, database.DatabaseException {
        db.openConnection();
        int ONE_GENERATION_IS_TWENTY = 20;
        int GET_BAPTISM_AT_EIGHT = 8;
        int PEOPLE_LIVE_ATLEAST_SIXTY = 60;
        int DEPENDS_ON_YOUR_CHOICES = 10;
        //random year selection
        int MY_BIRTH_YEAR = 1992;
        int birthYear = MY_BIRTH_YEAR - generationCount*ONE_GENERATION_IS_TWENTY - (int) (Math.random()*DEPENDS_ON_YOUR_CHOICES);
        int baptismYear = birthYear + GET_BAPTISM_AT_EIGHT;
        int marriageYear = birthYear + ONE_GENERATION_IS_TWENTY + (int)(Math.random()*DEPENDS_ON_YOUR_CHOICES);
        int deathYear = birthYear + PEOPLE_LIVE_ATLEAST_SIXTY + (int) (Math.random()*DEPENDS_ON_YOUR_CHOICES);

        //female follows male's marriage year
        if(personDAO.findPerson(personId).getGender().equals("f")) {
            marriageYear = spouseMarriageYear;
        }
        //set birth
        Event birth = setEvent("birth", userName, personId, birthYear);
        //set baptism
        Event baptism = setEvent("baptism", userName, personId, baptismYear);

        //if you got marry(if it is not user)
        if(spouseMarriageYear != 0) {
            //set marriage
            Event marriage = setEvent("marriage", userName, personId, marriageYear);
            //set death
            Event death = setEvent("death", userName, personId, deathYear);
            db.openConnection();
            //add Event
            eventDAO.addEvent(marriage);
            eventDAO.addEvent(death);
        }
        db.openConnection();
        //add Event
        eventDAO.addEvent(birth);
        eventDAO.addEvent(baptism);

        db.closeConnection(true);

        return marriageYear;
    }

    /**
     * set event for a person
     * @param eventType
     * @param userName
     * @param personId
     * @param year
     * @return
     * @throws database.DatabaseException
     * @throws SQLException
     */
    private Event setEvent(String eventType, String userName, String personId, int year) throws database.DatabaseException, SQLException {
        db.openConnection();

        //random location selection
        RandomFileReader randomFileReader = new RandomFileReader();
        RandomLocation.Location location = randomFileReader.getRandomLocation();

        //generate EventId
        Event event = new Event();
        event = eventDAO.generateEventId(event);
        db.closeConnection(true);

        //set event information
        event.setDescendant(userName);
        event.setPersonId(personId);
        event.setLatitude(location.getLatitude());
        event.setLongitude(location.getLongitude());
        event.setCountry(location.getCountry());
        event.setCity(location.getCity());
        event.setEventType(eventType);
        event.setYear(year);

        //check number of event added
        totalEventNum++;

        return event;
    }

    private class InvalidUsernameException extends Exception { }
}
