package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;

public class HarvestHours {
    private final IntegerProperty harvestHoursID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> harvestDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> startMorning = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> endMorning = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> startNoon = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> endNoon = new SimpleObjectProperty<>();
    private final LongProperty totalHours = new SimpleLongProperty();
    private final IntegerProperty employeeType = new SimpleIntegerProperty();
    private final StringProperty harvestRemarque = new SimpleStringProperty();

    private final HarvestProduction harvestProduction = new HarvestProduction();
    private final Employee employee = new Employee();
    private final Transport transport = new Transport();
    private final SimpleStringProperty transportAmount = new SimpleStringProperty();
    private final SimpleBooleanProperty transportStatus = new SimpleBooleanProperty();
    private final Credit credit = new Credit();
    private final SimpleStringProperty creditAmount = new SimpleStringProperty();

    public HarvestProduction getHarvestProduction() {
        return harvestProduction;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Transport getTransport() {
        return transport;
    }

    public String getTransportAmount() {
        this.transportAmount.set(String.valueOf(getTransport().getTransportAmount()));
        return transportAmount.get();
    }

    public Credit getCredit() {
        return credit;
    }

    public String getCreditAmount() {
        this.creditAmount.set(String.valueOf(getCredit().getCreditAmount()));
        return creditAmount.get();
    }

    public void setHarvestHoursID(int harvestHoursID) {
        this.harvestHoursID.set(harvestHoursID);
    }

    public Time getStartMorning() {
        return startMorning.get();
    }

    public void setStartMorning(Time startMorning) {
        this.startMorning.set(startMorning);
    }

    public Time getEndMorning() {
        return endMorning.get();
    }

    public void setEndMorning(Time endMorning) {
        this.endMorning.set(endMorning);
    }

    public Time getStartNoon() {
        return startNoon.get();
    }

    public void setStartNoon(Time startNoon) {
        this.startNoon.set(startNoon);
    }

    public Time getEndNoon() {
        return endNoon.get();
    }

    public void setEndNoon(Time endNoon) {
        this.endNoon.set(endNoon);
    }

    public long getTotalWorkOnMilliSeconds() {
        long totalMilliSeconds = 0;
        if (endMorning.getValue() != null
                && startMorning.getValue() != null
                && endNoon.getValue() != null
                && startNoon.getValue() != null
        ){
            totalMilliSeconds =  (endMorning.getValue().getTime() - startMorning.getValue().getTime()) + (endNoon.getValue().getTime() - startNoon.getValue().getTime());
        }
        this.totalHours.set(totalMilliSeconds);
        return totalHours.get();
    }

    public void setTotalHours(long totalHours) {
        this.totalHours.set(totalHours);
    }

    public boolean isTransportStatus() {
        return transportStatus.get();
    }

    public void setTransportStatus(boolean transportStatus) {
        this.transportStatus.set(transportStatus);
    }

    public SimpleBooleanProperty transportStatusProperty() {
        return transportStatus;
    }

    public String getHarvestRemarque() {
        return harvestRemarque.get();
    }

    public void setHarvestRemarque(String harvestRemarque) {
        this.harvestRemarque.set(harvestRemarque);
    }

    public StringProperty harvestRemarqueProperty() {
        return harvestRemarque;
    }

    public Date getHarvestDate() {
        return harvestDate.get();
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate.set(harvestDate);
    }

    public int getEmployeeType() {
        return employeeType.get();
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType.set(employeeType);
    }
}
