package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;

public class AddHarvestHours {
    private final IntegerProperty harvestHoursID;
    private final ObjectProperty<Time> startMorning;
    private final ObjectProperty<Time> endMorning;
    private final ObjectProperty<Time> startNoon;
    private final ObjectProperty<Time> endNoon;
    private final LongProperty totalHours;
    private final SimpleBooleanProperty transportStatus;
    private final IntegerProperty employeeType;
    private final StringProperty harvestRemarque;


    private final IntegerProperty harvestID;
    private final ObjectProperty<Date> harvestDate;

    private final SimpleIntegerProperty employeeId;
    private final SimpleBooleanProperty employeeStatus;
    private final StringProperty employeeFullName;

    private final SimpleIntegerProperty creditId;
    private final ObjectProperty<Date> creditDate;
    private final SimpleDoubleProperty creditAmount;

    private final SimpleIntegerProperty transportId;
    private final ObjectProperty<Date> transportDate;
    private final SimpleDoubleProperty transportAmount;

    public AddHarvestHours() {
        this.harvestHoursID = new SimpleIntegerProperty();
        this.startMorning = new SimpleObjectProperty<>();
        this.endMorning = new SimpleObjectProperty<>();
        this.startNoon = new SimpleObjectProperty<>();
        this.endNoon = new SimpleObjectProperty<>();
        this.totalHours = new SimpleLongProperty();
        this.transportStatus = new SimpleBooleanProperty();
        this.employeeType = new SimpleIntegerProperty();
        this.harvestRemarque = new SimpleStringProperty();

        this.harvestID = new SimpleIntegerProperty();
        this.harvestDate = new SimpleObjectProperty<>();

        this.employeeId = new SimpleIntegerProperty();
        this.employeeFullName = new SimpleStringProperty();
        this.employeeStatus = new SimpleBooleanProperty();

        this.creditId = new SimpleIntegerProperty();
        this.creditDate = new SimpleObjectProperty<>();
        this.creditAmount = new SimpleDoubleProperty();

        this.transportId = new SimpleIntegerProperty();
        this.transportDate = new SimpleObjectProperty<>();
        this.transportAmount = new SimpleDoubleProperty();
    }

    public int getHarvestHoursID() {
        return harvestHoursID.get();
    }

    public IntegerProperty harvestHoursIDProperty() {
        return harvestHoursID;
    }

    public void setHarvestHoursID(int harvestHoursID) {
        this.harvestHoursID.set(harvestHoursID);
    }

    public Time getStartMorning() {
        return startMorning.get();
    }

    public ObjectProperty<Time> startMorningProperty() {
        return startMorning;
    }

    public void setStartMorning(Time startMorning) {
        this.startMorning.set(startMorning);
    }

    public Time getEndMorning() {
        return endMorning.get();
    }

    public ObjectProperty<Time> endMorningProperty() {
        return endMorning;
    }

    public void setEndMorning(Time endMorning) {
        this.endMorning.set(endMorning);
    }

    public Time getStartNoon() {
        return startNoon.get();
    }

    public ObjectProperty<Time> startNoonProperty() {
        return startNoon;
    }

    public void setStartNoon(Time startNoon) {
        this.startNoon.set(startNoon);
    }

    public Time getEndNoon() {
        return endNoon.get();
    }

    public ObjectProperty<Time> endNoonProperty() {
        return endNoon;
    }

    public void setEndNoon(Time endNoon) {
        this.endNoon.set(endNoon);
    }

    public long getTotalHours() {
        return totalHours.get();
    }

    public LongProperty totalHoursProperty() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours.set(totalHours);
    }

    public boolean isTransportStatus() {
        return transportStatus.get();
    }

    public SimpleBooleanProperty transportStatusProperty() {
        return transportStatus;
    }

    public void setTransportStatus(boolean transportStatus) {
        this.transportStatus.set(transportStatus);
    }

    public String getHarvestRemarque() {
        return harvestRemarque.get();
    }

    public StringProperty harvestRemarqueProperty() {
        return harvestRemarque;
    }

    public void setHarvestRemarque(String harvestRemarque) {
        this.harvestRemarque.set(harvestRemarque);
    }

    public int getHarvestID() {
        return harvestID.get();
    }

    public IntegerProperty harvestIDProperty() {
        return harvestID;
    }

    public void setHarvestID(int harvestID) {
        this.harvestID.set(harvestID);
    }

    public Date getHarvestDate() {
        return harvestDate.get();
    }

    public ObjectProperty<Date> harvestDateProperty() {
        return harvestDate;
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate.set(harvestDate);
    }

    public int getEmployeeType() {
        return employeeType.get();
    }

    public IntegerProperty employeeTypeProperty() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType.set(employeeType);
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public SimpleIntegerProperty employeeIdProperty() {
        return employeeId;
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

    public String getEmployeeFullName() {
        return employeeFullName.get();
    }

    public StringProperty employeeFullNameProperty() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName.set(employeeFullName);
    }

    public int getCreditId() {
        return creditId.get();
    }

    public SimpleIntegerProperty creditIdProperty() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId.set(creditId);
    }

    public Date getCreditDate() {
        return creditDate.get();
    }

    public ObjectProperty<Date> creditDateProperty() {
        return creditDate;
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate.set(creditDate);
    }

    public double getCreditAmount() {
        return creditAmount.get();
    }

    public SimpleDoubleProperty creditAmountProperty() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount.set(creditAmount);
    }

    public int getTransportId() {
        return transportId.get();
    }

    public SimpleIntegerProperty transportIdProperty() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId.set(transportId);
    }

    public Date getTransportDate() {
        return transportDate.get();
    }

    public ObjectProperty<Date> transportDateProperty() {
        return transportDate;
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
}
