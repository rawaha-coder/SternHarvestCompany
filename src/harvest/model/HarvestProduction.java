package harvest.model;

import javafx.beans.property.*;

public class HarvestProduction {
    private final IntegerProperty harvestProductionID;
    private Harvest mHarvest;
    private final IntegerProperty harvestID;
    private final LongProperty harvestProductionTotalHours;
    private final DoubleProperty harvestProductionTotalAmount;
    private final DoubleProperty harvestProductionTotalQuantity;
    private final DoubleProperty harvestProductionPrice1;
    private final DoubleProperty harvestProductionPrice2;
    private final IntegerProperty harvestProductionTotalEmployee;
    private final DoubleProperty harvestProductionTotalCredit;
    private final DoubleProperty harvestProductionTotalTransport;

    public HarvestProduction() {
        this.harvestProductionID = new SimpleIntegerProperty();
        this.harvestProductionTotalHours = new SimpleLongProperty();
        this.harvestProductionTotalAmount = new SimpleDoubleProperty();
        this.harvestProductionTotalQuantity = new SimpleDoubleProperty();
        this.harvestProductionPrice1 = new SimpleDoubleProperty();
        this.harvestProductionPrice2 = new SimpleDoubleProperty();
        this.harvestProductionTotalEmployee = new SimpleIntegerProperty();
        this.harvestProductionTotalCredit = new SimpleDoubleProperty();
        this.harvestProductionTotalTransport = new SimpleDoubleProperty();
        this.mHarvest = new Harvest();
        this.harvestID = new SimpleIntegerProperty();
    }

    public int getHarvestProductionID() {
        return harvestProductionID.get();
    }

    public IntegerProperty harvestProductionIDProperty() {
        return harvestProductionID;
    }

    public void setHarvestProductionID(int harvestProductionID) {
        this.harvestProductionID.set(harvestProductionID);
    }

    public long getHarvestProductionTotalHours() {
        return harvestProductionTotalHours.get();
    }

    public LongProperty harvestProductionTotalHoursProperty() {
        return harvestProductionTotalHours;
    }

    public void setHarvestProductionTotalHours(long harvestProductionTotalHours) {
        this.harvestProductionTotalHours.set(harvestProductionTotalHours);
    }

    public double getHarvestProductionTotalAmount() {
        return harvestProductionTotalAmount.get();
    }

    public DoubleProperty harvestProductionTotalAmountProperty() {
        return harvestProductionTotalAmount;
    }

    public void setHarvestProductionTotalAmount(double harvestProductionTotalAmount) {
        this.harvestProductionTotalAmount.set(harvestProductionTotalAmount);
    }

    public double getHarvestProductionTotalQuantity() {
        return harvestProductionTotalQuantity.get();
    }

    public DoubleProperty harvestProductionTotalQuantityProperty() {
        return harvestProductionTotalQuantity;
    }

    public void setHarvestProductionTotalQuantity(double harvestProductionTotalQuantity) {
        this.harvestProductionTotalQuantity.set(harvestProductionTotalQuantity);
    }

    public double getHarvestProductionPrice1() {
        return harvestProductionPrice1.get();
    }

    public DoubleProperty harvestProductionPrice1Property() {
        return harvestProductionPrice1;
    }

    public void setHarvestProductionPrice1(double harvestProductionPrice1) {
        this.harvestProductionPrice1.set(harvestProductionPrice1);
    }

    public double getHarvestProductionPrice2() {
        return harvestProductionPrice2.get();
    }

    public DoubleProperty harvestProductionPrice2Property() {
        return harvestProductionPrice2;
    }

    public void setHarvestProductionPrice2(double harvestProductionPrice2) {
        this.harvestProductionPrice2.set(harvestProductionPrice2);
    }

    public int getHarvestProductionTotalEmployee() {
        return harvestProductionTotalEmployee.get();
    }

    public IntegerProperty harvestProductionTotalEmployeeProperty() {
        return harvestProductionTotalEmployee;
    }

    public void setHarvestProductionTotalEmployee(int harvestProductionTotalEmployee) {
        this.harvestProductionTotalEmployee.set(harvestProductionTotalEmployee);
    }

    public double getHarvestProductionTotalCredit() {
        return harvestProductionTotalCredit.get();
    }

    public DoubleProperty harvestProductionTotalCreditProperty() {
        return harvestProductionTotalCredit;
    }

    public void setHarvestProductionTotalCredit(double harvestProductionTotalCredit) {
        this.harvestProductionTotalCredit.set(harvestProductionTotalCredit);
    }

    public double getHarvestProductionTotalTransport() {
        return harvestProductionTotalTransport.get();
    }

    public DoubleProperty harvestProductionTotalTransportProperty() {
        return harvestProductionTotalTransport;
    }

    public void setHarvestProductionTotalTransport(double harvestProductionTotalTransport) {
        this.harvestProductionTotalTransport.set(harvestProductionTotalTransport);
    }

    public Harvest getHarvest() {
        return mHarvest;
    }

    public void setHarvest(Harvest harvest) {
        mHarvest = harvest;
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
}
