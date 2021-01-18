package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Transport {

    private final SimpleIntegerProperty transportId;
    private final ObjectProperty<Date> transportDate;
    private final SimpleDoubleProperty transportAmount;
    private Employee employee;
    private final StringProperty employeeName;
    private Farm farm;
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

    public SimpleDoubleProperty transportAmountProperty() {
        return transportAmount;
    }

    public void setTransportAmount(double transportAmount) {
        this.transportAmount.set(transportAmount);
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getEmployeeName() {
        employeeName.set(employee.getEmployeeFullName());
        return employeeName.get();
    }

    public Farm getFarm() {
        return farm;
    }

    public String getFarmName() {
        this.farmName.set(farm.getFarmName());
        return farmName.get();
    }
}
