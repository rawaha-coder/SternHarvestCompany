package harvest.database;

import harvest.model.Credit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.database.ConstantDAO.*;
import static harvest.view.DisplayCrdTrsController.CREDIT_LIST_LIVE_DATA;

public class CreditDAO extends DAO {

    private static CreditDAO sCreditDAO = new CreditDAO();

    private CreditDAO() { }

    public static CreditDAO getInstance() {
        if (sCreditDAO == null) {
            sCreditDAO = new CreditDAO();
            return sCreditDAO;
        }
        return sCreditDAO;
    }

    //*************************************************************
    //Get Credit Data
    //*************************************************************
    public ObservableList<Credit> getData() throws Exception {
        ObservableList<Credit> list = FXCollections.observableArrayList();
        String sqlStmt = "SELECT "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_DATE + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE  + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE  + "." + COLUMN_EMPLOYEE_LAST_NAME + " "
                + " FROM " + TABLE_CREDIT
                + " LEFT JOIN " + TABLE_EMPLOYEE
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_CREDIT + "." + COLUMN_CREDIT_EMPLOYEE_ID
                + " WHERE " + TABLE_CREDIT + "." + COLUMN_CREDIT_IS_EXIST + " = 1 "
                + " ORDER BY " + COLUMN_CREDIT_DATE + " DESC;";
        try(Statement statement = dbGetConnect().createStatement()) {
            try(ResultSet resultSet = statement.executeQuery(sqlStmt)) {
                while (resultSet.next()) {
                    Credit credit = new Credit();
                    credit.setCreditId(resultSet.getInt(1));
                    credit.setCreditDate(resultSet.getDate(2));
                    credit.setCreditAmount(resultSet.getDouble(3));
                    credit.getEmployee().setEmployeeId(resultSet.getInt(4));
                    credit.getEmployee().setEmployeeFirstName(resultSet.getString(5).toUpperCase());
                    credit.getEmployee().setEmployeeLastName(resultSet.getString(6).toUpperCase());
                    list.add(credit);
                }
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    //*************************************************************
    //Add new Credit Data
    //*************************************************************
    public boolean addData(Credit credit) {
        String sqlStmt = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ", "
                + COLUMN_CREDIT_IS_EXIST + ") "
                + "VALUES (?,?,?,?);";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(sqlStmt)) {
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.setInt(3, credit.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, 1);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Update Credit Data
    //*************************************************************
    public boolean editData(Credit credit) {
        PreparedStatement preparedStatement;
        String sqlStmt = "UPDATE " + TABLE_CREDIT + " SET " +
                "" + COLUMN_CREDIT_DATE + " =?," +
                "" + COLUMN_CREDIT_AMOUNT + " =?, " +
                "" + COLUMN_CREDIT_EMPLOYEE_ID + " =? " +
                " WHERE " + COLUMN_CREDIT_ID + " = " + credit.getCreditId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.setInt(3, credit.getEmployee().getEmployeeId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Delete Credit Data
    //*************************************************************
    public boolean deleteData(Credit credit) {
        String sqlStmt = "UPDATE " + TABLE_CREDIT
                + " SET " + COLUMN_CREDIT_IS_EXIST + " = 0 "
                + " WHERE " + COLUMN_CREDIT_ID + " = " + credit.getCreditId() + ";";
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        } finally {
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



     /*public void createCreditTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CREDIT + "("
                    + COLUMN_CREDIT_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_CREDIT_DATE + " DATE NOT NULL, "
                    + COLUMN_CREDIT_AMOUNT + " REAL NOT NULL, "
                    + COLUMN_CREDIT_EMPLOYEE_ID + " INTEGER NOT NULL, "
                    + COLUMN_CREDIT_IS_EXIST + " INTEGER DEFAULT 1 , "
                    + "FOREIGN KEY (" + COLUMN_CREDIT_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ") "
                    + ")");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }*/
}
