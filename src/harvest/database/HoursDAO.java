package harvest.database;

import harvest.model.Hours;
import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ConstantDAO.*;

public class HoursDAO extends DAO {

    private static HoursDAO sHoursDAO = new HoursDAO();

    private HoursDAO() { }

    public static HoursDAO getInstance() {
        if (sHoursDAO == null) {
            sHoursDAO = new HoursDAO();
        }
        return sHoursDAO;
    }

    //*******************************
    //Get Hours data
    //*******************************
    public List<Hours> getData(Date date) throws SQLException {
        Statement statement;
        ResultSet resultSet;
        String select = "SELECT "
                + TABLE_HOURS + "." + COLUMN_HOURS_ID + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_DATE + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_SM + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_EM + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_SN + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_EN + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_EMPLOYEE_TYPE  + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_PRICE  + ", "
                + TABLE_HOURS + "." + COLUMN_HARVEST_REMARQUE + ", "
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
                + " FROM " + TABLE_HOURS + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_EMPLOYEE_ID
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_TRANSPORT_ID
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_CREDIT_ID
                + " LEFT JOIN " + TABLE_PRODUCTION + " "
                + " ON " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_PRODUCTION_ID
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_FARM_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID
                + " WHERE " + TABLE_HOURS + "." + COLUMN_HOURS_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + TABLE_HOURS + "." + COLUMN_HOURS_DATE + " DESC ;";
        try {
            statement = dbGetConnect().createStatement();
            resultSet = statement.executeQuery(select);
            return getHarvestHoursFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    private ObservableList<Hours> getHarvestHoursFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<Hours> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Hours hours = new Hours();
            hours.setHoursID(resultSet.getInt(1));
            hours.setHarvestDate(resultSet.getDate(2));
            hours.setStartMorning(resultSet.getTime(3));
            hours.setEndMorning(resultSet.getTime(4));
            hours.setStartNoon(resultSet.getTime(5));
            hours.setEndNoon(resultSet.getTime(6));
            hours.setEmployeeType(resultSet.getInt(7));
            hours.setHourPrice(resultSet.getDouble(8));
            hours.setRemarque(resultSet.getString(9));
            hours.getEmployee().setEmployeeId(resultSet.getInt(10));
            hours.getEmployee().setEmployeeFirstName(resultSet.getString(11).toUpperCase());
            hours.getEmployee().setEmployeeLastName(resultSet.getString(12).toUpperCase());
            hours.getTransport().setTransportId(resultSet.getInt(13));
            hours.getTransport().setTransportAmount(resultSet.getDouble(14));
            hours.getCredit().setCreditId(resultSet.getInt(15));
            hours.getCredit().setCreditAmount(resultSet.getDouble(16));
            hours.getProduction().setProductionID(resultSet.getInt(17));
            hours.getProduction().getSupplier().setSupplierId(resultSet.getInt(18));
            hours.getProduction().getSupplier().setSupplierName(resultSet.getString(19).toUpperCase());
            hours.getProduction().getFarm().setFarmId(resultSet.getInt(20));
            hours.getProduction().getFarm().setFarmName(resultSet.getString(21).toUpperCase());
            hours.getProduction().getProduct().setProductId(resultSet.getInt(22));
            hours.getProduction().getProduct().setProductName(resultSet.getString(23).toUpperCase());
            hours.getProduction().getProductDetail().setProductDetailId(resultSet.getInt(24));
            hours.getProduction().getProductDetail().setProductType(resultSet.getString(25).toUpperCase());
            hours.getProduction().getProductDetail().setProductCode(resultSet.getString(26).toUpperCase());
            list.add(hours);
        }
        return list;
    }

