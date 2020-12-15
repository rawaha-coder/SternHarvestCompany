package harvest.database;

import harvest.model.Credit;
import harvest.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.EmployeeDAO.*;
import static harvest.ui.credit.DisplayCreditController.CREDIT_LIST_LIVE_DATA;

public class CreditDAO extends DAO implements DAOList<Credit> {

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

    public static final String CREDITS_TABLE = "credit";
    public static final String COLUMN_CREDIT_ID = "id";
    public static final String COLUMN_CREDIT_DATE = "date";
    public static final String COLUMN_CREDIT_AMOUNT = "amount";
    public static final String COLUMN_CREDIT_EMPLOYEE_ID = "employee_id";
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
    //*************************************************************
    //SELECT all Credit Data from CREDITS_TABLE and TABLE_EMPLOYEE
    //*************************************************************
    @Override
    public List<Credit> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT "
                + CREDITS_TABLE + "." + COLUMN_CREDIT_ID + ", "
                + CREDITS_TABLE + "." + COLUMN_CREDIT_DATE + ", "
                + CREDITS_TABLE + "." + COLUMN_CREDIT_AMOUNT + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME + " "
                + " FROM " + CREDITS_TABLE + " "
                + "LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + CREDITS_TABLE + "." + COLUMN_CREDIT_EMPLOYEE_ID + " "
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
            credit.setCreditEmployee(Validation.getFullName(resultSet.getString(5), resultSet.getString(6)));
            creditList.add(credit);
        }
        return creditList;
    }

    @Override
    public boolean addData(Credit credit) {
        PreparedStatement preparedStatement;
        String sqlStmt = "INSERT INTO " + CREDITS_TABLE + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?);";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.setInt(3, credit.getEmployeeId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

    @Override
    public boolean editData(Credit credit) {
        return false;
    }

    @Override
    public boolean deleteDataById(int Id) {
        //Declare a DELETE statement
        String sqlStmt = "DELETE FROM " + CREDITS_TABLE + " WHERE " + COLUMN_CREDIT_ID + " =" + Id + ";";
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

    @Override
    public void updateLiveData() {
        CREDIT_LIST_LIVE_DATA.clear();
        try {
            CREDIT_LIST_LIVE_DATA.setAll(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
