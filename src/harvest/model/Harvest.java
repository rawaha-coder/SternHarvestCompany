package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Harvest {

    private final IntegerProperty harvestingID;
    private final ObjectProperty<Date> harvestingDate;
    private final Supplier mSupplier;
    private final StringProperty SupplierName;
    private final Farm mFarm;
    private final StringProperty FarmName;
    private final Product mProduct;
    private final StringProperty ProductName;
    private final ProductDetail mProductDetail;
    private final StringProperty ProductCode;


    public Harvest() {
        this.harvestingID = new SimpleIntegerProperty();
        this.harvestingDate = new SimpleObjectProperty<>();
        mSupplier = new Supplier();
        SupplierName = new SimpleStringProperty(mSupplier.getSupplierName());
        mFarm = new Farm();
        FarmName = new SimpleStringProperty(mFarm.getFarmName());
        mProduct = new Product();
        ProductName = new SimpleStringProperty(mProduct.getProductName());
        mProductDetail = new ProductDetail();
        ProductCode = new SimpleStringProperty(mProductDetail.getProductCode());
    }

    public int getHarvestingID() {
        return harvestingID.get();
    }

    public IntegerProperty harvestingIDProperty() {
        return harvestingID;
    }

    public void setHarvestingID(int harvestingID) {
        this.harvestingID.set(harvestingID);
    }

    public Date getHarvestingDate() {
        return harvestingDate.get();
    }

    public ObjectProperty<Date> harvestingDateProperty() {
        return harvestingDate;
    }

    public void setHarvestingDate(Date harvestingDate) {
        this.harvestingDate.set(harvestingDate);
    }

    public Supplier getSupplier() {
        return mSupplier;
    }

    public String getSupplierName() {
        return SupplierName.get();
    }

    public StringProperty supplierNameProperty() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        this.SupplierName.set(supplierName);
    }

    public Farm getFarm() {
        return mFarm;
    }

    public String getFarmName() {
        return FarmName.get();
    }

    public StringProperty farmNameProperty() {
        return FarmName;
    }

    public void setFarmName(String farmName) {
        this.FarmName.set(farmName);
    }

    public Product getProduct() {
        return mProduct;
    }

    public String getProductName() {
        return ProductName.get();
    }

    public StringProperty productNameProperty() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName.set(productName);
    }

    public ProductDetail getProductDetail() {
        return mProductDetail;
    }

    public String getProductCode() {
        return ProductCode.get();
    }

    public StringProperty productCodeProperty() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        this.ProductCode.set(productCode);
    }
}
