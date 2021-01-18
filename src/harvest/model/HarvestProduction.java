package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class HarvestProduction {

    private final IntegerProperty harvestProductionID = new SimpleIntegerProperty();
    private final SimpleObjectProperty<Date> harvestProductionDate = new SimpleObjectProperty();
    private final IntegerProperty harvestProductionHarvestType = new SimpleIntegerProperty();
    private final IntegerProperty harvestProductionTotalEmployee = new SimpleIntegerProperty();
    private final LongProperty harvestProductionTotalHours = new SimpleLongProperty();
    private final DoubleProperty harvestProductionTotalQuantity = new SimpleDoubleProperty();
    private final DoubleProperty harvestProductionTotalAmount = new SimpleDoubleProperty();
    private final DoubleProperty harvestProductionTotalTransport = new SimpleDoubleProperty();
    private final DoubleProperty harvestProductionTotalCredit = new SimpleDoubleProperty();

    private Supplier supplier = new Supplier();
    private Product product = new Product();
    private ProductDetail productDetail = new ProductDetail();
    private Farm farm = new Farm();

    public int getHarvestProductionID() {
        return harvestProductionID.get();
    }

    public IntegerProperty harvestProductionIDProperty() {
        return harvestProductionID;
    }

    public void setHarvestProductionID(int harvestProductionID) {
        this.harvestProductionID.set(harvestProductionID);
    }

    public Date getHarvestProductionDate() {
        return harvestProductionDate.get();
    }

    public SimpleObjectProperty<Date> harvestProductionDateProperty() {
        return harvestProductionDate;
    }

    public void setHarvestProductionDate(Date harvestProductionDate) {
        this.harvestProductionDate.set(harvestProductionDate);
    }

    public int getHarvestProductionHarvestType() {
        return harvestProductionHarvestType.get();
    }

    public IntegerProperty harvestProductionHarvestTypeProperty() {
        return harvestProductionHarvestType;
    }

    public void setHarvestProductionHarvestType(int harvestProductionHarvestType) {
        this.harvestProductionHarvestType.set(harvestProductionHarvestType);
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier){
        this.supplier = supplier;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }
}
