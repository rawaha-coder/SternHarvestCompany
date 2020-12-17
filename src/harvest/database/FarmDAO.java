package harvest.database;

import harvest.model.Farm;
import harvest.model.FarmSeason;
import harvest.model.Season;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.SeasonDAO.*;
//import static harvest.ui.farm.AddFarmController.FARM_NAME_LIVE_DATA;
import static harvest.ui.farm.DisplayFarmSeasonController.FARM_LIST_LIVE_DATA;


public class FarmDAO extends DAO implements DAOList<Farm> {

    private static FarmDAO sFarmDAO = new FarmDAO();

    private FarmDAO(){}

    public static FarmDAO getInstance(){
        if (sFarmDAO == null ){
            sFarmDAO = new FarmDAO();
            return sFarmDAO;
        }
        return sFarmDAO;
    }

    public static final String TABLE_FARM = "farm";
    public static final String COLUMN_FARM_ID = "id";
    public static final String COLUMN_FARM_NAME = "name";
    public static final String COLUMN_FARM_ADDRESS = "address";

    public void createFarmTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS "+ TABLE_FARM
                    +"("+ COLUMN_FARM_ID +" INTEGER PRIMARY KEY, "
                    + COLUMN_FARM_NAME +" TEXT NOT NULL, "
                    + COLUMN_FARM_ADDRESS +" TEXT NOT NULL "
                    + ")");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    //***********************************************************************
    //SELECT all data from farm and season
    //***********************************************************************
    @Override
    public List<Farm> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT * FROM " + TABLE_FARM + " ORDER BY " + COLUMN_FARM_ID + " DESC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getCreditDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Farm> getCreditDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<Farm> list = new ArrayList<>();
        while (resultSet.next()) {
            Farm farm = new Farm();
            farm.setFarmId(resultSet.getInt(1));
            farm.setFarmName(resultSet.getString(2));
            farm.setFarmAddress(resultSet.getString(3));
            list.add(farm);
        }
        return list;
    }

    //***********************************************************************
    //Add new farm and season
    //***********************************************************************
    @Override
    public boolean addData(Farm farm) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String sqlStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + " "
                + "VALUES (?,?);";

        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setString(1, farm.getFarmName());
            preparedStatement.setString(2, farm.getFarmAddress());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean editData(Farm farm) {
        return false;
    }

    //***********************************************************************
    //Edit farm season
    //***********************************************************************
    public boolean editData(FarmSeason farmSeason) {
        return false;
    }

    //***********************************************************************
    //Delete farm season
    //***********************************************************************
    @Override
    public boolean deleteDataById(int Id) {
        //Declare a DELETE statement
        String sqlStmt = "DELETE FROM " + TABLE_FARM + " WHERE " + COLUMN_FARM_ID + " ="+Id+";";
        //Execute UPDATE operation
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while DELETE Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public void updateLiveData() {
        FARM_LIST_LIVE_DATA.clear();
        try {
            FARM_LIST_LIVE_DATA.setAll(getData());
        }catch (Exception  e){
            e.printStackTrace();
        }
    }

    public List<String> getFarmName() throws Exception {
        String sqlStmt = "SELECT " + COLUMN_FARM_NAME + " FROM " + TABLE_FARM + " ORDER BY " + COLUMN_FARM_NAME + " ;";
        List<String> list = new  ArrayList<>();
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            while (resultSet.next()){
                list.add(resultSet.getString(1));
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean addFarmSeason(Farm farm, Season season) throws Exception{
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
            preparedStatement.setString(1, farm.getFarmName());
            preparedStatement.setString(2, farm.getFarmAddress());
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
            assert connection != null;
            connection.rollback();
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            preparedStatement.close();
            dbDisConnect();
        }
    }

    public boolean deleteFarmSeason(int id) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        //Declare a INSERT statement
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
            return true;
        } catch (SQLException e) {
            assert connection != null;
            connection.rollback();
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            assert statement != null;
            statement.close();
            dbDisConnect();
        }
    }
}
