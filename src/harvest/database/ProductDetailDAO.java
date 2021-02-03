package harvest.database;

import harvest.model.Product;
import harvest.model.ProductDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.product.DisplayProductController.PRODUCT_DETAIL_LIVE_DATA;
import static harvest.database.ConstantDAO.*;

public class ProductDetailDAO extends DAO{

    private static ProductDetailDAO sProductDetail = new ProductDetailDAO();

    //private Constructor
    private ProductDetailDAO(){

    }

    public static ProductDetailDAO getInstance(){
        if (sProductDetail == null){
            sProductDetail = new ProductDetailDAO();
            return sProductDetail;
        }
        return sProductDetail;
    }

    //Get product detail by product
    public List<ProductDetail> getProductDetail(Product product) throws Exception {
        List<ProductDetail> list = new ArrayList<>();
        Statement statement;
        String sqlStmt = "SELECT * FROM " + TABLE_PRODUCT_DETAIL
                + " WHERE " + COLUMN_FOREIGN_KEY_PRODUCT_ID + " = " + product.getProductId()
                + " ORDER BY " + COLUMN_PRODUCT_TYPE + " ASC;";
        //Execute SELECT statement
        try {
            statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductDetailId(resultSet.getInt(1));
                productDetail.setProductType(resultSet.getString(2));
                productDetail.setProductCode(resultSet.getString(3));
                productDetail.setPriceEmployee(resultSet.getDouble(4));
                productDetail.setPriceCompany(resultSet.getDouble(5));
                productDetail.setProduct(product);
                list.add(productDetail);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //Add product detail
    public boolean addProductDetail(ProductDetail productDetail) {
        String insertProductDetail = "INSERT INTO " + TABLE_PRODUCT_DETAIL + " ("
                + COLUMN_PRODUCT_TYPE + ", "
                + COLUMN_PRODUCT_CODE + ", "
                + COLUMN_PRODUCT_PRICE_EMPLOYEE + ", "
                + COLUMN_PRODUCT_PRICE_COMPANY + ", "
                + COLUMN_FOREIGN_KEY_PRODUCT_ID + ") "
                + "VALUES (?,?,?,?,?);";

        try {
            PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertProductDetail);
            preparedStatement.setString(1, productDetail.getProductType());
            preparedStatement.setString(2, productDetail.getProductCode());
            preparedStatement.setDouble(3, productDetail.getPriceEmployee());
            preparedStatement.setDouble(4, productDetail.getPriceCompany());
            preparedStatement.setInt(5, productDetail.getProduct().getProductId());
            preparedStatement.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    // Edit product detail
    public boolean editData(ProductDetail productDetail) {
        PreparedStatement preparedStatement;
        String updateProductDetail = "UPDATE " + TABLE_PRODUCT_DETAIL + " SET "
                + COLUMN_PRODUCT_TYPE + "=?, "
                + COLUMN_PRODUCT_CODE + "=?, "
                + COLUMN_PRODUCT_PRICE_EMPLOYEE + "=?, "
                + COLUMN_PRODUCT_PRICE_COMPANY + "=? "
                + " WHERE " + COLUMN_PRODUCT_DETAIL_ID + " = " + productDetail.getProductDetailId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(updateProductDetail);
            preparedStatement.setString(1, productDetail.getProductType());
            preparedStatement.setString(2, productDetail.getProductCode());
            preparedStatement.setDouble(3, productDetail.getPriceEmployee());
            preparedStatement.setDouble(4, productDetail.getPriceCompany());
            preparedStatement.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //Delete product detail
    public boolean deleteDataById(int id) {
        String sqlStmt = "DELETE FROM " + TABLE_PRODUCT_DETAIL + " WHERE " + COLUMN_PRODUCT_DETAIL_ID + " = " + id +" ;";
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //Add new product and product detail
    public boolean addNewProductData(ProductDetail productDetail) {
        Connection connection = null;
        PreparedStatement preparedStatement;

        String insertProduct = "INSERT INTO " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_NAME+ ") VALUES (?);";

        String getProductId = "SELECT last_insert_rowid() FROM " + TABLE_PRODUCT + " ;";

        String insertProductDetail = "INSERT INTO " + TABLE_PRODUCT_DETAIL + " ("
                + COLUMN_PRODUCT_TYPE + ", "
                + COLUMN_PRODUCT_CODE + ", "
                + COLUMN_PRODUCT_PRICE_EMPLOYEE + ", "
                + COLUMN_PRODUCT_PRICE_COMPANY + ", "
                + COLUMN_FOREIGN_KEY_PRODUCT_ID + ") "
                + "VALUES (?,?,?,?,?);";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertProduct);
            preparedStatement.setString(1, productDetail.getProduct().getProductName());
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getProductId);
            int id = resultSet.getInt(1);

            preparedStatement = connection.prepareStatement(insertProductDetail);
            preparedStatement.setString(1, productDetail.getProductType());
            preparedStatement.setString(2, productDetail.getProductCode());
            preparedStatement.setDouble(3, productDetail.getPriceEmployee());
            preparedStatement.setDouble(4, productDetail.getPriceCompany());
            preparedStatement.setInt(5, id);
            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (Exception e) {
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

    //Update product detail live data
    public void updateLiveData(Product product) {
        PRODUCT_DETAIL_LIVE_DATA.clear();
        try {
            PRODUCT_DETAIL_LIVE_DATA.setAll(getProductDetail(product));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

// *   public void createProductDetailTable() throws SQLException {
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_DETAIL
//                    + "(" + COLUMN_PRODUCT_DETAIL_ID + " INTEGER PRIMARY KEY, "
//                    + COLUMN_PRODUCT_TYPE + " TEXT NOT NULL, "
//                    + COLUMN_PRODUCT_CODE + " TEXT UNIQUE, "
//                    + COLUMN_PRODUCT_PRICE_EMPLOYEE + " REAL NOT NULL, "
//                    + COLUMN_PRODUCT_PRICE_COMPANY + " REAL NOT NULL, "
//                    + COLUMN_FOREIGN_KEY_PRODUCT_ID + " INTEGER NOT NULL, "
//                    + "FOREIGN KEY (" + COLUMN_FOREIGN_KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ") "
//                    + ")");
//        }catch (SQLException e){
//            e.printStackTrace();
//            throw e;
//        }
//    }

}
