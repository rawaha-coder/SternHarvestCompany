package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {
    private SimpleIntegerProperty supplierId;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty supplierFirstname;
    private SimpleStringProperty supplierLastname;
    private Product supplierProduct;

    public Supplier() {
        this.supplierId = new SimpleIntegerProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierFirstname = new SimpleStringProperty();
        this.supplierLastname = new SimpleStringProperty();
        this.supplierProduct = new Product();
    }
}
