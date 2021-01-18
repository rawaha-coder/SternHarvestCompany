package harvest.database;

import harvest.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static harvest.util.Constant.*;
import static harvest.database.HarvestDAO.COLUMN_HARVEST_ID;
import static harvest.database.HarvestDAO.TABLE_HARVEST;
import static harvest.database.TransportDAO.*;
import static harvest.ui.harvest.SetHarvestWork.HARVEST_WORK_LIVE_LIST;

public class HarvestWorkDAO extends DAO {

    public static final String TABLE_HARVEST_INDIVIDUAL = "harvest_individual";
    public static final String COLUMN_HARVEST_INDIVIDUAL_ID = "id";
    public static final String COLUMN_HARVEST_INDIVIDUAL_DATE = "date";
    public static final String COLUMN_HARVEST_INDIVIDUAL_AQ = "all_quantity";
    public static final String COLUMN_HARVEST_INDIVIDUAL_BQ = "bad_quantity";
    public static final String COLUMN_HARVEST_INDIVIDUAL_GQ = "good_quantity";
    public static final String COLUMN_HARVEST_INDIVIDUAL_PRICE = "price";
    public static final String COLUMN_HARVEST_INDIVIDUAL_NET_AMOUNT = "net_amount";
    public static final String COLUMN_HARVEST_INDIVIDUAL_HARVEST_ID = "harvest_id";
    public static final String COLUMN_HARVEST_WORK_TYPE = "harvest_type";
    public static final String COLUMN_HARVEST_INDIVIDUAL_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_HARVEST_INDIVIDUAL_CREDIT_ID = "credit_id";
    public static final String COLUMN_HARVEST_INDIVIDUAL_TRANSPORT_ID = "transport_id";
    public static final String COLUMN_HARVEST_REMARQUE = "remarque";

    private static HarvestWorkDAO sHarvestWorkDAO = new HarvestWorkDAO();

    //private constructor
    private HarvestWorkDAO() {
    }

    public static HarvestWorkDAO getInstance() {
        if (sHarvestWorkDAO == null) {
            sHarvestWorkDAO = new HarvestWorkDAO();
            return sHarvestWorkDAO;
        }
        return sHarvestWorkDAO;
    }


