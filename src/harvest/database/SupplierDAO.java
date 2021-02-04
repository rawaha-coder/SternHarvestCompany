package harvest.database;

import harvest.model.Farm;
import harvest.model.Supplier;
import harvest.model.Supply;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static harvest.database.SupplyDAO.*;
import static harvest.database.SupplyDAO.COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID;
import static harvest.ui.supplier.DisplaySupplierController.*;
import static harvest.database.ConstantDAO.*;

public class SupplierDAO extends DAO{

    private static SupplierDAO sSupplierDAO = new SupplierDAO();

    //private Constructor
    private SupplierDAO(){ }

    public static SupplierDAO getInstance(){
        if(sSupplierDAO == null){
            sSupplierDAO = new SupplierDAO();
            return sSupplierDAO;
        }
        return sSupplierDAO;
    }

    //Get all supplier data
    public List<Supplier> getData() throws Exception {
        List<Supplier> supplierList = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_SUPPLIER + " ORDER BY " + COLUMN_SUPPLIER_NAME + " ASC;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()){
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getInt(1));
                supplier.setSupplierName(resultSet.getString(2));
                supplier.setSupplierFirstname(resultSet.getString(3));
                supplier.setSupplierLastname(resultSet.getString(4));
                supplierList.add(supplier);
            }
            return supplierList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //Get data farm as map by farm name
    public Map<String, Supplier> getSupplierMap() throws Exception {
        Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
        String sqlStmt = "SELECT * FROM " + TABLE_SUPPLIER + " ORDER BY " + COLUMN_SUPPLIER_NAME + " ASC;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)){
            while (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getInt(1));
                supplier.setSupplierName(resultSet.getString(2));
                supplier.setSupplierFirstname(resultSet.getString(3));
                supplier.setSupplierLastname(resultSet.getString(4));
                mSupplierMap.put(supplier.getSupplierName(), supplier);
            }
            return mSupplierMap;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    public boolean addData(Supplier supplier) {
        PreparedStatement preparedStatement;
        String insertStmt = "INSERT INTO " + TABLE_SUPPLIER + " ("
                + COLUMN_SUPPLIER_NAME + ", "
                + COLUMN_SUPPLIER_FIRSTNAME + ", "
                + COLUMN_SUPPLIER_LASTNAME + ") "
                + " VALUES (?,?,?);";

        try {
            preparedStatement = dbGetConnect().prepareStatement(insertStmt);
            preparedStatement.setString(1, supplier.getSupplierName());
            preparedStatement.setString(2, supplier.getSupplierFirstname());
            preparedStatement.setString(3, supplier.getSupplierLastname());
            preparedStatement.execute();
            updateLiveData();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Add data to Supplier and Supply tables
    //*************************************************************
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


    public boolean editData(Supplier supplier) {
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_SUPPLIER + " SET "
                + COLUMN_SUPPLIER_NAME + " =?, "
                + COLUMN_SUPPLIER_FIRSTNAME + " =?, "
                + COLUMN_SUPPLIER_LASTNAME + " =? "
                + " WHERE " + COLUMN_SUPPLIER_ID+ " = " + supplier.getSupplierId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(updateStmt);
            preparedStatement.setString(1, supplier.getSupplierName());
            preparedStatement.setString(2, supplier.getSupplierFirstname());
            preparedStatement.setString(3, supplier.getSupplierLastname());
            preparedStatement.execute();
            preparedStatement.close();
            updateLiveData();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //Delete Employee data from all tables
    public boolean deleteSupplierData(Supplier supplier){
        Connection connection = null;
        Statement statement = null;
        String deleteSupplier = "DELETE FROM " + TABLE_SUPPLIER   + " WHERE " + COLUMN_EMPLOYEE_ID + " ="+ supplier.getSupplierId() +" ;";
        String deleteSupply = "DELETE FROM " + TABLE_SUPPLY + " WHERE " + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " ="+ supplier.getSupplierId() +" ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(deleteSupplier);
            statement = connection.createStatement();
            statement.execute(deleteSupply);
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

    public void updateLiveData() {
        SUPPLIER_LIST_LIVE_DATA.clear();
        try {
            SUPPLIER_LIST_LIVE_DATA.setAll(getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

/* **
        public void createSupplierTable() throws SQLException{
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_SUPPLIER + "("
                    + COLUMN_SUPPLIER_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_SUPPLIER_NAME + " VARCHAR(32) UNIQUE NOT NULL, "
                    + COLUMN_SUPPLIER_FIRSTNAME + " VARCHAR(16) NULL, "
                    + COLUMN_SUPPLIER_LASTNAME + " VARCHAR(16) NULL)");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
*/

}
