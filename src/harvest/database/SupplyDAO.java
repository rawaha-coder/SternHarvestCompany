package harvest.database;

import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Supplier;
import harvest.model.Supply;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.FarmDAO.*;
import static harvest.database.ProductDAO.*;
import static harvest.database.SupplierDAO.*;
import static harvest.ui.supplier.DisplaySupplierController.*;

public class SupplyDAO extends DAO{

    private static SupplyDAO sSupplyDAO = new SupplyDAO();

    //private Constructor
    private SupplyDAO(){ }

    public static SupplyDAO getInstance(){
        if(sSupplyDAO == null){
            sSupplyDAO = new SupplyDAO();
            return sSupplyDAO;
        }
        return sSupplyDAO;
    }

    public static final String TABLE_SUPPLY = "supply";
    public static final String COLUMN_SUPPLY_ID = "id";
    public static final String COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_SUPPLY_FRGN_KEY_FARM_ID = "farm_id";
    public static final String COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID = "product_id";

///    public void createSupplyTable() throws SQLException {
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_SUPPLY  + "("
//                    + COLUMN_SUPPLY_ID + " INTEGER PRIMARY KEY, "
//                    + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID +" INTEGER NOT NULL, "
//                    + COLUMN_SUPPLY_FRGN_KEY_FARM_ID +" INTEGER NOT NULL, "
//                    + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID +" INTEGER NOT NULL, "
//                    + "FOREIGN KEY (" + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID + "), "
//                    + "FOREIGN KEY (" + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + "), "
//                    + "FOREIGN KEY (" + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ") "
//                    + ")");
//        }catch (SQLException e){
//            e.printStackTrace();
//            throw e;
//        }
//    }

    //Get all supplier data
    public List<Supply> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT "
                + TABLE_SUPPLY + "." + COLUMN_SUPPLY_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " "
                + " FROM " + TABLE_SUPPLY + " "
                + "LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " "
                + "LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + " "
                + "LEFT JOIN " + TABLE_PRODUCT  + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + " "
                + " ORDER BY " + COLUMN_SUPPLIER_NAME + " ASC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Supply> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<Supply> supplyList = new ArrayList<>();
        while (resultSet.next()){
            Supply supply = new Supply();
            supply.setSupplyId(resultSet.getInt(1));
            supply.setSupplier(new Supplier(resultSet.getInt(2), resultSet.getString(3)));
            supply.setFarm(new Farm(resultSet.getInt(4), resultSet.getString(5)));
            supply.setProduct(new Product(resultSet.getInt(6), resultSet.getString(7)));
            supplyList.add(supply);
        }
        return supplyList;
    }


    public boolean addData(Supply supply){
        PreparedStatement preparedStatement;
        String insertStmt = "INSERT INTO " + TABLE_SUPPLY + " ("
                + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + ", "
                + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + ", "
                + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + ") "
                + " VALUES (?,?,?);";

        try {
            preparedStatement = dbGetConnect().prepareStatement(insertStmt);
            preparedStatement.setInt(1, supply.getSupplier().getSupplierId());
            preparedStatement.setInt(2, supply.getFarm().getFarmId());
            preparedStatement.setInt(3, supply.getProduct().getProductId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }
    }

    public List<Supply> getSupplyDataBySupplier(Supplier supplier) throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT "
                + TABLE_SUPPLY + "." + COLUMN_SUPPLY_ID + ", "
                + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " "
                + " FROM " + TABLE_SUPPLY + " "
                + "LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " "
                + "LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + " "
                + "LEFT JOIN " + TABLE_PRODUCT  + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + " "
                + " WHERE " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " = " + supplier.getSupplierId() + " "
                + " ORDER BY " + TABLE_FARM + "." + COLUMN_FARM_NAME + " ASC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            List<Supply> supplyList = new ArrayList<>();
            while (resultSet.next()){
                Supply supply = new Supply();
                supply.setSupplyId(resultSet.getInt(1));
                supply.setSupplier(new Supplier(resultSet.getInt(2), resultSet.getString(3)));
                System.out.println(resultSet.getInt(2) + " " + resultSet.getString(3));
                supply.setFarm(new Farm(resultSet.getInt(4), resultSet.getString(5)));
                System.out.println(resultSet.getInt(4) + " " + resultSet.getString(5));
                supply.setProduct(new Product(resultSet.getInt(6), resultSet.getString(7)));
                System.out.println(resultSet.getInt(6) + " " + resultSet.getString(7));
                supplyList.add(supply);
            }
            return supplyList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    public void updateLiveData() {
        SUPPLY_LIST_LIVE_DATA.clear();
        if (SUPPLIER_LIST_LIVE_DATA.size() > 0){
            try {
                SUPPLY_LIST_LIVE_DATA.setAll(getSupplyDataBySupplier(SUPPLIER_LIST_LIVE_DATA.get(0)));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateLiveData(Supplier supplier) {
        SUPPLY_LIST_LIVE_DATA.clear();
        try {
            SUPPLY_LIST_LIVE_DATA.setAll(getSupplyDataBySupplier(supplier));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
