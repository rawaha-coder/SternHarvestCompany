package harvest.viewmodel;

import harvest.database.DBHandler;
import harvest.model.Employee;
import harvest.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IViewModelDAO {
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String COLUMN_EMPLOYEE_ID = "id";
    public static final String COLUMN_EMPLOYEE_STATUS = "status";
    public static final String COLUMN_EMPLOYEE_FIRST_NAME = "first_name";
    public static final String COLUMN_EMPLOYEE_LAST_NAME = "last_name";
    public static final String COLUMN_EMPLOYEE_HIRE_DATE = "hire_date";
    public static final String COLUMN_EMPLOYEE_FIRE_DATE = "fire_date";
    public static final String COLUMN_EMPLOYEE_PERMISSION_DATE = "permission_date";

    public static ObservableList<Employee> EMPLOYEE_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<PieChart.Data> EMPLOYEE_GRAPH_LIVE_DATA = FXCollections.observableArrayList();

    public static void createEmployeeTable() throws SQLException {
        try {
            Statement statement = DBHandler.getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_EMPLOYEE
                    + "(" + COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_EMPLOYEE_STATUS + " INTEGER NOT NULL, "
                    + COLUMN_EMPLOYEE_FIRST_NAME + " TEXT NOT NULL, "
                    + COLUMN_EMPLOYEE_LAST_NAME + " TEXT NOT NULL, "
                    + COLUMN_EMPLOYEE_HIRE_DATE + " DATE, "
                    + COLUMN_EMPLOYEE_FIRE_DATE + " DATE, "
                    + COLUMN_EMPLOYEE_PERMISSION_DATE + " DATE)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //*******************************
    //Get all employees data
    //*******************************
    public List<Employee> getAllEmployeeData() throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM " + TABLE_EMPLOYEE + " ORDER BY " + COLUMN_EMPLOYEE_ID + " DESC;";
        //Execute SELECT statement
        try {
            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
            return getEmployeeFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private List<Employee> getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        List<Employee> employeeList = new ArrayList<>();
        while (resultSet.next()) {
            Employee employee = new Employee();
            employee.setEmployeeId(resultSet.getInt(1));
            employee.setEmployeeStatus(resultSet.getInt(2));
            employee.setEmployeeFirstName(resultSet.getString(3));
            employee.setEmployeeLastName(resultSet.getString(4));
            employee.setEmployeeFullName(Validation.getFullName(resultSet.getString(3), resultSet.getString(4)));
            employee.setEmployeeHireDate(resultSet.getDate(5));
            employee.setEmployeeFireDate(resultSet.getDate(6));
            employee.setEmployeePermissionDate(resultSet.getDate(7));
            employeeList.add(employee);
        }
        return employeeList;
    }

    //*******************************
    //Get selected employees as graphic
    //*******************************
    public ObservableList<PieChart.Data> employeeStatusGraph() throws SQLException {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String q1 = "SELECT COUNT (" + COLUMN_EMPLOYEE_ID + ") FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = " + 1 + " ;";
        String q2 = "SELECT COUNT (" + COLUMN_EMPLOYEE_ID + ") FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = " + 0 + " ;";
        try {
            ResultSet resultSet = DBHandler.dbExecuteQuery(q1);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.addAll(new PieChart.Data("Selected Employees: (" + count + ")", count));
            }
            resultSet = DBHandler.dbExecuteQuery(q2);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.addAll(new PieChart.Data("Unselected Employees: (" + count + ")", count));
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //*************************************
    //Add Employee to database
    //*************************************
    public boolean addEmployee(int status, String firstName, String lastName, Date hireDate, Date fireDate, Date permissionDate) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String insertStmt = "INSERT INTO " + TABLE_EMPLOYEE + " ("
                + COLUMN_EMPLOYEE_STATUS + ", "
                + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + COLUMN_EMPLOYEE_LAST_NAME + ", "
                + COLUMN_EMPLOYEE_HIRE_DATE + ", "
                + COLUMN_EMPLOYEE_FIRE_DATE + ", "
                + COLUMN_EMPLOYEE_PERMISSION_DATE + ") \n"
                + "VALUES (?,?,?,?,?,?);";
        try {
            preparedStatement = DBHandler.getConnection().prepareStatement(insertStmt);
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setDate(4, hireDate);
            preparedStatement.setDate(5, fireDate);
            preparedStatement.setDate(6, permissionDate);
            DBHandler.executePreparedStatement(preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }
    }

    //*************************************
    //UPDATE Employee in database
    //*************************************
    public boolean updateEmployee(int id, int status, String firstName, String lastname, Date hireDate, Date fireDate, Date permissionDate) {
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_EMPLOYEE + " SET " +
                "" + COLUMN_EMPLOYEE_STATUS + " =?, " +
                "" + COLUMN_EMPLOYEE_FIRST_NAME + " =?, " +
                "" + COLUMN_EMPLOYEE_LAST_NAME + " =?, " +
                "" + COLUMN_EMPLOYEE_HIRE_DATE + " =?, " +
                "" + COLUMN_EMPLOYEE_FIRE_DATE + " =?, " +
                "" + COLUMN_EMPLOYEE_PERMISSION_DATE + " =? " +
                " WHERE " + COLUMN_EMPLOYEE_ID + " = " + id + " ;";
        try {
            preparedStatement = DBHandler.getConnection().prepareStatement(updateStmt);
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastname);
            preparedStatement.setDate(4, hireDate);
            preparedStatement.setDate(5, fireDate);
            preparedStatement.setDate(6, permissionDate);
            DBHandler.executePreparedStatement(preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }
    }

    //*************************************
    //UPDATE Employee status in database
    //*************************************
    public boolean updateEmployeeStatusById(int employeeId, boolean employeeStatus) {
        int status = 0;
        if (employeeStatus) {
            status = 1;
        }
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_EMPLOYEE + " SET " + COLUMN_EMPLOYEE_STATUS + " =?  WHERE " + COLUMN_EMPLOYEE_ID + " = " + employeeId + " ;";
        try {
            preparedStatement = DBHandler.getConnection().prepareStatement(updateStmt);
            preparedStatement.setInt(1, status);
            DBHandler.executePreparedStatement(preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }
    }

    //*************************************
    //DELETE Employee by Id
    //*************************************
    @Override
    public boolean deleteById(int keyId) {
        String updateStmt = "DELETE FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_ID + " =" + keyId + ";";
        //Execute UPDATE operation
        try {
            DBHandler.dbExecuteUpdate(updateStmt);
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }
    }

    //*************************************
    //Update Live data
    //*************************************
    @Override
    public void updateLivedata() {
        EMPLOYEE_LIST_LIVE_DATA.clear();
        EMPLOYEE_GRAPH_LIVE_DATA.clear();
        try {
            EMPLOYEE_LIST_LIVE_DATA.setAll(getAllEmployeeData());
            EMPLOYEE_GRAPH_LIVE_DATA.addAll(employeeStatusGraph());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //*******************************
    //SELECT employee Name and Id
    //*******************************

//    public List<Employee> getEmployeeIdAndName() throws SQLException {
//        List<Employee> list = new ArrayList<>();
//        String selectStmt = "SELECT "
//                + COLUMN_EMPLOYEE_ID + ", "
//                + COLUMN_EMPLOYEE_FIRST_NAME + ", "
//                + COLUMN_EMPLOYEE_LAST_NAME + " "
//                + " FROM " + TABLE_EMPLOYEE + " ;";
//        try {
//            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
//            while (resultSet.next()) {
//                Employee employee = new Employee();
//                employee.setEmployeeId(resultSet.getInt(1));
//                employee.setEmployeeFirstName(resultSet.getString(2));
//                employee.setEmployeeLastName(resultSet.getString(3));
//                employee.setEmployeeFullName(Validation.getFullName(resultSet.getString(2), resultSet.getString(3)));
//                list.add(employee);
//            }
//            return list;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    //*******************************
    //SELECT selected employees
    //*******************************
    /*
    public List<String> getSelectedEmployeeName() throws SQLException {
        List<String> list = new ArrayList<>();
        String selectStmt = "SELECT "
                + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + COLUMN_EMPLOYEE_LAST_NAME + " "
                + " FROM " + TABLE_EMPLOYEE + " "
                + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = " + 1 + " ;";
        try {
            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
            while (resultSet.next()) {
                list.add(Validation.getFullName(resultSet.getString(1), resultSet.getString(2)));
            }
            System.out.println("getSelectedEmployee() :");
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }*/


}
