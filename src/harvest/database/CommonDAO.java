package harvest.database;

import harvest.model.Season;
import harvest.model.Supply;

import java.sql.*;

import static harvest.database.FarmDAO.*;
import static harvest.database.SupplierDAO.*;
import static harvest.database.SupplyDAO.*;

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

    //Delete data from Farm and Season tables
    public boolean deleteSupplierSupplyById(int id){
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
    //Add data to Farm and Season tables
    public boolean addFarmSeasonData(Season season){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //Declare a INSERT statement
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

    //Delete data from Farm and Season tables
    public boolean deleteFarmSeasonById(int id){
        Connection connection = null;
        Statement statement = null;
        String sqlDeleteFarmStmt = "DELETE FROM " + TABLE_FARM + " WHERE " + COLUMN_FARM_ID + " ="+id+";";
        String sqlDeleteSeasonStmt = "DELETE FROM " + TABLE_SEASON + " WHERE " + COLUMN_SEASON_FARM_ID + " ="+id+";";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(sqlDeleteFarmStmt);
            statement = connection.createStatement();
            statement.execute(sqlDeleteSeasonStmt);
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
            assert statement != null;
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            dbDisConnect();
        }
    }
}
