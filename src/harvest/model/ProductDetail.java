package harvest.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductDetail {
    private final SimpleIntegerProperty productDetailId = new SimpleIntegerProperty();
    private final SimpleStringProperty productCode = new SimpleStringProperty();
    private final SimpleStringProperty productType = new SimpleStringProperty();
    private final SimpleDoubleProperty priceEmployee  = new SimpleDoubleProperty(0.0);
    private final SimpleDoubleProperty priceCompany = new SimpleDoubleProperty(0.0);
    private final Product product = new Product();

    public int getProductDetailId() {
        return productDetailId.get();
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId.set(productDetailId);
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

    public double getPriceEmployee() {
        return priceEmployee.get();
    }

    public void setPriceEmployee(double priceEmployee) {
        this.priceEmployee.set(priceEmployee);
    }

    public double getPriceCompany() {
        return priceCompany.get();
    }

    public void setPriceCompany(double priceCompany) {
        this.priceCompany.set(priceCompany);
    }

    public Product getProduct() {
        return product;
    }
}
