package harvest.database;

import java.sql.SQLException;
import java.sql.Statement;

import static harvest.database.CreditDAO.COLUMN_CREDIT_ID;
import static harvest.database.CreditDAO.TABLE_CREDIT;
import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.TABLE_EMPLOYEE;
import static harvest.database.FarmDAO.COLUMN_FARM_ID;
import static harvest.database.FarmDAO.TABLE_FARM;
import static harvest.database.ProductDAO.COLUMN_PRODUCT_ID;
import static harvest.database.ProductDAO.TABLE_PRODUCT;
import static harvest.database.ProductDetailDAO.COLUMN_PRODUCT_DETAIL_ID;
import static harvest.database.ProductDetailDAO.TABLE_PRODUCT_DETAIL;
import static harvest.database.SupplierDAO.COLUMN_SUPPLIER_ID;
import static harvest.database.SupplierDAO.TABLE_SUPPLIER;
import static harvest.database.TransportDAO.COLUMN_TRANSPORT_ID;
import static harvest.database.TransportDAO.TABLE_TRANSPORT;

public class HarvestHoursDAO extends DAO {

    public static final String TABLE_HARVEST_HOURS = "harvest_hours";
    public static final String COLUMN_HARVEST_HOURS_ID = "id";
    public static final String COLUMN_HARVEST_HOURS_SM = "start_morning";
    public static final String COLUMN_HARVEST_HOURS_EM  = "end_morning";
    public static final String COLUMN_HARVEST_HOURS_SN  = "start_noon";
    public static final String COLUMN_HARVEST_HOURS_EN  = "end_noon";
    public static final String COLUMN_HARVEST_HOURS_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_HOURS_CREDIT_ID = "supplier_id";
    public static final String COLUMN_HARVEST_HOURS_TRANSPORT_ID = "farm_id";
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
                + COLUMN_HARVEST_REMARQUE + " TEXT, "
                + COLUMN_HARVEST_HOURS_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_HOURS_TRANSPORT_ID + " INTEGER NOT NULL, "
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
}
