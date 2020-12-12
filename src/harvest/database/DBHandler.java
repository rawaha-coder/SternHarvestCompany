package harvest.database;

import java.sql.*;

public class DBHandler {

    public static final String DB_NAME = "database.db";
    public static final String CONNECTION_URL = "jdbc:sqlite:" + DB_NAME;
    private static Connection connection = null;
    private static final DBHandler instance = new DBHandler();

    public static Connection getConnection() {
        return connection;
    }

    private DBHandler(){
    }

    public static DBHandler getInstance() {
        return instance;
    }

    //DB Execute Query Operation
    public static void executePreparedStatement(PreparedStatement queryStmt) throws SQLException{
        try {
            queryStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problem occurred at executeQuery operation : " + e.getMessage());
            throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException{
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(queryStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problem occurred at executeQuery operation : " + e.getMessage());
            throw e;
        }
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException{
        //Declare statement as null
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        }
    }


    //Connect to Database
    public static void dbConnect() {
        try {
            connection = DriverManager.getConnection(CONNECTION_URL);
            System.out.println("Connect to database :)");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to database: " + e.getMessage());
        }
    }

    //Close Database Connection
    public static void dbDisconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Closes database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
}
