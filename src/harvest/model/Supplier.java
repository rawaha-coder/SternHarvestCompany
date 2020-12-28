package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private final SimpleIntegerProperty supplierId;
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty supplierFirstname;
    private final SimpleStringProperty supplierLastname;

    public Supplier() {
        this.supplierId = new SimpleIntegerProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierFirstname = new SimpleStringProperty();
        this.supplierLastname = new SimpleStringProperty();
    }

    public Supplier(int i) {
        this.supplierId = new SimpleIntegerProperty(i);
        this.supplierName = new SimpleStringProperty();
        this.supplierFirstname = new SimpleStringProperty();
        this.supplierLastname = new SimpleStringProperty();
    }

    public Supplier(int i, String s) {
        this.supplierId = new SimpleIntegerProperty(i);
        this.supplierName = new SimpleStringProperty(s);
        this.supplierFirstname = new SimpleStringProperty();
        this.supplierLastname = new SimpleStringProperty();
    }

    public int getSupplierId() {
        return supplierId.get();
    }

    public void setSupplierId(int supplierId) {
        this.supplierId.set(supplierId);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public String getSupplierFirstname() {
        return supplierFirstname.get();
    }

    public void setSupplierFirstname(String supplierFirstname) {
        this.supplierFirstname.set(supplierFirstname);
    }

    public String getSupplierLastname() {
        return supplierLastname.get();
    }

    public void setSupplierLastname(String supplierLastname) {
        this.supplierLastname.set(supplierLastname);
    }
}
