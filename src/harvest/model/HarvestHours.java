package harvest.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Time;

public class HarvestHours {
    private final IntegerProperty harvestHoursID;
    private final ObjectProperty<Date> harvestDate;
    private final ObjectProperty<Time> startMorning;
    private final ObjectProperty<Time> endMorning;
    private final ObjectProperty<Time> startNoon;
    private final ObjectProperty<Time> endNoon;
    private final LongProperty totalHours;
    private final SimpleBooleanProperty employeeStatus;
    private final StringProperty employeeFullName;
    private final SimpleBooleanProperty transportStatus;
    private final IntegerProperty employeeType;
    private final SimpleDoubleProperty creditAmount;
    private final StringProperty harvestRemarque;
    private final SimpleDoubleProperty transportAmount;
    private final IntegerProperty harvestID;
    private final SimpleIntegerProperty employeeId;
    private final SimpleIntegerProperty creditId;
    private final SimpleIntegerProperty farmId;
    private final SimpleIntegerProperty transportId;


    public HarvestHours() {
        this.harvestHoursID = new SimpleIntegerProperty();
        this.harvestDate = new SimpleObjectProperty<>();
        this.startMorning = new SimpleObjectProperty<>();
        this.endMorning = new SimpleObjectProperty<>();
        this.startNoon = new SimpleObjectProperty<>();
        this.endNoon = new SimpleObjectProperty<>();
        this.totalHours = new SimpleLongProperty();
        this.employeeFullName = new SimpleStringProperty();
        this.employeeStatus = new SimpleBooleanProperty();
        this.transportStatus = new SimpleBooleanProperty();
        this.employeeType = new SimpleIntegerProperty();
        this.transportAmount = new SimpleDoubleProperty();
        this.creditAmount = new SimpleDoubleProperty();
        this.harvestRemarque = new SimpleStringProperty();
        this.harvestID = new SimpleIntegerProperty();
        this.employeeId = new SimpleIntegerProperty();
        this.farmId = new SimpleIntegerProperty();
        this.creditId = new SimpleIntegerProperty();
        this.transportId = new SimpleIntegerProperty();

    }

    public int getHarvestHoursID() {
        return harvestHoursID.get();
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

    public void setEndNoon(Long endNoon) {

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

    public LongProperty totalHoursProperty() {
        return totalHours;
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

    public int getHarvestID() {
        return harvestID.get();
    }

    public void setHarvestID(int harvestID) {
        this.harvestID.set(harvestID);
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

    public int getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public boolean isEmployeeStatus() {
        return employeeStatus.get();
    }

    public void setEmployeeStatus(boolean employeeStatus) {
        this.employeeStatus.set(employeeStatus);
    }

    public SimpleBooleanProperty employeeStatusProperty() {
        return employeeStatus;
    }

    public String getEmployeeFullName() {
        return employeeFullName.get().toUpperCase();
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName.set(employeeFullName);
    }

    public void setEmployeeFullName(String first, String last) {
        this.employeeFullName.set(first + " " + last);
    }

    public int getCreditId() {
        return creditId.get();
    }

    public void setCreditId(int creditId) {
        this.creditId.set(creditId);
    }

    public double getCreditAmount() {
        return creditAmount.get();
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount.set(creditAmount);
    }

    public SimpleDoubleProperty creditAmountProperty() {
        return creditAmount;
    }

    public int getFarmId() {
        return farmId.get();
    }

    public void setFarmId(int farmId) {
        this.farmId.set(farmId);
    }

    public SimpleIntegerProperty farmIdProperty() {
        return farmId;
    }

    public int getTransportId() {
        return transportId.get();
    }

    public void setTransportId(int transportId) {
        this.transportId.set(transportId);
    }

    public SimpleIntegerProperty transportIdProperty() {
        return transportId;
    }

    public double getTransportAmount() {
        return transportAmount.get();
    }

    public void setTransportAmount(double transportAmount) {
        this.transportAmount.set(transportAmount);
    }

    public SimpleDoubleProperty transportAmountProperty() {
        return transportAmount;
    }
}
