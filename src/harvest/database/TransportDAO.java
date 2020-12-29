package harvest.database;

import harvest.model.Employee;
import harvest.model.Farm;
import harvest.model.Transport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static harvest.database.EmployeeDAO.*;
import static harvest.database.FarmDAO.*;
import static harvest.ui.credit.DisplayCrdTrsController.TRANSPORT_LIST_LIVE_DATA;

public class TransportDAO extends DAO{

    private static TransportDAO sTransportDAO = new TransportDAO();

    private TransportDAO(){
    }

    public static TransportDAO getInstance(){
        if (sTransportDAO == null){
            sTransportDAO = new TransportDAO();
            return sTransportDAO;
        }
        return sTransportDAO;
    }

    public static final String TABLE_TRANSPORT = "transport";
    public static final String COLUMN_TRANSPORT_ID = "id";
    public static final String COLUMN_TRANSPORT_DATE = "date";
    public static final String COLUMN_TRANSPORT_AMOUNT = "amount";
    public static final String COLUMN_TRANSPORT_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_TRANSPORT_FARM_ID = "farm_id";

    public void createTransportTable() throws SQLException {
        try {
            Statement statement = dbGetConnect().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSPORT
                    +"("+ COLUMN_TRANSPORT_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_TRANSPORT_DATE + " DATE NOT NULL, "
                    + COLUMN_TRANSPORT_AMOUNT + " REAL NOT NULL, "
                    + COLUMN_TRANSPORT_EMPLOYEE_ID + " INTEGER NOT NULL, "
                    + COLUMN_TRANSPORT_FARM_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY (" + COLUMN_TRANSPORT_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE +" (" + COLUMN_EMPLOYEE_ID + "), "
                    + "FOREIGN KEY (" + COLUMN_TRANSPORT_FARM_ID + ") REFERENCES " + TABLE_FARM +" (" + COLUMN_FARM_ID + ") )"
            );
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<Transport> getData() throws SQLException {
        List<Transport> list = new ArrayList<>();
        String selectStmt = "SELECT  "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_ID + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_DATE + ", "
                + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_AMOUNT + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_FIRST_NAME + ", "
                + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LAST_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ID + ", "
                + TABLE_FARM + "." + COLUMN_FARM_NAME + ", "
                + TABLE_FARM + "." + COLUMN_FARM_ADDRESS + " "
                + " FROM " + TABLE_TRANSPORT + " "
                + " LEFT JOIN " + TABLE_EMPLOYEE
                + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_EMPLOYEE_ID
                + " LEFT JOIN " + TABLE_FARM
                + " ON " + TABLE_FARM + "." + COLUMN_FARM_ID + " = " + TABLE_TRANSPORT + "." + COLUMN_TRANSPORT_FARM_ID
                + " ORDER BY " + COLUMN_TRANSPORT_DATE + " DESC ;";
        try(Statement statement = dbGetConnect().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(selectStmt);){
                while (resultSet.next()){
                    Transport transport = new Transport();
                    transport.setTransportId(resultSet.getInt(1));
                    transport.setTransportDate(resultSet.getDate(2));
                    transport.setTransportAmount(resultSet.getDouble(3));
                    transport.setEmployee(new Employee(resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6)));
                    transport.setFarm(new Farm(resultSet.getInt(7), resultSet.getString(8), resultSet.getString(9)));
                    list.add(transport);
                }
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }finally {
            dbDisConnect();
        }
    }


    public boolean addData(Transport transport) {
        String insertTransport = "INSERT INTO " + TABLE_TRANSPORT + " ("
                + COLUMN_TRANSPORT_DATE + ", "
                + COLUMN_TRANSPORT_AMOUNT + ", "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + ", "
                + COLUMN_TRANSPORT_FARM_ID + ") "
                + " VALUES (?,?,?,?) ";
        try (PreparedStatement preparedStatement = dbGetConnect().prepareStatement(insertTransport)) {
            preparedStatement.setDate(1, transport.getTransportDate());
            preparedStatement.setDouble(2, transport.getTransportAmount());
            preparedStatement.setInt(3, transport.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, transport.getFarm().getFarmId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbDisConnect();
        }
    }

    public boolean editData(Transport transport) {
        String updateStmt = "UPDATE " + TABLE_TRANSPORT + " SET "
                + COLUMN_TRANSPORT_DATE + " =?, "
                + COLUMN_TRANSPORT_AMOUNT + " =?, "
                + COLUMN_TRANSPORT_EMPLOYEE_ID + " =?, "
                + COLUMN_TRANSPORT_FARM_ID + " =? "
                + " WHERE " + COLUMN_TRANSPORT_ID + " = " +transport.getTransportId() + " ;";
        try(PreparedStatement preparedStatement =dbGetConnect().prepareStatement(updateStmt)) {
            preparedStatement.setDate(1, transport.getTransportDate());
            preparedStatement.setDouble(2, transport.getTransportAmount());
            preparedStatement.setInt(3, transport.getEmployee().getEmployeeId());
            preparedStatement.setInt(4, transport.getFarm().getFarmId());
            preparedStatement.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public boolean deleteDataById(int id) {
        String deleteStmt = "DELETE FROM " + TABLE_TRANSPORT + " WHERE " + COLUMN_TRANSPORT_ID + " = " + id + " ;";
        try (Statement statement = dbGetConnect().createStatement()){
            statement.execute(deleteStmt);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            dbDisConnect();
        }
    }

    public void updateLiveData() {
        TRANSPORT_LIST_LIVE_DATA.clear();
        try {
            TRANSPORT_LIST_LIVE_DATA.setAll(getData());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
