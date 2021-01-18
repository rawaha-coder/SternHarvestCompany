package harvest.database;

import harvest.model.Credit;
import harvest.util.Validation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static harvest.util.Constant.*;
import static harvest.ui.credit.DisplayCrdTrsController.CREDIT_LIST_LIVE_DATA;

public class CreditDAO extends DAO{

//    public static final String TABLE_CREDIT = "credit";
//    public static final String COLUMN_CREDIT_ID = "id";
//    public static final String COLUMN_CREDIT_DATE = "date";
//    public static final String COLUMN_CREDIT_AMOUNT = "amount";
//    public static final String COLUMN_CREDIT_EMPLOYEE_ID = "employee_id";

    private static CreditDAO sCreditDAO = new CreditDAO();

    //private Constructor
    private CreditDAO(){}

    public static CreditDAO getInstance(){
        if (sCreditDAO == null){
            sCreditDAO = new CreditDAO();
            return sCreditDAO;
        }
        return sCreditDAO;
    }

    //*************************************************************
    //Get Credit Data
    //*************************************************************
    public List<Credit> getData() throws Exception {
        String sqlStmt = "SELECT "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_DATE + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME + " "
                + " FROM " + TABLE_CREDIT + " "
                + "LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_CREDIT + "." + COLUMN_CREDIT_EMPLOYEE_ID + " "
                + " ORDER BY " + COLUMN_CREDIT_DATE + " DESC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getCreditDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private List<Credit> getCreditDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<Credit> creditList = new ArrayList<>();
        while (resultSet.next()) {
            Credit credit = new Credit();
            credit.setCreditId(resultSet.getInt(1));
            credit.setCreditDate(resultSet.getDate(2));
            credit.setCreditAmount(resultSet.getDouble(3));
            credit.setEmployeeId(resultSet.getInt(4));
            credit.setCreditEmployee(resultSet.getString(5) + " " + resultSet.getString(6));
            creditList.add(credit);
        }
        return creditList;
    }

    //*************************************************************
    //Add new Credit Data
    //*************************************************************
    public boolean addData(Credit credit) {
        String sqlStmt = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?);";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(sqlStmt)){
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.setInt(3, credit.getEmployeeId());
            preparedStatement.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Update Credit Data
    //*************************************************************
    public boolean editData(Credit credit) {
        PreparedStatement preparedStatement;
        //Declare a UPDATE statement
        String sqlStmt = "UPDATE " + TABLE_CREDIT + " SET " +
                "" + COLUMN_CREDIT_DATE + " =?," +
                "" + COLUMN_CREDIT_AMOUNT + " =? " +
                " WHERE " + COLUMN_CREDIT_ID + " = " + credit.getCreditId() + " ;";
        //Execute UPDATE operation
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Delete Credit Data
    //*************************************************************
    //@Override
    public boolean deleteDataById(int Id) {
        //Declare a DELETE statement
        String sqlStmt = "DELETE FROM " + TABLE_CREDIT + " WHERE " + COLUMN_CREDIT_ID + " =" + Id + ";";
        //Execute UPDATE operation
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Observe Credit Data
    //*************************************************************
    public void updateLiveData() {
        CREDIT_LIST_LIVE_DATA.clear();
        try {
            CREDIT_LIST_LIVE_DATA.setAll(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
//    public void createCreditTable() throws SQLException {
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS " + CREDITS_TABLE + "("
//                    + COLUMN_CREDIT_ID + " INTEGER PRIMARY KEY, "
//                    + COLUMN_CREDIT_DATE + " DATE NOT NULL, "
//                    + COLUMN_CREDIT_AMOUNT + " REAL NOT NULL, "
//                    + COLUMN_CREDIT_EMPLOYEE_ID + " INTEGER NOT NULL, "
//                    + "FOREIGN KEY (" + COLUMN_CREDIT_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ") "
//                    + ")");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
*/
}
