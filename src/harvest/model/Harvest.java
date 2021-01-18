package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Harvest {
//
//    private final IntegerProperty harvestID;
//    private final ObjectProperty<Date> harvestDate;
//    private final Supplier mSupplier;
//    private final StringProperty supplierName;
//    private final Farm mFarm;
//    private final StringProperty farmName;
//    private final Product mProduct;
//    private final StringProperty productName;
//    private final ProductDetail mProductDetail;
//    private final StringProperty productCode;
//
//
//    public Harvest() {
//        this.harvestID = new SimpleIntegerProperty();
//        this.harvestDate = new SimpleObjectProperty<>();
//        mSupplier = new Supplier();
//        supplierName = new SimpleStringProperty(mSupplier.getSupplierName());
//        mFarm = new Farm();
//        farmName = new SimpleStringProperty(mFarm.getFarmName());
//        mProduct = new Product();
//        productName = new SimpleStringProperty(mProduct.getProductName());
//        mProductDetail = new ProductDetail();
//        productCode = new SimpleStringProperty(mProductDetail.getProductCode());
//    }
//
//    public Harvest(Harvest harvest) {
//        this.harvestID = new SimpleIntegerProperty(harvest.getHarvestID());
//        this.harvestDate = new SimpleObjectProperty<>(harvest.getHarvestDate());
//        mSupplier = harvest.getSupplier();
//        this.supplierName = new SimpleStringProperty(harvest.getSupplierName());
//        mFarm = harvest.getFarm();
//        farmName = new SimpleStringProperty(harvest.getFarmName());
//        mProduct = harvest.getProduct();
//        productName = new SimpleStringProperty(harvest.getProductName());
//        mProductDetail = harvest.getProductDetail();
//        productCode = new SimpleStringProperty(harvest.getProductCode());
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
//    public Supplier getSupplier() {
//        return mSupplier;
//    }
//
//    public void setSupplier(Supplier supplier){
//        mSupplier.setSupplierId(supplier.getSupplierId());
//        mSupplier.setSupplierName(supplier.getSupplierName());
//        mSupplier.setSupplierFirstname(supplier.getSupplierFirstname());
//        mSupplier.setSupplierLastname(supplier.getSupplierLastname());
//        supplierName.set(mSupplier.getSupplierName());
//    }
//
//    public String getSupplierName() {
//        return supplierName.get();
//    }
//
//    public StringProperty supplierNameProperty() {
//        return supplierName;
//    }
//
//    public void setSupplierName(String supplierName) {
//        this.supplierName.set(supplierName);
//    }
//
//    public Farm getFarm() {
//        return mFarm;
//    }
//
//    public void setFarm(Farm farm){
//        mFarm.setFarmId(farm.getFarmId());
//        mFarm.setFarmName(farm.getFarmName());
//        mFarm.setFarmAddress(farm.getFarmAddress());
//        farmName.set(mFarm.getFarmName());
//    }
//
//    public String getFarmName() {
//        return farmName.get();
//    }
//
//    public StringProperty farmNameProperty() {
//        return farmName;
//    }
//
//    public void setFarmName(String farmName) {
//        this.farmName.set(farmName);
//    }
//
//    public Product getProduct() {
//        return mProduct;
//    }
//
//    public void setProduct(Product product){
//        mProduct.setProductId(product.getProductId());
//        mProduct.setProductName(product.getProductName());
//        productName.set(mProduct.getProductName());
//    }
//
//    public String getProductName() {
//        return productName.get();
//    }
//
//    public StringProperty productNameProperty() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName.set(productName);
//    }
//
//    public ProductDetail getProductDetail() {
//        return mProductDetail;
//    }
//
//    public void setProductDetail(ProductDetail productDetail){
//        mProductDetail.setProductDetailId(productDetail.getProductDetailId());
//        mProductDetail.setProductType(productDetail.getProductType());
//        mProductDetail.setProductCode(productDetail.getProductCode());
//        mProductDetail.setProductFirstPrice(productDetail.getProductFirstPrice());
//        mProductDetail.setProductSecondPrice(productDetail.getProductSecondPrice());
//        mProductDetail.setProduct(productDetail.getProduct());
//    }
//
//    public String getProductCode() {
//        return productCode.get();
//    }
//
//    public StringProperty productCodeProperty() {
//        return productCode;
//    }
//
//    public void setProductCode(String productCode) {
//        this.productCode.set(productCode);
//    }
}
