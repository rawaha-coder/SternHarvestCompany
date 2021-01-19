package harvest.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductDetail {
    private final SimpleIntegerProperty productDetailId;
    private final SimpleStringProperty productCode;
    private final SimpleStringProperty productType;
    private final SimpleDoubleProperty productFirstPrice;
    private final SimpleDoubleProperty productSecondPrice;
    private final Product product;

    public ProductDetail() {
        this.productDetailId = new SimpleIntegerProperty();
        this.productCode = new SimpleStringProperty();;
        this.productType = new SimpleStringProperty();;
        this.productFirstPrice = new SimpleDoubleProperty(0.0);;
        this.productSecondPrice = new SimpleDoubleProperty(0.0);;
        this.product = new Product();
    }

    public void ProductDetail(ProductDetail productDetail){
        this.productDetailId.set(productDetail.getProductDetailId());
        this.productCode.set(productDetail.getProductCode());
        this.productType.set(productDetail.getProductType());
        this.productFirstPrice.set(productDetail.getProductFirstPrice());
        this.productSecondPrice.set(productDetail.getProductSecondPrice());
    }

    public int getProductDetailId() {
        return productDetailId.get();
    }

    public SimpleIntegerProperty productDetailIdProperty() {
        return productDetailId;
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

    public SimpleStringProperty productTypeProperty() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType.set(productType);
    }

    public double getProductFirstPrice() {
        return productFirstPrice.get();
    }

    public SimpleDoubleProperty productFirstPriceProperty() {
        return productFirstPrice;
    }

    public void setProductFirstPrice(double productFirstPrice) {
        this.productFirstPrice.set(productFirstPrice);
    }

    public double getProductSecondPrice() {
        return productSecondPrice.get();
    }

    public SimpleDoubleProperty productSecondPriceProperty() {
        return productSecondPrice;
    }

    public void setProductSecondPrice(double productSecondPrice) {
        this.productSecondPrice.set(productSecondPrice);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product){
        this.product.setProductId(product.getProductId());
        this.product.setProductName(product.getProductName());
    }
}
