package harvest.database;

import harvest.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.supplier.DisplaySupplierController.*;

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

    public static final String TABLE_SUPPLIER = "supplier";
    public static final String COLUMN_SUPPLIER_ID = "id";
    public static final String COLUMN_SUPPLIER_NAME = "company_name";
    public static final String COLUMN_SUPPLIER_FIRSTNAME = "firstname";
    public static final String COLUMN_SUPPLIER_LASTNAME = "lastname";

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
