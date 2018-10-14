package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import Model.User;

/**
 * User Data Access class
 * get the user info from Database
 */
public class UserDAO {

    private database db;
    public UserDAO(database DB) {
        this.db = DB;
    }

    /**
     * add new user to database
     * @param user
     */
    public void addUser(User user) throws SQLException {
        PreparedStatement stmt = null;

        try {
            String sql = "insert into User "+
                    "(Username, Password, Email, First_Name, Last_Name, Gender, Person_ID) " +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
    }

    /**
     * add all user
     * use when user call load.
     * @param array
     * @throws SQLException
     */
    public void addAllUser(List<User> array) throws SQLException {
        for(int i = 0; i < array.size(); i++) {

            addUser(array.get(i));
        }
    }

    /**
     * access to database and get the user information
     * @return user full information from database
     */
    public User findUser(String username) throws SQLException {
        User user = null;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select Username, Password, Email, First_Name, "+
                        "Last_Name, Gender, Person_ID from User ";

            stmt = db.getConn().prepareStatement(sql);

            rs = stmt.executeQuery();

            while(rs.next()) {

                user = new User();//added
                String userName = rs.getString(1);
                String password = rs.getString(2);
                String email = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                String gender = rs.getString(6);
                String personId = rs.getString(7);

                user.setUsername(userName);
                user.setPassword(password);
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setGender(gender);
                user.setPersonId(personId);
                //System.out.println("while worked");

                if(user.getUsername().equals(username)) {
                    //System.out.println(user.getUsername()+" same username " + username);
                    return user;
                }
                else {
                    //System.out.println(user.getUsername() + " different username " + username);
                    user = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
            if(rs != null) rs.close();
        }
        return user;
    }

    /**
     * find user by authToken
     * @param token
     * @return User
     * @throws SQLException
     */
    public User findUserByToken(String token) throws SQLException {

        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select User.Username, User.Password, User.Email, User.First_Name, "+
                    "User.Last_Name, User.Gender, User.Person_ID, AuthToken.Auth_Token from User "+
                    "inner join AuthToken on User.Username = AuthToken.Username";

            stmt = db.getConn().prepareStatement(sql);
            rs = stmt.executeQuery();

            while(rs.next()) {
                user = new User();//added
                String userName = rs.getString(1);
                String password = rs.getString(2);
                String email = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                String gender = rs.getString(6);
                String personId = rs.getString(7);
                String authToken = rs.getString(8);

                user.setUsername(userName);
                user.setPassword(password);
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setGender(gender);
                user.setPersonId(personId);

                if(authToken.equals(token)) {
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }

        return user;
    }

    /**
     * clear all the user data
     */
    public void clear() throws SQLException {
        Statement stmt = null;

        try {
            stmt = db.getConn().createStatement();

            String sql = "DROP TABLE User ";

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
     * reset the User table after clear
     * @throws SQLException
     */
    private void reset() throws SQLException {
        Statement stmt = null;

        try {
            stmt = db.getConn().createStatement();

            String sql = "create table User" +
                    "(" +
                    "Username text not null primary key, " +
                    "Password text not null, " +
                    "Email text not null, " +
                    "First_Name text not null, " +
                    "Last_Name text not null, " +
                    "Gender ck_gender check (gender in ('f', 'm')), " +
                    "Person_ID text not null" +
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
