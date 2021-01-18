package harvest.database;

import harvest.model.HarvestProduction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static harvest.util.Constant.*;

public class HarvestProductionDAO extends DAO {

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

    //add list of harvesters
    public boolean addHarvestProduction(HarvestProduction harvestProduction){
        String insertHarvestHours = "INSERT INTO " + TABLE_HARVEST_PRODUCTION + " ("
                + COLUMN_HARVEST_PRODUCTION_DATE + ", "
                + COLUMN_HARVEST_PRODUCTION_TYPE + ", "
                + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + ", "
                + COLUMN_HARVEST_PRODUCTION_FARM_ID  + ", "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID  + ", "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID  + ") "
                + " VALUES (?,?,?,?,?,?);";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertHarvestHours)){
            preparedStatement.setDate(1, harvestProduction.getHarvestProductionDate());
            preparedStatement.setInt(2, harvestProduction.getHarvestProductionHarvestType());
            preparedStatement.setInt(3, harvestProduction.getSupplier().getSupplierId());
            preparedStatement.setInt(4, harvestProduction.getFarm().getFarmId());
            preparedStatement.setInt(5, harvestProduction.getProduct().getProductId());
            preparedStatement.setInt(6, harvestProduction.getProductDetail().getProductDetailId());
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

    //add list of harvesters
    public boolean updateHarvestProduction(HarvestProduction harvestProduction){
        String updateStmt = "UPDATE " + TABLE_HARVEST_PRODUCTION + " SET " +
                "" + COLUMN_HARVEST_PRODUCTION_TE + " =?, " +
                "" + COLUMN_HARVEST_PRODUCTION_TH + " =?, " +
                "" + COLUMN_HARVEST_PRODUCTION_TQ + " =?, " +
                "" + COLUMN_HARVEST_PRODUCTION_TA + " =?, " +
                "" + COLUMN_HARVEST_PRODUCTION_TT + " =?, " +
                "" + COLUMN_HARVEST_PRODUCTION_TC + " =? " +
                " WHERE " + COLUMN_HARVEST_PRODUCTION_ID + " = " + harvestProduction.getHarvestProductionID() + " ;";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)){
            preparedStatement.setInt(1, harvestProduction.getHarvestProductionTotalEmployee());
            preparedStatement.setDouble(2, harvestProduction.getHarvestProductionTotalHours());
            preparedStatement.setDouble(3, harvestProduction.getHarvestProductionTotalQuantity());
            preparedStatement.setDouble(4, harvestProduction.getHarvestProductionTotalAmount());
            preparedStatement.setDouble(5, harvestProduction.getHarvestProductionTotalTransport());
            preparedStatement.setDouble(6, harvestProduction.getHarvestProductionTotalCredit());
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

    public void createHarvestTable() throws SQLException {
        String createStmt =  "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST_PRODUCTION + " ("
                + COLUMN_HARVEST_PRODUCTION_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_PRODUCTION_DATE + " DATE NOT NULL, "
                + COLUMN_HARVEST_PRODUCTION_TYPE + " INTEGER, "
                + COLUMN_HARVEST_PRODUCTION_TE + " INTEGER, "
                + COLUMN_HARVEST_PRODUCTION_TH + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TQ + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TA + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TT + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_TC + " REAL, "
                + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_PRODUCTION_FARM_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + ") REFERENCES " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID+ ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID + ") REFERENCES " + TABLE_PRODUCT_DETAIL + " (" + COLUMN_PRODUCT_DETAIL_ID  + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCTION_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ")"
                + ");";
        try(Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public int isExists(HarvestProduction hp){
        int value = -1;
        String stmt = "SELECT EXISTS (SELECT " + COLUMN_HARVEST_PRODUCTION_ID + " FROM "+ TABLE_HARVEST_PRODUCTION + " WHERE  "
                + COLUMN_HARVEST_PRODUCTION_DATE + " = " + hp.getHarvestProductionDate().getTime() + " AND "
                + COLUMN_HARVEST_PRODUCTION_TYPE + " = " + hp.getHarvestProductionHarvestType() + " AND "
                + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + " = " + hp.getSupplier().getSupplierId() + " AND "
                + COLUMN_HARVEST_PRODUCTION_FARM_ID + " = " + hp.getFarm().getFarmId() + " AND "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID + " = " + hp.getProduct().getProductId() + " AND "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID + " = " + hp.getProductDetail().getProductDetailId()
                + " )";

        try(Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(stmt);
            value = resultSet.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dbDisConnect();
        }
        return value;
    }

    public int getHarvestProductionId(HarvestProduction hp){
        int value = -1;
        String stmt = "SELECT " + COLUMN_HARVEST_PRODUCTION_ID + " FROM "+ TABLE_HARVEST_PRODUCTION + " WHERE  "
                + COLUMN_HARVEST_PRODUCTION_DATE + " = " + hp.getHarvestProductionDate().getTime() + " AND "
                + COLUMN_HARVEST_PRODUCTION_TYPE + " = " + hp.getHarvestProductionHarvestType() + " AND "
                + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + " = " + hp.getSupplier().getSupplierId() + " AND "
                + COLUMN_HARVEST_PRODUCTION_FARM_ID + " = " + hp.getFarm().getFarmId() + " AND "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID + " = " + hp.getProduct().getProductId() + " AND "
                + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID + " = " + hp.getProductDetail().getProductDetailId()
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

}
