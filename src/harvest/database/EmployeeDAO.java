package harvest.database;

import harvest.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.employee.DisplayEmployeeController.EMPLOYEE_GRAPH_LIVE_DATA;
import static harvest.ui.employee.DisplayEmployeeController.EMPLOYEE_LIST_LIVE_DATA;
import static harvest.database.ConstantDAO.*;

public class EmployeeDAO extends DAO{

    private static EmployeeDAO sEmployeeDAO = new EmployeeDAO();

    //private Constructor
    private EmployeeDAO(){ }

    public static EmployeeDAO getInstance(){
        if (sEmployeeDAO == null){
            sEmployeeDAO = new EmployeeDAO();
            return sEmployeeDAO;
        }
        return sEmployeeDAO;
    }

    //*************************************
    //Update Live data
    //*************************************
    //@Override
    public void updateLiveData() {
        EMPLOYEE_LIST_LIVE_DATA.clear();
        EMPLOYEE_GRAPH_LIVE_DATA.clear();
        try {
            EMPLOYEE_LIST_LIVE_DATA.setAll(getData());
            EMPLOYEE_GRAPH_LIVE_DATA.addAll(employeeStatusGraph());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //*******************************
    //Get selected employees as graphic
    //*******************************
    public ObservableList<PieChart.Data> employeeStatusGraph() throws SQLException {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String q1 = "SELECT COUNT (" + COLUMN_EMPLOYEE_ID + ") FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = " + 1 + " ;";
        String q2 = "SELECT COUNT (" + COLUMN_EMPLOYEE_ID + ") FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = " + 0 + " ;";
        try(Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(q1);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.addAll(new PieChart.Data("Selected Employees: (" + count + ")", count));
            }
            resultSet = statement.executeQuery(q2);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.addAll(new PieChart.Data("Unselected Employees: (" + count + ")", count));
            }
            resultSet.close();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get all employees data
    //*******************************
    public List<Employee> getData() throws Exception {
        List<Employee> employeeList = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_EMPLOYEE + " ORDER BY " + COLUMN_EMPLOYEE_ID + " DESC;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt(1));
                employee.setEmployeeStatus(resultSet.getBoolean(2));
                employee.setEmployeeFirstName(resultSet.getString(3));
                employee.setEmployeeLastName(resultSet.getString(4));
                employee.setEmployeeHireDate(resultSet.getDate(5));
                employee.setEmployeeFireDate(resultSet.getDate(6));
                employee.setEmployeePermissionDate(resultSet.getDate(7));
                employeeList.add(employee);
            }
            return employeeList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }


    //Add Employee
    public boolean addData(Employee employee) {
        String insertStmt = "INSERT INTO " + TABLE_EMPLOYEE + " ("
                + COLUMN_EMPLOYEE_STATUS + ", "
                + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + COLUMN_EMPLOYEE_LAST_NAME + ", "
                + COLUMN_EMPLOYEE_HIRE_DATE + ", "
                + COLUMN_EMPLOYEE_FIRE_DATE + ", "
                + COLUMN_EMPLOYEE_PERMISSION_DATE + ") "
                + "VALUES (?,?,?,?,?,?);";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertStmt)) {
            preparedStatement.setBoolean(1, employee.isEmployeeStatus());
            preparedStatement.setString(2, employee.getEmployeeFirstName());
            preparedStatement.setString(3, employee.getEmployeeLastName());
            preparedStatement.setDate(4, employee.getEmployeeHireDate());
            preparedStatement.setDate(5, employee.getEmployeeFireDate());
            preparedStatement.setDate(6, employee.getEmployeePermissionDate());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //Edit Employee
    public boolean editData(Employee employee) {
        String updateStmt = "UPDATE " + TABLE_EMPLOYEE + " SET " +
                "" + COLUMN_EMPLOYEE_STATUS + " =?, " +
                "" + COLUMN_EMPLOYEE_FIRST_NAME + " =?, " +
                "" + COLUMN_EMPLOYEE_LAST_NAME + " =?, " +
                "" + COLUMN_EMPLOYEE_HIRE_DATE + " =?, " +
                "" + COLUMN_EMPLOYEE_FIRE_DATE + " =?, " +
                "" + COLUMN_EMPLOYEE_PERMISSION_DATE + " =? " +
                " WHERE " + COLUMN_EMPLOYEE_ID + " = " + employee.getEmployeeId() + " ;";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
            preparedStatement.setBoolean(1, employee.isEmployeeStatus());
            preparedStatement.setString(2, employee.getEmployeeFirstName());
            preparedStatement.setString(3, employee.getEmployeeLastName());
            preparedStatement.setDate(4, employee.getEmployeeHireDate());
            preparedStatement.setDate(5, employee.getEmployeeFireDate());
            preparedStatement.setDate(6, employee.getEmployeePermissionDate());
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

    //*************************************
    //UPDATE Employee status in database
    //*************************************
    public boolean updateEmployeeStatusById(int employeeId, boolean employeeStatus) {
        String updateStmt = "UPDATE " + TABLE_EMPLOYEE + " SET " + COLUMN_EMPLOYEE_STATUS + " =?  WHERE " + COLUMN_EMPLOYEE_ID + " = " + employeeId + " ;";
        try( PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
            System.out.println(employeeId + " " + employeeStatus);
            preparedStatement.setBoolean(1, employeeStatus);
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
    public boolean deleteEmployee(Employee employee) {
        String deleteEmployee = "DELETE FROM " + TABLE_EMPLOYEE  + " WHERE " + COLUMN_EMPLOYEE_ID + " ="+ employee.getEmployeeId() +" ;";
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(deleteEmployee);
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //Delete Employee data from all tables
    public boolean deleteAllEmployeeDataById(int id){
        Connection connection = null;
        Statement statement = null;
        String deleteFromEmployee = "DELETE FROM " + TABLE_EMPLOYEE  + " WHERE " + COLUMN_EMPLOYEE_ID + " ="+id+" ;";
        String deleteFromCredit = "DELETE FROM " + TABLE_CREDIT + " WHERE " + COLUMN_CREDIT_EMPLOYEE_ID + " ="+id+" ;";
        String deleteFromTransport = "DELETE FROM " + TABLE_TRANSPORT  + " WHERE " + COLUMN_TRANSPORT_EMPLOYEE_ID + " ="+id+" ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteFromEmployee);

            statement = connection.createStatement();
            statement.execute(deleteFromCredit);

            statement = connection.createStatement();
            statement.execute(deleteFromTransport);

            connection.commit();
            return true;
        } catch (SQLException ex1) {
            assert connection != null;
            try {
                connection.rollback();
            }catch (SQLException ex2){
                ex2.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + ex2.getMessage());
            }
            ex1.printStackTrace();
            return false;
        }finally {
            if (statement != null){
                try {
                    statement.close();
                }catch (SQLException e){/**/}
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){/**/}
            }
            dbDisConnect();
        }
    }

/* **
        public void createEmployeeTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_EMPLOYEE
                    + "(" + COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_EMPLOYEE_STATUS + " BOOLEAN NOT NULL, "
                    + COLUMN_EMPLOYEE_FIRST_NAME + " VARCHAR(16) NOT NULL, "
                    + COLUMN_EMPLOYEE_LAST_NAME + " VARCHAR(16) NOT NULL, "
                    + COLUMN_EMPLOYEE_HIRE_DATE + " DATE, "
                    + COLUMN_EMPLOYEE_FIRE_DATE + " DATE, "
                    + COLUMN_EMPLOYEE_PERMISSION_DATE + " DATE)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
*/
}
