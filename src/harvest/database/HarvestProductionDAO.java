package harvest.database;

import harvest.model.HarvestProduction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.*;

import static harvest.database.ConstantDAO.*;

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

    //*******************************
    //Get all HarvestWork data
    //*******************************
    public ObservableList<HarvestProduction> getData() throws Exception {
        String select = "SELECT "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_ID + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TYPE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TH + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TQ + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TA + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TT + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TC + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_CODE + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_PRICE_EMPLOYEE + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_PRICE_COMPANY + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + " "
                + " FROM " + TABLE_HARVEST_PRODUCTION + " "
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID  + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + " "
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID + " "
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID + " "
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_FARM_ID + " "
                + " ORDER BY " + COLUMN_HARVEST_PRODUCTION_DATE + " DESC ;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private ObservableList<HarvestProduction> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<HarvestProduction> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            HarvestProduction harvestProduction = new HarvestProduction();
            harvestProduction.setHarvestProductionID(resultSet.getInt(1));
            harvestProduction.setHarvestProductionDate(resultSet.getDate(2));
            harvestProduction.setHarvestProductionHarvestType(resultSet.getInt(3));
            harvestProduction.setHarvestProductionTotalEmployee(resultSet.getInt(4));
            harvestProduction.setHarvestProductionTotalHours(resultSet.getLong(5));
            harvestProduction.setHarvestProductionTotalQuantity(resultSet.getDouble(6));
            harvestProduction.setHarvestProductionTotalCost(resultSet.getDouble(7));
            harvestProduction.setHarvestProductionTotalTransport(resultSet.getDouble(8));
            harvestProduction.setHarvestProductionTotalCredit(resultSet.getDouble(9));
            harvestProduction.getSupplier().setSupplierId(resultSet.getInt(10));
            harvestProduction.getSupplier().setSupplierName(resultSet.getString(11));
            harvestProduction.getProduct().setProductId(resultSet.getInt(12));
            harvestProduction.getProduct().setProductName(resultSet.getString(13));
            harvestProduction.getProductDetail().setProductDetailId(resultSet.getInt(14));
            harvestProduction.getProductDetail().setProductCode(resultSet.getString(15));
            harvestProduction.getProductDetail().setPriceEmployee(resultSet.getDouble(16));
            harvestProduction.getProductDetail().setPriceCompany(resultSet.getDouble(17));
            harvestProduction.getFarm().setFarmId(resultSet.getInt(18));
            harvestProduction.getFarm().setFarmName(resultSet.getString(19));
            list.add(harvestProduction);
        }
        return list;
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
            preparedStatement.setDouble(4, harvestProduction.getHarvestProductionTotalCost());
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

    //*******************************
    //Get all HarvestWork data by date
    //*******************************
    public ObservableList<HarvestProduction> getDataByDate(Date date) throws Exception {
        String select = "SELECT "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_ID + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TYPE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TH + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TQ + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TA + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TT + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TC + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_CODE + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_PRICE_EMPLOYEE + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_PRICE_COMPANY + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + " "
                + " FROM " + TABLE_HARVEST_PRODUCTION + " "
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID  + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID + " "
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID + " "
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID + " "
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_FARM_ID + " "
                + " WHERE " + TABLE_HARVEST_WORK + "." + COLUMN_HARVEST_WORK_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + TABLE_HARVEST_WORK + "." + COLUMN_HARVEST_WORK_DATE + " DESC ;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getHPDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private ObservableList<HarvestProduction> getHPDataFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<HarvestProduction> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            HarvestProduction harvestProduction = new HarvestProduction();
            harvestProduction.setHarvestProductionID(resultSet.getInt(1));
            harvestProduction.setHarvestProductionDate(resultSet.getDate(2));
            harvestProduction.setHarvestProductionHarvestType(resultSet.getInt(3));
            harvestProduction.setHarvestProductionTotalEmployee(resultSet.getInt(4));
            harvestProduction.setHarvestProductionTotalHours(resultSet.getLong(5));
            harvestProduction.setHarvestProductionTotalQuantity(resultSet.getDouble(6));
            harvestProduction.setHarvestProductionTotalCost(resultSet.getDouble(7));
            harvestProduction.setHarvestProductionTotalTransport(resultSet.getDouble(8));
            harvestProduction.setHarvestProductionTotalCredit(resultSet.getDouble(9));
            harvestProduction.getSupplier().setSupplierId(resultSet.getInt(10));
            harvestProduction.getSupplier().setSupplierName(resultSet.getString(11));
            harvestProduction.getProduct().setProductId(resultSet.getInt(12));
            harvestProduction.getProduct().setProductName(resultSet.getString(13));
            harvestProduction.getProductDetail().setProductDetailId(resultSet.getInt(14));
            harvestProduction.getProductDetail().setProductCode(resultSet.getString(15));
            harvestProduction.getProductDetail().setPriceEmployee(resultSet.getDouble(16));
            harvestProduction.getProductDetail().setPriceCompany(resultSet.getDouble(17));
            harvestProduction.getFarm().setFarmId(resultSet.getInt(18));
            harvestProduction.getFarm().setFarmName(resultSet.getString(19));
            list.add(harvestProduction);
        }
        return list;
    }

    public ObservableList<HarvestProduction> searchDataByDate(Date fromDate, Date  toDate) throws Exception{
        String select = "SELECT "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_ID + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TYPE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TH + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TQ + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TA + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TT + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TC + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_CODE + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_PRICE_EMPLOYEE + ", "
                + TABLE_PRODUCT_DETAIL  + "." + COLUMN_PRODUCT_PRICE_COMPANY + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + " "
                + " FROM " + TABLE_HARVEST_PRODUCTION + " "
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID  + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_PRODUCT_DETAIL_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_FARM_ID
                + " WHERE " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + " >= " + fromDate.getTime()
                + " AND " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE+ " <= " + toDate.getTime()
                + " ORDER BY " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + " DESC ;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getHPDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get selected employees as graphic
    //*******************************
    public XYChart.Series<String, Number> harvestProductionGraph(Date fromDate, Date  toDate) throws SQLException {
        var data = new XYChart.Series<String, Number>();
        String q1 = "SELECT "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + ", "
                + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_TQ
                + " FROM " + TABLE_HARVEST_PRODUCTION
                + " WHERE " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + " >= " + fromDate.getTime()
                + " AND " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE+ " <= " + toDate.getTime()
                + " ORDER BY " + TABLE_HARVEST_PRODUCTION + "." + COLUMN_HARVEST_PRODUCTION_DATE + " ASC ;";

        try(Statement statement = dbGetConnect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(q1);
            while (resultSet.next()) {
                data.getData().add(new XYChart.Data<String , Number>(resultSet.getDate(1).toString(), resultSet.getDouble(2)));
                System.out.println(resultSet.getDate(1));
                System.out.println(resultSet.getDouble(2));
            }
            resultSet.close();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }
}
