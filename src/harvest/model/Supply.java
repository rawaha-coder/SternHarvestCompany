package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supply {
    private final SimpleIntegerProperty supplyId;
    private final Supplier mSupplier;
    private final Farm mFarm;
    private final Product mProduct;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty farmName;
    private SimpleStringProperty productName;

    public Supply() {
        this.supplyId = new SimpleIntegerProperty();
        this.mSupplier = new Supplier();
        this.mFarm = new Farm();
        this.mProduct = new Product();
        this.supplierName = new SimpleStringProperty();
        this.farmName = new SimpleStringProperty();
        this.productName = new SimpleStringProperty();
    }

    public Supply(Supplier supplier, Farm farm, Product product){
        this.supplyId = new SimpleIntegerProperty();;
        this.mSupplier = supplier;
        this.mFarm = farm;
        this.mProduct = product;
        this.supplierName = new SimpleStringProperty(mSupplier.getSupplierName());
        this.farmName = new SimpleStringProperty(mFarm.getFarmName());
        this.productName = new SimpleStringProperty(mProduct.getProductName());
    }

    public int getSupplyId() {
        return supplyId.get();
    }

    public SimpleIntegerProperty supplyIdProperty() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId.set(supplyId);
    }

    public Supplier getSupplier() {
        return mSupplier;
    }

    public void setSupplier(Supplier supplier){
        this.mSupplier.setSupplierId(supplier.getSupplierId());
        this.mSupplier.setSupplierName(supplier.getSupplierName());
        this.mSupplier.setSupplierFirstname(supplier.getSupplierFirstname());
        this.mSupplier.setSupplierLastname(supplier.getSupplierLastname());
        this.supplierName = new SimpleStringProperty(supplier.getSupplierName());
    }

    public Farm getFarm() {
        return mFarm;
    }

    public void setFarm(Farm farm){
        this.mFarm.setFarmId(farm.getFarmId());
        this.mFarm.setFarmName(farm.getFarmName());
        this.mFarm.setFarmAddress(farm.getFarmAddress());
        this.farmName = new SimpleStringProperty(farm.getFarmName());
    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product){
        this.mProduct.setProductId(product.getProductId());
        this.mProduct.setProductName(product.getProductName());
        this.productName = new SimpleStringProperty(product.getProductName());
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

    public String getFarmName() {
        return farmName.get();
    }

    public SimpleStringProperty farmNameProperty() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
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
}
