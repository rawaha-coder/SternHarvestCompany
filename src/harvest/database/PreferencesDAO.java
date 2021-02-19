package harvest.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.database.ConstantDAO.*;

public class PreferencesDAO extends DAO{

    private static PreferencesDAO mPreferencesDAO = new PreferencesDAO();

    private PreferencesDAO(){}

    public static PreferencesDAO getInstance(){
        if (mPreferencesDAO == null){
            mPreferencesDAO = new PreferencesDAO();
        }
        return  mPreferencesDAO;
    }

    public double[] getData() throws SQLException{
        String selectStmt = "SELECT * FROM " + TABLE_PREFERENCE ;
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(selectStmt)) {
            return getArraySet(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private double[] getArraySet(ResultSet resultSet) throws SQLException {
        double[] array = new double[3];
        try {
            while (resultSet.next()){
                array[0] = resultSet.getInt(1);
                array[1] = resultSet.getInt(2);
                array[2] = resultSet.getInt(3);
            }
            return array;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    //Edit product
    public boolean editData(Double p, Double gp, Double hp) {
        String updateStmt = "UPDATE " + TABLE_PREFERENCE + " SET "
                + COLUMN_PENALTY + " =?, "
                + COLUMN_GENERAL_PENALTY + " =?, "
                + COLUMN_HOUR_PRICE + " =? ;";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
            preparedStatement.setDouble(1,p);
            preparedStatement.setDouble(2, gp);
            preparedStatement.setDouble(3, hp);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }


    /*
     public void createPreferencesTable() throws SQLException {
         try {
             Statement statement = dbGetConnect().createStatement();
             statement.execute("CREATE TABLE IF NOT EXISTS "+ TABLE_PREFERENCE
                     +"("+ COLUMN_PENALTY +" REAL NOT NULL, "
                     + COLUMN_GENERAL_PENALTY +" REAL NOT NULL, "
                     + COLUMN_HOUR_PRICE +" REAL NOT NULL "
                     + ")");
         }catch (SQLException e){
             e.printStackTrace();
             throw e;
         }
         initPreferencesTable();
     }

     //init Preferences Table
     public boolean initPreferencesTable() {
         String initPreferences = "INSERT INTO " + TABLE_PREFERENCE + " ("
                 + COLUMN_PENALTY + ", "
                 + COLUMN_GENERAL_PENALTY + ", "
                 + COLUMN_HOUR_PRICE+ ") "
                 + "VALUES (?,?,?);";

         try {
             PreparedStatement preparedStatement = dbGetConnect().prepareStatement(initPreferences);
             preparedStatement.setDouble(1, 0);
             preparedStatement.setDouble(2, 0);
             preparedStatement.setDouble(3, 0);

             preparedStatement.execute();
             return true;
         } catch (Exception e) {
             e.printStackTrace();
             System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
             return false;
         }finally {
             dbDisConnect();
         }
     }
    */
}
