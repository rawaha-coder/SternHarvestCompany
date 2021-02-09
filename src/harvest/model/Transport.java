package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Transport {

    private final SimpleIntegerProperty transportId;
    private final ObjectProperty<Date> transportDate;
    private final SimpleDoubleProperty transportAmount;
    private final SimpleIntegerProperty employeeId;
    private final StringProperty employeeName;
    private final SimpleIntegerProperty farmId;
    private final StringProperty farmName;

    public Transport() {
        this.transportId = new SimpleIntegerProperty();
        this.transportDate = new SimpleObjectProperty<>();
        this.employeeId = new SimpleIntegerProperty();
        this.employeeName = new SimpleStringProperty();
        this.transportAmount = new SimpleDoubleProperty();
        this.farmId = new SimpleIntegerProperty();
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
        return employeeName.get().toUpperCase();
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public String getFarmName() {
        return farmName.get().toUpperCase();
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public int getFarmId() {
        return farmId.get();
    }

    public void setFarmId(int farmId) {
        this.farmId.set(farmId);
    }
}