    public void createHarvestTable() throws SQLException {
        String createStmt = "CREATE TABLE IF NOT EXISTS " + TABLE_HARVEST_INDIVIDUAL + " ("
                + COLUMN_HARVEST_INDIVIDUAL_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HARVEST_INDIVIDUAL_DATE + " DATE NOT NULL, "
                + COLUMN_HARVEST_INDIVIDUAL_AQ + " REAL, "
                + COLUMN_HARVEST_INDIVIDUAL_BQ + " REAL , "
                + COLUMN_HARVEST_INDIVIDUAL_GQ + " REAL, "
                + COLUMN_HARVEST_INDIVIDUAL_PRICE + " REAL, "
                + COLUMN_HARVEST_INDIVIDUAL_NET_AMOUNT + " REAL, "
                + COLUMN_HARVEST_WORK_TYPE + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_REMARQUE + " TEXT, "
                + COLUMN_HARVEST_INDIVIDUAL_HARVEST_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_INDIVIDUAL_EMPLOYEE_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_INDIVIDUAL_CREDIT_ID + " INTEGER NOT NULL, "
                + COLUMN_HARVEST_INDIVIDUAL_TRANSPORT_ID + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + COLUMN_HARVEST_INDIVIDUAL_HARVEST_ID + ") REFERENCES " + TABLE_HARVEST + " (" + COLUMN_HARVEST_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_INDIVIDUAL_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_INDIVIDUAL_CREDIT_ID + ") REFERENCES " + TABLE_CREDIT + " (" + COLUMN_CREDIT_ID + ")"
                + " FOREIGN KEY (" + COLUMN_HARVEST_INDIVIDUAL_TRANSPORT_ID + ") REFERENCES " + TABLE_TRANSPORT + " (" + COLUMN_TRANSPORT_ID + ")"
                + ");";
        try (Statement statement = dbGetConnect().createStatement()) {
            statement.execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<HarvestWork> getData(Date date) throws SQLException{
        Statement statement;
        ResultSet resultSet;
        String select = "SELECT "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_ID + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_DATE + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_AQ + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_BQ + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_PRICE + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_NET_AMOUNT + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_WORK_TYPE + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_REMARQUE + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME  + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + " "
                + " FROM " + TABLE_HARVEST_INDIVIDUAL + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID  + " = " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_EMPLOYEE_ID + " "
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID  + " = " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_TRANSPORT_ID + " "
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_CREDIT_ID + " "
                + " WHERE " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_DATE + " DESC ;";
        try{
            statement = dbGetConnect().createStatement();
            resultSet = statement.executeQuery(select);
            return getDataFromResultSet(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private ObservableList<HarvestWork> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<HarvestWork> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            HarvestWork harvestWork = new HarvestWork();

            list.add(harvestWork);
        }
        return list;
    }


    //*************************************************************
    //Observe Live Data
    //*************************************************************
    public void updateLiveData(Date date) {
        HARVEST_WORK_LIVE_LIST.clear();
        try {
            HARVEST_WORK_LIVE_LIST.setAll(getData(date));
            System.out.println(" Update list size: " + HARVEST_WORK_LIVE_LIST.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //add list of harvesters
    public boolean addHarvesters(HarvestWork harvestWork) {

        Connection connection;
        PreparedStatement preparedStatement;

        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", "
                + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", "
                + COLUMN_TRANSPORT_FARM_ID + ") "
                + " VALUES (?,?,?,?) ";

        String getTransportId = "SELECT MAX(id) FROM " + TABLE_TRANSPORT + " ;";

        String insertCredit = "INSERT INTO " + TABLE_CREDIT + " ("
                + COLUMN_CREDIT_DATE + ", "
                + COLUMN_CREDIT_AMOUNT + ", "
                + COLUMN_CREDIT_EMPLOYEE_ID + ") "
                + "VALUES (?,?,?);";

        String getCreditId = "SELECT MAX(id) FROM " + TABLE_CREDIT + " ;";

        String insertHarvestINDIVIDUAL = "INSERT INTO " + TABLE_HARVEST_INDIVIDUAL + " ("
                + COLUMN_HARVEST_INDIVIDUAL_DATE + ", "
                + COLUMN_HARVEST_INDIVIDUAL_AQ + ", "
                + COLUMN_HARVEST_INDIVIDUAL_BQ + ", "
                + COLUMN_HARVEST_INDIVIDUAL_GQ + ", "
                + COLUMN_HARVEST_INDIVIDUAL_PRICE + ", "
                + COLUMN_HARVEST_INDIVIDUAL_NET_AMOUNT + ", "
                + COLUMN_HARVEST_WORK_TYPE + ", "
                + COLUMN_HARVEST_REMARQUE + ", "
                + COLUMN_HARVEST_INDIVIDUAL_HARVEST_ID + ", "
                + COLUMN_HARVEST_INDIVIDUAL_EMPLOYEE_ID + ", "
                + COLUMN_HARVEST_INDIVIDUAL_CREDIT_ID + ", "
                + COLUMN_HARVEST_INDIVIDUAL_TRANSPORT_ID + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            int transportId = 0;
            int CreditId = 0;

            if (harvestWork.isTransportStatus()){
                System.out.println("inside the loop Transport: " +harvestWork.isTransportStatus());
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, harvestWork.getHarvestDate());
                preparedStatement.setDouble(2, harvestWork.getTransportAmount());
                preparedStatement.setInt(3, harvestWork.getEmployee().getEmployeeId());
                preparedStatement.setInt(4, harvestWork.getFarm().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }


            if (harvestWork.getCreditAmount() > 0.0){
                System.out.println("inside the loop Credit: " + harvestWork.getCreditAmount());
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, harvestWork.getHarvestDate());
                preparedStatement.setDouble(2, harvestWork.getCreditAmount());
                preparedStatement.setInt(3, harvestWork.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (harvestWork.isTransportStatus()){
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                System.out.println("transportId " + resultSet1.getInt(1));
                statement1.close();
            }

            if (harvestWork.getCreditAmount() > 0.0){
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                System.out.println("CreditId " + resultSet2.getInt(1));
                statement2.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvestINDIVIDUAL);
            preparedStatement.setDate(1, harvestWork.getHarvestDate());
            preparedStatement.setDouble(2, harvestWork.getAllQuantity());
            preparedStatement.setDouble(3, harvestWork.getBadQuality());
            preparedStatement.setDouble(4, harvestWork.getGoodQuality());
            preparedStatement.setDouble(5, harvestWork.getProductPrice());
            preparedStatement.setDouble(6, harvestWork.getNetAmount());
            preparedStatement.setInt(7, harvestWork.getHarvestType());
            preparedStatement.setString(8, harvestWork.getHarvestRemarque());
            preparedStatement.setInt(9, harvestWork.getHarvestProductionID());
            preparedStatement.setInt(10, harvestWork.getEmployee().getEmployeeId());
            preparedStatement.setInt(11, CreditId);
            preparedStatement.setInt(12, transportId);
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get all employees data
    //*******************************
    public ObservableList<HarvestWork> getHarvestWorkData() throws Exception {
        ObservableList<HarvestWork> harvestWorks = FXCollections.observableArrayList();
        String sqlStmt = "SELECT * FROM " + TABLE_EMPLOYEE + " ORDER BY " + COLUMN_EMPLOYEE_ID + " DESC;";

        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(sqlStmt)) {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt(1));
                employee.setEmployeeStatus(resultSet.getBoolean(2));
                employee.setEmployeeFirstName(resultSet.getString(3));
                employee.setEmployeeLastName(resultSet.getString(4));
                HarvestWork harvestWork = new HarvestWork();
                harvestWork.setEmployee(employee);
                harvestWorks.add(harvestWork);
            }
            return harvestWorks;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    //*******************************
    //Get all employees data
    //*******************************
    public ObservableList<HarvestWork> getHarvestWorkData(Date date) throws Exception {
        String select = "SELECT "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_ID + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_DATE + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_AQ + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_BQ + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_PRICE + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_NET_AMOUNT + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_WORK_TYPE + ", "
                + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_REMARQUE + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME  + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + ", "
                + TABLE_CREDIT + "." + COLUMN_CREDIT_AMOUNT + " "
                + " FROM " + TABLE_HARVEST_INDIVIDUAL + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE + " "
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID  + " = " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_EMPLOYEE_ID + " "
                + " LEFT JOIN " + TABLE_TRANSPORT + " "
                + " ON " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID  + " = " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_TRANSPORT_ID + " "
                + " LEFT JOIN " + TABLE_CREDIT + " "
                + " ON " + TABLE_CREDIT + "." + COLUMN_CREDIT_ID + " = " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_CREDIT_ID + " "
                + " WHERE " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_DATE + " = " + date.getTime() + " "
                + " ORDER BY " + TABLE_HARVEST_INDIVIDUAL + "." + COLUMN_HARVEST_INDIVIDUAL_DATE + " DESC ;";
        try(Statement statement = dbGetConnect().createStatement(); ResultSet resultSet = statement.executeQuery(select)) {
            return getHarvestWorkDataFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            dbDisConnect();
        }
    }

    private ObservableList<HarvestWork> getHarvestWorkDataFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<HarvestWork> list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            HarvestWork harvestWork = new HarvestWork();
            harvestWork.setHarvestWorkID(resultSet.getInt(1));
            harvestWork.setHarvestDate(resultSet.getDate(2));
            harvestWork.setAllQuantity(resultSet.getDouble(3));
            harvestWork.setBadQuality(resultSet.getDouble(4));
            harvestWork.setProductPrice(resultSet.getDouble(5));
            harvestWork.setNetAmount(resultSet.getDouble(6));
            harvestWork.setHarvestType(resultSet.getInt(7));
            harvestWork.setHarvestRemarque(resultSet.getString(8));
            harvestWork.setEmployee(new Employee(resultSet.getInt(9), resultSet.getString(10), resultSet.getString(11)));
            Transport transport = new Transport();
            transport.setTransportId(resultSet.getInt(12));
            transport.setTransportAmount(resultSet.getDouble(13));
            harvestWork.setTransport(transport);
            Credit credit = new Credit();
            credit.setCreditId(resultSet.getInt(14));
            credit.setCreditAmount(resultSet.getDouble(15));
            harvestWork.setCredit(credit);
            list.add(harvestWork);
        }
        return list;
    }

}
