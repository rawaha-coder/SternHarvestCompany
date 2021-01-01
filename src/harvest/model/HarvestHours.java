package harvest.model;

import javafx.beans.property.*;

import java.sql.Time;

public class HarvestHours {
    private final IntegerProperty harvestHoursID;
    private Harvest mHarvest;
    private final Employee mEmployee;
    private final ObjectProperty<Time> startMorning;
    private final ObjectProperty<Time> endMorning;
    private final ObjectProperty<Time> startNoon;
    private final ObjectProperty<Time> EndNoon;
    private final DoubleProperty totalHoursWork;
    private Credit mCredit;
    private final Transport mTransport;
    private final DoubleProperty workingDayAdvance;
    private final StringProperty workingDayRemarque;

    public HarvestHours() {
        this.harvestHoursID = new SimpleIntegerProperty();
        mHarvest = new Harvest();
        mEmployee = new Employee();
        this.startMorning = new SimpleObjectProperty<>();
        this.endMorning = new SimpleObjectProperty<>();
        this.startNoon = new SimpleObjectProperty<>();
        this.EndNoon = new SimpleObjectProperty<>();
        this.totalHoursWork = new SimpleDoubleProperty();
        mCredit = new Credit();
        mTransport = new Transport();
        this.workingDayAdvance = new SimpleDoubleProperty();
        this.workingDayRemarque = new SimpleStringProperty();
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

    public Harvest getHarvest() {
        return mHarvest;
    }

    public void setHarvest(Harvest harvest){
        mHarvest = new Harvest(harvest);
    }

    public Employee getEmployee() {
        return mEmployee;
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
        return EndNoon.get();
    }

    public ObjectProperty<Time> endNoonProperty() {
        return EndNoon;
    }

    public void setEndNoon(Time endNoon) {
        this.EndNoon.set(endNoon);
    }

    public double getTotalHoursWork() {
        return totalHoursWork.get();
    }

    public DoubleProperty totalHoursWorkProperty() {
        return totalHoursWork;
    }

    public void setTotalHoursWork(double totalHoursWork) {
        this.totalHoursWork.set(totalHoursWork);
    }

    public Transport getTransport() {
        return mTransport;
    }

    public double getWorkingDayAdvance() {
        return workingDayAdvance.get();
    }

    public DoubleProperty workingDayAdvanceProperty() {
        return workingDayAdvance;
    }

    public void setWorkingDayAdvance(double workingDayAdvance) {
        this.workingDayAdvance.set(workingDayAdvance);
    }

    public String getWorkingDayRemarque() {
        return workingDayRemarque.get();
    }

    public StringProperty workingDayRemarqueProperty() {
        return workingDayRemarque;
    }

    public void setWorkingDayRemarque(String workingDayRemarque) {
        this.workingDayRemarque.set(workingDayRemarque);
    }
}
