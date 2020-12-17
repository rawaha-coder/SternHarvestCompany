package harvest.database;

import harvest.model.Farm;
import harvest.model.Season;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.farm.DisplayFarmSeasonController.FARM_LIST_LIVE_DATA;
import static harvest.ui.farm.DisplayFarmSeasonController.SEASON_LIST_LIVE_DATA;


public class FarmDAO extends DAO implements DAOList<Season> {

    private static FarmDAO sFarmDAO = new FarmDAO();

    private FarmDAO(){}

    public static FarmDAO getInstance(){
        if (sFarmDAO == null ){
            sFarmDAO = new FarmDAO();
            return sFarmDAO;
        }
        return sFarmDAO;
    }

    //***********************************************************************
    //farm Table operations
    //***********************************************************************
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
    public List<Farm> getFarmData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT * FROM " + TABLE_FARM + " ORDER BY " + COLUMN_FARM_ID + " DESC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getFarmDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Farm> getFarmDataFromResultSet(ResultSet resultSet) throws SQLException {
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

    //Add new farm
    public boolean addFarmData(Farm farm) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String sqlStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + ") "
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

    //Delete data from farm
    public boolean deleteFarmById(int Id) {
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


    //***********************************************************************
    //Season Table operations
    //***********************************************************************
    public static final String TABLE_SEASON = "season";
    public static final String COLUMN_SEASON_ID = "id";
    public static final String COLUMN_SEASON_DATE_PLANTING = "planting";
    public static final String COLUMN_SEASON_DATE_HARVEST = "harvest";
    public static final String COLUMN_SEASON_FARM_ID = "farm_id";

    public void createSeasonTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS "+ TABLE_SEASON +"("
                    + COLUMN_SEASON_ID +" INTEGER PRIMARY KEY, "
                    + COLUMN_SEASON_DATE_PLANTING +" DATE, "
                    + COLUMN_SEASON_DATE_HARVEST +" DATE, "
                    + COLUMN_SEASON_FARM_ID + " INTEGER NOT NULL,  "
                    + "FOREIGN KEY (" + COLUMN_SEASON_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ") "
                    + ")");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean addSeasonData(Season season) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String sqlStmt = "INSERT INTO " + TABLE_SEASON + " ("
                + COLUMN_SEASON_DATE_PLANTING + ", "
                + COLUMN_SEASON_DATE_HARVEST + ", "
                + COLUMN_SEASON_FARM_ID + ") "
                + "VALUES (?,?,?);";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setDate(1, season.getFarmPlantingDate());
            preparedStatement.setDate(2, season.getFarmHarvestDate());
            preparedStatement.setInt(3, season.getSeasonFarm().getFarmId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }
    }

    public void updateSeasonListByFarm(Farm farm) {
        SEASON_LIST_LIVE_DATA.clear();
        try {
            SEASON_LIST_LIVE_DATA.setAll(getSeasonByFarm(farm));
        }catch (Exception  e){
            e.printStackTrace();
        }
    }


    public List<Season> getSeasonByFarm(Farm farm) throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT * FROM " + TABLE_SEASON
                + " WHERE " + COLUMN_SEASON_FARM_ID + " = " + farm.getFarmId() + " "
                + " ORDER BY " + COLUMN_SEASON_DATE_HARVEST + " DESC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getSeasonFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Season> getSeasonFromResultSet(ResultSet resultSet) throws SQLException {
        List<Season> list = new ArrayList<>();
        while (resultSet.next()) {
            Season season = new Season();
            season.setSeasonId(resultSet.getInt(1));
            season.setFarmPlantingDate(resultSet.getDate(2));
            season.setFarmHarvestDate(resultSet.getDate(3));
            list.add(season);
        }
        return list;
    }

    //***********************************************************************
    //Farm table join Season table operations
    //***********************************************************************

    //SELECT all data from farm and season
    @Override
    public List<Season> getData() throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT "
                + TABLE_SEASON + "." + COLUMN_SEASON_ID + ", "
                + TABLE_SEASON + "." + COLUMN_SEASON_DATE_PLANTING + ", "
                + TABLE_SEASON + "." + COLUMN_SEASON_DATE_HARVEST + " "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ADDRESS + ", "
                + " FROM " + TABLE_SEASON + " "
                + "LEFT JOIN " + TABLE_FARM + " "
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

    //Add data to farm and season
    @Override
    public boolean addData(Season season){
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
    @Override
    public boolean deleteDataById(int id){
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
            statement.close();
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
            assert statement != null;

            dbDisConnect();
        }
    }

    @Override
    public boolean editData(Season season) {
        return false;
    }

    @Override
    public void updateLiveData() {
        FARM_LIST_LIVE_DATA.clear();
        try {
            FARM_LIST_LIVE_DATA.setAll(getFarmData());
        }catch (Exception  e){
            e.printStackTrace();
        }
    }

}
