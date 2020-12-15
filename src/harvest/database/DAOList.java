package harvest.database;

import java.sql.SQLException;
import java.util.List;

public interface DAOList<T> {
    List<T> getData() throws Exception;
    boolean addData(T t);
    boolean editData(T t);
    boolean deleteDataById(int Id);
    void updateLiveData();

}
