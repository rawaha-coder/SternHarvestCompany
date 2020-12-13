package harvest.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private final SimpleIntegerProperty productId;
    private final SimpleStringProperty productName;
    private final SimpleStringProperty productCode;
    private final SimpleStringProperty productType;
    private final SimpleDoubleProperty productFirstPrice;
    private final SimpleDoubleProperty productSecondPrice;

    public Product() {
        this.productId = new SimpleIntegerProperty(0);
        this.productName = new SimpleStringProperty("");;
        this.productCode = new SimpleStringProperty("");;
        this.productType = new SimpleStringProperty("");;
        this.productFirstPrice = new SimpleDoubleProperty(0.0);;
        this.productSecondPrice = new SimpleDoubleProperty(0.0);;
    }

    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductCode() {
        return productCode.get();
    }

    public void setProductCode(String productCode) {
        this.productCode.set(productCode);
    }

    public String getProductType() {
        return productType.get();
    }

    public void setProductType(String productType) {
        this.productType.set(productType);
    }

    public double getProductFirstPrice() {
        return productFirstPrice.get();
    }

    public void setProductFirstPrice(double productFirstPrice) {
        this.productFirstPrice.set(productFirstPrice);
    }

    public double getProductSecondPrice() {
        return productSecondPrice.get();
    }

    public void setProductSecondPrice(double productSecondPrice) {
        this.productSecondPrice.set(productSecondPrice);
    }
}
