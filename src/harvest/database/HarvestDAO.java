package harvest.database;
import harvest.model.Harvest;
import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import static harvest.database.ConstantDAO.*;

public class HarvestDAO extends DAO {
    private static HarvestDAO sHarvestDAO = new HarvestDAO();
    private HarvestDAO() {
    }
    public static HarvestDAO getInstance() {
        if (sHarvestDAO == null) {
            sHarvestDAO = new HarvestDAO();
        }
        return sHarvestDAO;
    }

    //add list of harvesters
    public boolean addHarvesters(Harvest harvest) {
        Connection connection = null;
        PreparedStatement preparedStatement;

        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", "
                + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_NAME + ", "
                + COLUMN_TRANSPORT_FARM_ID + ", "
                + COLUMN_TRANSPORT_FARM_NAME + ") "
                + " VALUES (?,?,?,?,?,?) ";

        String getTransportId = "SELECT MAX(id) FROM " + TABLE_TRANSPORT + " ;";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_NAME + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?,?);";

        String getCreditId = "SELECT MAX(id) FROM " + TABLE_CREDIT + " ;";

        String insertHarvest = "INSERT INTO " + TABLE_HARVEST + " ("
                + COLUMN_HARVEST_DATE + ", "
                + COLUMN_HARVEST_AQ + ", "
                + COLUMN_HARVEST_BQ + ", "
                + COLUMN_HARVEST_PQ + ", "
                + COLUMN_HARVEST_GPQ + ", "
                + COLUMN_HARVEST_GQ + ", "
                + COLUMN_HARVEST_PRICE + ", "
                + COLUMN_HARVEST_EMPLOYEE_ID + ", "
                + COLUMN_HARVEST_EMPLOYEE_NAME + ", "
                + COLUMN_HARVEST_TRANSPORT_ID + ", "
                + COLUMN_HARVEST_TRANSPORT_AMOUNT + ", "
                + COLUMN_HARVEST_CREDIT_ID + ", "
                + COLUMN_HARVEST_CREDIT_AMOUNT + ", "
                + COLUMN_HARVEST_FARM_ID + ", "
                + COLUMN_HARVEST_FARM_NAME + ", "
                + COLUMN_HARVEST_NET_AMOUNT + ", "
                + COLUMN_HARVEST_HARVEST_REMARQUE + ", "
                + COLUMN_HARVEST_TYPE + ", "
                + COLUMN_HARVEST_PRODUCTION_ID + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            int transportId = 0;
            int creditId = 0;

            if (harvest.isTransportStatus()){
                System.out.println("inside the loop Transport: " +harvest.isTransportStatus());
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, harvest.getHarvestDate());
                preparedStatement.setDouble(2, harvest.getTransportAmount());
                preparedStatement.setInt(3, harvest.getEmployeeID());
                preparedStatement.setString(4, harvest.getEmployeeName());
                preparedStatement.setInt(5, harvest.getFarmID());
                preparedStatement.setString(6, harvest.getFarmName());
                preparedStatement.execute();
                preparedStatement.close();
            }


            if (harvest.getCreditAmount() > 0.0){
                System.out.println("inside the loop Credit: " + harvest.getCreditAmount());
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, harvest.getHarvestDate());
                preparedStatement.setDouble(2, harvest.getCreditAmount());
                preparedStatement.setString(3, harvest.getEmployeeName());
                preparedStatement.setInt(4, harvest.getEmployeeID());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (harvest.isTransportStatus()){
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                System.out.println("transportId " + resultSet1.getInt(1));
                statement1.close();
            }

            if (harvest.getCreditAmount() > 0.0){
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(getCreditId);
                creditId = resultSet2.getInt(1);
                System.out.println("CreditId " + resultSet2.getInt(1));
                statement2.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvest);
            preparedStatement.setDate(1, harvest.getHarvestDate());
            preparedStatement.setDouble(2, harvest.getAllQuantity());
            preparedStatement.setDouble(3, harvest.getBadQuantity());
            preparedStatement.setDouble(4, harvest.getPenaltyQuality());
            preparedStatement.setDouble(5, harvest.getGeneralPenaltyQuality());
            preparedStatement.setDouble(6, harvest.getGoodQuantity());
            preparedStatement.setDouble(7, harvest.getProductPrice());
            preparedStatement.setInt(8, harvest.getEmployeeID());
            preparedStatement.setString(9, harvest.getEmployeeName());
            preparedStatement.setInt(10, transportId);
            preparedStatement.setDouble(11, harvest.getTransportAmount());
            preparedStatement.setInt(12, creditId);
            preparedStatement.setDouble(13, harvest.getCreditAmount());
            preparedStatement.setInt(14, harvest.getFarmID());
            preparedStatement.setString(15, harvest.getFarmName());
            preparedStatement.setDouble(16, harvest.getAmountPayable());
            preparedStatement.setString(17, harvest.getRemarque());
            preparedStatement.setInt(18, harvest.getHarvestType());
            preparedStatement.setInt(19, harvest.getProductionID());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        } finally {
            dbDisConnect();
        }
    }

    public void createHarvestTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST + " ("
                + COLUMN_HARVEST_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_DATE + " DATE NOT NULL, "
                + COLUMN_HARVEST_AQ + " REAL, "
                + COLUMN_HARVEST_BQ + " REAL , "
                + COLUMN_HARVEST_PQ + " REAL, "
                + COLUMN_HARVEST_GPQ + " REAL, "
                + COLUMN_HARVEST_GQ + " REAL, "
                + COLUMN_HARVEST_PRICE + " REAL, "
                + COLUMN_HARVEST_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_EMPLOYEE_NAME + " TEXT, "
                + COLUMN_HARVEST_TRANSPORT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_TRANSPORT_AMOUNT + " REAL, "
                + COLUMN_HARVEST_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_CREDIT_AMOUNT + " REAL, "
                + COLUMN_HARVEST_FARM_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_FARM_NAME + " TEXT, "
                + COLUMN_HARVEST_NET_AMOUNT + " REAL, "
                + COLUMN_HARVEST_HARVEST_REMARQUE + " TEXT, "
                + COLUMN_HARVEST_TYPE + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_PRODUCTION_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_PRODUCTION_ID+ ") REFERENCES " + TABLE_PRODUCTION + " (" + COLUMN_PRODUCTION_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_FARM_ID + ") REFERENCES " + TABLE_FARM + " (" + COLUMN_FARM_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_CREDIT_ID + ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //*******************************
    //Get selected employees Names and ID
    //*******************************
    public ObservableList<Harvest> getHarvestData() throws Exception {
        ObservableList<Harvest> list = FXCollections.observableArrayList();
        String sqlStmt = "SELECT "
                + COLUMN_EMPLOYEE_ID + ", "
                + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + COLUMN_EMPLOYEE_LAST_NAME
                + " FROM " + TABLE_EMPLOYEE
                + " WHERE " + COLUMN_EMPLOYEE_STATUS + " = " + 1
                + " ORDER BY " + COLUMN_EMPLOYEE_FIRST_NAME + " ASC;";

        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Harvest harvest = new Harvest();
                harvest.setEmployeeID(resultSet.getInt(1));
                harvest.setEmployeeName(resultSet.getString(2) + " " + resultSet.getString(3));
                list.add(harvest);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }
}
