package harvest.database;

import harvest.model.Transport;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.TABLE_EMPLOYEE;
import static harvest.database.FarmDAO.COLUMN_FARM_ID;
import static harvest.database.FarmDAO.TABLE_FARM;

public class TransportDAO extends DAO{

    private static TransportDAO sTransportDAO = new TransportDAO();

    private TransportDAO(){}

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



    public List<Transport> getData() throws Exception {
        return null;
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
            preparedStatement.setDouble(2, transport.getAmount());
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


    public boolean editData(Object o) {
        return false;
    }


    public boolean deleteDataById(int Id) {
        return false;
    }


    public void updateLiveData() {

    }
}
