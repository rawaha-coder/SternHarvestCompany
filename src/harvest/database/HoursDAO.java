package harvest.database;

import harvest.model.Hours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ConstantDAO.*;
import static harvest.ui.hours.DisplayHoursController.HARVEST_HOURS_LIVE_LIST;

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

    public void createTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST_HOURS + " ("
                + COLUMN_HARVEST_HOURS_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_HOURS_DATE + " DATE NOT NULL, "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_NAME + " TEXT, "
                + COLUMN_HARVEST_HOURS_SM + " DATE NOT NULL, "
                + COLUMN_HARVEST_HOURS_EM + " REAL NOT NULL, "
                + COLUMN_HARVEST_HOURS_SN + " REAL NOT NULL, "
                + COLUMN_HARVEST_HOURS_EN + " REAL NOT NULL, "
                + COLUMN_HARVEST_HOURS_TOTAL + " REAL, "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_TYPE + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_TRANSPORT_AMOUNT + " REAL, "
                + COLUMN_HARVEST_HOURS_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_CREDIT_AMOUNT + " REAL, "
                + COLUMN_HARVEST_HOURS_PRICE + " REAL, "
                + COLUMN_HARVEST_HOURS_SUPPLIER_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_SUPPLIER_NAME + " TEXT, "
                + COLUMN_HARVEST_HOURS_FARM_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_FARM_NAME + " TEXT, "
                + COLUMN_HARVEST_HOURS_PRODUCT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_PRODUCT_NAME + " TEXT, "
                + COLUMN_HARVEST_HOURS_PRODUCT_CODE + " TEXT, "
                + COLUMN_HARVEST_HOURS_NET_AMOUNT + " REAL, "
                + COLUMN_HARVEST_HOURS_REMARQUE + " TEXT, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_CREDIT_ID + ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Hours> getData(Date date) throws SQLException {
        Statement statement;
        ResultSet resultSet;
        String select = "SELECT "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_ID + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_DATE + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_SM + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_EM + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_SN + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_EN + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_REMARQUE + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_FARM_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + " "
                + " FROM " + TABLE_HARVEST_HOURS + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + " "
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + " = " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_TRANSPORT_ID + " "
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_CREDIT_ID + " "
                + " WHERE " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_DATE + " DESC ;";
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
            hours.setRemarque(resultSet.getString(7));
            list.add(hours);
        }
        return list;
    }

    public void updateLiveData(Date date) {
        HARVEST_HOURS_LIVE_LIST.clear();
        try {
            HARVEST_HOURS_LIVE_LIST.setAll(getData(date));
            System.out.println(" Update list size: " + HARVEST_HOURS_LIVE_LIST.size());
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
               // + COLUMN_TRANSPORT_FARM_NAME + ") "
                + " VALUES (?,?,?,?,?,?) ";

        String getTransportId = "SELECT MAX(id) FROM " + TABLE_TRANSPORT + " ;";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ", "
                //+ COLUMN_CREDIT_EMPLOYEE_NAME + ") "
                + "VALUES (?,?,?,?);";

        String getCreditId = "SELECT MAX(id) FROM " + TABLE_CREDIT + " ;";

        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_HOURS + " ("
                + COLUMN_HARVEST_HOURS_DATE + ", "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + ", "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_NAME + ", "
                + COLUMN_HARVEST_HOURS_SM + ", "
                + COLUMN_HARVEST_HOURS_EM + ", "
                + COLUMN_HARVEST_HOURS_SN + ", "
                + COLUMN_HARVEST_HOURS_EN + ", "
                + COLUMN_HARVEST_HOURS_TOTAL + ", "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_TYPE + ", "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID + ", "
                + COLUMN_HARVEST_HOURS_TRANSPORT_AMOUNT + ", "
                + COLUMN_HARVEST_HOURS_CREDIT_ID + ", "
                + COLUMN_HARVEST_HOURS_CREDIT_AMOUNT + ", "
                + COLUMN_HARVEST_HOURS_PRICE + ", "
                + COLUMN_HARVEST_HOURS_SUPPLIER_ID + ", "
                + COLUMN_HARVEST_HOURS_SUPPLIER_NAME + ", "
                + COLUMN_HARVEST_HOURS_FARM_ID + ", "
                + COLUMN_HARVEST_HOURS_FARM_NAME + ", "
                + COLUMN_HARVEST_HOURS_PRODUCT_ID + ", "
                + COLUMN_HARVEST_HOURS_PRODUCT_NAME + ", "
                + COLUMN_HARVEST_HOURS_PRODUCT_CODE + ", "
                + COLUMN_HARVEST_HOURS_NET_AMOUNT + ", "
                + COLUMN_HARVEST_HOURS_REMARQUE + ") "
                + " VALUES (" +
                " ?, " +
                " ?, " +
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
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?, " +
                " ?  " +
                ") ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            int transportId = 0;
            int CreditId = 0;

            if (hours.isTransportStatus()){
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getTransportAmount());
                preparedStatement.setInt(3, hours.getEmployeeID());
                preparedStatement.setString(4, hours.getEmployeeName());
                preparedStatement.setInt(5, hours.getFarmID());
                preparedStatement.setString(6, hours.getFarmName());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (hours.getCreditAmount() > 0.0){
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getCreditAmount());
                preparedStatement.setString(3, hours.getEmployeeName());
                preparedStatement.setInt(4, hours.getEmployeeID());
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
            preparedStatement.setInt(2, hours.getEmployeeID());
            preparedStatement.setString(3, hours.getEmployeeName());
            preparedStatement.setDouble(4, hours.getTotalHours());
            preparedStatement.setInt(5, hours.getEmployeeType());
            preparedStatement.setInt(6, transportId);
            preparedStatement.setDouble(7, hours.getTransportAmount());
            preparedStatement.setInt(8, CreditId);
            preparedStatement.setDouble(9, hours.getCreditAmount());
            preparedStatement.setDouble(10, hours.getHourPrice());
            preparedStatement.setInt(11, hours.getSupplierID());
            preparedStatement.setString(12, hours.getSupplierName());
            preparedStatement.setInt(13, hours.getFarmID());
            preparedStatement.setString(14, hours.getFarmName());
            preparedStatement.setInt(15, hours.getProductID());
            preparedStatement.setString(16, hours.getProductName());
            preparedStatement.setString(17, hours.getProductCode());
            preparedStatement.setDouble(18, hours.getAmountPayable());
            preparedStatement.setString(19, hours.getRemarque());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
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
    //Get all employees data
    //*******************************
    public List<Hours> getAddHoursData() throws Exception {
        List<Hours> hoursList = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_HARVEST_HOURS + " ORDER BY " + COLUMN_EMPLOYEE_FIRST_NAME + " ASC";

        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Hours hours = new Hours();
                hours.setEmployeeID(resultSet.getInt(1));
                hours.setEmployeeName(resultSet.getString(2) + " " + resultSet.getString(3));
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
}
