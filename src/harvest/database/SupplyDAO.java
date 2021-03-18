package harvest.database;

import harvest.model.Supplier;
import harvest.model.Supply;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ConstantDAO.*;
import static harvest.controller.DisplaySupplierController.*;

public class SupplyDAO extends DAO{

    private static SupplyDAO sSupplyDAO = new SupplyDAO();

    private SupplyDAO(){ }

    public static SupplyDAO getInstance(){
        if(sSupplyDAO == null){
            sSupplyDAO = new SupplyDAO();
            return sSupplyDAO;
        }
        return sSupplyDAO;
    }

    //*************************************************************
    //Get all supply data
    //*************************************************************
//    public List<Supply> getData() throws Exception {
//        String sqlStmt = "SELECT "
//                + TABLE_SUPPLY + "." + COLUMN_SUPPLY_ID + ", "
//                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
//                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
//                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
//                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
//                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
//                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " "
//                + " FROM " + TABLE_SUPPLY + " "
//                + "LEFT JOIN " + TABLE_SUPPLIER + " "
//                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " "
//                + "LEFT JOIN " + TABLE_FARM + " "
//                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + " "
//                + "LEFT JOIN " + TABLE_PRODUCT  + " "
//                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID  + " = " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + " "
//                + " ORDER BY " + COLUMN_SUPPLIER_NAME + " ASC;";
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            ResultSet resultSet = statement.executeQuery(sqlStmt);
//            return getDataFromResultSet(resultSet);
//        } catch (SQLException e) {
//            System.out.println("SQL select operation has been failed: " + e);
//            throw e;
//        }finally {
//            dbDisConnect();
//        }
//    }

    private List<Supply> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<Supply> supplyList = new ArrayList<>();
        while (resultSet.next()){
            Supply supply = new Supply();
            supply.setSupplyId(resultSet.getInt(1));
            supply.getSupplier().setSupplierId(resultSet.getInt(2));
            supply.getSupplier().setSupplierName(resultSet.getString(3));
            supply.getFarm().setFarmId(resultSet.getInt(4));
            supply.getFarm().setFarmName(resultSet.getString(5));
            supply.getProduct().setProductId(resultSet.getInt(6));
            supply.getProduct().setProductName(resultSet.getString(7));
            supplyList.add(supply);
        }
        return supplyList;
    }

    //*************************************************************
    //Add supply data
    //*************************************************************
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

    //*************************************************************
    //Edit supply data
    //*************************************************************
    public boolean editData(Supply supply) {
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_SUPPLY + " SET "
                + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " =?, "
                + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + " =?, "
                + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID+ " =? "
                + " WHERE " + COLUMN_SUPPLY_ID + " = " + supply.getSupplyId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(updateStmt);
            preparedStatement.setInt(1, supply.getSupplier().getSupplierId());
            preparedStatement.setInt(2, supply.getFarm().getFarmId());
            preparedStatement.setInt(3, supply.getProduct().getProductId());
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

    //*************************************************************
    //delete supply Data By Id
    //*************************************************************
    public boolean deleteDataById(Supply supply) {
        String deleteSupply = "DELETE FROM " + TABLE_SUPPLY + " WHERE " + COLUMN_SUPPLY_ID + " = " + supply.getSupplyId() + " ;";
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(deleteSupply);
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

    //*************************************************************
    //Update supply Data
    //*************************************************************
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

    //*************************************************************
    //Update supply Data by supplier
    //*************************************************************
    public void updateLiveData(Supplier supplier) {
        SUPPLY_LIST_LIVE_DATA.clear();
        try {
            SUPPLY_LIST_LIVE_DATA.setAll(getSupplyDataBySupplier(supplier));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //*************************************************************
    //Get all supply data by Supplier
    //*************************************************************
    public List<Supply> getSupplyDataBySupplier(Supplier supplier) throws Exception {
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
                + " WHERE " + TABLE_SUPPLY + "." + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " = " + supplier.getSupplierId() + " "
                + " ORDER BY " + TABLE_FARM + "." + COLUMN_FARM_NAME + " ASC;";
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
}
