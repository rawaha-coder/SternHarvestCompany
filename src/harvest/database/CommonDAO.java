package harvest.database;

import harvest.model.*;
import java.sql.*;

import static harvest.database.ConstantDAO.*;
import static harvest.database.SupplyDAO.*;

public class CommonDAO extends DAO{

    private static CommonDAO sCommonDAO = new CommonDAO();

    //private Constructor
    private CommonDAO(){ }

    public static CommonDAO getInstance(){
        if(sCommonDAO == null){
            sCommonDAO = new CommonDAO();
            return sCommonDAO;
        }
        return sCommonDAO;
    }

    //*************************************************************
    //Supplier and Supply tables
    //*************************************************************
    //Add data to Farm and Season tables
    public boolean addSupplierSupplyData(Supply supply){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSupplier = "INSERT INTO " + TABLE_SUPPLIER + " ("
                + COLUMN_SUPPLIER_NAME + ", "
                + COLUMN_SUPPLIER_FIRSTNAME + ", "
                + COLUMN_SUPPLIER_LASTNAME + ") "
                + " VALUES (?,?,?);";

        String sqlGetLastId = "SELECT last_insert_rowid() FROM " + TABLE_SUPPLIER + " ;";

        String insertSupply = "INSERT INTO " + TABLE_SUPPLY + " ("
                + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + ", "
                + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + ", "
                + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + ") "
                + "VALUES (?,?,?);";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertSupplier);
            preparedStatement.setString(1, supply.getSupplier().getSupplierName());
            preparedStatement.setString(2, supply.getSupplier().getSupplierFirstname());
            preparedStatement.setString(3, supply.getSupplier().getSupplierLastname());
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetLastId);
            int id = resultSet.getInt(1);

            preparedStatement = connection.prepareStatement(insertSupply);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, supply.getFarm().getFarmId());
            preparedStatement.setInt(3, supply.getProduct().getProductId());
            preparedStatement.execute();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            assert connection != null;
            assert preparedStatement != null;
            try {
                connection.rollback();
                preparedStatement.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public boolean deleteAllSupplierDataById(int id){
        Connection connection = null;
        Statement statement;
        String deleteSupplier = "DELETE FROM " + TABLE_SUPPLIER + " WHERE " + COLUMN_SUPPLIER_ID + " = " + id + " ;";
        String deleteSupply = "DELETE FROM " +  TABLE_SUPPLY + " WHERE " + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " = " + id + " ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(deleteSupplier);
            statement = connection.createStatement();
            statement.execute(deleteSupply);
            connection.commit();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + sqlException.getMessage());
            }
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            dbDisConnect();
        }
    }

    //*************************************************************
    //Employee and others tables
    //*************************************************************
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
}
