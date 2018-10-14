package DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import Model.AuthToken;

public class AuthTokenDAO {

    //to use same database object get db from service
    private database db;

    /**
     * constructor get the database
     * @param DB
     */
    public AuthTokenDAO(database DB) { this.db = DB; }

    /**
     * make unique authToken
     * @return authToken
     */
    public AuthToken generateAuthToken(String username) {

        //make UUID
        String token = UUID.randomUUID().toString();

        //set token and username
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken(token);
        authToken.setUserId(username);

        return authToken;
    }

    /**
     * add authtoken to database
     *
     * @param authToken
     * @throws SQLException
     */
    public void addAuthToken(AuthToken authToken) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into AuthToken "+
                    "(Username, Auth_token) " +
                    "values (?, ?)";
            stmt = db.getConn().prepareStatement(sql);
            stmt.setString(1, authToken.getUsername());
            stmt.setString(2, authToken.getAuthToken());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
        }
    }

    /**
     * check if this authToken is in the database
     * @param authToken
     * @return boolean if I found this authToken from database
     * @throws SQLException
     */
    public boolean checkAuthToken(String authToken) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean find = false;
        try {
            String sql = "select Auth_token from AuthToken";

            stmt = db.getConn().prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                String token = rs.getString(1);
                if(authToken.equals(token)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(stmt != null) stmt.close();
            if(rs != null) rs.close();
        }
        return find;
    }

    /**
     * clear all person data
     */
    public void clear() throws SQLException {
        Statement stmt = null;

        boolean commit = true;
        try {

            stmt = db.getConn().createStatement();

            String sql = "DROP TABLE AuthToken ";

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
     * after reset AuthToken class reset
     *
     * @throws SQLException
     */
    private void reset() throws SQLException {
        Statement stmt = null;

        try {
            stmt = db.getConn().createStatement();

            String sql = "create table AuthToken " +
                    "(" +
                    "Username text not null, " +
                    "Auth_token text not null primary key " +
                    ")";

            stmt.executeUpdate(sql);

            System.out.println("final reset finished");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }
}
