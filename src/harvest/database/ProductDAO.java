package harvest.database;

import harvest.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.product.AddProductController.PRODUCT_NAME_LIVE_DATA;
import static harvest.ui.product.AddProductController.PRODUCT_TYPE_LIVE_DATA;
import static harvest.ui.product.DisplayProductController.PRODUCT_LIST_LIVE_DATA;

public class ProductDAO extends DAO implements DAOList<Product>{

    private static ProductDAO sProductDAO = new ProductDAO();

    //private Constructor
    private ProductDAO(){
    }

    public static ProductDAO getInstance(){
        if (sProductDAO == null){
            sProductDAO = new ProductDAO();
            return sProductDAO;
        }
        return sProductDAO;
    }

    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_TYPE = "type";
    public static final String COLUMN_PRODUCT_CODE = "code";
    public static final String COLUMN_PRODUCT_PRICE_1 = "price_1";
    public static final String COLUMN_PRODUCT_PRICE_2 = "price_2";


/*    public void createProductTable() throws SQLException{
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT
//                    + "(" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY, "
//                    + COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
//                    + COLUMN_PRODUCT_TYPE + " TEXT NOT NULL, "
//                    + COLUMN_PRODUCT_CODE + " TEXT UNIQUE, "
//                    + COLUMN_PRODUCT_PRICE_1 + " REAL NOT NULL, "
//                    + COLUMN_PRODUCT_PRICE_2 + " REAL )");
//        }catch (SQLException e){
//            e.printStackTrace();
//            throw e;
//        }
//    }*/

    //*************************************
    //get all Product data
    //*************************************
    @Override
    public List<Product> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT * FROM " + TABLE_PRODUCT + " ORDER BY " +COLUMN_PRODUCT_TYPE+ " ASC;";
        //Execute SELECT statement
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getProductFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private static List<Product> getProductFromResultSet(ResultSet resultSet) throws SQLException {
        List<Product> list = new ArrayList<>();
        while (resultSet.next()){
            Product product = new Product();
            product.setProductId(resultSet.getInt(1));
            product.setProductName(resultSet.getString(2));
            product.setProductType(resultSet.getString(3));
            product.setProductCode(resultSet.getString(4));
            product.setProductFirstPrice(resultSet.getDouble(5));
            product.setProductSecondPrice(resultSet.getDouble(6));
            list.add(product);
        }
        return list;
    }

    //*************************************
    //Add new Product
    //*************************************
    @Override
    public boolean addData(Product product) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String insertStmt = "INSERT INTO " + TABLE_PRODUCT + " ("
                + COLUMN_PRODUCT_NAME + ", "
                + COLUMN_PRODUCT_TYPE + ", "
                + COLUMN_PRODUCT_CODE + ", "
                + COLUMN_PRODUCT_PRICE_1 + ", "
                + COLUMN_PRODUCT_PRICE_2 + ")\n" +
                "VALUES (?,?,?,?,?);";
        try {
            preparedStatement = dbGetConnect().prepareStatement(insertStmt);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductType());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getProductFirstPrice());
            preparedStatement.setDouble(5, product.getProductSecondPrice());
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

    //*************************************
    //UPDATE Product
    //*************************************
    @Override
    public boolean editData(Product product) {
        PreparedStatement preparedStatement;
        String sqlStmt = "UPDATE " + TABLE_PRODUCT
                + " SET " +
                "" + COLUMN_PRODUCT_NAME + " =?, " +
                "" + COLUMN_PRODUCT_TYPE + " =?, " +
                "" + COLUMN_PRODUCT_CODE + " =?, " +
                "" + COLUMN_PRODUCT_PRICE_1 + " =?, " +
                "" + COLUMN_PRODUCT_PRICE_2 + " =? " +
                " WHERE " +COLUMN_PRODUCT_ID+ " = "+product.getProductId()+ ";";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductType());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getProductFirstPrice());
            preparedStatement.setDouble(5, product.getProductSecondPrice());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************
    //DELETE product
    //*************************************
    @Override
    public boolean deleteDataById(int Id) {
        String sqlStmt = "DELETE FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + " ="+Id+";";
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    @Override
    public void updateLiveData() {
        PRODUCT_NAME_LIVE_DATA.clear();
        PRODUCT_LIST_LIVE_DATA.clear();
        PRODUCT_TYPE_LIVE_DATA.clear();
        try {
            PRODUCT_NAME_LIVE_DATA.setAll(getProductName());
            PRODUCT_TYPE_LIVE_DATA.setAll(getProductType());
            if(PRODUCT_NAME_LIVE_DATA.size() != 0) {
                PRODUCT_LIST_LIVE_DATA.setAll(productList(PRODUCT_NAME_LIVE_DATA.get(0)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public void updateAddLivedata() {
//        PRODUCT_NAME_LIVE_DATA.clear();
//        PRODUCT_TYPE_LIVE_DATA.clear();
//        try {
//            PRODUCT_NAME_LIVE_DATA.setAll(getProductName());
//            PRODUCT_TYPE_LIVE_DATA.setAll(getProductType());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    //*******************************
    //SELECT all products names
    //*******************************
    public List<String> getProductName() throws SQLException{
        String sqlStmt = "SELECT DISTINCT " + COLUMN_PRODUCT_NAME + " FROM " + TABLE_PRODUCT + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
        List<String> list = new ArrayList<>();
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()){
                list.add(resultSet.getString(1));
            }
            statement.close();
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*******************************
    //SELECT all products Types
    //*******************************
    public List<String> getProductType() throws SQLException{
        String sqlStmt = "SELECT DISTINCT " + COLUMN_PRODUCT_TYPE + " FROM " + TABLE_PRODUCT + " ORDER BY " +COLUMN_PRODUCT_TYPE+ " ASC;";
        List<String> list = new ArrayList<>();
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()){
                list.add(resultSet.getString(1));
            }
            statement.close();
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    public void updateProductListByName(String productName){
        PRODUCT_NAME_LIVE_DATA.clear();
        PRODUCT_LIST_LIVE_DATA.clear();
        try {
            PRODUCT_NAME_LIVE_DATA.setAll(getProductName());
            PRODUCT_LIST_LIVE_DATA.setAll(productList(productName));
        }catch (SQLException  e){
            e.printStackTrace();
        }
    }

    public List<Product> productList(String productName) {
        List<Product> selectProduct = new ArrayList<>();
        List<Product> allProduct = new ArrayList<>();
        try {
            allProduct.addAll(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Product product : allProduct) {
            if (product.getProductName().equals(productName)) {
                selectProduct.add(product);
            }
        }
        return selectProduct;
    }

}
