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

    public void createHarvestTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST_HOURS + " ("
                + COLUMN_HARVEST_HOURS_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_HOURS_DATE + " DATE NOT NULL, "
                + COLUMN_HARVEST_HOURS_SM + " DATE NOT NULL, "
                + COLUMN_HARVEST_HOURS_EM + " REAL NOT NULL, "
                + COLUMN_HARVEST_HOURS_SN + " REAL NOT NULL, "
                + COLUMN_HARVEST_HOURS_EN + " REAL NOT NULL, "
                + COLUMN_HARVEST_HOURS_TYPE + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_REMARQUE + " TEXT, "
                + COLUMN_HARVEST_HOURS_HARVEST_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_HARVEST_ID + ") REFERENCES " + TABLE_PRODUCTION + " (" + COLUMN_PRODUCTION_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
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

    public List<Hours> getData(Date date) throws SQLException{
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
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME  + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_FARM_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + " "
                + " FROM " + TABLE_HARVEST_HOURS + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID  + " = " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + " "
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID  + " = " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_TRANSPORT_ID + " "
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_CREDIT_ID + " "
                + " WHERE " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_DATE + " DESC ;";
        try{
            statement = dbGetConnect().createStatement();
            resultSet = statement.executeQuery(select);
            return getHarvestHoursFromResultSet(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private ObservableList<Hours> getHarvestHoursFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<Hours> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Hours hours = new Hours();
            hours.setHarvestHoursID(resultSet.getInt(1));
            hours.setHarvestDate(resultSet.getDate(2));
            hours.setStartMorning(resultSet.getTime(3));
            hours.setEndMorning(resultSet.getTime(4));
            hours.setStartNoon(resultSet.getTime(5));
            hours.setEndNoon(resultSet.getTime(6));
            hours.setHarvestRemarque(resultSet.getString(7));
            hours.getEmployee().setEmployeeId(resultSet.getInt(8));
            hours.getEmployee().setEmployeeFirstName(resultSet.getString(9));
            hours.getEmployee().setEmployeeLastName(resultSet.getString(10));
            hours.getTransport().setTransportId(resultSet.getInt(11));
            hours.getTransport().setFarmId(resultSet.getInt(12));
            hours.getTransport().setTransportAmount(resultSet.getDouble(13));
            hours.getCredit().setCreditId(resultSet.getInt(14));
            hours.getCredit().setCreditAmount(resultSet.getDouble(15));
            list.add(hours);
        }
        return list;
    }

    public void updateLiveData(Date date) {
        HARVEST_HOURS_LIVE_LIST.clear();
        try {
            HARVEST_HOURS_LIVE_LIST.setAll(getData(date));
            System.out.println(" Update list size: " + HARVEST_HOURS_LIVE_LIST.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //add list of harvesters
    public boolean addHarvesters(Hours hours) {

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

        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_HOURS + " ("
                + COLUMN_HARVEST_HOURS_DATE + ", "
                + COLUMN_HARVEST_HOURS_SM + ", "
                + COLUMN_HARVEST_HOURS_EM + ", "
                + COLUMN_HARVEST_HOURS_SN + ", "
                + COLUMN_HARVEST_HOURS_EN + ", "
                + COLUMN_HARVEST_HOURS_TYPE + ", "
                + COLUMN_HARVEST_REMARQUE + ", "
                + COLUMN_HARVEST_HOURS_HARVEST_ID + ", "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + ", "
                + COLUMN_HARVEST_HOURS_CREDIT_ID + ", "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID + ") "
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
                preparedStatement.setInt(4, hours.getTransport().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (hours.getCredit().getCreditAmount() > 0.0){
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getCredit().getCreditAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (hours.isTransportStatus()){
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                System.out.println("transportId " + resultSet1.getInt(1));
                statement1.close();
            }

            if (hours.getCredit().getCreditAmount() > 0.0){
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                System.out.println("CreditId " + resultSet2.getInt(1));
                statement2.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setDate(1, hours.getHarvestDate());
            preparedStatement.setInt(2, hours.getEmployeeType());
            preparedStatement.setString(3, hours.getHarvestRemarque());
            //preparedStatement.setInt(4, hours.getHarvestProduction().getHarvestProductionID());
            preparedStatement.setInt(5, hours.getEmployee().getEmployeeId());
            preparedStatement.setInt(6, CreditId);
            preparedStatement.setInt(7, transportId);
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
    public List<Hours> getHarvestHoursData() throws Exception {
        List<Hours> hoursList = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_EMPLOYEE + " ORDER BY " + COLUMN_EMPLOYEE_ID + " DESC;";

        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Hours hours = new Hours();
                hours.getEmployee().setEmployeeId(resultSet.getInt(1));
                hours.getEmployee().setEmployeeStatus(resultSet.getBoolean(2));
                hours.getEmployee().setEmployeeFirstName(resultSet.getString(3));
                hours.getEmployee().setEmployeeLastName(resultSet.getString(4));
                hoursList.add(hours);
            }
            return hoursList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }
}
