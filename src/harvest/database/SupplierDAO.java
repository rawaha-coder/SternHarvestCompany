package harvest.database;

import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.FarmDAO.*;
import static harvest.database.ProductDAO.*;
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
    public static final String COLUMN_SUPPLIER_FRGN_KEY_FARM_ID = "farm_id";
    public static final String COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID = "product_id";


    public List<Supplier> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_FIRSTNAME + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_LASTNAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " "
                + " FROM " + TABLE_SUPPLIER + " "
                + "LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID  + " = " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_FRGN_KEY_FARM_ID + " "
                + "LEFT JOIN " + TABLE_PRODUCT  + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID  + " = " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID + " "
                + " ORDER BY " + COLUMN_SUPPLIER_NAME + " ASC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getSupplierFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Supplier> getSupplierFromResultSet(ResultSet resultSet) throws SQLException {
        List<Supplier> supplierList = new ArrayList<>();
        while (resultSet.next()){
            Supplier supplier = new Supplier();
            supplier.setSupplierId(resultSet.getInt(1));
            supplier.setSupplierName(resultSet.getString(2));
            supplier.setSupplierFirstname(resultSet.getString(3));
            supplier.setSupplierLastname(resultSet.getString(4));
            supplier.setSupplierFarm(new Farm(resultSet.getInt(5), resultSet.getString(6)));
            supplier.setSupplierProduct(new Product(resultSet.getInt(7), resultSet.getString(8)));
            supplierList.add(supplier);
        }
        return supplierList;
    }


    public boolean addData(Supplier supplier) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String insertStmt = "INSERT INTO " + TABLE_SUPPLIER + " ("
                + COLUMN_SUPPLIER_NAME + ", "
                + COLUMN_SUPPLIER_FIRSTNAME + ", "
                + COLUMN_SUPPLIER_LASTNAME + ", "
                + COLUMN_SUPPLIER_FRGN_KEY_FARM_ID + ", "
                + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID + ") " +
                "VALUES (?,?,?,?,?);";

        try {
            preparedStatement = dbGetConnect().prepareStatement(insertStmt);
            preparedStatement.setString(1, supplier.getSupplierName());
            preparedStatement.setString(2, supplier.getSupplierFirstname());
            preparedStatement.setString(3, supplier.getSupplierLastname());
            preparedStatement.setInt(4, supplier.getSupplierFarm().getFarmId());
            preparedStatement.setInt(5, supplier.getSupplierProduct().getProductId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }
    }


    public boolean editData(Supplier supplier) {
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_SUPPLIER + " SET "
                + COLUMN_SUPPLIER_NAME + " =?, "
                + COLUMN_SUPPLIER_FIRSTNAME + " =?, "
                + COLUMN_SUPPLIER_LASTNAME + " =?, "
                + COLUMN_SUPPLIER_FRGN_KEY_FARM_ID + " =?, "
                + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID + " =? "
                + " WHERE " + COLUMN_SUPPLIER_ID+ " = " + supplier.getSupplierId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(updateStmt);
            preparedStatement.setString(1, supplier.getSupplierName());
            preparedStatement.setString(2, supplier.getSupplierFirstname());
            preparedStatement.setString(3, supplier.getSupplierLastname());
            preparedStatement.setInt(4, supplier.getSupplierFarm().getFarmId());
            preparedStatement.setInt(5, supplier.getSupplierProduct().getProductId());
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


    public boolean deleteDataById(int Id) {
        String sqlStmt = "DELETE FROM " + TABLE_SUPPLIER + " WHERE " + COLUMN_SUPPLIER_ID + " = " + Id + " ;";
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //@Override
    public void updateLiveData() {
        SUPPLIER_LIST_LIVE_DATA.clear();
        try {
            SUPPLIER_LIST_LIVE_DATA.setAll(getData());
        }catch (Exception e){
            e.printStackTrace();
        }
        SUPPLIER_FARM_LIST_LIVE_DATA.clear();
        SUPPLIER_PRODUCT_LIST_LIVE_DATA.clear();
        if (SUPPLIER_LIST_LIVE_DATA.size() > 0){
            for (Supplier supplier : SUPPLIER_LIST_LIVE_DATA){
                SUPPLIER_FARM_LIST_LIVE_DATA.add(supplier.getSupplierFarm());
                SUPPLIER_PRODUCT_LIST_LIVE_DATA.add(supplier.getSupplierProduct());
            }
        }
    }

/* **
        public boolean createSupplierTable() throws SQLException{
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_SUPPLIERS + "("
                    + COLUMN_SUPPLIER_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_SUPPLIER_NAME + " VARCHAR(32) UNIQUE NOT NULL, "
                    + COLUMN_SUPPLIER_FIRSTNAME + " VARCHAR(16) NULL, "
                    + COLUMN_SUPPLIER_LASTNAME + " VARCHAR(16) NULL, "
                    + COLUMN_SUPPLIER_FRGN_KEY_FARM_ID +" INTEGER NOT NULL, "
                    + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID +" INTEGER NOT NULL, "
                    + "FOREIGN KEY (" + COLUMN_SUPPLIER_FRGN_KEY_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + "), "
                    + "FOREIGN KEY (" + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ") "
                    + ")");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
*/
}
