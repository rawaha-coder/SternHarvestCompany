package harvest.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static harvest.database.EmployeeDAO.COLUMN_EMPLOYEE_ID;
import static harvest.database.EmployeeDAO.TABLE_EMPLOYEE;

public class TransportDAO extends DAO implements DAOList{

//
//    public static final String TABLE_TRANSPORT = "transport";
//    public static final String COLUMN_TRANSPORT_ID = "id";
//    public static final String COLUMN_TRANSPORT_DATE = "date";
//    public static final String COLUMN_TRANSPORT_AMOUNT = "amount";
//    public static final String COLUMN_TRANSPORT_EMPLOYEE_ID = "employee_id";
//    public static final String COLUMN_TRANSPORT_FARM_ID = "farm_id";
//
//    public void createTransportTable() throws SQLException {
//        try {
//            Statement statement = dbGetConnect().createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSPORT
//                    +"("+ COLUMN_TRANSPORT_ID + " INTEGER PRIMARY KEY, "
//                    + COLUMN_TRANSPORT_DATE + " DATE NOT NULL, "
//                    + COLUMN_TRANSPORT_AMOUNT + " REAL NOT NULL, "
//                    + COLUMN_TRANSPORT_EMPLOYEE_ID + " INTEGER NOT NULL, "
//                    + COLUMN_TRANSPORT_FARM_ID + " INTEGER NOT NULL, "
//                    + "FOREIGN KEY (" + COLUMN_TRANSPORT_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE +" (" + COLUMN_EMPLOYEE_ID + "), "
//                    + "FOREIGN KEY (" + COLUMN_TRANSPORT_FARM_ID + ") REFERENCES " + TABLE_FARM +" (" + COLUMN_FARM_ID + ") )"
//            );
//        }catch (SQLException e){
//            e.printStackTrace();
//            throw e;
//        }
//    }
//

    @Override
    public List getData() throws Exception {
        return null;
    }

    @Override
    public boolean addData(Object o) {
        return false;
    }

    @Override
    public boolean editData(Object o) {
        return false;
    }

    @Override
    public boolean deleteDataById(int Id) {
        return false;
    }

    @Override
    public void updateLiveData() {

    }
}
