package harvest.model;

import javafx.beans.property.*;

import java.sql.Time;
import java.time.Duration;

public class HarvestHours {
    private final IntegerProperty harvestHoursID;
    private final ObjectProperty<Time> startMorning;
    private final ObjectProperty<Time> endMorning;
    private final ObjectProperty<Time> startNoon;
    private final ObjectProperty<Time> endNoon;
    private LongProperty totalHours;
    private final StringProperty harvestRemarque;
    private final IntegerProperty employeeType;

    private Harvest mHarvest;
    private Employee mEmployee;
    private final SimpleIntegerProperty employeeId;
    private final SimpleBooleanProperty employeeStatus;
    private final StringProperty employeeFullName;
    private Credit mCredit;
    private Transport mTransport;


    public HarvestHours() {
        this.harvestHoursID = new SimpleIntegerProperty();
        this.startMorning = new SimpleObjectProperty<>();
        this.endMorning = new SimpleObjectProperty<>();
        this.startNoon = new SimpleObjectProperty<>();
        this.endNoon = new SimpleObjectProperty<>();
        this.totalHours = new SimpleLongProperty();
        this.employeeType = new SimpleIntegerProperty();
        this.harvestRemarque = new SimpleStringProperty();
        mHarvest = new Harvest();
        mEmployee = new Employee();
        this.employeeId = new SimpleIntegerProperty();
        this.employeeFullName = new SimpleStringProperty();
        this.employeeStatus = new SimpleBooleanProperty();
        mCredit = new Credit();
        mTransport = new Transport();
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

    public long getTotalHours() {
        Duration morningDuration = Duration.between(startMorning.get().toLocalTime(), endMorning.get().toLocalTime());
        Duration noonDuration = Duration.between(startNoon.get().toLocalTime(), endNoon.get().toLocalTime());
        this.totalHours = new SimpleLongProperty((Duration.ofSeconds(morningDuration.getSeconds() + noonDuration.getSeconds())).getSeconds());
        return totalHours.get();
    }

    public String getHarvestRemarque() {
        return harvestRemarque.get();
    }

    public void setHarvestRemarque(String harvestRemarque) {
        this.harvestRemarque.set(harvestRemarque);
    }

    public int getEmployeeType() {
        return employeeType.get();
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType.set(employeeType);
    }

    public Harvest getHarvest() {
        return mHarvest;
    }

    public void setHarvest(Harvest harvest){
        mHarvest = new Harvest(harvest);
    }

    public Employee getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Employee employee){
        this.mEmployee = employee;
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
        this.employeeFullName.set(this.mEmployee.getEmployeeFullName());
        return employeeFullName.get();
    }

    public StringProperty employeeFullNameProperty() {
        this.employeeFullName.set(this.mEmployee.getEmployeeFullName());
        return employeeFullName;
    }

    public Credit getCredit() {
        return mCredit;
    }

    public void setCredit(Credit credit) {
        mCredit = credit;
    }

    public Transport getTransport() {
        return mTransport;
    }

    public void setTransport(Transport transport) {
        mTransport = transport;
    }


}
