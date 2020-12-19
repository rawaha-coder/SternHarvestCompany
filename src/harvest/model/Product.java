package harvest.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    private final SimpleIntegerProperty productId;
    private final SimpleStringProperty productName;

    public Product() {
        this.productId = new SimpleIntegerProperty();
        this.productName = new SimpleStringProperty();
    }

    public Product(int productId, String productName) {
        this.productId = new SimpleIntegerProperty(productId);;
        this.productName = new SimpleStringProperty(productName);
    }

    public Product(String productName) {
        this.productId = new SimpleIntegerProperty();;
        this.productName = new SimpleStringProperty(productName);
    }

    public int getProductId() {
        return productId.get();
    }

    public SimpleIntegerProperty productIdProperty() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
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
