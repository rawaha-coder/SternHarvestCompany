package harvest.database;

import harvest.model.Harvest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.util.Constant.*;

public class HarvestDAO extends DAO{

    public static final String TABLE_HARVEST  = "harvest";
    public static final String COLUMN_HARVEST_ID = "id";
    public static final String COLUMN_HARVEST_DATE = "date";
    public static final String COLUMN_HARVEST_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_HARVEST_FARM_ID = "farm_id";
    public static final String COLUMN_HARVEST_PRODUCT_ID = "product_id";
    public static final String COLUMN_HARVEST_PRODUCT_DETAIL_ID = "product_detail_id";

    public static final String COLUMN_HARVEST_TOTAL_HOURS = "total_hours";
    public static final String COLUMN_HARVEST_TOTAL_EMPLOYEE  = "total_employees";
    public static final String COLUMN_HARVEST_TOTAL_QUANTITY  = "total_quantity";
    public static final String COLUMN_HARVEST_TOTAL_CREDIT  = "total_credits";
    public static final String COLUMN_HARVEST_PRODUCT_PRICE_1 = "price_1";
    public static final String COLUMN_HARVEST_PRODUCT_PRICE_2 = "price_2";

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


    public int isExists(Harvest harvest){
        int value = -1;
        String stmt = "SELECT EXISTS (SELECT " + COLUMN_HARVEST_ID + " FROM "+ TABLE_HARVEST + " WHERE  "
                + COLUMN_HARVEST_DATE + " = " + harvest.getHarvestDate().getTime() + " AND "
                + COLUMN_HARVEST_SUPPLIER_ID + " = " + harvest.getSupplier().getSupplierId() + " AND "
                + COLUMN_HARVEST_FARM_ID + " = " + harvest.getFarm().getFarmId() + " AND "
                + COLUMN_HARVEST_PRODUCT_ID + " = " + harvest.getProduct().getProductId() + " AND "
                + COLUMN_HARVEST_PRODUCT_DETAIL_ID + " = " + harvest.getProductDetail().getProductDetailId()
                + " )";

        try(Statement statement = dbGetConnect().createStatement();) {
            ResultSet resultSet = statement.executeQuery(stmt);
            value = resultSet.getInt(1);
            System.out.println(value);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dbDisConnect();
        }
        return value;
    }

    public int getHarvestId(Harvest harvest){
        int value = -1;
        String stmt = "SELECT " + COLUMN_HARVEST_ID + " FROM "+ TABLE_HARVEST + " WHERE  "
                + COLUMN_HARVEST_DATE + " = " + harvest.getHarvestDate().getTime() + " AND "
                + COLUMN_HARVEST_SUPPLIER_ID + " = " + harvest.getSupplier().getSupplierId() + " AND "
                + COLUMN_HARVEST_FARM_ID + " = " + harvest.getFarm().getFarmId() + " AND "
                + COLUMN_HARVEST_PRODUCT_ID + " = " + harvest.getProduct().getProductId() + " AND "
                + COLUMN_HARVEST_PRODUCT_DETAIL_ID + " = " + harvest.getProductDetail().getProductDetailId()
                + " ;";
        try(Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(stmt);
            value = resultSet.getInt(1);
            System.out.println(value);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dbDisConnect();
        }
        return value;
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
            preparedStatement.setDate(1, harvest.getHarvestDate());
            preparedStatement.setInt(2, harvest.getSupplier().getSupplierId());
            preparedStatement.setInt(3, harvest.getFarm().getFarmId());
            preparedStatement.setInt(4, harvest.getProduct().getProductId());
            preparedStatement.setInt(5, harvest.getProductDetail().getProductDetailId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

}
