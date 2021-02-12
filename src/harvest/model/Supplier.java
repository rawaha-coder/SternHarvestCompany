package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private final SimpleIntegerProperty supplierId = new SimpleIntegerProperty();
    private final SimpleStringProperty supplierName = new SimpleStringProperty();
    private final SimpleStringProperty supplierFirstname = new SimpleStringProperty();
    private final SimpleStringProperty supplierLastname = new SimpleStringProperty();

    public Supplier() {
    }

    public Supplier(int id) {
        this.supplierId.set(id);
    }

    public Supplier(int id, String name) {
        this.supplierId.set(id);
        this.supplierName.set(name);
    }



    public int getSupplierId() {
        return supplierId.get();
    }

    public void setSupplierId(int supplierId) {
        this.supplierId.set(supplierId);
    }

    public String getSupplierName() {
        return supplierName.get().toUpperCase();
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public String getSupplierFirstname() {
        return supplierFirstname.get().toUpperCase();
    }

    public void setSupplierFirstname(String supplierFirstname) {
        this.supplierFirstname.set(supplierFirstname.toUpperCase());
    }

    public String getSupplierLastname() {
        return supplierLastname.get().toUpperCase();
    }

    public void setSupplierLastname(String supplierLastname) {
        this.supplierLastname.set(supplierLastname.toUpperCase());
    }
}
