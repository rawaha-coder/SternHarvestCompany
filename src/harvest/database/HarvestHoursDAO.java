package harvest.database;

import harvest.model.AddHarvestHours;
import harvest.model.Harvest;
import harvest.model.HarvestHours;

import java.sql.*;
import java.util.List;

import static harvest.database.CreditDAO.COLUMN_CREDIT_ID;
import static harvest.database.CreditDAO.TABLE_CREDIT;
import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.TABLE_EMPLOYEE;
import static harvest.database.HarvestDAO.*;
import static harvest.database.ProductDAO.TABLE_PRODUCT;
import static harvest.database.TransportDAO.COLUMN_TRANSPORT_ID;
import static harvest.database.TransportDAO.TABLE_TRANSPORT;

public class HarvestHoursDAO extends DAO {

    public static final String TABLE_HARVEST_HOURS = "harvest_hours";
    public static final String COLUMN_HARVEST_HOURS_ID = "id";
    public static final String COLUMN_HARVEST_HOURS_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_HOURS_SM = "start_morning";
    public static final String COLUMN_HARVEST_HOURS_EM  = "end_morning";
    public static final String COLUMN_HARVEST_HOURS_SN  = "start_noon";
    public static final String COLUMN_HARVEST_HOURS_EN  = "end_noon";
    public static final String COLUMN_HARVEST_HOURS_TYPE  = "employee_type";
    public static final String COLUMN_HARVEST_HOURS_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_HOURS_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_HOURS_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_REMARQUE = "remarque";

    private static HarvestHoursDAO sHarvestHoursDAO = new HarvestHoursDAO();
    //private constructor
    private HarvestHoursDAO(){ }
    public static HarvestHoursDAO getInstance(){
        if (sHarvestHoursDAO == null){
            sHarvestHoursDAO = new HarvestHoursDAO();
            return sHarvestHoursDAO;
        }
        return sHarvestHoursDAO;
    }

    public void createHarvestTable() throws SQLException {
        String createStmt =  "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST_HOURS + " ("
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
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_CREDIT_ID+ ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_HOURS_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
                + ");";
        try(Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean addHarvestHours(AddHarvestHours harvestHours, Harvest harvest){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertHarvest = "INSERT INTO " + TABLE_HARVEST + " ("
                + COLUMN_HARVEST_DATE + ", "
                + COLUMN_HARVEST_SUPPLIER_ID + ", "
                + COLUMN_HARVEST_FARM_ID + ", "
                + COLUMN_HARVEST_PRODUCT_ID + ", "
                + COLUMN_HARVEST_PRODUCT_DETAIL_ID + ") "
                + " VALUES(?,?,?,?,?) ";

        String getProductId = "SELECT last_insert_rowid() FROM " + TABLE_PRODUCT + " ;";

        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_HOURS + " ("
                + COLUMN_HARVEST_HOURS_SM + ", "
                + COLUMN_HARVEST_HOURS_EM  + ", "
                + COLUMN_HARVEST_HOURS_SN  + ", "
                + COLUMN_HARVEST_HOURS_EN  + ", "
                + COLUMN_HARVEST_REMARQUE  + ", "
                + COLUMN_HARVEST_HOURS_TYPE + ", "
                + COLUMN_HARVEST_HOURS_HARVEST_ID  + ", "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID  + ", "
                + COLUMN_HARVEST_HOURS_CREDIT_ID  + ", "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID  + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?);";

        try{
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertHarvest);
            preparedStatement.setDate(1, harvest.getHarvestDate());
            preparedStatement.setInt(2, harvest.getSupplier().getSupplierId());
            preparedStatement.setInt(3, harvest.getFarm().getFarmId());
            preparedStatement.setInt(4, harvest.getProduct().getProductId());
            preparedStatement.setInt(5, harvest.getProductDetail().getProductDetailId());

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getProductId);
            int id = resultSet.getInt(1);

            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setTime(1, harvestHours.getStartMorning());
            preparedStatement.setTime(2, harvestHours.getEndMorning());
            preparedStatement.setTime(3, harvestHours.getStartNoon());
            preparedStatement.setTime(4, harvestHours.getEndNoon());
            preparedStatement.setString(5, harvestHours.getHarvestRemarque());
            preparedStatement.setInt(6, harvestHours.getEmployeeType());
            preparedStatement.setInt(7, id);
            preparedStatement.setInt(8, harvestHours.getEmployeeId());
            preparedStatement.setInt(9, 02);
            preparedStatement.setInt(10, 03);

            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //add list of harvesters
    public boolean addHarvesters(List<AddHarvestHours> list, Harvest harvest){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertHarvest = "INSERT INTO " + TABLE_HARVEST + " ("
                + COLUMN_HARVEST_DATE + ", "
                + COLUMN_HARVEST_SUPPLIER_ID + ", "
                + COLUMN_HARVEST_FARM_ID + ", "
                + COLUMN_HARVEST_PRODUCT_ID + ", "
                + COLUMN_HARVEST_PRODUCT_DETAIL_ID + ") "
                + " VALUES(?,?,?,?,?) ";

        String getProductId = "SELECT last_insert_rowid() FROM " + TABLE_PRODUCT + " ;";

        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_HOURS + " ("
                + COLUMN_HARVEST_HOURS_SM + ", "
                + COLUMN_HARVEST_HOURS_EM  + ", "
                + COLUMN_HARVEST_HOURS_SN  + ", "
                + COLUMN_HARVEST_HOURS_EN  + ", "
                + COLUMN_HARVEST_REMARQUE  + ", "
                + COLUMN_HARVEST_HOURS_TYPE + ", "
                + COLUMN_HARVEST_HOURS_HARVEST_ID  + ", "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID  + ", "
                + COLUMN_HARVEST_HOURS_CREDIT_ID  + ", "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID  + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?);";

        try{
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertHarvest);
            preparedStatement.setDate(1, harvest.getHarvestDate());
            preparedStatement.setInt(2, harvest.getSupplier().getSupplierId());
            preparedStatement.setInt(3, harvest.getFarm().getFarmId());
            preparedStatement.setInt(4, harvest.getProduct().getProductId());
            preparedStatement.setInt(5, harvest.getProductDetail().getProductDetailId());

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getProductId);
            int id = resultSet.getInt(1);

            for (AddHarvestHours harvestHours : list){
                preparedStatement = connection.prepareStatement(insertHarvestHours);
                preparedStatement.setTime(1, harvestHours.getStartMorning());
                preparedStatement.setTime(2, harvestHours.getEndMorning());
                preparedStatement.setTime(3, harvestHours.getStartNoon());
                preparedStatement.setTime(4, harvestHours.getEndNoon());
                preparedStatement.setString(5, harvestHours.getHarvestRemarque());
                preparedStatement.setInt(6, harvestHours.getEmployeeType());
                preparedStatement.setInt(7, id);
                preparedStatement.setInt(8, harvestHours.getEmployeeId());
                preparedStatement.setInt(9, 00);
                preparedStatement.setInt(10, 00);
            }

            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }finally {
            dbDisConnect();
        }
    }
}
