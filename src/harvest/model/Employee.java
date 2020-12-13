package harvest.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.sql.Date;

public class Employee {
    private final SimpleIntegerProperty employeeId;
    private final SimpleBooleanProperty employeeStatus;
    private final SimpleStringProperty employeeFirstName;
    private final SimpleStringProperty employeeLastName;
    private final SimpleStringProperty employeeFullName;
    private final SimpleStringProperty employeeHireDate;
    private final SimpleStringProperty employeeFireDate;
    private final SimpleStringProperty employeePermissionDate;

    public Employee() {
        this.employeeId = new SimpleIntegerProperty();
        this.employeeStatus = new SimpleBooleanProperty();
        this.employeeFirstName = new SimpleStringProperty();
        this.employeeFullName = new SimpleStringProperty();
        this.employeeLastName = new SimpleStringProperty();
        this.employeeHireDate = new SimpleStringProperty();
        this.employeeFireDate = new SimpleStringProperty();
        this.employeePermissionDate = new SimpleStringProperty();
    }

    //Employee check box status help function
    public int getEmployeeStatus() {
        if (this.employeeStatus.get()){
            return 1;
        }else {
            return 0;
        }
    }

    public void setEmployeeStatus(int intValue) {
        boolean status = intValue == 1;
        this.employeeStatus.set(status);
    }

    //Getters
    public int getEmployeeId() {
        return employeeId.get();
    }

    public boolean isEmployeeSelected() {
        return employeeStatus.get();
    }

    public SimpleBooleanProperty employeeStatusProperty() {
        return employeeStatus;
    }

    public String getEmployeeFullName() {
        return employeeFullName.get();
    }

    public String getEmployeeFirstName() {
        return employeeFirstName.get();
    }

    public String getEmployeeLastName() {
        return employeeLastName.get();
    }

    public String getEmployeeHireDate() {
        return employeeHireDate.get();
    }

    public LocalDate getHireDate() {
        return LocalDate.parse(employeeHireDate.get());
    }

    public String getEmployeeFireDate() {
        return employeeFireDate.get();
    }

    public LocalDate getFireDate() {
        return LocalDate.parse(employeeFireDate.get());
    }

    public String getEmployeePermissionDate() {
        return employeePermissionDate.get();
    }

    public LocalDate getPermissionDate() {
        return LocalDate.parse(employeePermissionDate.get());
    }

    // Setters
    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName.set(employeeFullName);
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName.set(employeeFirstName);
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName.set(employeeLastName);
    }

    public void setEmployeeHireDate(String employeeHireDate) {
        this.employeeHireDate.set(employeeHireDate);
    }

    public void setEmployeeHireDate(Date date) {
        LocalDate localD = date.toLocalDate();
        this.employeeHireDate.set(localD.toString());
    }

    public void setEmployeeFireDate(String employeeFireDate) {
        this.employeeFireDate.set(employeeFireDate);
    }

    public void setEmployeeFireDate(Date date) {
        LocalDate localD = date.toLocalDate();
        this.employeeFireDate.set(localD.toString());
    }

    public void setEmployeePermissionDate(String employeePermissionDate) {
        this.employeePermissionDate.set(employeePermissionDate);
    }

    public void setEmployeePermissionDate(Date date) {
        LocalDate localD = date.toLocalDate();
        this.employeePermissionDate.set(localD.toString());
    }
}
