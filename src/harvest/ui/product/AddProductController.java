package harvest.ui.product;

import harvest.database.ProductDetailDAO;
import harvest.model.Product;
import harvest.model.ProductDetail;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import harvest.database.ProductDAO;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.util.*;

public class AddProductController implements Initializable {

    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
    ObservableList<String> observableProductTypeList = FXCollections.observableArrayList();
    private final Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();

    @FXML
    private ComboBox<String> fxProductNameComboBox;
    @FXML
    private ComboBox<String> fxProductTypeComboBox;
    @FXML
    private TextField fxProductCode;
    @FXML
    private TextField fxProductPrice1;
    @FXML
    private TextField fxProductPrice2;
    @FXML
    private AnchorPane fxAddProductUI;


    private boolean isEditStatus = false;
    private boolean isOldProduct = false;
    private final Product mProduct = new Product();
    private final ProductDetail mProductDetail = new ProductDetail();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    private final AlertMaker alert = new AlertMaker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getProductList();
        observeSelectProduct();
    }

    //fill the ChoiceBox by employee list
    private void getProductList() {
        observableProductList.clear();
        try {
            List<Product> products = new ArrayList<>(mProductDAO.getData());
            if (products.size() > 0) {
                for (Product product: products) {
                    observableProductList.add(product.getProductName());
                    mProductMap.put(product.getProductName(), product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxProductNameComboBox.setItems(observableProductList);
    }

    private void observeSelectProduct() {
        fxProductNameComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if(mProductMap.get(new_val) != null){
                        getProductTypeList(mProductMap.get(new_val));
                        isOldProduct = true;
                    }
                }
        );
    }

    private void getProductTypeList(Product product) {
        observableProductTypeList.clear();
        try {
            List<ProductDetail> productDetails = new ArrayList<>(mProductDetailDAO.getProductDetail(product));
            if (productDetails.size() > 0) {
                for (ProductDetail productDetail: productDetails) {
                    observableProductTypeList.add(productDetail.getProductType());
                    mProductDetailMap.put(productDetail.getProductType(), productDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxProductTypeComboBox.setItems(observableProductTypeList);
    }

    @FXML void handleClearFieldsButton(){
        fxProductNameComboBox.valueProperty().set(null);
        fxProductTypeComboBox.valueProperty().set(null);
        fxProductCode.setText("");
        fxProductPrice1.setText("");
        fxProductPrice2.setText("");
    }

    @FXML
    private void handleSaveButton() {

        if (Validation.isEmpty(fxProductNameComboBox.getEditor().getText(),
                fxProductTypeComboBox.getEditor().getText(),
                fxProductCode.getText(),
                fxProductPrice1.getText(),
                fxProductPrice2.getText())
                || !Validation.isDouble(fxProductPrice1.getText())
                || !Validation.isDouble(fxProductPrice2.getText())
        )
        {
            alert.show("Required fields are missing", "Please enter correct data in required fields!", AlertType.INFORMATION);
            return;
        }
        if (isEditStatus){
            handleEditOperation(mProductDetail);
        }else {
            handleAddProductOperation();
        }
    }

    private void handleAddProductOperation() {
        Product oldProduct = mProductMap.get(fxProductNameComboBox.getValue());
        if (oldProduct != null){
            ProductDetail oldProductDetail = new ProductDetail();
            oldProductDetail.setProductType(fxProductTypeComboBox.getValue());
            oldProductDetail.setProductCode(fxProductCode.getText());
            oldProductDetail.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
            oldProductDetail.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
            oldProductDetail.setProduct(oldProduct);
            if (mProductDetailDAO.addProductDetail(oldProductDetail)){
                handleClearFieldsButton();
                getProductList();
                alert.saveItem("Product", true);
            }else {
                alert.saveItem("product", false);
            }
        }else{
            ProductDetail newProductDetail = new ProductDetail();
            newProductDetail.setProductType(fxProductTypeComboBox.getValue());
            newProductDetail.setProductCode(fxProductCode.getText());
            newProductDetail.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
            newProductDetail.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
            newProductDetail.setProduct(new Product(fxProductNameComboBox.getValue()));
            if (mProductDetailDAO.addData(newProductDetail)){
                handleClearFieldsButton();
                getProductList();
                alert.saveItem("Product", true);
            }else {
                alert.saveItem("product", false);
            }
        }
    }


    private void handleEditOperation(ProductDetail productDetail) {
        productDetail.setProductType(fxProductTypeComboBox.getSelectionModel().getSelectedItem());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
        productDetail.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
        productDetail.setProduct(new Product(fxProductNameComboBox.getSelectionModel().getSelectedItem()));
        if (mProductDetailDAO.addData(productDetail)){
            handleClearFieldsButton();
            alert.updateItem("Product", true);
        }else {
            alert.updateItem("Product", false);
        }
        isEditStatus = false;
        handleCancelButton();
    }

    @FXML
    private void handleCancelButton() {
        Stage stage = (Stage) fxAddProductUI.getScene().getWindow();
        stage.close();
    }

    public void inflateProductDetailUI(ProductDetail productDetail){
        fxProductNameComboBox.getSelectionModel().select(productDetail.getProduct().getProductName());
        fxProductTypeComboBox.getSelectionModel().select(productDetail.getProductType());
        fxProductCode.setText(productDetail.getProductCode());
        fxProductPrice1.setText(String.valueOf(productDetail.getProductFirstPrice()));
        fxProductPrice2.setText(String.valueOf(productDetail.getProductSecondPrice()));
        isEditStatus = true;
        mProductDetail.setProductDetailId(productDetail.getProductDetailId());
    }
}
