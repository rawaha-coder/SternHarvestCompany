package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Credit {

    private final SimpleIntegerProperty creditId = new SimpleIntegerProperty();
    private final ObjectProperty<Date> creditDate = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty creditAmount = new SimpleDoubleProperty();
    private final Employee employee = new Employee();

    public int getCreditId() {
        return creditId.get();
    }

    public void setCreditId(int creditId) {
        this.creditId.set(creditId);
    }

    public Date getCreditDate() {
        return creditDate.get();
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate.set(creditDate);
    }

    public double getCreditAmount() {
        return creditAmount.get();
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount.set(creditAmount);
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getEmployeeName(){
        return this.employee.getEmployeeFullName();
    }
}
