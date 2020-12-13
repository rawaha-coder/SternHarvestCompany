package harvest.viewmodel;

import harvest.database.DBHandler;
import harvest.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IViewModelDAO{

    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_TYPE = "type";
    public static final String COLUMN_PRODUCT_CODE = "code";
    public static final String COLUMN_PRODUCT_PRICE_1 = "price_1";
    public static final String COLUMN_PRODUCT_PRICE_2 = "price_2";

    public static ObservableList<Product> PRODUCT_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<String> PRODUCT_NAME_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<String> PRODUCT_TYPE_LIVE_DATA = FXCollections.observableArrayList();

    public static void createProductTable() throws SQLException{
        try {
            Statement statement = DBHandler.getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT
                    + "(" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                    + COLUMN_PRODUCT_TYPE + " TEXT NOT NULL, "
                    + COLUMN_PRODUCT_CODE + " TEXT UNIQUE, "
                    + COLUMN_PRODUCT_PRICE_1 + " REAL NOT NULL, "
                    + COLUMN_PRODUCT_PRICE_2 + " REAL )");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    //*******************************
    //SELECT all products names
    //*******************************
    public List<String> getProductName() throws SQLException{
        String selectStmt = "SELECT DISTINCT " + COLUMN_PRODUCT_NAME + " FROM " + TABLE_PRODUCT + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
        List<String> list = new ArrayList<>();
        try {
            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
            while (resultSet.next()){
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    //*******************************
    //SELECT all products Types
    //*******************************
    public static List<String> getProductType() throws SQLException{
        String selectStmt = "SELECT DISTINCT " + COLUMN_PRODUCT_TYPE + " FROM " + TABLE_PRODUCT + " ORDER BY " +COLUMN_PRODUCT_TYPE+ " ASC;";
        List<String> list = new ArrayList<>();
        try {
            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
            while (resultSet.next()){
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    //*******************************
    //SELECT all products
    //*******************************
    public static List<Product> getAllProducts () throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM " + TABLE_PRODUCT + " ORDER BY " +COLUMN_PRODUCT_TYPE+ " ASC;";
        //Execute SELECT statement
        try {
            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
            return getProductFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
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
    //Add Product
    //*************************************
    public boolean addProduct(String name, String type, String code, double price1, double price2){
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
            preparedStatement = DBHandler.getConnection().prepareStatement(insertStmt);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, code);
            preparedStatement.setDouble(4, price1);
            preparedStatement.setDouble(5, price2);
            DBHandler.executePreparedStatement(preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }
    }

    //*************************************
    //UPDATE Product
    //*************************************
    public boolean updateProduct(int id, String name, String code, String type, Double price1, Double price2){
        PreparedStatement preparedStatement;
        //Declare a UPDATE statement
        String updateStmt = "UPDATE " + TABLE_PRODUCT
                + " SET " +
                "" + COLUMN_PRODUCT_NAME + " =?, " +
                "" + COLUMN_PRODUCT_CODE + " =?, " +
                "" + COLUMN_PRODUCT_TYPE + " =?, " +
                "" + COLUMN_PRODUCT_PRICE_1 + " =?, " +
                "" + COLUMN_PRODUCT_PRICE_2 + " =? " +
                " WHERE " +COLUMN_PRODUCT_ID+ " = "+id+ ";";
        try {
            preparedStatement = DBHandler.getConnection().prepareStatement(updateStmt);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, code);
            preparedStatement.setString(3, type);
            preparedStatement.setDouble(4, price1);
            preparedStatement.setDouble(5, price2);
            DBHandler.executePreparedStatement(preparedStatement);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
            return false;
        }
    }

    //*************************************
    //DELETE product
    //*************************************
    @Override
    public boolean deleteById(int keyId) {
        String updateStmt = "DELETE FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + " ="+keyId+";";
        try {
            DBHandler.dbExecuteUpdate(updateStmt);
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void updateLivedata() {
        PRODUCT_NAME_LIVE_DATA.clear();
        PRODUCT_LIST_LIVE_DATA.clear();
        try {
            PRODUCT_NAME_LIVE_DATA.setAll(getProductName());
            if(PRODUCT_NAME_LIVE_DATA.size() != 0) {
                PRODUCT_LIST_LIVE_DATA.setAll(productList(PRODUCT_NAME_LIVE_DATA.get(0)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateAddLivedata() {
        PRODUCT_NAME_LIVE_DATA.clear();
        PRODUCT_TYPE_LIVE_DATA.clear();
        try {
            PRODUCT_NAME_LIVE_DATA.setAll(getProductName());
            PRODUCT_TYPE_LIVE_DATA.setAll(getProductType());
        } catch (SQLException e) {
            e.printStackTrace();
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
            allProduct.addAll(getAllProducts());
        } catch (SQLException e) {
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
