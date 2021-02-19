package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Harvest {

    private final SimpleIntegerProperty harvestID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> harvestDate = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty allQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty badQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty penaltyQuality = new SimpleDoubleProperty();
    private final SimpleDoubleProperty generalPenaltyQuality = new SimpleDoubleProperty();
    private final SimpleDoubleProperty goodQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty productPrice = new SimpleDoubleProperty();
    private final SimpleIntegerProperty employeeID = new SimpleIntegerProperty();
    private final SimpleStringProperty employeeName = new SimpleStringProperty();
    private final SimpleIntegerProperty transportID = new SimpleIntegerProperty();
    private final SimpleDoubleProperty transportAmount = new SimpleDoubleProperty();
    private final SimpleBooleanProperty transportStatus = new SimpleBooleanProperty();
    private final SimpleIntegerProperty creditID = new SimpleIntegerProperty();
    private final SimpleDoubleProperty creditAmount = new SimpleDoubleProperty();
    private final SimpleIntegerProperty farmID = new SimpleIntegerProperty();
    private final SimpleStringProperty farmName = new SimpleStringProperty();
    private final SimpleDoubleProperty amountPayable = new SimpleDoubleProperty();
    private final SimpleStringProperty remarque = new SimpleStringProperty();
    private final SimpleIntegerProperty harvestType = new SimpleIntegerProperty();
    private final SimpleIntegerProperty productionID = new SimpleIntegerProperty();

    public int getHarvestID() {
        return harvestID.get();
    }

    public SimpleIntegerProperty harvestIDProperty() {
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

    public double getAllQuantity() {
        return allQuantity.get();
    }

    public SimpleDoubleProperty allQuantityProperty() {
        return allQuantity;
    }

    public void setAllQuantity(double allQuantity) {
        this.allQuantity.set(allQuantity);
    }

    public double getBadQuantity() {
        return badQuantity.get();
    }

    public SimpleDoubleProperty badQuantityProperty() {
        return badQuantity;
    }

    public void setBadQuantity(double badQuantity) {
        this.badQuantity.set(badQuantity);
    }

    public double getPenaltyQuality() {
        return penaltyQuality.get();
    }

    public SimpleDoubleProperty penaltyQualityProperty() {
        return penaltyQuality;
    }

    public void setPenaltyQuality(double penaltyQuality) {
        this.penaltyQuality.set(penaltyQuality);
    }

    public double getGeneralPenaltyQuality() {
        return generalPenaltyQuality.get();
    }

    public SimpleDoubleProperty generalPenaltyQualityProperty() {
        return generalPenaltyQuality;
    }

    public void setGeneralPenaltyQuality(double generalPenaltyQuality) {
        this.generalPenaltyQuality.set(generalPenaltyQuality);
    }

    public double getGoodQuantity() {
        return goodQuantity.get();
    }

    public SimpleDoubleProperty goodQuantityProperty() {
        return goodQuantity;
    }

    public void setGoodQuantity(double goodQuantity) {
        this.goodQuantity.set(goodQuantity);
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

    public int getEmployeeID() {
        return employeeID.get();
    }

    public SimpleIntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID.set(employeeID);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public SimpleStringProperty employeeNameProperty() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public int getTransportID() {
        return transportID.get();
    }

    public SimpleIntegerProperty transportIDProperty() {
        return transportID;
    }

    public void setTransportID(int transportID) {
        this.transportID.set(transportID);
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

    public boolean isTransportStatus() {
        return transportStatus.get();
    }

    public SimpleBooleanProperty transportStatusProperty() {
        return transportStatus;
    }

    public void setTransportStatus(boolean transportStatus) {
        this.transportStatus.set(transportStatus);
    }

    public int getCreditID() {
        return creditID.get();
    }

    public SimpleIntegerProperty creditIDProperty() {
        return creditID;
    }

    public void setCreditID(int creditID) {
        this.creditID.set(creditID);
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

    public int getFarmID() {
        return farmID.get();
    }

    public SimpleIntegerProperty farmIDProperty() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID.set(farmID);
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

    public double getAmountPayable() {
        return amountPayable.get();
    }

    public SimpleDoubleProperty amountPayableProperty() {
        return amountPayable;
    }

    public void setAmountPayable(double amountPayable) {
        this.amountPayable.set(amountPayable);
    }

    public String getRemarque() {
        return remarque.get();
    }

    public SimpleStringProperty remarqueProperty() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque.set(remarque);
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

    public int getProductionID() {
        return productionID.get();
    }

    public SimpleIntegerProperty productionIDProperty() {
        return productionID;
    }

    public void setProductionID(int productionID) {
        this.productionID.set(productionID);
    }
}
