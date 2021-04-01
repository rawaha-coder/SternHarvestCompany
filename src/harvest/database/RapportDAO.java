package harvest.database;

import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static harvest.database.ConstantDAO.*;
import static harvest.database.ConstantDAO.COLUMN_PRODUCTION_DATE;

public class RapportDAO extends DAO{
    private static RapportDAO sRapportDAO = new RapportDAO();
    private RapportDAO(){}
    public static RapportDAO getInstance(){
        if (sRapportDAO == null){
            sRapportDAO = new RapportDAO();
        }
        return sRapportDAO;
    }

    public List<Production> getCompanyRapport(Date fromDate, Date toDate, int type) throws Exception{
        String select = "SELECT "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_ID + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_DATE + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_EMPLOYEES+ ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_QUANTITY + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_TOTAL_MINUTES + ", "
                + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRICE + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", "
                + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_TYPE + ", "
                + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_CODE + " "
                + " FROM " + TABLE_PRODUCTION
                + " LEFT JOIN " + TABLE_SUPPLIER + " "
                + " ON " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_SUPPLIER_ID
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_FARM_ID
                + " LEFT JOIN " + TABLE_PRODUCT + " "
                + " ON " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_ID
                + " LEFT JOIN " + TABLE_PRODUCT_DETAIL + " "
                + " ON " + TABLE_PRODUCT_DETAIL + "." + COLUMN_PRODUCT_DETAIL_ID + " = " + TABLE_PRODUCTION + "." + COLUMN_PRODUCTION_PRODUCT_DETAIL_ID
                + " WHERE " + COLUMN_PRODUCTION_DATE + " >= " + fromDate.getTime()
                + " AND " + COLUMN_PRODUCTION_DATE + " <= " + toDate.getTime()
                + " AND " + COLUMN_PRODUCTION_TYPE + " = " + type
                + " ORDER BY " + COLUMN_PRODUCTION_DATE + " DESC ;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getProductionFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            dbDisConnect();
        }
    }

    //Help method to get data from resultSet
    private ObservableList<Production> getProductionFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<Production> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Production production = new Production();
            production.setProductionID(resultSet.getInt(1));
            production.setProductionDate(resultSet.getDate(2));
            production.setTotalEmployee(resultSet.getInt(3));
            production.setTotalQuantity(resultSet.getLong(4));
            production.setTotalMinutes(resultSet.getLong(5));
            production.setPrice(resultSet.getDouble(6));
            production.getSupplier().setSupplierId(resultSet.getInt(7));
            production.getSupplier().setSupplierName(resultSet.getString(8));
            production.getFarm().setFarmId(resultSet.getInt(9));
            production.getFarm().setFarmName(resultSet.getString(10));
            production.getProduct().setProductId(resultSet.getInt(11));
            production.getProduct().setProductName(resultSet.getString(12));
            production.getProductDetail().setProductDetailId(resultSet.getInt(13));
            production.getProductDetail().setProductType(resultSet.getString(14));
            production.getProductDetail().setProductCode(resultSet.getString(15));
            list.add(production);
        }
        return list;
    }

}
