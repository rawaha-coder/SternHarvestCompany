package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private final SimpleIntegerProperty supplierId;
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty supplierFirstname;
    private final SimpleStringProperty supplierLastname;
    private final Farm supplierFarm;
    private final Product supplierProduct;

    public Supplier() {
        this.supplierId = new SimpleIntegerProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierFirstname = new SimpleStringProperty();
        this.supplierLastname = new SimpleStringProperty();
        this.supplierFarm = new Farm();
        this.supplierProduct = new Product();
    }

    public int getSupplierId() {
        return supplierId.get();
    }

    public SimpleIntegerProperty supplierIdProperty() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId.set(supplierId);
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

    public String getSupplierFirstname() {
        return supplierFirstname.get();
    }

    public SimpleStringProperty supplierFirstnameProperty() {
        return supplierFirstname;
    }

    public void setSupplierFirstname(String supplierFirstname) {
        this.supplierFirstname.set(supplierFirstname);
    }

    public String getSupplierLastname() {
        return supplierLastname.get();
    }

    public SimpleStringProperty supplierLastnameProperty() {
        return supplierLastname;
    }

    public void setSupplierLastname(String supplierLastname) {
        this.supplierLastname.set(supplierLastname);
    }

    public Farm getSupplierFarm() {
        return supplierFarm;
    }

    public void setSupplierFarm(Farm farm){
        this.supplierFarm.setFarmId(farm.getFarmId());
        this.supplierFarm.setFarmName(farm.getFarmName());
        this.supplierFarm.setFarmAddress(farm.getFarmAddress());

    }

    public Product getSupplierProduct() {
        return supplierProduct;
    }

    public void setSupplierProduct(Product product){
        this.supplierProduct.setProductId(product.getProductId());
        this.supplierProduct.setProductName(product.getProductName());
    }
}
