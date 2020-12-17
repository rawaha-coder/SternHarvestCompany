package harvest.database;

import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Season;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.FarmDAO.*;
import static harvest.ui.farm.DisplayFarmSeasonController.FARM_LIST_LIVE_DATA;
import static harvest.ui.farm.DisplayFarmSeasonController.SEASON_LIST_LIVE_DATA;

public class SeasonDAO extends DAO implements DAOList<Season>{

    public static final String TABLE_SEASON = "season";
    public static final String COLUMN_SEASON_ID = "id";
    public static final String COLUMN_SEASON_DATE_PLANTING = "planting";
    public static final String COLUMN_SEASON_DATE_HARVEST = "harvest";
    public static final String COLUMN_SEASON_FARM_ID = "farm_id";

    private static SeasonDAO sSeasonDAO = new SeasonDAO();

    private SeasonDAO(){};

    public static SeasonDAO getInstance(){
        if (sSeasonDAO == null ){
            sSeasonDAO = new SeasonDAO();
            return sSeasonDAO;
        }
        return sSeasonDAO;
    }

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


    //***********************************************************************
    //SELECT all data from farm and season
    //***********************************************************************
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

    @Override
    public boolean addData(Season season) {
        return false;
    }

    @Override
    public boolean editData(Season season) {
        return false;
    }

    @Override
    public boolean deleteDataById(int Id) {
        return false;
    }

    FarmDAO mFarmDAO = FarmDAO.getInstance();
    @Override
    public void updateLiveData() {
        mFarmDAO.updateLiveData();
        if (FARM_LIST_LIVE_DATA.size() > 0){
            updateSeasonListByFarm(FARM_LIST_LIVE_DATA.get(0));
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

    public boolean addFarmSeason(Farm farm, Season season) {
        PreparedStatement preparedStatement;
        //Declare a INSERT statement
        String sqlInsertFarmStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + ") "
                + "VALUES (?,?);";

        String sqlGetFarmIdStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + ") "
                + "VALUES (?,?);";

        String sqlInsertSeasonStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + ") "
                + "VALUES (?,?);";

        try {
            preparedStatement = dbGetConnect().prepareCall("BEGIN TRANSACTION");
            preparedStatement.execute("BEGIN TRANSACTION");
            preparedStatement.execute(sqlInsertFarmStmt);
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
}
