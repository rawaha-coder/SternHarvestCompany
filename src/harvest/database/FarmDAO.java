package harvest.database;

import harvest.model.Farm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.ui.farm.DisplayFarmSeasonController.FARM_LIST_LIVE_DATA;
import static harvest.ui.farm.DisplayFarmSeasonController.SEASON_LIST_LIVE_DATA;
import static harvest.util.Constant.*;

public class FarmDAO extends DAO{

//    public static final String TABLE_FARM = "farm";
//    public static final String COLUMN_FARM_ID = "id";
//    public static final String COLUMN_FARM_NAME = "name";
//    public static final String COLUMN_FARM_ADDRESS = "address";

    private static FarmDAO sFarmDAO = new FarmDAO();

    private FarmDAO(){}

    public static FarmDAO getInstance(){
        if (sFarmDAO == null ){
            sFarmDAO = new FarmDAO();
            return sFarmDAO;
        }
        return sFarmDAO;
    }

    //Get data farm
    public List<Farm> getFarmData() throws Exception {
        List<Farm> list = new ArrayList<>();
        String sqlStmt = "SELECT * FROM " + TABLE_FARM + " ORDER BY " + COLUMN_FARM_ID + " DESC;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Farm farm = new Farm();
                farm.setFarmId(resultSet.getInt(1));
                farm.setFarmName(resultSet.getString(2));
                farm.setFarmAddress(resultSet.getString(3));
                list.add(farm);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //Add new farm
    public boolean addFarmData(Farm farm) {
        String sqlStmt = "INSERT INTO " + TABLE_FARM + " ("
                + COLUMN_FARM_NAME + ", "
                + COLUMN_FARM_ADDRESS + ") "
                + "VALUES (?,?);";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(sqlStmt)) {
            preparedStatement.setString(1, farm.getFarmName());
            preparedStatement.setString(2, farm.getFarmAddress());
            preparedStatement.execute();
            updateFarmListByFarm(farm);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public boolean editFarmData(Farm farm) {
        String sqlStmt = "UPDATE " + TABLE_FARM + " SET "
                + COLUMN_FARM_NAME + " =?, "
                + COLUMN_FARM_ADDRESS + " =? "
                + " WHERE " + COLUMN_FARM_ID+ " = " + farm.getFarmId() + " ;";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(sqlStmt)) {
            preparedStatement.setString(1, farm.getFarmName());
            preparedStatement.setString(2, farm.getFarmAddress());
            preparedStatement.execute();
            updateFarmListByFarm(farm);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public void updateLiveData() {
        FARM_LIST_LIVE_DATA.clear();
        SEASON_LIST_LIVE_DATA.clear();
        try {
            FARM_LIST_LIVE_DATA.setAll(getFarmData());
            if (FARM_LIST_LIVE_DATA.size() > 0){
                SeasonDAO seasonDAO = SeasonDAO.getInstance();
                SEASON_LIST_LIVE_DATA.setAll(seasonDAO.getSeasonByFarm(FARM_LIST_LIVE_DATA.get(0)));
            }

        }catch (Exception  e){
            e.printStackTrace();
        }
    }

    public void updateFarmListByFarm(Farm farm) {
        FARM_LIST_LIVE_DATA.clear();
        SEASON_LIST_LIVE_DATA.clear();
        SeasonDAO seasonDAO = SeasonDAO.getInstance();
        try {
            FARM_LIST_LIVE_DATA.setAll(getFarmData());
            SEASON_LIST_LIVE_DATA.setAll(seasonDAO.getSeasonByFarm(farm));
        }catch (Exception  e){
            e.printStackTrace();
        }
    }

    public void updateSeasonListByFarm(Farm farm) {
        SEASON_LIST_LIVE_DATA.clear();
        SeasonDAO seasonDAO = SeasonDAO.getInstance();
        try {
            SEASON_LIST_LIVE_DATA.setAll(seasonDAO.getSeasonByFarm(farm));
        }catch (Exception  e){
            e.printStackTrace();
        }
    }

    /* *
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
     */

}
