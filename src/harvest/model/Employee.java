package harvest.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.sql.Date;

public class Employee {
    private final SimpleIntegerProperty employeeId;
    private final SimpleBooleanProperty employeeStatus;
    private final SimpleStringProperty employeeFirstName;
    private final SimpleStringProperty employeeLastName;
    private final SimpleStringProperty employeeFullName;
    private final ObjectProperty<Date> employeeHireDate;
    private final ObjectProperty<Date> employeeFireDate;
    private final ObjectProperty<Date> employeePermissionDate;

    public Employee() {
        this.employeeId = new SimpleIntegerProperty();
        this.employeeStatus = new SimpleBooleanProperty();
        this.employeeFirstName = new SimpleStringProperty();
        this.employeeFullName = new SimpleStringProperty();
        this.employeeLastName = new SimpleStringProperty();
        this.employeeHireDate = new SimpleObjectProperty<>();
        this.employeeFireDate = new SimpleObjectProperty<>();
        this.employeePermissionDate = new SimpleObjectProperty<>();
    }

    //Setters and getters
    public int getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public boolean isEmployeeStatus() {
        return employeeStatus.get();
    }

    public SimpleBooleanProperty employeeStatusProperty() {
        return employeeStatus;
    }

    public void setEmployeeStatus(boolean employeeStatus) {
        this.employeeStatus.set(employeeStatus);
    }

    public String getEmployeeFirstName() {
        return employeeFirstName.get();
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName.set(employeeFirstName);
    }

    public String getEmployeeLastName() {
        return employeeLastName.get();
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName.set(employeeLastName);
    }

    public String getEmployeeFullName() {
        return employeeFullName.get();
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName.set(employeeFullName);
    }

    public Date getEmployeeHireDate() {
        return employeeHireDate.get();
    }

    public void setEmployeeHireDate(Date employeeHireDate) {
        this.employeeHireDate.set(employeeHireDate);
    }

    public Date getEmployeeFireDate() {
        return employeeFireDate.get();
    }

    public void setEmployeeFireDate(Date employeeFireDate) {
        this.employeeFireDate.set(employeeFireDate);
    }

    public Date getEmployeePermissionDate() {
        return employeePermissionDate.get();
    }

    public void setEmployeePermissionDate(Date employeePermissionDate) {
        this.employeePermissionDate.set(employeePermissionDate);
    }
}
