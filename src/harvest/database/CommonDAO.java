package harvest.database;

import harvest.model.*;
import harvest.util.Validation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.CreditDAO.*;
import static harvest.database.EmployeeDAO.*;
import static harvest.database.FarmDAO.*;
import static harvest.database.ProductDAO.*;
import static harvest.database.ProductDetailDAO.*;
import static harvest.database.SeasonDAO.*;
import static harvest.database.SupplierDAO.*;
import static harvest.database.SupplyDAO.*;
import static harvest.database.TransportDAO.*;

public class CommonDAO extends DAO{

    private static CommonDAO sCommonDAO = new CommonDAO();

    //private Constructor
    private CommonDAO(){ }

    public static CommonDAO getInstance(){
        if(sCommonDAO == null){
            sCommonDAO = new CommonDAO();
            return sCommonDAO;
        }
        return sCommonDAO;
    }

    //*******************************
    //Get all employees data
    //*******************************
    public List<AddHarvestHours> getHarvestHoursData() throws Exception {
        List<AddHarvestHours> addHarvestHoursList = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_EMPLOYEE + " ORDER BY " + COLUMN_EMPLOYEE_ID + " DESC;";

        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                AddHarvestHours addHarvestHours = new AddHarvestHours();
                addHarvestHours.setEmployeeId(resultSet.getInt(1));
                addHarvestHours.setEmployeeStatus(resultSet.getBoolean(2));
                addHarvestHours.setEmployeeFullName(resultSet.getString(3) + " " + resultSet.getString(4));
                addHarvestHoursList.add(addHarvestHours);
            }
            return addHarvestHoursList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*************************************************************
    //Product and ProductDetail tables
    //*************************************************************

