package harvest.model;

import javafx.beans.property.*;
import java.sql.Date;

public class Employee {
    private final SimpleIntegerProperty employeeId = new SimpleIntegerProperty();
    private final SimpleBooleanProperty employeeStatus = new SimpleBooleanProperty();
    private final SimpleStringProperty employeeFirstName = new SimpleStringProperty();
    private final SimpleStringProperty employeeLastName = new SimpleStringProperty();
    private final SimpleStringProperty employeeFullName = new SimpleStringProperty();
    private final ObjectProperty<Date> employeeHireDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> employeeFireDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> employeePermissionDate = new SimpleObjectProperty<>();

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
        return employeeFirstName.get().toUpperCase();
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName.set(employeeFirstName);
    }

    public String getEmployeeLastName() {
        return employeeLastName.get().toUpperCase();
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName.set(employeeLastName);
    }

    public String getEmployeeFullName() {
        this.employeeFullName.set(getEmployeeFirstName() + " " + getEmployeeLastName());
        return employeeFullName.get().toUpperCase();
    }

    public SimpleStringProperty employeeFullNameProperty() {
        this.employeeFullName.set((getEmployeeFirstName() + " " + getEmployeeLastName()).toUpperCase());
        return employeeFullName;
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
