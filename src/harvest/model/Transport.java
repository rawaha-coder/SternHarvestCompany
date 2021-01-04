package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Transport {

    private final SimpleIntegerProperty transportId;
    private final ObjectProperty<Date> transportDate;
    private Employee employee;
    private final StringProperty employeeName;
    private final SimpleDoubleProperty transportAmount;
    private final Farm farm;
    private final StringProperty farmName;

    public Transport() {
        this.transportId = new SimpleIntegerProperty();
        this.transportDate = new SimpleObjectProperty<>();
        this.employee = new Employee();
        this.employeeName = new SimpleStringProperty();
        this.transportAmount = new SimpleDoubleProperty();
        this.farm = new Farm();
        this.farmName = new SimpleStringProperty();
    }

    public int getTransportId() {
        return transportId.get();
    }

    public void setTransportId(int transportId) {
        this.transportId.set(transportId);
    }

    public Date getTransportDate() {
        return transportDate.get();
    }

    public void setTransportDate(Date transportDate) {
        this.transportDate.set(transportDate);
    }

    public double getTransportAmount() {
        return transportAmount.get();
    }

    public void setTransportAmount(double transportAmount) {
        this.transportAmount.set(transportAmount);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public StringProperty employeeNameProperty() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public String getFarmName() {
        return farmName.get();
    }

    public StringProperty farmNameProperty() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
    }

    public Employee getEmployee() {
        return employee;
    }


    public void setEmployee(Employee employee){
        this.employee = employee;
//        this.employee.setEmployeeId(employee.getEmployeeId());
//        this.employee.setEmployeeFirstName(employee.getEmployeeFirstName());
//        this.employee.setEmployeeLastName(employee.getEmployeeLastName());
//        this.employee.setEmployeeFullName(employee.getEmployeeFullName());
//        this.employee.setEmployeeHireDate(employee.getEmployeeHireDate());
//        this.employee.setEmployeeFireDate(employee.getEmployeeFireDate());
//        this.employee.setEmployeeStatus(employee.isEmployeeStatus());
//        this.employeeName.set(employee.getEmployeeFullName());
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm){
        this.farm.setFarmId(farm.getFarmId());
        this.farm.setFarmName(farm.getFarmName());
        this.farm.setFarmAddress(farm.getFarmAddress());
        this.farmName.set(farm.getFarmName());
    }
}
