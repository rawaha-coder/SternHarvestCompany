package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Transport {

    private final SimpleIntegerProperty transportId = new SimpleIntegerProperty();
    private final ObjectProperty<Date> transportDate = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty transportAmount = new SimpleDoubleProperty();
    private final Employee employee = new Employee();
    private final Farm farm = new Farm();

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

    public Employee getEmployee() {
        return employee;
    }

    public Farm getFarm() {
        return farm;
    }

    public String getEmployeeName(){
        return this.employee.getEmployeeFullName();
    }

    public String getFarmName() {
        return this.farm.getFarmName();
    }

}
