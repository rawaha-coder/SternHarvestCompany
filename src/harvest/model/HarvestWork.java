package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class HarvestWork {
    private final IntegerProperty harvestWorkID;
    private final ObjectProperty<Date> harvestDate;
    private final SimpleIntegerProperty harvestType;
    private final SimpleDoubleProperty allQuantity;
    private final SimpleDoubleProperty badQuality;
    private final SimpleDoubleProperty goodQuality;
    private final SimpleDoubleProperty productPrice;
    private final SimpleDoubleProperty netAmount;
    private final SimpleStringProperty harvestRemarque;

    private final SimpleIntegerProperty harvestID;

    private final SimpleIntegerProperty employeeId;
    private final SimpleBooleanProperty employeeStatus;
    private final SimpleStringProperty employeeFullName;

    private final SimpleIntegerProperty transportId;
    private final SimpleBooleanProperty transportStatus;
    private final SimpleDoubleProperty transportAmount;

    private final SimpleIntegerProperty creditId;
    private final SimpleDoubleProperty creditAmount;

    private final SimpleIntegerProperty farmId;
    private final SimpleStringProperty farmName;

    public HarvestWork() {
        this.harvestWorkID = new SimpleIntegerProperty();
        this.harvestDate = new SimpleObjectProperty<>();
        this.harvestType = new SimpleIntegerProperty();
        this.allQuantity = new SimpleDoubleProperty();
        this.badQuality =new SimpleDoubleProperty();
        this.goodQuality = new SimpleDoubleProperty();
        this.productPrice = new SimpleDoubleProperty();
        this.netAmount = new SimpleDoubleProperty();
        this.harvestRemarque = new SimpleStringProperty();
        this.harvestID = new SimpleIntegerProperty();
        this.employeeId = new SimpleIntegerProperty();
        this.employeeStatus = new SimpleBooleanProperty();
        this.employeeFullName = new SimpleStringProperty();
        this.transportId = new SimpleIntegerProperty();
        this.transportStatus = new SimpleBooleanProperty();
        this.transportAmount = new SimpleDoubleProperty();
        this.creditId = new SimpleIntegerProperty();
        this.creditAmount = new SimpleDoubleProperty();
        this.farmId = new SimpleIntegerProperty();
        this.farmName = new SimpleStringProperty();
    }

    public int getHarvestWorkID() {
        return harvestWorkID.get();
    }

    public IntegerProperty harvestWorkIDProperty() {
        return harvestWorkID;
    }

    public void setHarvestWorkID(int harvestWorkID) {
        this.harvestWorkID.set(harvestWorkID);
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

    public int getHarvestType() {
        return harvestType.get();
    }

    public SimpleIntegerProperty harvestTypeProperty() {
        return harvestType;
    }

    public void setHarvestType(int harvestType) {
        this.harvestType.set(harvestType);
    }

    public double getAllQuantity() {
        return allQuantity.get();
    }

    public SimpleDoubleProperty allQuantityProperty() {
        return allQuantity;
    }

    public void setAllQuantity(double allQuantity) {
        this.allQuantity.set(allQuantity);
    }

    public double getBadQuality() {
        return badQuality.get();
    }

    public SimpleDoubleProperty badQualityProperty() {
        return badQuality;
    }

    public void setBadQuality(double badQuality) {
        this.badQuality.set(badQuality);
    }

    public double getGoodQuality() {
        return goodQuality.get();
    }

    public SimpleDoubleProperty goodQualityProperty() {
        return goodQuality;
    }

    public void setGoodQuality(double goodQuality) {
        this.goodQuality.set(goodQuality);
    }

    public double getProductPrice() {
        return productPrice.get();
    }

    public SimpleDoubleProperty productPriceProperty() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice.set(productPrice);
    }

    public double getNetAmount() {
        return netAmount.get();
    }

    public SimpleDoubleProperty netAmountProperty() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount.set(netAmount);
    }

    public String getHarvestRemarque() {
        return harvestRemarque.get();
    }

    public SimpleStringProperty harvestRemarqueProperty() {
        return harvestRemarque;
    }

    public void setHarvestRemarque(String harvestRemarque) {
        this.harvestRemarque.set(harvestRemarque);
    }

    public int getHarvestID() {
        return harvestID.get();
    }

    public SimpleIntegerProperty harvestIDProperty() {
        return harvestID;
    }

    public void setHarvestID(int harvestID) {
        this.harvestID.set(harvestID);
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

    public SimpleStringProperty employeeFullNameProperty() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName.set(employeeFullName);
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

    public boolean isTransportStatus() {
        return transportStatus.get();
    }

    public SimpleBooleanProperty transportStatusProperty() {
        return transportStatus;
    }

    public void setTransportStatus(boolean transportStatus) {
        this.transportStatus.set(transportStatus);
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

    public int getCreditId() {
        return creditId.get();
    }

    public SimpleIntegerProperty creditIdProperty() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId.set(creditId);
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

    public int getFarmId() {
        return farmId.get();
    }

    public SimpleIntegerProperty farmIdProperty() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId.set(farmId);
    }

    public String getFarmName() {
        return farmName.get();
    }

    public SimpleStringProperty farmNameProperty() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
    }
}
