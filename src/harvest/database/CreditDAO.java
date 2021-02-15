package harvest.database;

import harvest.model.Credit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.database.ConstantDAO.*;
import static harvest.ui.credit.DisplayCrdTrsController.CREDIT_LIST_LIVE_DATA;

public class CreditDAO extends DAO {

    private static CreditDAO sCreditDAO = new CreditDAO();

    private CreditDAO() {
    }

    public static CreditDAO getInstance() {
        if (sCreditDAO == null) {
            sCreditDAO = new CreditDAO();
            return sCreditDAO;
        }
        return sCreditDAO;
    }

    //*************************************************************
    //Add new Credit Data
    //*************************************************************
    public boolean addData(Credit credit) {
        String sqlStmt = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ", "
                + COLUMN_CREDIT_EMPLOYEE_NAME + ") "
                + "VALUES (?,?,?,?);";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(sqlStmt)) {
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.setInt(3, credit.getEmployeeId());
            preparedStatement.setString(4, credit.getEmployeeName());
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
                "" + COLUMN_CREDIT_EMPLOYEE_ID + " =?, " +
                "" + COLUMN_CREDIT_EMPLOYEE_NAME + " =? " +
                " WHERE " + COLUMN_CREDIT_ID + " = " + credit.getCreditId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setDate(1, credit.getCreditDate());
            preparedStatement.setDouble(2, credit.getCreditAmount());
            preparedStatement.setInt(3, credit.getEmployeeId());
            preparedStatement.setString(4, credit.getEmployeeName());
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
        String sqlStmt = "DELETE FROM " + TABLE_CREDIT + " WHERE " + COLUMN_CREDIT_ID + " =" + credit.getCreditId() + ";";
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

    //*************************************************************
    //Get Credit Data
    //*************************************************************
    public ObservableList<Credit> getData() throws Exception {
        ObservableList<Credit> list = FXCollections.observableArrayList();
        String sqlStmt = "SELECT * FROM " + TABLE_CREDIT + " ORDER BY " + COLUMN_CREDIT_DATE + " DESC;";
        try(Statement statement = dbGetConnect().createStatement()) {
            try(ResultSet resultSet = statement.executeQuery(sqlStmt)) {
                while (resultSet.next()) {
                    Credit credit = new Credit();
                    credit.setCreditId(resultSet.getInt(1));
                    credit.setCreditDate(resultSet.getDate(2));
                    credit.setCreditAmount(resultSet.getDouble(3));
                    credit.setEmployeeName(resultSet.getString(4));
                    credit.setEmployeeId(resultSet.getInt(5));
                    list.add(credit);
                }
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    /* /public void createCreditTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CREDIT + "("
                    + COLUMN_CREDIT_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_CREDIT_DATE + " DATE NOT NULL, "
                    + COLUMN_CREDIT_AMOUNT + " REAL NOT NULL, "
                    + COLUMN_CREDIT_EMPLOYEE_NAME + " VARCHAR(32) NOT NULL, "
                    + COLUMN_CREDIT_EMPLOYEE_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY (" + COLUMN_CREDIT_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ") "
                    + ")");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }*/
}
