package harvest.database;

import harvest.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static harvest.controller.DisplayProductController.PRODUCT_NAME_LIVE_DATA;
import static harvest.database.ConstantDAO.*;

public class ProductDAO extends DAO{

    private static ProductDAO sProductDAO = new ProductDAO();

    private ProductDAO(){ }

    public static ProductDAO getInstance(){
        if (sProductDAO == null){
            sProductDAO = new ProductDAO();
            return sProductDAO;
        }
        return sProductDAO;
    }

    //*************************************************************
    //Get all data product
    //*************************************************************
    public List<Product> getData() throws Exception {
        List<Product> list = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_PRODUCT
                + " WHERE " + COLUMN_PRODUCT_IS_EXIST + " = " + 1
                + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)){
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt(1));
                product.setProductName(resultSet.getString(2));
                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Get data product as map by product name
    //*************************************************************
    public Map<String, Product> getProductMap() throws Exception {
        Map<String, Product> mProductMap = new LinkedHashMap<>();
        String sqlStmt = "SELECT * FROM " + TABLE_PRODUCT
                + " WHERE " + COLUMN_PRODUCT_IS_EXIST + " = " + 1
                + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
        try (Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)){
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt(1));
                product.setProductName(resultSet.getString(2));
                mProductMap.put(product.getProductName(), product);
            }
            return mProductMap;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Edit product
    //*************************************************************
    public boolean editData(Product product) {
        String updateStmt = "UPDATE " + TABLE_PRODUCT + " SET " + COLUMN_PRODUCT_NAME + " =? " +
                " WHERE " + COLUMN_PRODUCT_ID + " = " + product.getProductId()+ " ;";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(updateStmt)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Delete product
    //*************************************************************
    public boolean deleteProductById(Product product) {
        Connection connection = null;
        Statement statement;

        String deleteProduct = "UPDATE " + TABLE_PRODUCT
                + " SET " + COLUMN_PRODUCT_IS_EXIST + " = 0 "
                + " WHERE " + COLUMN_PRODUCT_ID + " = "+ product.getProductId() +" ;";

        String deleteProductDetail = "UPDATE " + TABLE_PRODUCT_DETAIL
                + " SET " + COLUMN_PRODUCT_DETAIL_IS_EXIST + " = 0, "
                + COLUMN_PRODUCT_CODE + " = null "
                + " WHERE " + COLUMN_FOREIGN_KEY_PRODUCT_ID + " = "+ product.getProductId() +" ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteProduct);

            statement = connection.createStatement();
            statement.execute(deleteProductDetail);
            connection.commit();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            assert connection != null;
            try {
                connection.rollback();
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + sqlException.getMessage());
            }
            System.out.print("Error occurred while INSERT Operation: " + exception.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public void updateLiveData() {
        PRODUCT_NAME_LIVE_DATA.clear();
        try {
            PRODUCT_NAME_LIVE_DATA.setAll(getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

     //Create Product table
//    public void createProductTable() throws SQLException{
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT
//                    + "(" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY, "
//                    + COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
//                    + COLUMN_PRODUCT_IS_EXIST + " INTEGER DEFAULT 1)");
//        }catch (SQLException e){
//            e.printStackTrace();
//            throw e;
//        }
//    }


}
