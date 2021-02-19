package harvest.database;
import harvest.model.Harvest;
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
