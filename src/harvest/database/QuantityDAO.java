package harvest.database;

import harvest.model.Production;
import harvest.model.Quantity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ConstantDAO.*;
import static harvest.database.ConstantDAO.COLUMN_PRODUCTION_ID;

public class QuantityDAO extends DAO{
    
    private static QuantityDAO sQuantityDAO = new QuantityDAO();
    private QuantityDAO(){}
    public static QuantityDAO getInstance(){
        if (sQuantityDAO == null){
            sQuantityDAO = new QuantityDAO();
        }
        return sQuantityDAO;
    }

    //*******************************
    //add QUANTITYWork
    //*******************************
    public boolean addHarvestQuantity(Quantity quantity) {
        Connection connection;
        PreparedStatement preparedStatement;

        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", " + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", " + COLUMN_TRANSPORT_FARM_ID + ") "
                + " VALUES (?,?,?,?) ";

        String getTransportId = "SELECT MAX(id) FROM " + TABLE_TRANSPORT + " ;";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", " + COLUMN_CREDIT_AMOUNT + ", " + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?);";

        String getCreditId = "SELECT MAX(id) FROM " + TABLE_CREDIT + " ;";

        String insertHarvestQuantity = "INSERT INTO " + TABLE_QUANTITY + " ("
                + COLUMN_QUANTITY_PRODUCTION_ID + ", "
                + COLUMN_QUANTITY_DATE + ", "
                + COLUMN_QUANTITY_ALL + ", "
                + COLUMN_QUANTITY_BAD + ", "
                + COLUMN_QUANTITY_PG + ", "
                + COLUMN_QUANTITY_DG + ", "
                + COLUMN_QUANTITY_GOOD + ", "
                + COLUMN_QUANTITY_HARVEST_TYPE + ", "
                + COLUMN_QUANTITY_EMPLOYEE_ID + ", "
                + COLUMN_QUANTITY_TRANSPORT_ID + ", "
                + COLUMN_QUANTITY_CREDIT_ID + ", "
                + COLUMN_QUANTITY_PRICE + ", "
                + COLUMN_QUANTITY_REMARQUE + ") "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            int transportId = 0;
            int CreditId = 0;

