package harvest.database;

import harvest.model.Hours;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static harvest.database.ConstantDAO.*;

public class CommonDAO extends DAO{

    private static CommonDAO sCommonDAO = new CommonDAO();

    private CommonDAO(){}

    public static CommonDAO getInstance(){
        if (sCommonDAO == null){
            sCommonDAO = new CommonDAO();
        }
        return sCommonDAO;
    }

    //add list of harvesters
    public boolean addHoursWork(Hours hours) {

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

        String insertHarvestHours = "INSERT INTO " + TABLE_HOURS + " ("
                + COLUMN_HOURS_DATE + ", "
                + COLUMN_HOURS_SM + ", "
                + COLUMN_HOURS_EM + ", "
                + COLUMN_HOURS_SN + ", "
                + COLUMN_HOURS_EN + ", "
                + COLUMN_HOURS_EMPLOYEE_TYPE + ", "
                + COLUMN_HOURS_EMPLOYEE_ID + ", "
                + COLUMN_HOURS_TRANSPORT_ID + ", "
                + COLUMN_HOURS_CREDIT_ID + ", "
                + COLUMN_HOURS_PRICE + ", "
                + COLUMN_HOURS_REMARQUE + ", "
                + COLUMN_HOURS_PRODUCTION_ID + ") "
                + " VALUES (?, " +
                " julianday('" + hours.getStartMorning() + "'), " +
                " julianday('" + hours.getEndMorning() + "'), " +
                " julianday('" + hours.getStartNoon() + "'), " +
                " julianday('" + hours.getEndNoon() + "'), " +
                " ?, ?, ?, ?, ?, ?, ? ) ;";
        try {
            connection = dbGetConnect();
            connection.setAutoCommit(false);

            int transportId = 0;
            int CreditId = 0;

            if (hours.isTransportStatus()){
                preparedStatement = dbGetConnect().prepareStatement(insertTransport);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getTransport().getTransportAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.setInt(4, hours.getProduction().getFarm().getFarmId());
                preparedStatement.execute();
                preparedStatement.close();
            }

            if (hours.getCreditAmount() > 0.0){
                preparedStatement = dbGetConnect().prepareStatement(insertCredit);
                preparedStatement.setDate(1, hours.getHarvestDate());
                preparedStatement.setDouble(2, hours.getCreditAmount());
                preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
                preparedStatement.execute();
                preparedStatement.close();
            }


            if (hours.isTransportStatus()){
                Statement transportStmt = connection.createStatement();
                ResultSet resultSet1 = transportStmt.executeQuery(getTransportId);
                transportId = resultSet1.getInt(1);
                transportStmt.close();
            }

            if (hours.getCreditAmount() > 0.0){
                Statement creditStmt = connection.createStatement();
                ResultSet resultSet2 = creditStmt.executeQuery(getCreditId);
                CreditId = resultSet2.getInt(1);
                creditStmt.close();
            }

            preparedStatement = connection.prepareStatement(insertHarvestHours);
            preparedStatement.setDate(1, hours.getHarvestDate());
            preparedStatement.setInt(2, hours.getEmployeeType());
            preparedStatement.setInt(3, hours.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, transportId);
            preparedStatement.setInt(5, CreditId);
            preparedStatement.setDouble(6, hours.getHourPrice());
            preparedStatement.setString(7, hours.getRemarque());
            preparedStatement.setInt(8, hours.getProduction().getProductionID());
            preparedStatement.execute();
            preparedStatement.close();

            connection.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error occurred while INSERT Operation: " + e.getMessage());
            return false;
        } finally {
            dbDisConnect();
        }
    }

}
