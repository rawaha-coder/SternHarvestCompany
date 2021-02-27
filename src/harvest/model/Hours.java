package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;

public class Hours {

    private final IntegerProperty hoursID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> harvestDate = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty employeeID = new SimpleIntegerProperty();
    private final SimpleStringProperty employeeName = new SimpleStringProperty();
    private final ObjectProperty<Time> startMorning = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> endMorning = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> startNoon = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> endNoon = new SimpleObjectProperty<>();
    private final LongProperty totalHours = new SimpleLongProperty();
    private final IntegerProperty employeeType = new SimpleIntegerProperty();
    private final SimpleIntegerProperty transportID = new SimpleIntegerProperty();
    private final SimpleDoubleProperty transportAmount = new SimpleDoubleProperty();
    private final SimpleBooleanProperty transportStatus = new SimpleBooleanProperty();
    private final SimpleIntegerProperty creditID = new SimpleIntegerProperty();
    private final SimpleDoubleProperty creditAmount = new SimpleDoubleProperty();
    private final SimpleDoubleProperty hourPrice = new SimpleDoubleProperty();
    private final SimpleIntegerProperty supplierID = new SimpleIntegerProperty();
    private final SimpleStringProperty supplierName = new SimpleStringProperty();
    private final SimpleIntegerProperty farmID = new SimpleIntegerProperty();
    private final SimpleStringProperty farmName = new SimpleStringProperty();
    private final SimpleIntegerProperty productID = new SimpleIntegerProperty();
    private final SimpleStringProperty productName = new SimpleStringProperty();
    private final SimpleStringProperty productCode = new SimpleStringProperty();
    private final SimpleDoubleProperty amountPayable = new SimpleDoubleProperty();
    private final SimpleStringProperty remarque = new SimpleStringProperty();

    public int getHoursID() {
        return hoursID.get();
    }

    public IntegerProperty hoursIDProperty() {
        return hoursID;
    }

    public void setHoursID(int hoursID) {
        this.hoursID.set(hoursID);
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

    public long getTotalSecond(){
        long totalSecond = (getEndMorning().getTime()) - getStartMorning().getTime() + (getEndNoon().getTime() - getStartNoon().getTime());
        return totalSecond;
    }

    public long getTotalMinute(){
        long totalSecond = ((getEndMorning().getTime() - getStartMorning().getTime()) + (getEndNoon().getTime() - getStartNoon().getTime())) / (1000 * 60) ;
        return totalSecond;
    }

    public LongProperty totalHoursProperty() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours.set(totalHours);
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

    public double getHourPrice() {
        return hourPrice.get();
    }

    public SimpleDoubleProperty hourPriceProperty() {
        return hourPrice;
    }

    public void setHourPrice(double hourPrice) {
        this.hourPrice.set(hourPrice);
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public SimpleIntegerProperty supplierIDProperty() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
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

    public int getProductID() {
        return productID.get();
    }

    public SimpleIntegerProperty productIDProperty() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductCode() {
        return productCode.get();
    }

    public SimpleStringProperty productCodeProperty() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode.set(productCode);
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

    public void getTotalWorkOnMilliSeconds() {
        long totalMilliSeconds = 0;
        if (endMorning.getValue() != null && startMorning.getValue() != null
                && endNoon.getValue() != null && startNoon.getValue() != null){
            totalMilliSeconds =  (endMorning.getValue().getTime() - startMorning.getValue().getTime()) + (endNoon.getValue().getTime() - startNoon.getValue().getTime());
        }
        this.totalHours.set(totalMilliSeconds);
    }
}
