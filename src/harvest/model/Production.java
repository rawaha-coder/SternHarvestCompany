package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Production {

    private final SimpleIntegerProperty productionID = new SimpleIntegerProperty();
    private final SimpleIntegerProperty productionType = new SimpleIntegerProperty();
    private final ObjectProperty<Date> productionDate = new SimpleObjectProperty<>();
    private final Supplier supplier = new Supplier();
    private final Farm farm = new Farm();
    private final Product product = new Product();
    private final ProductDetail productDetail = new ProductDetail();
    private final SimpleIntegerProperty totalEmployee = new SimpleIntegerProperty(0);
    private final SimpleDoubleProperty totalQuantity = new SimpleDoubleProperty(0.0);
    private final SimpleLongProperty totalMinutes = new SimpleLongProperty(0);
    private final SimpleDoubleProperty price = new SimpleDoubleProperty(0.0);

    public int getProductionID() {
        return productionID.get();
    }

    public SimpleIntegerProperty productionIDProperty() {
        return productionID;
    }

    public void setProductionID(int productionID) {
        this.productionID.set(productionID);
    }

    public int getProductionType() {
        return productionType.get();
    }

    public SimpleIntegerProperty productionTypeProperty() {
        return productionType;
    }

    public void setProductionType(int productionType) {
        this.productionType.set(productionType);
    }

    public Date getProductionDate() {
        return productionDate.get();
    }

    public ObjectProperty<Date> productionDateProperty() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate.set(productionDate);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Farm getFarm() {
        return farm;
    }

    public Product getProduct() {
        return product;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public int getTotalEmployee() {
        return totalEmployee.get();
    }

    public SimpleIntegerProperty totalEmployeeProperty() {
        return totalEmployee;
    }

    public void setTotalEmployee(int totalEmployee) {
        this.totalEmployee.set(totalEmployee);
    }

    public double getTotalQuantity() {
        return totalQuantity.get();
    }

    public SimpleDoubleProperty totalQuantityProperty() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity.set(totalQuantity);
    }

    public long getTotalMinutes() {
        return totalMinutes.get();
    }

    public SimpleLongProperty totalMinutesProperty() {
        return totalMinutes;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes.set(totalMinutes);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
}
