package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;

public class Supply {

    private final SimpleIntegerProperty supplyId = new SimpleIntegerProperty();
    private final Supplier mSupplier = new Supplier();
    private final Farm mFarm = new Farm();
    private final Product mProduct = new Product();

    public int getSupplyId() {
        return supplyId.get();
    }

    public void setSupplyId(int id) {
        this.supplyId.set(id);
    }

    public Supplier getSupplier() {
        return mSupplier;
    }

    public Farm getFarm() {
        return mFarm;
    }

    public Product getProduct() {
        return mProduct;
    }

    public String getSupplierName() {
        return mSupplier.getSupplierName();
    }

    public String getFarmName() {
        return mFarm.getFarmName();
    }

    public String getProductName() {
        return mProduct.getProductName();
    }
}
