package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Model.AuthToken;
import Model.Person;
import Model.User;

import static org.junit.Assert.*;

public class PersonDAOTest {
    private PersonDAO personDAO;
    private Person person;
    private database db;
    private AuthTokenDAO authTokenDAO;
    private AuthToken authToken;

    @Before
    public void setUp() throws database.DatabaseException {
        person = new Person();
        db = new database();
        personDAO = new PersonDAO(db);
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
    public void generatePersonIdTest() {

        assertEquals(null, person.getPersonId());

        person = personDAO.generatePersonId(person);

        assertNotEquals(null, person.getPersonId());
    }

    @Test
    public void addPersonTest() throws SQLException {
        //full input
        person = personDAO.generatePersonId(person);
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        personDAO.addPerson(person);

        assertEquals(person, personDAO.findPerson(person.getPersonId()));
    }

    @Test
    public void addPersonTest2() throws SQLException {
        //some empty input(can be null)
        person = personDAO.generatePersonId(person);
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("");
        person.setMother("");
        person.setSpouse("");

        personDAO.addPerson(person);

        assertEquals(person, personDAO.findPerson(person.getPersonId()));
    }

    @Test
    public void addAllPersonTest() throws SQLException {
        List<Person> expected = new ArrayList<>();

        person = personDAO.generatePersonId(person);
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        expected.add(person);

        Person person2 = new Person();

        person2 = personDAO.generatePersonId(person2);
        person2.setDescendant("bluedia");
        person2.setFirstName("Ethan");
        person2.setLastName("Hwang");
        person2.setGender("m");
        person2.setFather("Father");
        person2.setMother("Mother");
        person2.setSpouse("Spouse");

        expected.add(person2);

        personDAO.addAllPerson(expected);

        assertEquals(person, personDAO.findPerson(person.getPersonId()));
        assertEquals(person2, personDAO.findPerson(person2.getPersonId()));

    }

    @Test
    public void findPerson() throws SQLException {
        person = personDAO.generatePersonId(person);
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("Father");
        person.setMother("Mother");
        person.setSpouse("Spouse");

        //add
        personDAO.addPerson(person);

        //test find correct personId
        assertEquals(person, personDAO.findPerson(person.getPersonId()));

        //test find incorrect personId
        assertNotEquals(person, personDAO.findPerson("wrongID"));

    }

    @Test
    public void editPersonTest() throws SQLException {
        person = personDAO.generatePersonId(person);
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("");
        person.setMother("");
        person.setSpouse("");

        //add
        personDAO.addPerson(person);

        //check added
        assertEquals(person, personDAO.findPerson(person.getPersonId()));

        Person father = new Person();
        Person mother = new Person();

        father.setPersonId("Father");
        mother.setPersonId("Mother");

        personDAO.editPerson(person.getPersonId(), "Father", "Mother");
        person = personDAO.findPerson(person.getPersonId());

        //see if person have same mother and father as person2
        assertEquals("Father", person.getFather());
        assertEquals("Mother", person.getMother());
    }

    @Test
    public void clear() throws SQLException {
        person = personDAO.generatePersonId(person);
        person.setDescendant("bluedia");
        person.setFirstName("Ethan");
        person.setLastName("Hwang");
        person.setGender("m");
        person.setFather("");
        person.setMother("");
        person.setSpouse("");

        //add
        personDAO.addPerson(person);

        assertEquals(person, personDAO.findPerson(person.getPersonId()));

        personDAO.clear();

        assertNotEquals(person, personDAO.findPerson(person.getPersonId()));
        assertEquals(null, personDAO.findPerson(person.getPersonId()));

    }
}