    //Add new product and product detail
    public boolean addNewProductData(ProductDetail productDetail) {
        Connection connection = null;
        PreparedStatement preparedStatement;

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

    //Delete all product data
    public boolean deleteProductDataById(int Id) {
        Connection connection = null;
        Statement statement;
        String deleteFarm = "DELETE FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + " = " + Id + " ;";
        String deleteSeason = "DELETE FROM " + TABLE_PRODUCT_DETAIL + " WHERE " + COLUMN_FOREIGN_KEY_PRODUCT_ID + " = " + Id +" ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteFarm);

            statement = connection.createStatement();
            statement.execute(deleteSeason);
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

    //*************************************************************
    //Supplier and Supply tables
    //*************************************************************
    //Add data to Farm and Season tables
    public boolean addSupplierSupplyData(Supply supply){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSupplier = "INSERT INTO " + TABLE_SUPPLIER + " ("
                + COLUMN_SUPPLIER_NAME + ", "
                + COLUMN_SUPPLIER_FIRSTNAME + ", "
                + COLUMN_SUPPLIER_LASTNAME + ") "
                + " VALUES (?,?,?);";

        String sqlGetLastId = "SELECT last_insert_rowid() FROM " + TABLE_SUPPLIER + " ;";

        String insertSupply = "INSERT INTO " + TABLE_SUPPLY + " ("
                + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + ", "
                + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + ", "
                + COLUMN_SUPPLY_FRGN_KEY_PRODUCT_ID + ") "
                + "VALUES (?,?,?);";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertSupplier);
            preparedStatement.setString(1, supply.getSupplier().getSupplierName());
            preparedStatement.setString(2, supply.getSupplier().getSupplierFirstname());
            preparedStatement.setString(3, supply.getSupplier().getSupplierLastname());
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetLastId);
            int id = resultSet.getInt(1);

            preparedStatement = connection.prepareStatement(insertSupply);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, supply.getFarm().getFarmId());
            preparedStatement.setInt(3, supply.getProduct().getProductId());
            preparedStatement.execute();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            assert connection != null;
            assert preparedStatement != null;
            try {
                connection.rollback();
                preparedStatement.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public boolean deleteAllSupplierDataById(int id){
        Connection connection = null;
        Statement statement;
        String deleteSupplier = "DELETE FROM " + TABLE_SUPPLIER + " WHERE " + COLUMN_SUPPLIER_ID + " = " + id + " ;";
        String deleteSupply = "DELETE FROM " +  TABLE_SUPPLY + " WHERE " + COLUMN_SUPPLY_FRGN_KEY_SUPPLIER_ID + " = " + id + " ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(deleteSupplier);
            statement = connection.createStatement();
            statement.execute(deleteSupply);
            connection.commit();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + sqlException.getMessage());
            }
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            dbDisConnect();
        }
    }

    //*************************************************************
    //Farm and Season tables
    //*************************************************************
    //Get data from farm and season tables
    public List<Season> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT "
                + TABLE_SEASON + "." + COLUMN_SEASON_ID + ", "
                + TABLE_SEASON + "." + COLUMN_SEASON_DATE_PLANTING + ", "
                + TABLE_SEASON + "." + COLUMN_SEASON_DATE_HARVEST + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ADDRESS + ", "
                + " FROM " + TABLE_SEASON + " "
                + " LEFT JOIN " + TABLE_FARM + " "
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID  + " = " + TABLE_SEASON + "." + COLUMN_SEASON_FARM_ID + " "
                + " ORDER BY " + COLUMN_SEASON_DATE_HARVEST + " DESC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Season> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<Season> list = new ArrayList<>();
        while (resultSet.next()) {
            Season season = new Season();
            season.setSeasonId(resultSet.getInt(1));
            season.setFarmPlantingDate(resultSet.getDate(2));
            season.setFarmHarvestDate(resultSet.getDate(3));
            season.setSeasonFarm(new Farm(resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6)));
            list.add(season);
        }
        return list;
    }

    //Add data to Farm and Season tables
    public boolean addFarmSeasonData(Season season){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sqlInsertFarmStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + ") "
                + "VALUES (?,?);";

        String sqlGetLastId = "SELECT last_insert_rowid() FROM " + TABLE_FARM + " ;";

        String sqlInsertSeasonStmt = "INSERT INTO " + TABLE_SEASON + " ("
                + COLUMN_SEASON_DATE_PLANTING + ", "
                + COLUMN_SEASON_DATE_HARVEST + ", "
                + COLUMN_SEASON_FARM_ID + ") "
                + "VALUES (?,?,?);";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sqlInsertFarmStmt);
            preparedStatement.setString(1, season.getSeasonFarm().getFarmName());
            preparedStatement.setString(2, season.getSeasonFarm().getFarmAddress());
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetLastId);
            int id = resultSet.getInt(1);

            preparedStatement = connection.prepareStatement(sqlInsertSeasonStmt);
            preparedStatement.setDate(1, season.getFarmPlantingDate());
            preparedStatement.setDate(2, season.getFarmHarvestDate());
            preparedStatement.setInt(3, id);
            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            assert connection != null;
            assert preparedStatement != null;
            try {
                connection.rollback();
                preparedStatement.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }finally {
            dbDisConnect();
        }
    }

    //Delete data from farm and season
    public boolean deleteAllFarmDataById(int id){
        Connection connection = null;
        Statement statement = null;
        String deleteFarmStmt = "DELETE FROM " + TABLE_FARM + " WHERE " + COLUMN_FARM_ID + " ="+id+" ;";
        String deleteSeasonStmt = "DELETE FROM " + TABLE_SEASON + " WHERE " + COLUMN_SEASON_FARM_ID + " ="+id+" ;";
        String deleteSupplyStmt = "DELETE FROM " + TABLE_SUPPLY + " WHERE " + COLUMN_SUPPLY_FRGN_KEY_FARM_ID + " ="+id+" ;";
        String deleteTransportStmt = "DELETE FROM " + TABLE_TRANSPORT + " WHERE " + COLUMN_TRANSPORT_FARM_ID + " ="+id+" ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteFarmStmt);

            statement = connection.createStatement();
            statement.execute(deleteSeasonStmt);

            statement = connection.createStatement();
            statement.execute(deleteSupplyStmt);

            statement = connection.createStatement();
            statement.execute(deleteTransportStmt);

            connection.commit();
            statement.close();
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
            System.out.print("Error occurred while Delete Operation: " + exception.getMessage());
            return false;
        }finally {
            if (statement != null){
                try {
                    statement.close();
                }catch (SQLException e){/**/}
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){/**/}
            }
            dbDisConnect();
        }
    }

    //*************************************************************
    //Employee and others tables
    //*************************************************************
    //Delete Employee data from all tables
    public boolean deleteAllEmployeeDataById(int id){
        Connection connection = null;
        Statement statement = null;
        String deleteFromEmployee = "DELETE FROM " + TABLE_EMPLOYEE  + " WHERE " + COLUMN_EMPLOYEE_ID + " ="+id+" ;";
        String deleteFromCredit = "DELETE FROM " + TABLE_CREDIT + " WHERE " + COLUMN_CREDIT_EMPLOYEE_ID + " ="+id+" ;";
        String deleteFromTransport = "DELETE FROM " + TABLE_TRANSPORT  + " WHERE " + COLUMN_TRANSPORT_EMPLOYEE_ID + " ="+id+" ;";

        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute(deleteFromEmployee);

            statement = connection.createStatement();
            statement.execute(deleteFromCredit);

            statement = connection.createStatement();
            statement.execute(deleteFromTransport);

            connection.commit();
            return true;
        } catch (SQLException ex1) {
            assert connection != null;
            try {
                connection.rollback();
            }catch (SQLException ex2){
                ex2.printStackTrace();
                System.out.print("Error occurred while rollback Operation: " + ex2.getMessage());
            }
            ex1.printStackTrace();
            return false;
        }finally {
            if (statement != null){
                try {
                    statement.close();
                }catch (SQLException e){/**/}
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException e){/**/}
            }
            dbDisConnect();
        }
    }
}