    //*******************************
    //Get Hours data by production id
    //*******************************
    public List<Hours> getHoursDataByProductionId(Production production) throws SQLException {
        Statement statement;
        ResultSet resultSet;
        String select = "SELECT "
                + TABLE_HOURS + "." + COLUMN_HOURS_ID + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_DATE + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_SM + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_EM + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_SN + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_EN + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_EMPLOYEE_TYPE  + ", "
                + TABLE_HOURS + "." + COLUMN_HOURS_PRICE  + ", "
                + TABLE_HOURS + "." + COLUMN_HARVEST_REMARQUE + ", "
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
                + " FROM " + TABLE_HOURS + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_EMPLOYEE_ID
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_TRANSPORT_ID
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_CREDIT_ID
                + " LEFT JOIN " + TABLE_PRODUCTION + " "
                + " ON " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_PRODUCTION_ID
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_FARM_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID
                + " WHERE " + TABLE_HOURS + "." + COLUMN_HOURS_PRODUCTION_ID + " = " + production.getProductionID() + " "
                + " ORDER BY " + TABLE_HOURS + "." + COLUMN_HOURS_DATE + " DESC ;";
        try {
            statement = dbGetConnect().createStatement();
            resultSet = statement.executeQuery(select);
            return getHarvestHoursFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get Add Hours data
    //*******************************
    public List<Hours> getAddHoursData() throws Exception {
        List<Hours> hoursList = new ArrayList<>();
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
                Hours hours = new Hours();
                hours.getEmployee().setEmployeeId(resultSet.getInt(1));
                hours.getEmployee().setEmployeeFirstName(resultSet.getString(2));
                hours.getEmployee().setEmployeeLastName(resultSet.getString(3));
                hoursList.add(hours);
            }
            return hoursList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //add Harvest Hours
    //*******************************
    public boolean addHoursWork(Hours hours) {
        Connection connection = null;
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

        String insertHarvestHours = "INSERT INTO " + TABLE_HOURS + " ("
                + COLUMN_HOURS_DATE + ", "
                + COLUMN_HOURS_SM + ", "
                + COLUMN_HOURS_EM + ", "
                + COLUMN_HOURS_SN + ", "
                + COLUMN_HOURS_EN + ", "
                + COLUMN_HOURS_EMPLOYEE_TYPE + ", "
                + COLUMN_HOURS_EMPLOYEE_ID + ", "
                + COLUMN_HOURS_TRANSPORT_ID + ", "
                + COLUMN_HOURS_CREDIT_ID + ", "
                + COLUMN_HOURS_PRICE + ", "
                + COLUMN_HOURS_REMARQUE + ", "
                + COLUMN_HOURS_PRODUCTION_ID + ") "
                + " VALUES (?, " +
                " julianday('" + hours.getStartMorning() + "'), " +
                " julianday('" + hours.getEndMorning() + "'), " +
                " julianday('" + hours.getStartNoon() + "'), " +
                " julianday('" + hours.getEndNoon() + "'), " +
                " ?, ?, ?, ?, ?, ?, ? ) ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            int transportId = 0;
            int CreditId = 0;

            if (hours.isTransportStatus()){
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getTransport().getTransportAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.setInt(4, hours.getProduction().getFarm().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (hours.getCreditAmount() > 0.0){
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getCreditAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (hours.isTransportStatus()){
                Statement transportStmt = connection.createStatement();
                ResultSet resultSet1 = transportStmt.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                transportStmt.close();
            }

            if (hours.getCreditAmount() > 0.0){
                Statement creditStmt = connection.createStatement();
                ResultSet resultSet2 = creditStmt.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                creditStmt.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setDate(1, hours.getHarvestDate());
            preparedStatement.setInt(2, hours.getEmployeeType());
            preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, transportId);
            preparedStatement.setInt(5, CreditId);
            preparedStatement.setDouble(6, hours.getHourPrice());
            preparedStatement.setString(7, hours.getRemarque());
            preparedStatement.setInt(8, hours.getProduction().getProductionID());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            return true;
        } catch (Exception e) {
            assert connection != null;
            try {
                connection.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + ex.getMessage());
            }
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //Create Hours table
    public void createHoursTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_HOURS + " ("
                + COLUMN_HOURS_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HOURS_DATE + " DATE NOT NULL, "
                + COLUMN_HOURS_SM + " REAL NOT NULL, "
                + COLUMN_HOURS_EM + " REAL NOT NULL, "
                + COLUMN_HOURS_SN + " REAL NOT NULL, "
                + COLUMN_HOURS_EN + " REAL NOT NULL, "
                + COLUMN_HOURS_EMPLOYEE_TYPE + " INTEGER NOT NULL, "
                + COLUMN_HOURS_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_HOURS_TRANSPORT_ID + " INTEGER NOT NULL, "
                + COLUMN_HOURS_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_HOURS_PRICE + " REAL, "
                + COLUMN_HOURS_REMARQUE + " TEXT, "
                + COLUMN_HOURS_PRODUCTION_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HOURS_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HOURS_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HOURS_CREDIT_ID + ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HOURS_PRODUCTION_ID + ") REFERENCES " + TABLE_PRODUCTION + " (" + COLUMN_PRODUCTION_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public boolean updateHoursWork(Hours hours) {
        Connection connection = null;
        PreparedStatement preparedStatement;

        String updateTransport = "UPDATE " + TABLE_TRANSPORT + " SET "
                + COLUMN_TRANSPORT_DATE + " =?, "
                + COLUMN_TRANSPORT_AMOUNT + " =?, "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + " =?, "
                + COLUMN_TRANSPORT_FARM_ID + " =? "
                + " WHERE " + COLUMN_TRANSPORT_ID + " = " + hours.getTransport().getTransportId() + " ;";

        String updateCredit = "UPDATE " + TABLE_CREDIT + " SET "
                + COLUMN_CREDIT_DATE + " =?, "
                + COLUMN_CREDIT_AMOUNT + " =?, "
                + COLUMN_CREDIT_EMPLOYEE_ID + " =? "
                + " WHERE " + COLUMN_CREDIT_ID + " = " + hours.getCredit().getCreditId() + " ;";

        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", " + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", " + COLUMN_TRANSPORT_FARM_ID + ") "
                + " VALUES (?,?,?,?) ";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", " + COLUMN_CREDIT_AMOUNT + ", " + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?);";

        String deleteTransport = "DELETE FROM " + TABLE_TRANSPORT
                + " WHERE " + COLUMN_TRANSPORT_ID + " = " + hours.getTransport().getTransportId() + " ;";

        String deleteCredit = "DELETE FROM " + TABLE_CREDIT
                + " WHERE " + COLUMN_CREDIT_ID + " = " + hours.getCredit().getCreditId() + " ;";

        String getTransportId = "SELECT MAX(id) FROM " + TABLE_TRANSPORT + " ;";

        String getCreditId = "SELECT MAX(id) FROM " + TABLE_CREDIT + " ;";

        String updateHarvestHours = "UPDATE " + TABLE_HOURS + " SET "
                + COLUMN_HOURS_DATE + " =?, "
                + COLUMN_HOURS_SM + " = " + " julianday('" + hours.getStartMorning() + "'), "
                + COLUMN_HOURS_EM + " = " + " julianday('" + hours.getEndMorning() + "'), "
                + COLUMN_HOURS_SN + " = " + " julianday('" + hours.getStartNoon() + "'), "
                + COLUMN_HOURS_EN + " = " + " julianday('" + hours.getEndNoon() + "'), "
                + COLUMN_HOURS_EMPLOYEE_TYPE + " =?, "
                + COLUMN_HOURS_EMPLOYEE_ID + " =?, "
                + COLUMN_HOURS_TRANSPORT_ID + " =?, "
                + COLUMN_HOURS_CREDIT_ID + " =?, "
                + COLUMN_HOURS_PRICE + " =?, "
                + COLUMN_HOURS_REMARQUE + " =?, "
                + COLUMN_HOURS_PRODUCTION_ID + " =? "
                + " WHERE " + COLUMN_HOURS_ID + " = " + hours.getHoursID() + " ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            boolean toDeleteTransport = false;
            boolean toDeleteCredit = false;
            int transportId = 0;
            int CreditId = 0;

            if (hours.isTransportStatus() && hours.getTransport().getTransportId() != 0){
                preparedStatement = dbGetConnect().prepareStatement(updateTransport);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getTransport().getTransportAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.setInt(4, hours.getProduction().getFarm().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }else if (hours.isTransportStatus() && hours.getTransport().getTransportId() == 0){
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getTransport().getTransportAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.setInt(4, hours.getProduction().getFarm().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }else {
                toDeleteTransport = true;
            }

            System.out.println();
            if (hours.getCreditAmount() > 0.0 && hours.getCredit().getCreditId() != 0){
                System.out.println("!= 0 : " + hours.getCredit().getCreditId());
                preparedStatement = dbGetConnect().prepareStatement(updateCredit);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getCreditAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }else if (hours.getCreditAmount() > 0.0 && hours.getCredit().getCreditId() == 0){
                System.out.println("== 0 : " + hours.getCredit().getCreditId());
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getCreditAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }else {
                toDeleteCredit = true;
            }

            if (toDeleteTransport){
                Statement transportDeleteStmt = connection.createStatement();
                transportDeleteStmt.execute(deleteTransport);
                transportDeleteStmt.close();
            }

            if (toDeleteCredit){
                Statement creditDeleteStmt = connection.createStatement();
                creditDeleteStmt.execute(deleteCredit);
                creditDeleteStmt.close();
            }

            if (hours.isTransportStatus() && hours.getTransport().getTransportId() != 0){
                transportId = hours.getTransport().getTransportId();
            }else if (hours.isTransportStatus() && hours.getTransport().getTransportId() == 0){
                Statement transportStmt = connection.createStatement();
                ResultSet resultSet1 = transportStmt.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                transportStmt.close();
            }

            if (hours.getCreditAmount() > 0.0 && hours.getCredit().getCreditId() != 0){
                CreditId = hours.getCredit().getCreditId();
            }else if (hours.getCreditAmount() > 0.0 && hours.getCredit().getCreditId() == 0){
                Statement creditStmt = connection.createStatement();
                ResultSet resultSet2 = creditStmt.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                creditStmt.close();
            }

            preparedStatement = connection.prepareStatement(updateHarvestHours);
            preparedStatement.setDate(1, hours.getHarvestDate());
            preparedStatement.setInt(2, hours.getEmployeeType());
            preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, transportId);
            preparedStatement.setInt(5, CreditId);
            preparedStatement.setDouble(6, hours.getHourPrice());
            preparedStatement.setString(7, hours.getRemarque());
            preparedStatement.setInt(8, hours.getProduction().getProductionID());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            return true;
        } catch (Exception e) {
            assert connection != null;
            try {
                connection.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + ex.getMessage());
            }
            e.printStackTrace();
            System.out.print("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }
}