            if (quantity.isTransportStatus()){
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, quantity.getHarvestDate());
                preparedStatement.setDouble(2, quantity.getTransport().getTransportAmount());
                preparedStatement.setInt(3, quantity.getEmployee().getEmployeeId());
                preparedStatement.setInt(4, quantity.getProduction().getFarm().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (quantity.getCreditAmount() > 0.0){
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, quantity.getHarvestDate());
                preparedStatement.setDouble(2, quantity.getCreditAmount());
                preparedStatement.setInt(3, quantity.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (quantity.isTransportStatus()){
                Statement transportStmt = connection.createStatement();
                ResultSet resultSet1 = transportStmt.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                transportStmt.close();
            }

            if (quantity.getCreditAmount() > 0.0){
                Statement creditStmt = connection.createStatement();
                ResultSet resultSet2 = creditStmt.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                creditStmt.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvestQuantity);
            preparedStatement.setInt(1, quantity.getProduction().getProductionID());
            preparedStatement.setDate(2, quantity.getHarvestDate());
            preparedStatement.setDouble(3, quantity.getAllQuantity());
            preparedStatement.setDouble(4, quantity.getBadQuantity());
            preparedStatement.setDouble(5, quantity.getPenaltyGeneral());
            preparedStatement.setDouble(6, quantity.getDamageGeneral());
            preparedStatement.setDouble(7, quantity.getGoodQuantity());
            preparedStatement.setInt(8, quantity.getHarvestType());
            preparedStatement.setInt(9, quantity.getEmployee().getEmployeeId());
            preparedStatement.setInt(10, transportId);
            preparedStatement.setInt(11, CreditId);
            preparedStatement.setDouble(12, quantity.getProductPrice());
            preparedStatement.setString(13, quantity.getRemarque());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get Add Quantity data
    //*******************************
    public List<Quantity> getAddQuantityData() throws Exception {
        List<Quantity> QUANTITYList = new ArrayList<>();
        String sqlStmt = "SELECT "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME + " "
                + " FROM " + TABLE_EMPLOYEE
                + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = 1 "
                + " AND " + COLUMN_EMPLOYEE_IS_EXIST + " = 1 "
                + " ORDER BY " + COLUMN_EMPLOYEE_FIRST_NAME + " ASC;";

        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Quantity quantity = new Quantity();
                quantity.getEmployee().setEmployeeId(resultSet.getInt(1));
                quantity.getEmployee().setEmployeeFirstName(resultSet.getString(2));
                quantity.getEmployee().setEmployeeLastName(resultSet.getString(3));
                QUANTITYList.add(quantity);
            }
            return QUANTITYList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    public List<Quantity> getQuantityDataByProductionId(Production production) throws SQLException {
        Statement statement;
        ResultSet resultSet;
        String select = "SELECT "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_ID + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_DATE + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_ALL + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_BAD + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_PG + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_DG + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_GOOD  + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_HARVEST_TYPE  + ", "
                + TABLE_QUANTITY + "." + COLUMN_QUANTITY_PRICE  + ", "
                + TABLE_QUANTITY + "." + COLUMN_HARVEST_REMARQUE + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_TYPE + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_CODE + " "
                + " FROM " + TABLE_QUANTITY + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_QUANTITY + "." + COLUMN_QUANTITY_EMPLOYEE_ID
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + " = " + TABLE_QUANTITY + "." + COLUMN_QUANTITY_TRANSPORT_ID
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_QUANTITY + "." + COLUMN_QUANTITY_CREDIT_ID
                + " LEFT JOIN " + TABLE_PRODUCTION + " "
                + " ON " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_ID + " = " + TABLE_QUANTITY + "." + COLUMN_QUANTITY_PRODUCTION_ID
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_FARM_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID
                + " WHERE " + TABLE_QUANTITY + "." + COLUMN_QUANTITY_PRODUCTION_ID + " = " + production.getProductionID() + " "
                + " ORDER BY " + TABLE_QUANTITY + "." + COLUMN_QUANTITY_DATE + " DESC ;";
        try {
            statement = dbGetConnect().createStatement();
            resultSet = statement.executeQuery(select);
            return getHarvestQuantityFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    private ObservableList<Quantity> getHarvestQuantityFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<Quantity> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Quantity quantity = new Quantity();
            quantity.setQuantityID(resultSet.getInt(1));
            quantity.setHarvestDate(resultSet.getDate(2));
            quantity.setAllQuantity(resultSet.getDouble(3));
            quantity.setBadQuantity(resultSet.getDouble(4));
            quantity.setPenaltyGeneral(resultSet.getDouble(5));
            quantity.setDamageGeneral(resultSet.getDouble(6));
            quantity.setGoodQuantity(resultSet.getDouble(7));
            quantity.setHarvestType(resultSet.getInt(8));
            quantity.setProductPrice(resultSet.getDouble(9));
            quantity.setRemarque(resultSet.getString(10));
            quantity.getEmployee().setEmployeeId(resultSet.getInt(11));
            quantity.getEmployee().setEmployeeFirstName(resultSet.getString(12));
            quantity.getEmployee().setEmployeeLastName(resultSet.getString(13));
            quantity.getTransport().setTransportId(resultSet.getInt(14));
            quantity.getTransport().setTransportAmount(resultSet.getDouble(15));
            quantity.getCredit().setCreditId(resultSet.getInt(16));
            quantity.getCredit().setCreditAmount(resultSet.getDouble(17));
            quantity.getProduction().setProductionID(resultSet.getInt(18));
            quantity.getProduction().getSupplier().setSupplierId(resultSet.getInt(19));
            quantity.getProduction().getSupplier().setSupplierName(resultSet.getString(20));
            quantity.getProduction().getFarm().setFarmId(resultSet.getInt(21));
            quantity.getProduction().getFarm().setFarmName(resultSet.getString(22));
            quantity.getProduction().getProduct().setProductId(resultSet.getInt(23));
            quantity.getProduction().getProduct().setProductName(resultSet.getString(24));
            quantity.getProduction().getProductDetail().setProductDetailId(resultSet.getInt(25));
            quantity.getProduction().getProductDetail().setProductType(resultSet.getString(26));
            quantity.getProduction().getProductDetail().setProductCode(resultSet.getString(27));
            list.add(quantity);
        }
        return list;
    }

    public void createQuantityTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_QUANTITY + " ("
                + COLUMN_QUANTITY_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_QUANTITY_PRODUCTION_ID + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY_DATE + " DATE NOT NULL, "
                + COLUMN_QUANTITY_ALL + " REAL DEFAULT 0, "
                + COLUMN_QUANTITY_BAD + " REAL DEFAULT 0, "
                + COLUMN_QUANTITY_PG + " REAL DEFAULT 0, "
                + COLUMN_QUANTITY_DG + " REAL DEFAULT 0, "
                + COLUMN_QUANTITY_GOOD + " REAL DEFAULT 0, "
                + COLUMN_QUANTITY_HARVEST_TYPE + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY_TRANSPORT_ID + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY_PRICE + " REAL NOT NULL, "
                + COLUMN_QUANTITY_REMARQUE + " TEXT, "
                + " FOREIGN KEY (" + COLUMN_QUANTITY_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
                + " FOREIGN KEY (" + COLUMN_QUANTITY_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_QUANTITY_CREDIT_ID + ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_QUANTITY_PRODUCTION_ID + ") REFERENCES " + TABLE_PRODUCTION + " (" + COLUMN_PRODUCTION_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
