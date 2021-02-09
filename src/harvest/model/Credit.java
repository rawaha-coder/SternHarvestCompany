package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Credit {

    private final SimpleIntegerProperty creditId;
    private final ObjectProperty<Date> creditDate;
    private final SimpleDoubleProperty creditAmount;
    private final SimpleIntegerProperty employeeId;
    private final SimpleStringProperty employeeName;

    public Credit() {
        this.creditId = new SimpleIntegerProperty();
        this.creditDate = new SimpleObjectProperty<>();
        this.creditAmount = new SimpleDoubleProperty();
        this.employeeId = new SimpleIntegerProperty();
        this.employeeName = new SimpleStringProperty();
    }

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

    public int getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getEmployeeName() {
        return employeeName.get().toUpperCase();
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }
}
