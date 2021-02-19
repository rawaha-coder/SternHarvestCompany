package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Production {

    private final SimpleIntegerProperty productionID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> productionDate = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty supplierID = new SimpleIntegerProperty();
    private final SimpleStringProperty supplierName = new SimpleStringProperty();
    private final SimpleIntegerProperty farmID = new SimpleIntegerProperty();
    private final SimpleStringProperty farmName = new SimpleStringProperty();
    private final SimpleIntegerProperty productID = new SimpleIntegerProperty();
    private final SimpleStringProperty productName = new SimpleStringProperty();
    private final SimpleStringProperty productCode = new SimpleStringProperty();
    private final SimpleIntegerProperty totalEmployee = new SimpleIntegerProperty(0);
    private final SimpleDoubleProperty goodQuantity = new SimpleDoubleProperty(0.0);
    private final SimpleDoubleProperty productionPrice = new SimpleDoubleProperty(0.0);
    private final SimpleDoubleProperty productionCost = new SimpleDoubleProperty(0.0);

    public int getProductionID() {
        return productionID.get();
    }

    public void setProductionID(int productionID) {
        this.productionID.set(productionID);
    }

    public Date getProductionDate() {
        return productionDate.get();
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate.set(productionDate);
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public int getFarmID() {
        return farmID.get();
    }

    public void setFarmID(int farmID) {
        this.farmID.set(farmID);
    }

    public String getFarmName() {
        return farmName.get();
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
    }

    public int getProductID() {
        return productID.get();
    }

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductCode() {
        return productCode.get();
    }

    public void setProductCode(String productCode) {
        this.productCode.set(productCode);
    }

    public int getTotalEmployee() {
        return totalEmployee.get();
    }

    public void setTotalEmployee(int totalEmployee) {
        this.totalEmployee.set(totalEmployee);
    }

    public double getGoodQuantity() {
        return goodQuantity.get();
    }

    public void setGoodQuantity(double goodQuantity) {
        this.goodQuantity.set(goodQuantity);
    }

    public double getProductionPrice() {
        return productionPrice.get();
    }

    public void setProductionPrice(double productionPrice) {
        this.productionPrice.set(productionPrice);
    }

    public double getProductionCost() {
        this.productionCost.set(this.getGoodQuantity() * this.getProductionPrice());
        return productionCost.get();
    }

    public void setProductionCost(double productionCost) {
        this.productionCost.set(productionCost);
    }
}
