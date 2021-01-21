package harvest.database;

import java.sql.*;

public class DAO {

    private Connection connection = null;


    //Connect to Database
    public Connection dbGetConnect() throws SQLException{
        String DB_NAME = "database.db";
        String CONNECTION_URL = "jdbc:sqlite:" + DB_NAME;
        connection = DriverManager.getConnection(CONNECTION_URL);
        if (connection != null){
            System.out.println("Connect to database, called by: " + getClass().getName());
            return connection;
        }
        return null;
    }

    //Close Database Connection
    public void dbDisConnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("\nCloses database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
}
