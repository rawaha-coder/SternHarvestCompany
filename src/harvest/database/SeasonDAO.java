package harvest.database;

import harvest.model.Farm;
import harvest.model.Season;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.ConstantDAO.*;
import static harvest.controller.DisplayFarmSeasonController.SEASON_LIST_LIVE_DATA;

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

    public List<Season> getSeasonByFarm(Farm farm) throws Exception {
        String sqlStmt = "SELECT * FROM " + TABLE_SEASON
                + " WHERE " + COLUMN_SEASON_FARM_ID + " = " + farm.getFarmId() + " "
                + " ORDER BY " + COLUMN_SEASON_DATE_HARVEST + " DESC;";
        List<Season> list = new ArrayList<>();
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Season season = new Season();
                season.setSeasonId(resultSet.getInt(1));
                season.setFarmPlantingDate(resultSet.getDate(2));
                season.setFarmHarvestDate(resultSet.getDate(3));
                season.getFarm().setFarmId(farm.getFarmId());
                season.getFarm().setFarmName(farm.getFarmName());
                season.getFarm().setFarmAddress(farm.getFarmAddress());
                list.add(season);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    public boolean addSeasonData(Season season) {
        String sqlStmt = "INSERT INTO " + TABLE_SEASON + " ("
                + COLUMN_SEASON_DATE_PLANTING + ", "
                + COLUMN_SEASON_DATE_HARVEST + ", "
                + COLUMN_SEASON_FARM_ID + ") "
                + "VALUES (?,?,?);";
        try(PreparedStatement preparedStatement = dbGetConnect().prepareStatement(sqlStmt)) {
            preparedStatement.setDate(1, season.getFarmPlantingDate());
            preparedStatement.setDate(2, season.getFarmHarvestDate());
            preparedStatement.setInt(3, season.getFarm().getFarmId());
            preparedStatement.execute();
            updateSeasonListByFarm(season.getFarm());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        }finally {
            dbDisConnect();
        }
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
            preparedStatement.setString(1, season.getFarm().getFarmName());
            preparedStatement.setString(2, season.getFarm().getFarmAddress());
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

    public boolean deleteSeasonById(int id){
        String sqlStmt = "DELETE FROM " + TABLE_SEASON + " WHERE " + COLUMN_SEASON_ID + " ="+id+";";

        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute(sqlStmt);
            statement.close();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + exception.getMessage());
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

    /* *
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
}
