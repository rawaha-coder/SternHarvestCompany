package harvest.database;

import harvest.model.Hours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.presenter.AddHoursPresenter.ADD_HOURS_LIVE_DATA;
import static harvest.database.ConstantDAO.*;
import static harvest.controller.DisplayHoursController.DISPLAY_HOURS_LIVE_DATA;

public class HoursDAO extends DAO {

    private static HoursDAO sHoursDAO = new HoursDAO();

    private HoursDAO() {
    }

    public static HoursDAO getInstance() {
        if (sHoursDAO == null) {
            sHoursDAO = new HoursDAO();
            return sHoursDAO;
        }
        return sHoursDAO;
    }

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
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_FARM_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_HOURS + "." + COLUMN_HOURS_PRODUCT_DETAIL_ID
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
            hours.getEmployee().setEmployeeFirstName(resultSet.getString(11));
            hours.getEmployee().setEmployeeLastName(resultSet.getString(12));
            hours.getTransport().setTransportId(resultSet.getInt(13));
            hours.getTransport().setTransportAmount(resultSet.getDouble(14));
            hours.getCredit().setCreditId(resultSet.getInt(15));
            hours.getCredit().setCreditAmount(resultSet.getDouble(16));
            hours.getSupplier().setSupplierId(resultSet.getInt(17));
            hours.getSupplier().setSupplierName(resultSet.getString(18));
            hours.getFarm().setFarmId(resultSet.getInt(19));
            hours.getFarm().setFarmName(resultSet.getString(20));
            hours.getProduct().setProductId(resultSet.getInt(21));
            hours.getProduct().setProductName(resultSet.getString(22));
            hours.getProductDetail().setProductDetailId(resultSet.getInt(23));
            hours.getProductDetail().setProductType(resultSet.getString(24));
            hours.getProductDetail().setProductCode(resultSet.getString(25));
            list.add(hours);
        }
        return list;
    }

    //*******************************
    //Get all employees data
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

    public void updateLiveData() {
        ADD_HOURS_LIVE_DATA.clear();
        try {
            ADD_HOURS_LIVE_DATA.setAll(getAddHoursData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLiveData(Date date) {
        DISPLAY_HOURS_LIVE_DATA.clear();
        try {
            DISPLAY_HOURS_LIVE_DATA.setAll(getData(date));
            System.out.println(" Update list size: " + DISPLAY_HOURS_LIVE_DATA.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //add list of harvesters
    public boolean addHarvestHours(Hours hours) {

        Connection connection;
        PreparedStatement preparedStatement;

        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", "
                + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", "
                + COLUMN_TRANSPORT_FARM_ID + ") "
                + " VALUES (?,?,?,?) ";

        String getTransportId = "SELECT MAX(id) FROM " + TABLE_TRANSPORT + " ;";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ") "
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
                + COLUMN_HOURS_SUPPLIER_ID + ", "
                + COLUMN_HOURS_FARM_ID + ", "
                + COLUMN_HOURS_PRODUCT_ID + ", "
                + COLUMN_HOURS_PRODUCT_DETAIL_ID + ", "
                + COLUMN_HOURS_REMARQUE + ") "
                + " VALUES (" +
                " ?, " +
                " julianday('" + hours.getStartMorning() + "'), " +
                " julianday('" + hours.getEndMorning() + "'), " +
                " julianday('" + hours.getStartNoon() + "'), " +
                " julianday('" + hours.getEndNoon() + "'), " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ? " +
                ") ;";
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
                preparedStatement.setInt(4, hours.getFarm().getFarmId());
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
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                statement1.close();
            }

            if (hours.getCreditAmount() > 0.0){
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                statement2.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setDate(1, hours.getHarvestDate());
            preparedStatement.setInt(2, hours.getEmployeeType());
            preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, transportId);
            preparedStatement.setInt(5, CreditId);
            preparedStatement.setDouble(6, hours.getHourPrice());
            preparedStatement.setInt(7, hours.getSupplier().getSupplierId());
            preparedStatement.setInt(8, hours.getFarm().getFarmId());
            preparedStatement.setInt(9, hours.getProduct().getProductId());
            preparedStatement.setInt(10, hours.getProductDetail().getProductDetailId());
            preparedStatement.setString(11, hours.getRemarque());
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


}

//    public void createHoursTable() throws SQLException {
//        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_HOURS + " ("
//                + COLUMN_HOURS_ID + " INTEGER PRIMARY KEY, "
//                + COLUMN_HOURS_DATE + " DATE NOT NULL, "
//                + COLUMN_HOURS_SM + " REAL NOT NULL, "
//                + COLUMN_HOURS_EM + " REAL NOT NULL, "
//                + COLUMN_HOURS_SN + " REAL NOT NULL, "
//                + COLUMN_HOURS_EN + " REAL NOT NULL, "
//                + COLUMN_HOURS_EMPLOYEE_TYPE + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_EMPLOYEE_ID + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_TRANSPORT_ID + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_CREDIT_ID + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_PRICE + " REAL, "
//                + COLUMN_HOURS_SUPPLIER_ID + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_FARM_ID + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_PRODUCT_ID + " INTEGER NOT NULL, "
//                + COLUMN_HOURS_PRODUCT_CODE_ID + " INTEGER NOT NULL, "
//                + COLUMN_HARVEST_HOURS_REMARQUE + " TEXT, "
//                + " FOREIGN KEY (" + COLUMN_HOURS_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
//                + " FOREIGN KEY (" + COLUMN_HOURS_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
//                + " FOREIGN KEY (" + COLUMN_HOURS_CREDIT_ID + ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
//                + " FOREIGN KEY (" + COLUMN_HOURS_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID + ")"
//                + " FOREIGN KEY (" + COLUMN_HOURS_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ")"
//                + " FOREIGN KEY (" + COLUMN_HOURS_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ")"
//                + " FOREIGN KEY (" + COLUMN_HOURS_PRODUCT_CODE_ID + ") REFERENCES " + TABLE_PRODUCT_DETAIL + " (" + COLUMN_PRODUCT_DETAIL_ID + ")"
//                + ");";
//        try (Statement statement = dbGetConnect().createStatement()) {
//            statement.execute(createStmt);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
