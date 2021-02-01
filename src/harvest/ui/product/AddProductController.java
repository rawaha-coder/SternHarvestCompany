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
import java.net.URL;
import java.util.*;

public class AddProductController implements Initializable {

    private Map<String, Product> mProductMap = new LinkedHashMap<>();
    private final ObservableList<String> observableProductList = FXCollections.observableArrayList();
    private final ObservableList<String> observableProductTypeList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> fxProductNameComboBox;
    @FXML
    private ComboBox<String> fxProductTypeComboBox;
    @FXML
    private TextField fxProductCode;
    @FXML
    private TextField fxProductPriceEmployee;
    @FXML
    private TextField fxProductPriceCompany;
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

    @FXML void handleClearButton(){
        if (isEditProduct){
            fxProductNameComboBox.valueProperty().set(null);
        }else if(isEditDetail){
            fxProductTypeComboBox.valueProperty().set(null);
            fxProductCode.setText("");
            fxProductPriceEmployee.setText("");
            fxProductPriceCompany.setText("");
        }else {
            fxProductNameComboBox.valueProperty().set(null);
            fxProductTypeComboBox.valueProperty().set(null);
            fxProductCode.setText("");
            fxProductPriceEmployee.setText("");
            fxProductPriceCompany.setText("");
        }
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
                fxProductPriceEmployee.getText(),
                fxProductPriceCompany.getText())
                || !Validation.isDouble(fxProductPriceEmployee.getText())
                || !Validation.isDouble(fxProductPriceCompany.getText())
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
            oldProductDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
            oldProductDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
            oldProductDetail.setProduct(product);
            alert.saveItem("Product", mProductDetailDAO.addProductDetail(oldProductDetail));

        }else{
            ProductDetail newProductDetail = new ProductDetail();
            newProductDetail.setProductType(fxProductTypeComboBox.getValue());
            newProductDetail.setProductCode(fxProductCode.getText());
            newProductDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
            newProductDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
            newProductDetail.setProduct(new Product(fxProductNameComboBox.getValue()));
            alert.saveItem("Product", mProductDetailDAO.addNewProductData(newProductDetail));
        }
        handleClearButton();
        mProductDAO.updateLiveData();
        getProductList();
    }


    private void EditProductDetail(ProductDetail productDetail) {
        productDetail.setProductType(fxProductTypeComboBox.getSelectionModel().getSelectedItem());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
        productDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
        alert.updateItem("Product", mProductDetailDAO.editData(productDetail));
        isEditProduct = false;
        handleClearButton();
        mProductDetailDAO.updateLiveData(productDetail.getProduct());
        handleCancelButton();
    }

    private void EditProduct(Product product) {
        product.setProductName(fxProductNameComboBox.getSelectionModel().getSelectedItem());
        alert.updateItem("Product", mProductDAO.editData(product));
        isEditDetail = false;
        mProductDAO.updateLiveData();
        handleClearButton();
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
        fxProductPriceEmployee.setText(String.valueOf(productDetail.getPriceEmployee()));
        fxProductPriceCompany.setText(String.valueOf(productDetail.getPriceCompany()));
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
        fxProductPriceEmployee.setDisable(true);
        fxProductPriceCompany.setDisable(true);
        mProduct.setProductId(product.getProductId());
    }
}
