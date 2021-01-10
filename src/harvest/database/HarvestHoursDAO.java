package harvest.database;

import harvest.model.HarvestHours;
import harvest.util.Validation;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static harvest.database.CreditDAO.*;
import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.*;
import static harvest.database.FarmDAO.TABLE_FARM;
import static harvest.database.HarvestDAO.*;
import static harvest.database.SeasonDAO.COLUMN_SEASON_FARM_ID;
import static harvest.database.SeasonDAO.TABLE_SEASON;
import static harvest.database.TransportDAO.*;
import static harvest.ui.harvest.DisplayHarvestHoursController.HARVEST_HOURS_LIST_LIVE_DATA;

public class HarvestHoursDAO extends DAO {

    public static final String TABLE_HARVEST_HOURS = "harvest_hours";
    public static final String COLUMN_HARVEST_HOURS_ID = "id";
    public static final String COLUMN_HARVEST_HOURS_DATE = "date";
    public static final String COLUMN_HARVEST_HOURS_SM = "start_morning";
    public static final String COLUMN_HARVEST_HOURS_EM = "end_morning";
    public static final String COLUMN_HARVEST_HOURS_SN = "start_noon";
    public static final String COLUMN_HARVEST_HOURS_EN = "end_noon";
    public static final String COLUMN_HARVEST_HOURS_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_HOURS_TYPE = "employee_type";
    public static final String COLUMN_HARVEST_HOURS_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_HOURS_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_HOURS_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_REMARQUE = "remarque";

    private static HarvestHoursDAO sHarvestHoursDAO = new HarvestHoursDAO();

    //private constructor
    private HarvestHoursDAO() {
    }

    public static HarvestHoursDAO getInstance() {
        if (sHarvestHoursDAO == null) {
            sHarvestHoursDAO = new HarvestHoursDAO();
            return sHarvestHoursDAO;
        }
        return sHarvestHoursDAO;
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
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_HARVEST_ID + ") REFERENCES " + TABLE_HARVEST + " (" + COLUMN_HARVEST_ID + ")"
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

    public List<HarvestHours> getData(Date date) throws SQLException{
        Statement statement;
        ResultSet resultSet;
        List<HarvestHours> harvestHoursList = new ArrayList<>();
        //String select = "SELECT end_noon FROM harvest_hours;";
        String select = "SELECT "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_ID + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_DATE + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_SM + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_EM + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_SN + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_HOURS_EN + ", "
                + TABLE_HARVEST_HOURS + "." + COLUMN_HARVEST_REMARQUE + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME  + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
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

        /*
                        + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + ", "
         */
        try{
            statement = dbGetConnect().createStatement();
            resultSet = statement.executeQuery(select);
            while (resultSet.next()){
                HarvestHours harvestHours = new HarvestHours();
                harvestHours.setHarvestHoursID(resultSet.getInt(1));
                harvestHours.setHarvestDate(resultSet.getDate(2));
                harvestHours.setStartMorning(resultSet.getTime(3));
                harvestHours.setEndMorning(resultSet.getTime(4));
                harvestHours.setStartNoon(resultSet.getTime(5));
                harvestHours.setEndNoon(resultSet.getTime(6));
                harvestHours.setHarvestRemarque(resultSet.getString(7));
                harvestHours.setEmployeeFullName(resultSet.getString(8), resultSet.getString(9));
                harvestHours.setTransportAmount(resultSet.getDouble(10));
                harvestHours.setCreditAmount(resultSet.getDouble(11));
                System.out.println(resultSet.getDouble(10));
                harvestHoursList.add(harvestHours);

            }
            return harvestHoursList;
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    public void updateLiveData(Date date) {
        HARVEST_HOURS_LIST_LIVE_DATA.clear();
        try {
            HARVEST_HOURS_LIST_LIVE_DATA.setAll(getData(date));
            System.out.println(" Update list size: " + HARVEST_HOURS_LIST_LIVE_DATA.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //add list of harvesters
    public boolean addHarvesters(HarvestHours harvestHours) {

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
                " julianday('" + harvestHours.getStartMorning() + "'), " +
                " julianday('" + harvestHours.getEndMorning() + "'), " +
                " julianday('" + harvestHours.getStartNoon() + "'), " +
                " julianday('" + harvestHours.getEndNoon() + "'), " +
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

            if (harvestHours.isTransportStatus()){
                System.out.println("inside the loop Transport: " +harvestHours.isTransportStatus());
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, harvestHours.getHarvestDate());
                preparedStatement.setDouble(2, harvestHours.getTransportAmount());
                preparedStatement.setInt(3, harvestHours.getEmployeeId());
                preparedStatement.setInt(4, harvestHours.getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }


            if (harvestHours.getCreditAmount() > 0.0){
                System.out.println("inside the loop Credit: " + harvestHours.getCreditAmount());
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, harvestHours.getHarvestDate());
                preparedStatement.setDouble(2, harvestHours.getCreditAmount());
                preparedStatement.setInt(3, harvestHours.getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (harvestHours.isTransportStatus()){
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);;
                System.out.println("transportId " + resultSet1.getInt(1));
                statement1.close();
            }

            if (harvestHours.getCreditAmount() > 0.0){
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                System.out.println("CreditId " + resultSet2.getInt(1));
                statement2.close();
            }



            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setDate(1, harvestHours.getHarvestDate());
            preparedStatement.setInt(2, harvestHours.getEmployeeType());
            preparedStatement.setString(3, harvestHours.getHarvestRemarque());
            preparedStatement.setInt(4, harvestHours.getHarvestID());
            preparedStatement.setInt(5, harvestHours.getEmployeeId());
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
    private String timeToStringTime(long time) {
        int sec = (int) time/1000;
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }

}
