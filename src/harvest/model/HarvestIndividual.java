package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class HarvestIndividual {
//
//    private final IntegerProperty harvestIndividualID;
//    private final ObjectProperty<Date> harvestDate;
//
//    private final SimpleDoubleProperty allQuantity;
//    private final SimpleDoubleProperty badQuality;
//    private final SimpleDoubleProperty goodQuality;
//    private final SimpleDoubleProperty price;
//    private final SimpleDoubleProperty netAmount;
//
//    private final SimpleBooleanProperty employeeStatus;
//    private final StringProperty employeeFullName;
//    private final SimpleBooleanProperty transportStatus;
//    private final IntegerProperty harvestType;
//    private final SimpleDoubleProperty creditAmount;
//    private final StringProperty harvestRemarque;
//    private final SimpleDoubleProperty transportAmount;
//
//    private final IntegerProperty harvestID;
//    private final SimpleIntegerProperty employeeId;
//    private final SimpleIntegerProperty creditId;
//    private final SimpleIntegerProperty farmId;
//    private final SimpleIntegerProperty transportId;
//
//
//    public HarvestIndividual() {
//        this.harvestIndividualID = new SimpleIntegerProperty();
//        this.harvestDate = new SimpleObjectProperty<>();
//        this.allQuantity = new SimpleDoubleProperty(0.0);
//        this.badQuality = new SimpleDoubleProperty(0.0);
//        this.goodQuality = new SimpleDoubleProperty();
//        this.price = new SimpleDoubleProperty(0.0);
//        this.netAmount = new SimpleDoubleProperty(0.0);
//        this.employeeFullName = new SimpleStringProperty();
//        this.employeeStatus = new SimpleBooleanProperty();
//        this.transportStatus = new SimpleBooleanProperty();
//        this.harvestType = new SimpleIntegerProperty();
//        this.transportAmount = new SimpleDoubleProperty(0.0);
//        this.creditAmount = new SimpleDoubleProperty(0.0);
//        this.harvestRemarque = new SimpleStringProperty();
//        this.harvestID = new SimpleIntegerProperty();
//        this.employeeId = new SimpleIntegerProperty();
//        this.farmId = new SimpleIntegerProperty();
//        this.creditId = new SimpleIntegerProperty();
//        this.transportId = new SimpleIntegerProperty();
//    }
//
//    public int getHarvestIndividualID() {
//        return harvestIndividualID.get();
//    }
//
//    public IntegerProperty harvestIndividualIDProperty() {
//        return harvestIndividualID;
//    }
//
//    public void setHarvestIndividualID(int harvestIndividualID) {
//        this.harvestIndividualID.set(harvestIndividualID);
//    }
//
//    public Date getHarvestDate() {
//        return harvestDate.get();
//    }
//
//    public ObjectProperty<Date> harvestDateProperty() {
//        return harvestDate;
//    }
//
//    public void setHarvestDate(Date harvestDate) {
//        this.harvestDate.set(harvestDate);
//    }
//
//    public double getAllQuantity() {
//        return allQuantity.get();
//    }
//
//    public SimpleDoubleProperty allQuantityProperty() {
//        return allQuantity;
//    }
//
//    public void setAllQuantity(double allQuantity) {
//        this.allQuantity.set(allQuantity);
//    }
//
//    public double getBadQuality() {
//        return badQuality.get();
//    }
//
//    public SimpleDoubleProperty badQualityProperty() {
//        return badQuality;
//    }
//
//    public void setBadQuality(double badQuality) {
//        this.badQuality.set(badQuality);
//    }
//
//    public double getGoodQuality() {
//        this.goodQuality.set(this.getAllQuantity() - this.getBadQuality());
//        return goodQuality.get();
//    }
//
//    public SimpleDoubleProperty goodQualityProperty() {
//        this.goodQuality.set(this.getAllQuantity() - this.getBadQuality());
//        return goodQuality;
//    }
//
//    public void setGoodQuality(double goodQuality) {
//        this.goodQuality.set(goodQuality);
//    }
//
//    public double getPrice() {
//        return price.get();
//    }
//
//    public SimpleDoubleProperty priceProperty() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price.set(price);
//    }
//
//    public double getNetAmount() {
//        return netAmount.get();
//    }
//
//    public SimpleDoubleProperty netAmountProperty() {
//        return netAmount;
//    }
//
//    public void setNetAmount(double netAmount) {
//        this.netAmount.set(netAmount);
//    }
//
//    public boolean isEmployeeStatus() {
//        return employeeStatus.get();
//    }
//
//    public SimpleBooleanProperty employeeStatusProperty() {
//        return employeeStatus;
//    }
//
//    public void setEmployeeStatus(boolean employeeStatus) {
//        this.employeeStatus.set(employeeStatus);
//    }
//
//    public String getEmployeeFullName() {
//        return employeeFullName.get();
//    }
//
//    public StringProperty employeeFullNameProperty() {
//        return employeeFullName;
//    }
//
//    public void setEmployeeFullName(String employeeFullName) {
//        this.employeeFullName.set(employeeFullName);
//    }
//
//    public boolean isTransportStatus() {
//        return transportStatus.get();
//    }
//
//    public SimpleBooleanProperty transportStatusProperty() {
//        return transportStatus;
//    }
//
//    public void setTransportStatus(boolean transportStatus) {
//        this.transportStatus.set(transportStatus);
//    }
//
//    public int getHarvestType() {
//        return harvestType.get();
//    }
//
//    public IntegerProperty harvestTypeProperty() {
//        return harvestType;
//    }
//
//    public void setHarvestType(int harvestType) {
//        this.harvestType.set(harvestType);
//    }
//
//    public double getCreditAmount() {
//        return creditAmount.get();
//    }
//
//    public SimpleDoubleProperty creditAmountProperty() {
//        return creditAmount;
//    }
//
//    public void setCreditAmount(double creditAmount) {
//        this.creditAmount.set(creditAmount);
//    }
//
//    public String getHarvestRemarque() {
//        return harvestRemarque.get();
//    }
//
//    public StringProperty harvestRemarqueProperty() {
//        return harvestRemarque;
//    }
//
//    public void setHarvestRemarque(String harvestRemarque) {
//        this.harvestRemarque.set(harvestRemarque);
//    }
//
//    public double getTransportAmount() {
//        return transportAmount.get();
//    }
//
//    public SimpleDoubleProperty transportAmountProperty() {
//        return transportAmount;
//    }
//
//    public void setTransportAmount(double transportAmount) {
//        this.transportAmount.set(transportAmount);
//    }
//
//    public int getHarvestID() {
//        return harvestID.get();
//    }
//
//    public IntegerProperty harvestIDProperty() {
//        return harvestID;
//    }
//
//    public void setHarvestID(int harvestID) {
//        this.harvestID.set(harvestID);
//    }
//
//    public int getEmployeeId() {
//        return employeeId.get();
//    }
//
//    public SimpleIntegerProperty employeeIdProperty() {
//        return employeeId;
//    }
//
//    public void setEmployeeId(int employeeId) {
//        this.employeeId.set(employeeId);
//    }
//
//    public int getCreditId() {
//        return creditId.get();
//    }
//
//    public SimpleIntegerProperty creditIdProperty() {
//        return creditId;
//    }
//
//    public void setCreditId(int creditId) {
//        this.creditId.set(creditId);
//    }
//
//    public int getFarmId() {
//        return farmId.get();
//    }
//
//    public SimpleIntegerProperty farmIdProperty() {
//        return farmId;
//    }
//
//    public void setFarmId(int farmId) {
//        this.farmId.set(farmId);
//    }
//
//    public int getTransportId() {
//        return transportId.get();
//    }
//
//    public SimpleIntegerProperty transportIdProperty() {
//        return transportId;
//    }
//
//    public void setTransportId(int transportId) {
//        this.transportId.set(transportId);
//    }
}
