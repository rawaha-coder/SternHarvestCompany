package harvest.view;

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
    ObservableList<String> productsNamesList = FXCollections.observableArrayList();

    @FXML private ComboBox<String> fxProductNameComboBox;
    @FXML private ComboBox<String> fxProductTypeComboBox;
    @FXML private TextField fxProductCode;
    @FXML private TextField fxProductPriceEmployee;
    @FXML private TextField fxProductPriceCompany;
    @FXML private AnchorPane fxAddProductUI;

    private boolean isEditProduct = false;
    private boolean isEditDetail = false;
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

    //fill the ChoiceBox by product list
    private void getProductList() {
        productsNamesList.clear();
        mProductMap.clear();
        try {
            mProductMap = mProductDAO.getProductMap();
            productsNamesList.setAll(mProductMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxProductNameComboBox.setItems(productsNamesList);
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
        ObservableList<String> observableProductTypeList = FXCollections.observableArrayList();
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
            fxProductNameComboBox.getSelectionModel().clearSelection();
        }else if(isEditDetail){
            fxProductTypeComboBox.getSelectionModel().clearSelection();
            fxProductCode.setText("");
            fxProductPriceEmployee.setText("");
            fxProductPriceCompany.setText("");
        }else {
            fxProductNameComboBox.getSelectionModel().clearSelection();
            fxProductTypeComboBox.getSelectionModel().clearSelection();
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
            addNewProduct();
        }
    }

    private void addNewProduct() {
        if (validateInput())
        {
            alert.missingInfo("Product");
            return;
        }
        Product product = mProductMap.get(fxProductNameComboBox.getValue());
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductType(fxProductTypeComboBox.getValue());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
        productDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
        if (product != null){
            productDetail.getProduct().setProductId(product.getProductId());
            productDetail.getProduct().setProductName(product.getProductName());
            alert.saveItem("Product", mProductDetailDAO.addProductDetail(productDetail));
            refreshTable(product);
        }else{
            productDetail.getProduct().setProductName(fxProductNameComboBox.getValue());
            productDetail.getProduct().setProductId(mProductDetailDAO.addNewProductDetail(productDetail));
            if (productDetail.getProduct().getProductId() != -1){
                alert.saveItem("Product", true);
                refreshTable(productDetail.getProduct());
            }else {
                alert.saveItem("Product", false);
            }
        }
        handleClearButton();
        getProductList();
    }

    private void EditProductDetail(ProductDetail productDetail) {
        productDetail.setProductType(fxProductTypeComboBox.getSelectionModel().getSelectedItem());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
        productDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
        alert.updateItem("Product", mProductDetailDAO.editProductDetail(productDetail));
        refreshTable(productDetail.getProduct());
        handleClearButton();
        handleCancelButton();
    }

    private void EditProduct(Product product) {
        product.setProductName(fxProductNameComboBox.getValue());
        alert.updateItem("Product", mProductDAO.editData(product));
        refreshTable(product);
        handleClearButton();
        handleCancelButton();
    }

    void refreshTable(Product product) {
        mProductDAO.updateLiveData();
        mProductDetailDAO.updateLiveData(product);
    }

    @FXML
    private void handleCancelButton() {
        isEditProduct = false;
        isEditDetail = false;
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
        mProductDetail.getProduct().setProductId(productDetail.getProduct().getProductId());
        mProductDetail.getProduct().setProductName(productDetail.getProduct().getProductName());
    }

    public void inflateProductUI(Product product){
        fxProductNameComboBox.setItems(null);
        fxProductNameComboBox.setValue(product.getProductName());
        isEditProduct = true;
        fxProductTypeComboBox.setDisable(true);
        fxProductCode.setDisable(true);
        fxProductPriceEmployee.setDisable(true);
        fxProductPriceCompany.setDisable(true);
        mProduct.setProductId(product.getProductId());
    }

    private boolean validateInput(){
        return Validation.isEmpty(fxProductNameComboBox.getEditor().getText(),
                fxProductTypeComboBox.getEditor().getText(),
                fxProductCode.getText(),
                fxProductPriceEmployee.getText(),
                fxProductPriceCompany.getText())
                || !Validation.isDouble(fxProductPriceEmployee.getText())
                || !Validation.isDouble(fxProductPriceCompany.getText());
    }
}
