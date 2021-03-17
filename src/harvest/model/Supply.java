package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supply {
    private final SimpleIntegerProperty supplyId = new SimpleIntegerProperty();
    private Supplier mSupplier = new Supplier();
    private Farm mFarm = new Farm();
    private final Product mProduct = new Product();
    private final SimpleStringProperty supplierName = new SimpleStringProperty();
    private final SimpleStringProperty farmName = new SimpleStringProperty();
    private final SimpleStringProperty productName = new SimpleStringProperty();

    public Supply(){ }

    public int getSupplyId() {
        return supplyId.get();
    }

    public void setSupplyId(int supplyId) {
        this.supplyId.set(supplyId);
    }

    public Supplier getSupplier() {
        return mSupplier;
    }

    public void setSupplier(Supplier supplier){
        this.mSupplier = supplier;
        this.supplierName.set(supplier.getSupplierName().toUpperCase());
    }

    public Farm getFarm() {
        return mFarm;
    }

//    public void setFarm(Farm farm){
//        this.mFarm = farm;
//        this.farmName.set(farm.getFarmName());
//    }

    public Product getProduct() {
        return mProduct;
    }

//    public void setProduct(Product product){
//        this.mProduct = product;
//        this.productName.set(product.getProductName());
//    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public String getFarmName() {
        return farmName.get();
    }

    public String getProductName() {
        return productName.get();
    }
}
