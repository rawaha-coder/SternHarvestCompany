package harvest.database;

import harvest.model.Farm;
import harvest.model.Season;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.farm.DisplayFarmSeasonController.FARM_LIST_LIVE_DATA;
import static harvest.ui.farm.DisplayFarmSeasonController.SEASON_LIST_LIVE_DATA;

public class SeasonDAO extends DAO{

    private static SeasonDAO sSeasonDAO = new SeasonDAO();

    private SeasonDAO(){}

    public static SeasonDAO getInstance(){
        if (sSeasonDAO == null){
            sSeasonDAO = new SeasonDAO();
            return sSeasonDAO;
        }
        return sSeasonDAO;
    }

    public static final String TABLE_SEASON = "season";
    public static final String COLUMN_SEASON_ID = "id";
    public static final String COLUMN_SEASON_DATE_PLANTING = "planting";
    public static final String COLUMN_SEASON_DATE_HARVEST = "harvest";
    public static final String COLUMN_SEASON_FARM_ID = "farm_id";

        /*
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
     */

    public List<Season> getSeasonByFarm(Farm farm) throws Exception {
        //Declare a SELECT statement
        String sqlStmt = "SELECT * FROM " + TABLE_SEASON
                + " WHERE " + COLUMN_SEASON_FARM_ID + " = " + farm.getFarmId() + " "
                + " ORDER BY " + COLUMN_SEASON_DATE_HARVEST + " DESC;";
        try {
            Statement statement = dbGetConnect().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStmt);
            return getSeasonFromResultSet(resultSet, farm);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private List<Season> getSeasonFromResultSet(ResultSet resultSet, Farm farm) throws SQLException {
        List<Season> list = new ArrayList<>();
        while (resultSet.next()) {
            Season season = new Season();
            season.setSeasonId(resultSet.getInt(1));
            season.setFarmPlantingDate(resultSet.getDate(2));
            season.setFarmHarvestDate(resultSet.getDate(3));
            season.setSeasonFarm(farm);
            list.add(season);
        }
        return list;
    }

    public boolean editSeasonData(Season season) {
        PreparedStatement preparedStatement;
        String sqlStmt = "UPDATE " + TABLE_SEASON + " SET "
                + COLUMN_SEASON_DATE_PLANTING + " =?, "
                + COLUMN_SEASON_DATE_HARVEST + " =? "
                + " WHERE " + COLUMN_SEASON_ID + " = " + season.getSeasonId() + " ;";
        try {
            preparedStatement = dbGetConnect().prepareStatement(sqlStmt);
            preparedStatement.setDate(1, season.getFarmPlantingDate());
            preparedStatement.setDate(2, season.getFarmHarvestDate());
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

    public void updateSeasonListByFarm(Farm farm) {
        SEASON_LIST_LIVE_DATA.clear();
        try {
            SEASON_LIST_LIVE_DATA.setAll(getSeasonByFarm(farm));
        }catch (Exception  e){
            e.printStackTrace();
        }
    }
}
