package harvest.database;

import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static harvest.database.ConstantDAO.*;
import static harvest.database.ConstantDAO.COLUMN_FARM_ID;

public class ProductionDAO extends DAO {

    private static ProductionDAO mProductionDAO = new ProductionDAO();

    private ProductionDAO() {
    }

    public static ProductionDAO getInstance() {
        if (mProductionDAO == null) {
            mProductionDAO = new ProductionDAO();
        }
        return mProductionDAO;
    }



    //*******************************
    //Get all production data
    //*******************************
    public ObservableList<Production> getData() throws Exception {
        String select = "SELECT * FROM " + TABLE_PRODUCTION + " WHERE " +
                " ORDER BY " + COLUMN_PRODUCTION_DATE + " DESC ;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getProductionFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get all production data by date
    //*******************************
    public ObservableList<Production> getDataByDate(Date date) throws Exception {
        String select = "SELECT * FROM " + TABLE_PRODUCTION + " WHERE " + COLUMN_PRODUCTION_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + COLUMN_PRODUCTION_DATE + " DESC ;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getProductionFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Search production data by date
    //*******************************
    public ObservableList<Production> searchDataByDate(Date fromDate, Date toDate) throws Exception {
        String select = "SELECT * FROM " + TABLE_PRODUCTION + " WHERE " + COLUMN_PRODUCTION_DATE + " >= " + fromDate.getTime()
                + " AND " + COLUMN_PRODUCTION_DATE + " <= " + toDate.getTime()
                + " ORDER BY " + COLUMN_PRODUCTION_DATE + " DESC ;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getProductionFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    //Help method to get data from resultSet
    private ObservableList<Production> getProductionFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<Production> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Production production = new Production();
            production.setProductionID(resultSet.getInt(1));
            production.setProductionDate(resultSet.getDate(2));
            production.setSupplierID(resultSet.getInt(3));
            production.setSupplierName(resultSet.getString(4));
            production.setFarmID(resultSet.getInt(5));
            production.setFarmName(resultSet.getString(6));
            production.setProductID(resultSet.getInt(7));
            production.setProductName(resultSet.getString(8));
            production.setProductCode(resultSet.getString(9));
            production.setTotalEmployee(resultSet.getInt(10));
            production.setGoodQuantity(resultSet.getDouble(11));
            production.setProductionPrice(resultSet.getDouble(12));
            production.setProductionCost(resultSet.getDouble(13));
            list.add(production);
        }
        return list;
    }

    //*******************************
    //Add production data
    //*******************************
    public boolean addProduction(Production production) {
        String insertHarvestHours = "INSERT INTO " + TABLE_PRODUCTION + " ("
                + COLUMN_PRODUCTION_DATE + ", "
                + COLUMN_PRODUCTION_SUPPLIER_ID + ", "
                + COLUMN_PRODUCTION_SUPPLIER_NAME + ", "
                + COLUMN_PRODUCTION_FARM_ID + ", "
                + COLUMN_PRODUCTION_FARM_NAME + ", "
                + COLUMN_PRODUCTION_PRODUCT_ID + ", "
                + COLUMN_PRODUCTION_PRODUCT_NAME + ", "
                + COLUMN_PRODUCTION_PRODUCT_CODE + ", "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + ", "
                + COLUMN_PRODUCTION_GOOD_QUANTITY + ", "
                + COLUMN_PRODUCTION_PRICE + ", "
                + COLUMN_PRODUCTION_COST + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertHarvestHours)) {
            preparedStatement.setDate(1, production.getProductionDate());
            preparedStatement.setInt(2, production.getSupplierID());
            preparedStatement.setString(3, production.getSupplierName());
            preparedStatement.setInt(4, production.getFarmID());
            preparedStatement.setString(5, production.getFarmName());
            preparedStatement.setInt(6, production.getProductID());
            preparedStatement.setString(7, production.getProductName());
            preparedStatement.setString(8, production.getProductCode());
            preparedStatement.setInt(9, production.getTotalEmployee());
            preparedStatement.setDouble(10, production.getGoodQuantity());
            preparedStatement.setDouble(11, production.getProductionPrice());
            preparedStatement.setDouble(12, production.getProductionCost());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Update Production data
    //*******************************
    public boolean updateProduction(Production production) {
        String updateStmt = "UPDATE " + TABLE_PRODUCTION + " SET "
                + COLUMN_PRODUCTION_DATE + " =?, "
                + COLUMN_PRODUCTION_SUPPLIER_ID + " =?, "
                + COLUMN_PRODUCTION_SUPPLIER_NAME + " =?, "
                + COLUMN_PRODUCTION_FARM_ID + " =?, "
                + COLUMN_PRODUCTION_FARM_NAME + " =?, "
                + COLUMN_PRODUCTION_PRODUCT_ID + " =?, "
                + COLUMN_PRODUCTION_PRODUCT_NAME + " =?, "
                + COLUMN_PRODUCTION_PRODUCT_CODE + " =?, "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + " =?, "
                + COLUMN_PRODUCTION_GOOD_QUANTITY + " =?, "
                + COLUMN_PRODUCTION_PRICE + " =?, "
                + COLUMN_PRODUCTION_COST + " =? "
                + " WHERE " + COLUMN_PRODUCTION_ID + " = " + production.getProductionID() + " ;";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //Check if production id exist
    public int isExists(Production production) {
        int value = -1;
        String stmt = "SELECT EXISTS (SELECT " + COLUMN_PRODUCTION_ID + " FROM " + TABLE_PRODUCTION + " WHERE  "
                + COLUMN_PRODUCTION_DATE + " = " + production.getProductionDate().getTime() + " AND "
                + COLUMN_PRODUCTION_SUPPLIER_ID + " = " + production.getSupplierID() + " AND "
                + COLUMN_PRODUCTION_FARM_ID + " = " + production.getFarmID() + " AND "
                + COLUMN_PRODUCTION_PRODUCT_ID + " = " + production.getProductID() + " AND "
                + COLUMN_PRODUCTION_PRODUCT_CODE + " = " + production.getProductCode()
                + " )";
        try (Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(stmt);
            value = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbDisConnect();
        }
        return value;
    }

    public int getProductionId(Production production) {
        int value = -1;
        String stmt = "SELECT EXISTS (SELECT " + COLUMN_PRODUCTION_ID + " FROM " + TABLE_PRODUCTION + " WHERE  "
                + COLUMN_PRODUCTION_DATE + " = " + production.getProductionDate().getTime() + " AND "
                + COLUMN_PRODUCTION_SUPPLIER_ID + " = " + production.getSupplierID() + " AND "
                + COLUMN_PRODUCTION_FARM_ID + " = " + production.getFarmID() + " AND "
                + COLUMN_PRODUCTION_PRODUCT_ID + " = " + production.getProductID() + " AND "
                + COLUMN_PRODUCTION_PRODUCT_CODE + " = " + production.getProductCode()
                + " )";
        try (Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(stmt);
            value = resultSet.getInt(1);
            System.out.println(value);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbDisConnect();
        }
        return value;
    }

    //Create production table
    public void createProductionTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTION + " ("
                + COLUMN_PRODUCTION_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_PRODUCTION_DATE + " DATE NOT NULL, "
                + COLUMN_PRODUCTION_SUPPLIER_ID + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_SUPPLIER_NAME + " TEXT NOT NULL, "
                + COLUMN_PRODUCTION_FARM_ID + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_FARM_NAME + " TEXT NOT NULL, "
                + COLUMN_PRODUCTION_PRODUCT_ID + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_PRODUCT_NAME + " TEXT NOT NULL, "
                + COLUMN_PRODUCTION_PRODUCT_CODE + " TEXT NOT NULL, "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_GOOD_QUANTITY + " REAL NOT NULL, "
                + COLUMN_PRODUCTION_PRICE + " REAL NOT NULL, "
                + COLUMN_PRODUCTION_COST + " REAL NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID + ")"
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
