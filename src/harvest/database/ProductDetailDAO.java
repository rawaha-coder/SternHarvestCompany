package harvest.database;

import harvest.model.Product;
import harvest.model.ProductDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ProductDAO.*;

public class ProductDetailDAO extends DAO implements DAOList<ProductDetail>{

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

    public static final String TABLE_PRODUCT_DETAIL = "product_detail";
    public static final String COLUMN_PRODUCT_DETAIL_ID = "id";
    public static final String COLUMN_PRODUCT_TYPE = "type";
    public static final String COLUMN_PRODUCT_CODE = "code";
    public static final String COLUMN_PRODUCT_PRICE_1 = "price_1";
    public static final String COLUMN_PRODUCT_PRICE_2 = "price_2";
    public static final String COLUMN_FOREIGN_KEY_PRODUCT_ID = "product_id";

    public void createProductDetailTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_DETAIL
                    + "(" + COLUMN_PRODUCT_DETAIL_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_PRODUCT_TYPE + " TEXT NOT NULL, "
                    + COLUMN_PRODUCT_CODE + " TEXT UNIQUE, "
                    + COLUMN_PRODUCT_PRICE_1 + " REAL NOT NULL, "
                    + COLUMN_PRODUCT_PRICE_2 + " REAL, "
                    + COLUMN_FOREIGN_KEY_PRODUCT_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY (" + COLUMN_FOREIGN_KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ") "
                    + ")");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ProductDetail> getData() throws Exception {
        List<ProductDetail> list = new ArrayList<>();
        Statement statement;
        String sqlStmt = "SELECT "
                + TABLE_PRODUCT_DETAIL +"." + COLUMN_PRODUCT_DETAIL_ID + ", "
                + TABLE_PRODUCT_DETAIL +"." + COLUMN_PRODUCT_TYPE + ", "
                + TABLE_PRODUCT_DETAIL +"." + COLUMN_PRODUCT_CODE + ", "
                + TABLE_PRODUCT_DETAIL +"." + COLUMN_PRODUCT_PRICE_1 + ", "
                + TABLE_PRODUCT_DETAIL +"." + COLUMN_PRODUCT_PRICE_2 + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", "
                + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " "
                + " FROM " + TABLE_PRODUCT_DETAIL
                + " LEFT JOIN " + TABLE_PRODUCT
                + " On " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = " + TABLE_PRODUCT_DETAIL + "." + COLUMN_FOREIGN_KEY_PRODUCT_ID
                + " ORDER BY " + COLUMN_PRODUCT_DETAIL_ID + " DESC;";
        //Execute SELECT statement
        try {
            statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductDetailId(resultSet.getInt(1));
                productDetail.setProductType(resultSet.getString(2));
                productDetail.setProductCode(resultSet.getString(3));
                productDetail.setProductFirstPrice(resultSet.getDouble(4));
                productDetail.setProductSecondPrice(resultSet.getDouble(5));
                productDetail.setProduct(new Product(resultSet.getInt(6), resultSet.getString(7)));
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

    @Override
    public boolean addData(ProductDetail productDetail) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //Declare a INSERT statement
        String insertProduct = "INSERT INTO " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_NAME+ ") VALUES (?);";

        String getProductId = "SELECT last_insert_rowid() FROM " + TABLE_PRODUCT + " ;";

        String insertProductDetail = "INSERT INTO " + TABLE_PRODUCT_DETAIL + " ("
                + COLUMN_PRODUCT_TYPE + ", "
                + COLUMN_PRODUCT_CODE + ", "
                + COLUMN_PRODUCT_PRICE_1 + ", "
                + COLUMN_PRODUCT_PRICE_2 + ", "
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
            preparedStatement.setDouble(3, productDetail.getProductFirstPrice());
            preparedStatement.setDouble(4, productDetail.getProductSecondPrice());
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

    @Override
    public boolean editData(ProductDetail productDetail) {
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_PRODUCT_DETAIL + " SET ("
                + COLUMN_PRODUCT_TYPE + ", "
                + COLUMN_PRODUCT_CODE + ", "
                + COLUMN_PRODUCT_PRICE_1 + ", "
                + COLUMN_PRODUCT_PRICE_2 + ") "
                + " VALUES (?,?,?,?)"
                + " WHERE " + COLUMN_PRODUCT_DETAIL_ID + " = " + productDetail.getProductDetailId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(updateStmt);
            preparedStatement.setString(1, productDetail.getProductType());
            preparedStatement.setString(2, productDetail.getProductCode());
            preparedStatement.setDouble(3, productDetail.getProductFirstPrice());
            preparedStatement.setDouble(4, productDetail.getProductSecondPrice());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    @Override
    public boolean deleteDataById(int Id) {
        Connection connection = null;
        Statement statement = null;
        //Declare a INSERT statement
        String sqlDeleteFarmStmt = "DELETE FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + " = " + Id + " ;";

        String sqlDeleteSeasonStmt = "DELETE FROM " + TABLE_PRODUCT_DETAIL + " WHERE " + COLUMN_FOREIGN_KEY_PRODUCT_ID + " = " + Id +" ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(sqlDeleteFarmStmt);

            statement = connection.createStatement();
            statement.execute(sqlDeleteSeasonStmt);
            connection.commit();
            updateLiveData();
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

    @Override
    public void updateLiveData() {

    }

    public List<ProductDetail> getProductDetail(Product product) throws Exception {
        List<ProductDetail> list = new ArrayList<>();
        Statement statement;
        String sqlStmt = "SELECT * FROM " + TABLE_PRODUCT_DETAIL
                + " WHERE " + COLUMN_FOREIGN_KEY_PRODUCT_ID + " = " + product.getProductId()
                + " ORDER BY " + COLUMN_PRODUCT_DETAIL_ID + " DESC;";
        //Execute SELECT statement
        try {
            statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductDetailId(resultSet.getInt(1));
                productDetail.setProductType(resultSet.getString(2));
                productDetail.setProductCode(resultSet.getString(3));
                productDetail.setProductFirstPrice(resultSet.getDouble(4));
                productDetail.setProductSecondPrice(resultSet.getDouble(5));
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

    public boolean addProductDetail(ProductDetail productDetail) {
        String insertProductDetail = "INSERT INTO " + TABLE_PRODUCT_DETAIL + " ("
                + COLUMN_PRODUCT_TYPE + ", "
                + COLUMN_PRODUCT_CODE + ", "
                + COLUMN_PRODUCT_PRICE_1 + ", "
                + COLUMN_PRODUCT_PRICE_2 + ", "
                + COLUMN_FOREIGN_KEY_PRODUCT_ID + ") "
                + "VALUES (?,?,?,?,?);";

        try {
            PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertProductDetail);
            preparedStatement.setString(1, productDetail.getProductType());
            preparedStatement.setString(2, productDetail.getProductCode());
            preparedStatement.setDouble(3, productDetail.getProductFirstPrice());
            preparedStatement.setDouble(4, productDetail.getProductSecondPrice());
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
}
