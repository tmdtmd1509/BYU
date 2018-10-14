package DataAccess;

import java.io.File;
import java.sql.*;

/**
 *
 * @author Heeseung Hwang
 * @version 1.0 July 12, 2018
 *
 *
 * load the database from SQL.
 * close the database
 * create the table and dictionary to use.
 *
 * */
public class database {
    /**
     *start the driver. (load to communicate)
     */
    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection conn;

    /**
     *Open the database connection
     *
     *if connection is already open, skip
     * @throws DatabaseException
     */
    public void openConnection() throws DatabaseException {
        if(conn == null) {
            try {
                String CONNECTION_URL = "jdbc:sqlite:FamilyMap.db";

                // Open a database connection
                conn = DriverManager.getConnection(CONNECTION_URL);

                // Start a transaction
                conn.setAutoCommit(false);
//            userDAO = new UserDAO(conn);
            } catch (SQLException e) {
                throw new DatabaseException();
            }
        }

    }

    /**
     * get connection
     * @return
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * close database.
     * decide if I want to keep change or rollbasck
     * @param commit
     * @throws SQLException
     */
    public void closeConnection(boolean commit) throws SQLException {
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            conn.close();
        }
        conn = null;
    }
    /**
     * database Exception
     */
    public class DatabaseException extends Exception {
    }
}