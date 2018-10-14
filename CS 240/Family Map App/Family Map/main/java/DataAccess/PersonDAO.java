package DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Model.Person;


/**
 * Person Data Access class
 * get the person info from Database
 */
public class PersonDAO {

    private database db;
    public PersonDAO(database DB) {
        this.db = DB;
    }

    /**
     * make unique personId
     * @param person
     * @return
     */
    public Person generatePersonId(Person person) {
        //make UUID
        String token = UUID.randomUUID().toString();
        //set personId
        person.setPersonId(token);

        return person;
    }

    /**
     * add new person to database
     * @param person
     */
    public void addPerson(Person person) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into Person "+
                    "(Person_ID, Descendant, First_Name, Last_Name, Gender, Father, Mother, Spouse) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = db.getConn().prepareStatement(sql);

            //System.out.println(person.getPersonId()+" "+person.getDescendant() + " "+person.getFirstName()+" "+person.getLastName()+" "+person.getGender());
            stmt.setString(1, person.getPersonId());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
    }

    /**
     * add all person
     * use when user call load.
     * @param array
     * @throws SQLException
     */
    public void addAllPerson(List<Person> array) throws SQLException {
        for(int i = 0; i < array.size(); i++) {
            addPerson(array.get(i));
        }
    }

    /**
     * check if this person is exist and find the person
     * if not null
     *
     * @param personId
     * @return person full information from database
     */
    public Person findPerson(String personId) throws SQLException {
        if(personId == null) return null;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Person person = null;

        try {
            String sql = "select Person_ID, Descendant, First_Name, "+
                    "Last_Name, Gender, Father, Mother, Spouse from Person "+
                    "where Person.Person_ID = ?";

            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, personId);
            rs = stmt.executeQuery();

            while(rs.next()) {
                person = new Person();

                person.setPersonId(rs.getString(1));
                person.setDescendant(rs.getString(2));
                person.setFirstName(rs.getString(3));
                person.setLastName(rs.getString(4));
                person.setGender(rs.getString(5));
                person.setFather(rs.getString(6));
                person.setMother(rs.getString(7));
                person.setSpouse(rs.getString(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        return person;
    }

    /**
     * edit person information
     * when user generate family, person's data should be updated
     * use when user use fill or register
     * @param personId
     * @param fatherId
     * @param motherId
     * @return
     * @throws SQLException
     */
    public Person editPerson(String personId, String fatherId, String motherId) throws SQLException {
        if(personId == null) return null;
        System.out.println("edit person");

        Person person = new Person();
        PreparedStatement stmt = null;

        try {
            String sql = "update Person set Father = ?, Mother = ?"
                        + " where Person_ID = ?";

            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, fatherId);
            stmt.setString(2, motherId);
            stmt.setString(3, personId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
        return person;
    }

    /**
     * set a male and a female as a couple
     * @param personId
     * @param spouseId
     * @return
     * @throws SQLException
     */
    public Person setSpouse(String personId, String spouseId) throws SQLException {
        if(personId == null) return null;

        Person person = new Person();
        PreparedStatement stmt = null;

        try {
            String sql = "update Person set Spouse = ?"
                    + " where Person_ID = ?";

            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, spouseId);
            stmt.setString(2, personId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
        return person;
    }

    /**
     * get all the family or user
     * use when user use person
     * @param username
     * @return
     * @throws SQLException
     */
    public List<Person> getFamily(String username) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Person> family = null;
        try {
            String sql = "select Person_ID, Descendant, First_Name,"+
                        "Last_Name, Gender, Father, Mother, Spouse from Person where Person.Descendant = ?";
            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, username);//?
            rs = stmt.executeQuery();
            family = new ArrayList<Person>();

            while(rs.next()) {
                Person person = new Person();

                person.setPersonId(rs.getString(1));
                person.setDescendant(rs.getString(2));
                person.setFirstName(rs.getString(3));
                person.setLastName(rs.getString(4));
                person.setGender(rs.getString(5));
                person.setFather(rs.getString(6));
                person.setMother(rs.getString(7));
                person.setSpouse(rs.getString(8));

                family.add(person);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        return family;
    }

    /**
     * delete all person related to user
     * @param username
     * @throws SQLException
     */
    public void deleteRelated(String username) throws SQLException {
        Statement stmt = null;
        //PreparedStatement stmt2 = null;

        boolean delete = false;
        try {
            stmt = db.getConn().createStatement();

            System.out.println("delete1");
            String sql = "delete from Person where Descendant = \'"+username+"\'";

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
//            if(stmt2 != null) stmt2.close();
        }
    }
    /**
     * clear all person data
     */
    public void clear() throws SQLException {
        Statement stmt = null;
        //PreparedStatement stmt2 = null;

        try {
            stmt = db.getConn().createStatement();

            String sql = "DROP TABLE Person ";

            stmt.executeUpdate(sql);

            reset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
//            if(stmt2 != null) stmt2.close();
        }
    }

    /**
     * after clear reset Person table
     * @throws SQLException
     */
    private void reset() throws SQLException {
        Statement stmt = null;
        boolean commit = true;

        try {
            stmt = db.getConn().createStatement();

            String sql = "create table Person " +
                    "(" +
                    "Person_ID text not null primary key, " +
                    "Descendant text not null, " +
                    "First_Name text not null, " +
                    "Last_Name text not null, " +
                    "Gender ck_gender check (gender in ('f', 'm')), " +
                    "Father text, " +
                    "Mother text, " +
                    "Spouse text" +
                    ")";

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }

}
