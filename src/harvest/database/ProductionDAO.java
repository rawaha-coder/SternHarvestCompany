package harvest.database;

import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

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
        String select = "SELECT * FROM " + TABLE_PRODUCTION + " ORDER BY " + COLUMN_PRODUCTION_DATE + " DESC ;";
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
//            production.setProductionID(resultSet.getInt(1));
//            production.setProductionDate(resultSet.getDate(2));
//            production.setSupplierID(resultSet.getInt(3));
//            production.setSupplierName(resultSet.getString(4));
//            production.setFarmID(resultSet.getInt(5));
//            production.setFarmName(resultSet.getString(6));
//            production.setProductID(resultSet.getInt(7));
//            production.setProductName(resultSet.getString(8));
//            production.setProductCode(resultSet.getString(9));
//            production.setTotalEmployee(resultSet.getInt(10));
//            production.setGoodQuantity(resultSet.getDouble(11));
//            production.setProductionPrice(resultSet.getDouble(12));
//            production.setProductionCost(resultSet.getDouble(13));
            list.add(production);
        }
        return list;
    }

    //*******************************
    //Add production data
    //*******************************
    public boolean addProduction(Production production) {
        String insertProduction = "INSERT INTO " + TABLE_PRODUCTION + " ("
                + COLUMN_PRODUCTION_DATE + ", "
                + COLUMN_PRODUCTION_SUPPLIER_ID + ", "
                + COLUMN_PRODUCTION_FARM_ID + ", "
                + COLUMN_PRODUCTION_PRODUCT_ID + ", "
                + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + ", "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + ", "
                + COLUMN_PRODUCTION_TOTAL_QUANTITY + ", "
                + COLUMN_PRODUCTION_PRICE + ", "
                //+ COLUMN_PRODUCTION_COST + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertProduction)) {
//            preparedStatement.setDate(1, production.getProductionDate());
//            preparedStatement.setInt(2, production.getSupplierID());
//            preparedStatement.setString(3, production.getSupplierName());
//            preparedStatement.setInt(4, production.getFarmID());
//            preparedStatement.setString(5, production.getFarmName());
//            preparedStatement.setInt(6, production.getProductID());
//            preparedStatement.setString(7, production.getProductName());
//            preparedStatement.setString(8, production.getProductCode());
//            preparedStatement.setInt(9, production.getTotalEmployee());
//            preparedStatement.setDouble(10, production.getGoodQuantity());
//            preparedStatement.setDouble(11, production.getProductionPrice());
//            preparedStatement.setDouble(12, production.getProductionCost());
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
//    public boolean updateProduction(Production production) {
//        String updateStmt = "UPDATE " + TABLE_PRODUCTION + " SET "
//                + COLUMN_PRODUCTION_DATE + " =?, "
//                + COLUMN_PRODUCTION_SUPPLIER_ID + " =?, "
//                + COLUMN_PRODUCTION_FARM_ID + " =?, "
//                + COLUMN_PRODUCTION_PRODUCT_ID + " =?, "
//                + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + " =?, "
//                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + " =?, "
//                + COLUMN_PRODUCTION_TOTAL_QUANTITY + " =?, "
//                + COLUMN_PRODUCTION_PRICE + " =?, "
//                + COLUMN_PRODUCTION_COST + " =? "
//                + " WHERE " + COLUMN_PRODUCTION_ID + " = " + production.getProductionID() + " ;";
//        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
//            preparedStatement.execute();
//            preparedStatement.close();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
//            return false;
//        } finally {
//            dbDisConnect();
//        }
//    }

    //Check if production id exist
    public int isExists(Production production) {
        int value = -1;
        String stmt = "SELECT EXISTS (SELECT " + COLUMN_PRODUCTION_ID + " FROM " + TABLE_PRODUCTION + " WHERE  "
//                + COLUMN_PRODUCTION_DATE + " = " + production.getProductionDate().getTime() + " AND "
//                + COLUMN_PRODUCTION_SUPPLIER_ID + " = " + production.getSupplierID() + " AND "
//                + COLUMN_PRODUCTION_FARM_ID + " = " + production.getFarmID() + " AND "
//                + COLUMN_PRODUCTION_PRODUCT_ID + " = " + production.getProductID() + " AND "
//                + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + " = " + production.getProductCode()
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

    public int addProductionAndGetId(Production production) {
        int value = -1;
        Connection connection;
        PreparedStatement preparedStatement;

        String insertProduction = "INSERT INTO " + TABLE_PRODUCTION + " ("
                + COLUMN_PRODUCTION_TYPE + ", "
                + COLUMN_PRODUCTION_DATE + ", "
                + COLUMN_PRODUCTION_SUPPLIER_ID + ", "
                + COLUMN_PRODUCTION_FARM_ID + ", "
                + COLUMN_PRODUCTION_PRODUCT_ID + ", "
                + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + ", "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + ", "
                + COLUMN_PRODUCTION_TOTAL_QUANTITY + ", "
                + COLUMN_PRODUCTION_TOTAL_HOURS + ", "
                + COLUMN_PRODUCTION_PRICE + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?) ";

        String getProductionId = "SELECT MAX(id) FROM " + TABLE_PRODUCTION + " ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertProduction);
            preparedStatement.setInt(1, production.getProductionType());
            preparedStatement.setDate(2, production.getProductionDate());
            preparedStatement.setInt(3, production.getSupplier().getSupplierId());
            preparedStatement.setInt(4, production.getFarm().getFarmId());
            preparedStatement.setInt(5, production.getProduct().getProductId());
            preparedStatement.setInt(6, production.getProductDetail().getProductDetailId());
            preparedStatement.setInt(7, production.getTotalEmployee());
            preparedStatement.setDouble(8, production.getTotalQuantity());
            preparedStatement.setLong(9, production.getTotalMinutes());
            preparedStatement.setDouble(10, production.getPrice());
            preparedStatement.execute();
            preparedStatement.close();

            Statement statement = connection.createStatement();
            ResultSet resultSet0 = statement.executeQuery(getProductionId);
            value = resultSet0.getInt(1);

            System.out.println("productionId " + resultSet0.getInt(1));
            statement.close();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbDisConnect();
        }
        return value;
    }

    //*******************************
    //Get selected employees as graphic
    //*******************************
    public XYChart.Series<String, Number> harvestProductionGraph(Date fromDate, Date  toDate) throws SQLException {
        var data = new XYChart.Series<String, Number>();
        String q1 = "SELECT "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_DATE + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_QUANTITY + " "
                + " FROM " + TABLE_PRODUCTION
                + " WHERE " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_DATE  + " >= " + fromDate.getTime()
                + " AND " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_DATE + " <= " + toDate.getTime()
                + " ORDER BY " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_DATE + " ASC ;";
        try(Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(q1);
            while (resultSet.next()) {
                data.getData().add(new XYChart.Data<String , Number>(resultSet.getDate(1).toString(), resultSet.getDouble(2)));
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
    //Create production table
    //*******************************
    public void createProductionTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTION + " ("
                + COLUMN_PRODUCTION_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_PRODUCTION_TYPE + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_DATE + " DATE NOT NULL, "
                + COLUMN_PRODUCTION_SUPPLIER_ID + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_FARM_ID + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_PRODUCT_ID + " INTEGER NOT NULL, "
                + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + " TEXT NOT NULL, "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + " INTEGER, "
                + COLUMN_PRODUCTION_TOTAL_QUANTITY + " REAL, "
                + COLUMN_PRODUCTION_TOTAL_HOURS + " REAL, "
                + COLUMN_PRODUCTION_PRICE + " REAL NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID + ")"
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ")"
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + ") REFERENCES " + TABLE_PRODUCT_DETAIL + " (" + COLUMN_PRODUCT_DETAIL_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
