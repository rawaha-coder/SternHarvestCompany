package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;

public class Credit {

    private final SimpleIntegerProperty creditId;
    private final ObjectProperty<Date> creditDate;
    private final SimpleDoubleProperty creditAmount;
    private final SimpleIntegerProperty employeeId;
    private final SimpleStringProperty creditEmployee;

    public Credit() {
        this.creditId = new SimpleIntegerProperty();
        this.creditDate = new SimpleObjectProperty<>();
        this.creditAmount = new SimpleDoubleProperty();
        this.employeeId = new SimpleIntegerProperty();
        this.creditEmployee = new SimpleStringProperty();
    }

    public int getCreditId() {
        return creditId.get();
    }

    public SimpleIntegerProperty creditIdProperty() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId.set(creditId);
    }

    public Date getCreditDate() {
        return creditDate.get();
    }

    public ObjectProperty<Date> creditDateProperty() {
        return creditDate;
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate.set(creditDate);
    }

//    public void setCreditDate(LocalDate localDate) {
//        this.creditDate.set(Date.valueOf(localDate));
//    }

    public double getCreditAmount() {
        return creditAmount.get();
    }

    public SimpleDoubleProperty creditAmountProperty() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount.set(creditAmount);
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public SimpleIntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getCreditEmployee() {
        return creditEmployee.get();
    }

    public SimpleStringProperty creditEmployeeProperty() {
        return creditEmployee;
    }

    public void setCreditEmployee(String creditEmployee) {
        this.creditEmployee.set(creditEmployee);
    }
}
