package harvest.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductDetail {
    private final SimpleIntegerProperty productDetailId;
    private final SimpleStringProperty productCode;
    private final SimpleStringProperty productType;
    private final SimpleDoubleProperty priceEmployee;
    private final SimpleDoubleProperty priceCompany;
    private final Product product;

    public ProductDetail() {
        this.productDetailId = new SimpleIntegerProperty();
        this.productCode = new SimpleStringProperty();
        this.productType = new SimpleStringProperty();
        this.priceEmployee = new SimpleDoubleProperty(0.0);
        this.priceCompany = new SimpleDoubleProperty(0.0);
        this.product = new Product();
    }

    public int getProductDetailId() {
        return productDetailId.get();
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId.set(productDetailId);
    }

    public String getProductCode() {
        return productCode.get();
    }

    public SimpleStringProperty productCodeProperty() {
        return productCode;
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

    public void setProduct(Product product){
        this.product.setProductId(product.getProductId());
        this.product.setProductName(product.getProductName());
    }
}
