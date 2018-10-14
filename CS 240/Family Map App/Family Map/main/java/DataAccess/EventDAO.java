package DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import Model.Event;
import Model.Person;


/**
 * Event Data Access class
 * get the event info from Database
 */
public class EventDAO {

    private database db;
    /**
     * constructor: get the database
     */
    public EventDAO(database DB) {
        this.db = DB;
    }

    /**
     * get unique EventId
     * @param event
     * @return
     */
    public Event generateEventId(Event event) {
        //make UUID
        String token = UUID.randomUUID().toString();
        //set EventID
        event.setEventId(token);

        return event;
    }

    /**
     * add new event to database
     * @param event
     */
    public void addEvent(Event event) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into Event "+
                    "(Event_ID, Descendant, Person, Latitude, Longitude, Country, City, EventType, Year) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = db.getConn().prepareStatement(sql);

            stmt.setString(1, event.getEventId());
            stmt.setString(2, event.getDescendant());
            stmt.setString(3, event.getPersonId());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }

    }

    /**
     * add all events
     * use when user call load.
     * @param array
     * @throws SQLException
     */
    public void addAllEvents(List<Event> array) throws SQLException {
        for(int i = 0; i < array.size(); i++) {
            addEvent(array.get(i));
        }
    }

    /**
     * check if this event is exist and find the event
     *
     * @param eventId
     * @return event full information from database
     */
    public Event findEvent(String eventId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event event = null;

        boolean commit = false;

        try {
            String sql = "select * from Event where Event.Event_ID = ?";

            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, eventId);
            rs = stmt.executeQuery();

            while(rs.next()) {
                event = new Event();

                event.setEventId(rs.getString(1));
                event.setDescendant(rs.getString(2));
                event.setPersonId(rs.getString(3));
                event.setLatitude(Double.valueOf(rs.getString(4)));
                event.setLongitude(Double.valueOf(rs.getString(5)));
                event.setCountry(rs.getString(6));
                event.setCity(rs.getString(7));
                event.setEventType(rs.getString(8));
                event.setYear(rs.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        return event;
    }

    /**
     * find the events belongs to this person
     * only getFamilyEvents call this method
     *
     * @param personId
     * @return every events belongs to this person
     */
    public List<Event> findEventByPerson(String personId) throws SQLException {
        List<Event> events = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event event;

        try {
            String sql = "select Event_ID, Descendant, Person, Latitude, Longitude, Country, City, EventType, Year "+
                    "from Event where Person = ?";

            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, personId);
            rs = stmt.executeQuery();
            events = new ArrayList<>();

            while(rs.next()) {
                event = new Event();

                event.setEventId(rs.getString(1));
                event.setDescendant(rs.getString(2));
                event.setPersonId(rs.getString(3));
                event.setLatitude(Double.valueOf(rs.getString(4)));
                event.setLongitude(Double.valueOf(rs.getString(5)));
                event.setCountry(rs.getString(6));
                event.setCity(rs.getString(7));
                event.setEventType(rs.getString(8));
                event.setYear(rs.getInt(9));

                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
            if (rs != null) rs.close();
        }

        return events;
    }

    /**
     * get  ALL events for ALL family members of the current user.
     * use when user call event
     * @param username
     * @return
     * @throws SQLException
     * @throws database.DatabaseException
     */
    public List<Event> getFamilyEvents(String username) throws SQLException {
        PersonDAO personDAO = new PersonDAO(db);
        List<Event> events = new ArrayList<Event>();
        List<Person> family = personDAO.getFamily(username);

        for(int i = 0; i < family.size(); i++) {
            String personId = family.get(i).getPersonId();
            events.addAll(findEventByPerson(personId));
        }
        return events;
    }

    /**
     * delete related to descendant
     */
    public void deleteRelated(String username) throws SQLException {
        Statement stmt = null;

        try {
            stmt = db.getConn().createStatement();
            System.out.println("delete2");
            String sql = "delete from Event where Descendant = \'"+username+"\'";

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
    }


    /**
     * clear all event data
     */
    public void clear() throws SQLException {
        Statement stmt = null;

        try {
            stmt = db.getConn().createStatement();

            String sql = "DROP TABLE Event ";

            stmt.executeUpdate(sql);

            reset();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
    }

    /**
     * after clear reset Event table
     * @throws SQLException
     */
    private void reset() throws SQLException {
        Statement stmt = null;

        try {
            stmt = db.getConn().createStatement();

            String sql = "create table Event " +
                    "(" +
                    "Event_ID text not null primary key, " +
                    "Descendant text not null, " +
                    "Person text not null, " +
                    "Latitude real, " +
                    "Longitude real, " +
                    "Country text, " +
                    "City text, " +
                    "EventType text not null, " +
                    "Year integer " +
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
