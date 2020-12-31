package harvest.database;

import harvest.model.Harvest;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.database.FarmDAO.COLUMN_FARM_ID;
import static harvest.database.FarmDAO.TABLE_FARM;
import static harvest.database.ProductDAO.COLUMN_PRODUCT_ID;
import static harvest.database.ProductDAO.TABLE_PRODUCT;
import static harvest.database.ProductDetailDAO.COLUMN_PRODUCT_DETAIL_ID;
import static harvest.database.ProductDetailDAO.TABLE_PRODUCT_DETAIL;
import static harvest.database.SupplierDAO.COLUMN_SUPPLIER_ID;
import static harvest.database.SupplierDAO.TABLE_SUPPLIER;

public class HarvestDAO extends DAO{

    public static final String TABLE_HARVEST  = "harvest";
    public static final String COLUMN_HARVEST_ID = "id";
    public static final String COLUMN_HARVEST_DATE = "date";
    public static final String COLUMN_HARVEST_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_HARVEST_FARM_ID = "farm_id";
    public static final String COLUMN_HARVEST_PRODUCT_ID = "product_id";
    public static final String COLUMN_HARVEST_PRODUCT_DETAIL_ID = "product_detail_id";

    private static HarvestDAO sHarvestDAO = new HarvestDAO();
    //private constructor
    private HarvestDAO(){ }
    public static HarvestDAO getInstance(){
        if (sHarvestDAO == null){
            sHarvestDAO = new HarvestDAO();
            return sHarvestDAO;
        }
        return sHarvestDAO;
    }

    public void createHarvestTable() throws SQLException {
        String createStmt =  "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST + " ("
                + COLUMN_HARVEST_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_DATE + " DATE NOT NULL, "
                + COLUMN_HARVEST_SUPPLIER_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_FARM_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_PRODUCT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_PRODUCT_DETAIL_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCT_DETAIL_ID + ") REFERENCES " + TABLE_PRODUCT_DETAIL + " (" + COLUMN_PRODUCT_DETAIL_ID + ")"
                + ");";
        try(Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean addHarvestDate(Harvest harvest){
        String insertHarvest = "INSERT INTO " + TABLE_HARVEST + " ("
                + COLUMN_HARVEST_DATE + ", "
                + COLUMN_HARVEST_SUPPLIER_ID + ", "
                + COLUMN_HARVEST_FARM_ID + ", "
                + COLUMN_HARVEST_PRODUCT_ID + ", "
                + COLUMN_HARVEST_PRODUCT_DETAIL_ID + ") "
                + " VALUES(?,?,?,?,?) ";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertHarvest)) {
            preparedStatement.setDate(1, harvest.getHarvestingDate());
            preparedStatement.setInt(2, harvest.getSupplier().getSupplierId());
            preparedStatement.setInt(3, harvest.getFarm().getFarmId());
            preparedStatement.setInt(4, harvest.getProduct().getProductId());
            preparedStatement.setInt(5, harvest.getProductDetail().getProductDetailId());
            preparedStatement.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }




}
