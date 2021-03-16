package harvest.controller;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
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

    //fill the ChoiceBox by product list
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
            addNewProduct();
        }
    }

    private void addNewProduct() {
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
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductType(fxProductTypeComboBox.getValue());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
        productDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
        if (product != null){
            productDetail.setProduct(product);
            alert.saveItem("Product", mProductDetailDAO.addProductDetail(productDetail));

        }else{
            Product newProduct = new Product();
            newProduct.setProductName(fxProductNameComboBox.getValue());
            productDetail.setProduct(newProduct);
            boolean saved = false;
            int id = mProductDetailDAO.addNewProductDetail(productDetail);
            if (id != -1){
                newProduct.setProductId(id);
                saved = true;
            }
            alert.saveItem("Product", saved);
        }
        handleClearButton();
        mProductDAO.updateLiveData();
        mProductDetailDAO.updateLiveData(productDetail.getProduct());
        displayTable(productDetail.getProduct());
        getProductList();
    }

    void displayTable(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/res/layout/display_product.fxml"));
            loader.load();
            DisplayProductController controller = loader.getController();
            controller.updateLiveData(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void EditProductDetail(ProductDetail productDetail) {
        productDetail.setProductType(fxProductTypeComboBox.getSelectionModel().getSelectedItem());
        productDetail.setProductCode(fxProductCode.getText());
        productDetail.setPriceEmployee(Double.parseDouble(fxProductPriceEmployee.getText().trim()));
        productDetail.setPriceCompany(Double.parseDouble(fxProductPriceCompany.getText().trim()));
        alert.updateItem("Product", mProductDetailDAO.editData(productDetail));
        handleClearButton();
        mProductDetailDAO.updateLiveData(productDetail.getProduct());
        handleCancelButton();
    }

    private void EditProduct(Product product) {
        product.setProductName(fxProductNameComboBox.getSelectionModel().getSelectedItem());
        alert.updateItem("Product", mProductDAO.editData(product));
        mProductDAO.updateLiveData();
        handleClearButton();
        handleCancelButton();
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
