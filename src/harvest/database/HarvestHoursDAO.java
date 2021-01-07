package harvest.database;

import harvest.model.AddHarvestHours;

import java.sql.*;

import static harvest.database.CreditDAO.*;
import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.TABLE_EMPLOYEE;
import static harvest.database.HarvestDAO.*;
import static harvest.database.TransportDAO.*;

public class HarvestHoursDAO extends DAO {

    public static final String TABLE_HARVEST_HOURS = "harvest_hours";
    public static final String COLUMN_HARVEST_HOURS_ID = "id";
    public static final String COLUMN_HARVEST_HOURS_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_HOURS_SM = "start_morning";
    public static final String COLUMN_HARVEST_HOURS_EM = "end_morning";
    public static final String COLUMN_HARVEST_HOURS_SN = "start_noon";
    public static final String COLUMN_HARVEST_HOURS_EN = "end_noon";
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

    //add list of harvesters
    public boolean addHarvesters(AddHarvestHours harvestHours) {
        int transportId = 0;
        int CreditId = 0;
        Connection connection;
        PreparedStatement preparedStatement;

        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", "
                + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", "
                + COLUMN_TRANSPORT_FARM_ID + ") "
                + " VALUES (?,?,?,?) ";

        String getTransportId = "SELECT last_insert_rowid() FROM " + TABLE_TRANSPORT + " ;";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?);";

        String getCreditId = "SELECT last_insert_rowid() FROM " + TABLE_CREDIT + " ;";

        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_HOURS + " ("
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
                + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            if (harvestHours.isTransportStatus()){
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, harvestHours.getHarvestDate());
                preparedStatement.setDouble(2, harvestHours.getTransportAmount());
                preparedStatement.setInt(3, harvestHours.getEmployeeId());
                preparedStatement.setInt(4, harvestHours.getFarmId());
                preparedStatement.execute();
            }

            if (harvestHours.getCreditAmount() > 0.0){
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, harvestHours.getHarvestDate());
                preparedStatement.setDouble(2, harvestHours.getCreditAmount());
                preparedStatement.setInt(3, harvestHours.getEmployeeId());
                preparedStatement.execute();
            }

            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(getTransportId);
            transportId = resultSet1.getInt(1);

            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(getCreditId);
            CreditId = resultSet2.getInt(1);

            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setTime(1, harvestHours.getStartMorning());
            preparedStatement.setTime(2, harvestHours.getEndMorning());
            preparedStatement.setTime(3, harvestHours.getStartNoon());
            preparedStatement.setTime(4, harvestHours.getEndNoon());
            preparedStatement.setInt(5, harvestHours.getEmployeeType());
            preparedStatement.setString(6, harvestHours.getHarvestRemarque());
            preparedStatement.setInt(7, harvestHours.getHarvestID());
            preparedStatement.setInt(8, harvestHours.getEmployeeId());
            preparedStatement.setInt(9, CreditId);
            preparedStatement.setInt(10, transportId);
            preparedStatement.execute();

            statement1.close();
            statement2.close();
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
}
