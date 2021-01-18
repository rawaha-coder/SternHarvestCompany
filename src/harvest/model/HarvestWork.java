package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class HarvestWork {
    private final IntegerProperty harvestWorkID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> harvestDate = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty harvestType  = new SimpleIntegerProperty();
    private final SimpleDoubleProperty allQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty badQuality = new SimpleDoubleProperty();
    private final SimpleDoubleProperty goodQuality = new SimpleDoubleProperty();
    private final SimpleDoubleProperty productPrice = new SimpleDoubleProperty();
    private final SimpleDoubleProperty netAmount = new SimpleDoubleProperty();
    private final SimpleStringProperty harvestRemarque = new SimpleStringProperty();
    private final SimpleIntegerProperty harvestProductionID = new SimpleIntegerProperty();
    private Employee employee = new Employee();
    private Transport transport = new Transport();
    private final SimpleBooleanProperty transportStatus = new SimpleBooleanProperty();
    private final SimpleDoubleProperty transportAmount = new SimpleDoubleProperty();
    private Credit credit = new Credit();
    private final SimpleDoubleProperty creditAmount = new SimpleDoubleProperty();
    private Farm farm = new Farm();
    private final SimpleStringProperty farmName = new SimpleStringProperty();

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public double getTransportAmount() {
        this.transportAmount.set(this.transport.getTransportAmount());
        return transportAmount.get();
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public double getCreditAmount() {
        this.creditAmount.set(this.credit.getCreditAmount());
        return creditAmount.get();
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public String getFarmName() {
        this.farmName.set(this.farm.getFarmName());
        return farmName.get();
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
        this.goodQuality.set(this.getAllQuantity() - this.getBadQuality());
        return goodQuality.get();
    }

    public SimpleDoubleProperty goodQualityProperty() {
        this.goodQuality.set(this.getAllQuantity() - this.getBadQuality());
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

    public int getHarvestProductionID() {
        return harvestProductionID.get();
    }

    public SimpleIntegerProperty harvestProductionIDProperty() {
        return harvestProductionID;
    }

    public void setHarvestProductionID(int harvestProductionID) {
        this.harvestProductionID.set(harvestProductionID);
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
}
