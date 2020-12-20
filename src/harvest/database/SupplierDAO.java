package harvest.database;

import harvest.model.Supplier;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static harvest.database.ProductDAO.COLUMN_PRODUCT_ID;
import static harvest.database.ProductDAO.TABLE_PRODUCT;

public class SupplierDAO extends DAO{
    public static final String TABLE_SUPPLIERS = "suppliers";
    public static final String COLUMN_SUPPLIER_ID = "id";
    public static final String COLUMN_SUPPLIER_NAME = "name";
    public static final String COLUMN_SUPPLIER_FIRSTNAME = "firstname";
    public static final String COLUMN_SUPPLIER_LASTNAME = "lastname";
    public static final String COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID = "product_id";

    public boolean createSupplierTable() throws SQLException{
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_SUPPLIERS + "("
                    + COLUMN_SUPPLIER_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_SUPPLIER_NAME + " TEXT UNIQUE, "
                    + COLUMN_SUPPLIER_FIRSTNAME + " TEXT NOT NULL, "
                    + COLUMN_SUPPLIER_LASTNAME + " TEXT NOT NULL, "
                    + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID +" TEXT NOT NULL"
                    + "FOREIGN KEY (" + COLUMN_SUPPLIER_FRGN_KEY_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID + ") "
                    + ")");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }


    public List<Supplier> getData() throws Exception {
        return null;
    }


    public boolean addData(Supplier supplier) {
        return false;
    }


    public boolean editData(Supplier supplier) {
        return false;
    }


    public boolean deleteDataById(int Id) {
        return false;
    }

    //@Override
    public void updateLiveData() {

    }


//    //*******************************
//    //SELECT all suppliers
//    //*******************************
//    public ObservableList<String> getSuppliersNames () throws SQLException {
//        String selectStmt = "SELECT DISTINCT " +COLUMN_SUPPLIER_NAME+ " FROM " +TABLE_SUPPLIERS+ " ORDER BY " +COLUMN_SUPPLIER_NAME+ " ASC;";
//        ObservableList<String> namesList = FXCollections.observableArrayList();
//        System.out.println(selectStmt);
//        //Execute SELECT statement
//        try {
//            ResultSet resultSet = dbG.dbExecuteQuery(selectStmt);
//            while (resultSet.next()){
//                namesList.add(resultSet.getString(1));
//            }
//            return namesList;
//        } catch (SQLException e) {
//            System.out.println("SQL select operation has been failed: " + e);
//            //Return exception
//            throw e;
//        }
//    }
//
//    //*******************************
//    //SELECT all suppliers
//    //*******************************
//    public static List<Supplier> getAllSuppliers () throws SQLException {
//        //Declare a SELECT statement
//        String selectStmt = "SELECT * FROM " +TABLE_SUPPLIERS+ " ORDER BY " +COLUMN_SUPPLIER_NAME+ " ASC;";
//        //Execute SELECT statement
//        try {
//            ResultSet resultSet = DBHandler.dbExecuteQuery(selectStmt);
//            return getSupplierFromResultSet(resultSet);
//        } catch (SQLException e) {
//            System.out.println("SQL select operation has been failed: " + e);
//            //Return exception
//            throw e;
//        }
//    }
//
//    private static List<Supplier> getSupplierFromResultSet(ResultSet resultSet) throws SQLException {
//        List<Supplier> supplierList = new ArrayList<>();
//        while (resultSet.next()){
//            Supplier supplier = new Supplier();
//            supplier.setSupplierId(resultSet.getInt(1));
//            supplier.setSupplierName(resultSet.getString(2));
//            supplier.setSupplierFirstname(resultSet.getString(3));
//            supplier.setSupplierLastname(resultSet.getString(4));
//            supplier.setSupplierProduct(resultSet.getString(5));
//            supplierList.add(supplier);
//        }
//        return supplierList;
//    }
//
//    public static void insertSupplier (String Supplier, String firstName, String lastName, String productName) throws SQLException{
//        PreparedStatement preparedStatement;
//        //Declare a INSERT statement
//        String insertStmt = "INSERT INTO " +TABLE_SUPPLIERS+ " (" +COLUMN_SUPPLIER_NAME+ ", " +COLUMN_SUPPLIER_FIRSTNAME+ ", " +COLUMN_SUPPLIER_LASTNAME+ ", "+COLUMN_PRODUCT_NAME+ ")\n" +
//                "VALUES (?,?,?,?);";
//        preparedStatement = DBHandler.getConnection().prepareStatement(insertStmt);
//        preparedStatement.setString(1, Supplier);
//        preparedStatement.setString(2, firstName);
//        preparedStatement.setString(3, lastName);
//        preparedStatement.setString(4, productName);
//
//        try {
//            DBHandler.executePreparedStatement(preparedStatement);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    //*************************************
//    //DELETE supplier With Code
//    //*************************************
//    public static void deleteSupplierWithId (int supplierId) throws SQLException {
//        //Declare a DELETE statement
//        String updateStmt = "DELETE FROM " +TABLE_SUPPLIERS+ " WHERE " +COLUMN_SUPPLIER_ID+ " ="+supplierId+";";
//        //Execute UPDATE operation
//        try {
//            DBHandler.dbExecuteUpdate(updateStmt);
//        } catch (SQLException e) {
//            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
//            throw e;
//        }
//    }
//
//
//    //*************************************
//    //UPDATE Product's price
//    //*************************************
//    public static boolean updateSupplier(int supplierId, String supplier, String firstName, String lastName, String product) throws SQLException {
//        PreparedStatement preparedStatement;
//        //Declare a UPDATE statement
//        String updateStmt = "UPDATE " + TABLE_SUPPLIERS + " SET " +
//                "" +COLUMN_SUPPLIER_NAME+ " =?," +
//                "" +COLUMN_SUPPLIER_FIRSTNAME+ " =?," +
//                "" +COLUMN_SUPPLIER_LASTNAME+ " =?," +
//                "" +COLUMN_PRODUCT_NAME+ " =?" +
//                " WHERE " +COLUMN_SUPPLIER_ID+ " = "+supplierId+ ";";
//        preparedStatement = DBHandler.getConnection().prepareStatement(updateStmt);
//        preparedStatement.setString(1, supplier);
//        preparedStatement.setString(2, firstName);
//        preparedStatement.setString(3, lastName);
//        preparedStatement.setString(4, product);
//        //Execute UPDATE operation
//        try {
//            DBHandler.executePreparedStatement(preparedStatement);
//            return true;
//        }catch (SQLException e){
//            e.printStackTrace();
//            System.out.println("Error occurred while UPDATE Operation: " + e.getMessage());
//            return false;
//        }
//    }
}
