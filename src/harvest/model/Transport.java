package harvest.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;

public class Transport {

    private final SimpleIntegerProperty transportId;
    private final ObjectProperty<Date> transportDate;
    private final Employee employee;
    private final SimpleDoubleProperty amount;
    private final Farm farm;

    public Transport() {
        this.transportId = new SimpleIntegerProperty();
        this.transportDate = new SimpleObjectProperty<>();
        this.employee = new Employee();
        this.amount = new SimpleDoubleProperty();
        this.farm = new Farm();
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

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee){
        this.employee.setEmployeeId(employee.getEmployeeId());
        this.employee.setEmployeeFirstName(employee.getEmployeeFirstName());
        this.employee.setEmployeeLastName(employee.getEmployeeLastName());
        this.employee.setEmployeeFullName(employee.getEmployeeFullName());
        this.employee.setEmployeeHireDate(employee.getEmployeeHireDate());
        this.employee.setEmployeeFireDate(employee.getEmployeeFireDate());
        this.employee.setEmployeeStatus(employee.isEmployeeStatus());
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm){
        this.farm.setFarmId(farm.getFarmId());
        this.farm.setFarmName(farm.getFarmName());
        this.farm.setFarmAddress(farm.getFarmAddress());
    }
}
