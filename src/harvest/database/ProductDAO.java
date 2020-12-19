package harvest.database;

import harvest.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ProductDetailDAO.COLUMN_FOREIGN_KEY_PRODUCT_ID;
import static harvest.database.ProductDetailDAO.TABLE_PRODUCT_DETAIL;
import static harvest.ui.product.DisplayProductController.PRODUCT_NAME_LIVE_DATA;

public class ProductDAO extends DAO{

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

/* create table
public void createProductTable() throws SQLException{
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT
                    + "(" + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_PRODUCT_NAME + " TEXT NOT NULL)");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
*/

    //@Override
    public List<Product> getData() throws Exception {
        List<Product> list = new ArrayList<>();
        Statement statement;
        String sqlStmt = "SELECT * FROM " + TABLE_PRODUCT + " ORDER BY " + COLUMN_PRODUCT_ID + " DESC;";
        //Execute SELECT statement
        try {
            statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt(1));
                product.setProductName(resultSet.getString(2));
                list.add(product);
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

    /* **
    public boolean addData(Product product) {
        PreparedStatement preparedStatement;
        String sqlStmt = "INSERT INTO " + TABLE_PRODUCT +  " (" + COLUMN_PRODUCT_NAME + ") VALUES(?);";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }

    }
    */

    //Edit product
    public boolean editData(Product product) {
        PreparedStatement preparedStatement;
        String updateStmt = "UPDATE " + TABLE_PRODUCT + " SET " + COLUMN_PRODUCT_NAME + " =? " +
                " WHERE " + COLUMN_PRODUCT_ID + " = " + product.getProductId()+ " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(updateStmt);
            preparedStatement.setString(1, product.getProductName());
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

    //@Override
    public boolean deleteDataById(int Id) {
        Connection connection = null;
        Statement statement;
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

    //@Override
    public void updateLiveData() {
        PRODUCT_NAME_LIVE_DATA.clear();
        try {
            PRODUCT_NAME_LIVE_DATA.setAll(getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
