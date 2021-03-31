package harvest.database;

import harvest.model.Hours;
import harvest.model.Production;
import harvest.model.Quantity;
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
    //Search production data by date
    //*******************************
    public ObservableList<Production> searchHoursProductionData(Date fromDate, Date toDate, int type) throws Exception {
        String select = "SELECT "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_ID + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_DATE + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_EMPLOYEES+ ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_QUANTITY + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_MINUTES + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRICE + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_TYPE + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_CODE + " "
                + " FROM " + TABLE_PRODUCTION
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_FARM_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID
                + " WHERE " + COLUMN_PRODUCTION_DATE + " >= " + fromDate.getTime()
                + " AND " + COLUMN_PRODUCTION_DATE + " <= " + toDate.getTime()
                + " AND " + COLUMN_PRODUCTION_TYPE + " = " + type
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
            production.setTotalEmployee(resultSet.getInt(3));
            production.setTotalQuantity(resultSet.getLong(4));
            production.setTotalMinutes(resultSet.getLong(5));
            production.setPrice(resultSet.getDouble(6));
            production.getSupplier().setSupplierId(resultSet.getInt(7));
            production.getSupplier().setSupplierName(resultSet.getString(8));
            production.getFarm().setFarmId(resultSet.getInt(9));
            production.getFarm().setFarmName(resultSet.getString(10));
            production.getProduct().setProductId(resultSet.getInt(11));
            production.getProduct().setProductName(resultSet.getString(12));
            production.getProductDetail().setProductDetailId(resultSet.getInt(13));
            production.getProductDetail().setProductType(resultSet.getString(14));
            production.getProductDetail().setProductCode(resultSet.getString(15));
            list.add(production);
        }
        return list;
    }

    //*******************************
    //Add production data
    //*******************************
    public int addProductionAndGetId(Production production) {
        int value = -1;
        Connection connection = null;
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
                + COLUMN_PRODUCTION_TOTAL_MINUTES + ", "
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
            assert connection != null;
            try {
                connection.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + ex.getMessage());
            }
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
        } finally {
            dbDisConnect();
        }
        return value;
    }

    //*******************************
    //Get production as graphic
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
                + COLUMN_PRODUCTION_TOTAL_MINUTES + " REAL, "
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

    //*******************************
    //Update Production data
    //*******************************
    public boolean updateProductionData(Production production) {
        String updateStmt = "UPDATE " + TABLE_PRODUCTION + " SET "
                + COLUMN_PRODUCTION_TYPE + " =?, "
                + COLUMN_PRODUCTION_DATE + " =?, "
                + COLUMN_PRODUCTION_SUPPLIER_ID + " =?, "
                + COLUMN_PRODUCTION_FARM_ID + " =?, "
                + COLUMN_PRODUCTION_PRODUCT_ID + " =?, "
                + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID + " =?, "
                + COLUMN_PRODUCTION_TOTAL_EMPLOYEES + " =?, "
                + COLUMN_PRODUCTION_TOTAL_QUANTITY + " =?, "
                + COLUMN_PRODUCTION_TOTAL_MINUTES + " =?, "
                + COLUMN_PRODUCTION_PRICE + " =? "
                + " WHERE " + COLUMN_PRODUCTION_ID + " = " + production.getProductionID() + " ;";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Delete Hours Production data
    //*******************************
    public boolean deleteHoursProductionData(Production production, Hours hours) {
        Connection connection = null;
        Statement statement = null;

        String deleteTransport = "DELETE FROM " + TABLE_TRANSPORT
                + " WHERE " + COLUMN_TRANSPORT_ID + " = " + hours.getTransport().getTransportId() +" ;";

        String deleteCredit = "DELETE FROM " + TABLE_CREDIT
                + " WHERE " + COLUMN_CREDIT_ID + " = " + hours.getCredit().getCreditId() +" ;";

        String deleteHours = "DELETE FROM " + TABLE_HOURS
                + " WHERE " + COLUMN_HOURS_PRODUCTION_ID + " = " + production.getProductionID() +" ;";

        String deleteProduction = "DELETE FROM " + TABLE_PRODUCTION
                + " WHERE " + COLUMN_PRODUCTION_ID + " = " + production.getProductionID() + " ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteTransport);

            statement = connection.createStatement();
            statement.execute(deleteCredit);

            statement = connection.createStatement();
            statement.execute(deleteHours);

            statement = connection.createStatement();
            statement.execute(deleteProduction);

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
            System.out.print("Error occurred while delete Operation: " + ex1.getMessage());
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

    //*******************************
    //Delete Hours Production data
    //*******************************
    public boolean deleteQuantityProductionData(Production production, Quantity quantity) {
        Connection connection = null;
        Statement statement = null;

        String deleteTransport = "DELETE FROM " + TABLE_TRANSPORT
                + " WHERE " + COLUMN_TRANSPORT_ID + " = " + quantity.getTransport().getTransportId() +" ;";

        String deleteCredit = "DELETE FROM " + TABLE_CREDIT
                + " WHERE " + COLUMN_CREDIT_ID + " = " + quantity.getCredit().getCreditId() +" ;";

        String deleteQuantity = "DELETE FROM " + TABLE_QUANTITY
                + " WHERE " + COLUMN_QUANTITY_PRODUCTION_ID + " = " + production.getProductionID() +" ;";

        String deleteProduction = "DELETE FROM " + TABLE_PRODUCTION
                + " WHERE " + COLUMN_PRODUCTION_ID + " = " + production.getProductionID() + " ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteTransport);

            statement = connection.createStatement();
            statement.execute(deleteCredit);

            statement = connection.createStatement();
            statement.execute(deleteQuantity);

            statement = connection.createStatement();
            statement.execute(deleteProduction);

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
            System.out.print("Error occurred while delete Operation: " + ex1.getMessage());
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
