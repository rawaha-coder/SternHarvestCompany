package harvest.ui.product;

import harvest.database.CommonDAO;
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
import java.net.URL;
import java.util.*;

import static harvest.ui.product.DisplayProductController.PRODUCT_NAME_LIVE_DATA;

public class AddProductController implements Initializable {

    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    private Map<String, Product> mProductMap = new LinkedHashMap<>();
    ObservableList<String> observableProductTypeList = FXCollections.observableArrayList();

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

    private boolean isEditProduct = false;
    private boolean isEditDetail = false;
    private final ProductDetail mProductDetail = new ProductDetail();
    private final Product mProduct = new Product();
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
        mProductMap.clear();
        try {
            mProductMap = mProductDAO.getProductMap();
            observableProductList.setAll(mProductMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

/// /       try {
//            List<Product> products = new ArrayList<>(mProductDAO.getData());
//            if (products.size() > 0) {
//                for (Product product: products) {
//                    observableProductList.add(product.getProductName());
//                    mProductMap.put(product.getProductName(), product);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        fxProductNameComboBox.setItems(observableProductList);
    }

    private void observeSelectProduct() {
        fxProductNameComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if(mProductMap.get(new_val) != null){
                        getProductTypeList(mProductMap.get(new_val));
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
        if (isEditDetail){
            EditProductDetail(mProductDetail);
        }else if (isEditProduct){
            EditProduct(mProduct);
        }else {
            handleAddProductOperation();
        }
    }

    private void handleAddProductOperation() {
        if (Validation.isEmpty(fxProductNameComboBox.getEditor().getText(),
                fxProductTypeComboBox.getEditor().getText(),
                fxProductCode.getText(),
                fxProductPrice1.getText(),
                fxProductPrice2.getText())
                || !Validation.isDouble(fxProductPrice1.getText())
                || !Validation.isDouble(fxProductPrice2.getText())
        )
        {
            alert.missingInfo("Product");
            return;
        }
        Product product = mProductMap.get(fxProductNameComboBox.getValue());
        if (product != null){
            ProductDetail oldProductDetail = new ProductDetail();
            oldProductDetail.setProductType(fxProductTypeComboBox.getValue());
            oldProductDetail.setProductCode(fxProductCode.getText());
            oldProductDetail.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
            oldProductDetail.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
            oldProductDetail.setProduct(product);
            alert.saveItem("Product", mProductDetailDAO.addProductDetail(oldProductDetail));

        }else{
            CommonDAO commonDAO = CommonDAO.getInstance();
            ProductDetail newProductDetail = new ProductDetail();
            newProductDetail.setProductType(fxProductTypeComboBox.getValue());
            newProductDetail.setProductCode(fxProductCode.getText());
            newProductDetail.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
            newProductDetail.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
            newProductDetail.setProduct(new Product(fxProductNameComboBox.getValue()));
            alert.saveItem("Product", commonDAO.addNewProductData(newProductDetail));
        }
        handleClearFieldsButton();
        mProductDAO.updateLiveData();
        if (PRODUCT_NAME_LIVE_DATA.size() > 0){
            mProductDetailDAO.updateLiveData(PRODUCT_NAME_LIVE_DATA.get(PRODUCT_NAME_LIVE_DATA.size()-1));
        }
        getProductList();
    }


    private void EditProductDetail(ProductDetail productDetail) {
        productDetail.setProductType(fxProductTypeComboBox.getSelectionModel().getSelectedItem());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
        productDetail.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
        alert.updateItem("Product", mProductDetailDAO.editData(productDetail));
        isEditProduct = false;
        handleClearFieldsButton();
        mProductDetailDAO.updateLiveData(productDetail.getProduct());
        handleCancelButton();
    }

    private void EditProduct(Product product) {
        product.setProductName(fxProductNameComboBox.getSelectionModel().getSelectedItem());
        alert.updateItem("Product", mProductDAO.editData(product));
        isEditDetail = false;
        mProductDAO.updateLiveData();
        handleClearFieldsButton();
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
        isEditDetail = true;
        fxProductNameComboBox.setDisable(true);
        mProductDetail.setProductDetailId(productDetail.getProductDetailId());
        mProductDetail.setProduct(new Product(productDetail.getProduct().getProductId(), productDetail.getProduct().getProductName()));
    }

    public void inflateProductUI(Product product){
        fxProductNameComboBox.getSelectionModel().select(product.getProductName());
        isEditProduct = true;
        fxProductTypeComboBox.setDisable(true);
        fxProductCode.setDisable(true);
        fxProductPrice1.setDisable(true);
        fxProductPrice2.setDisable(true);
        mProduct.setProductId(product.getProductId());
    }
}
