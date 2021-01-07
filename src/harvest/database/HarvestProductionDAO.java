package harvest.database;

import harvest.model.AddHarvestHours;
import harvest.model.HarvestProduction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.database.CreditDAO.COLUMN_CREDIT_ID;
import static harvest.database.CreditDAO.TABLE_CREDIT;
import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.TABLE_EMPLOYEE;
import static harvest.database.HarvestDAO.COLUMN_HARVEST_ID;
import static harvest.database.HarvestDAO.TABLE_HARVEST;
import static harvest.database.TransportDAO.COLUMN_TRANSPORT_ID;
import static harvest.database.TransportDAO.TABLE_TRANSPORT;

public class HarvestProductionDAO extends DAO {

    public static final String TABLE_HARVEST_PRODUCTION = "harvest_hours";
    public static final String COLUMN_HARVEST_PRODUCTION_ID = "id";
    public static final String COLUMN_HARVEST_PRODUCTION_TH = "total_hours";
    public static final String COLUMN_HARVEST_PRODUCTION_TE  = "total_employees";
    public static final String COLUMN_HARVEST_PRODUCTION_TQ  = "total_quantity";
    public static final String COLUMN_HARVEST_PRODUCTION_TA  = "total_amount";
    public static final String COLUMN_HARVEST_PRODUCTION_TC  = "total_credits";
    public static final String COLUMN_HARVEST_PRODUCTION_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_PRODUCTION_PRICE_1 = "price_1";
    public static final String COLUMN_HARVEST_PRODUCTION_PRICE_2 = "price_2";

    private static HarvestProductionDAO sHarvestProductionDAO = new HarvestProductionDAO();
    //private constructor
    private HarvestProductionDAO(){ }
    public static HarvestProductionDAO getInstance(){
        if (sHarvestProductionDAO == null){
            sHarvestProductionDAO = new HarvestProductionDAO();
            return sHarvestProductionDAO;
        }
        return sHarvestProductionDAO;
    }

    public void createHarvestTable() throws SQLException {
        String createStmt =  "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST_PRODUCTION + " ("
                + COLUMN_HARVEST_PRODUCTION_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_PRODUCTION_TH + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TE + " INTEGER, "
                + COLUMN_HARVEST_PRODUCTION_TQ + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TA + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TC + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_PRICE_1 + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_PRICE_2 + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_HARVEST_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCTION_HARVEST_ID + ") REFERENCES " + TABLE_HARVEST + " (" + COLUMN_HARVEST_ID + ")"
                + ");";
        try(Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    //add list of harvesters
    public boolean addHarvestProduction(HarvestProduction harvestProduction){
        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_PRODUCTION + " ("
                + COLUMN_HARVEST_PRODUCTION_TH + ", "
                + COLUMN_HARVEST_PRODUCTION_TE  + ", "
                + COLUMN_HARVEST_PRODUCTION_TQ  + ", "
                + COLUMN_HARVEST_PRODUCTION_TA  + ", "
                + COLUMN_HARVEST_PRODUCTION_TC + ", "
                + COLUMN_HARVEST_PRODUCTION_PRICE_1 + ", "
                + COLUMN_HARVEST_PRODUCTION_PRICE_2  + ", "
                + COLUMN_HARVEST_PRODUCTION_HARVEST_ID  + ") "
                + " VALUES (?,?,?,?,?,?,?,?);";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertHarvestHours)){
            preparedStatement.setDouble(1, harvestProduction.getHarvestProductionTotalHours());
            preparedStatement.setInt(2, harvestProduction.getHarvestProductionTotalEmployee());
            preparedStatement.setDouble(3, harvestProduction.getHarvestProductionTotalQuantity());
            preparedStatement.setDouble(4, harvestProduction.getHarvestProductionTotalAmount());
            preparedStatement.setDouble(5, harvestProduction.getHarvestProductionTotalCredit());
            preparedStatement.setDouble(6, harvestProduction.getHarvestProductionPrice1());
            preparedStatement.setDouble(7, harvestProduction.getHarvestProductionPrice2());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

}
