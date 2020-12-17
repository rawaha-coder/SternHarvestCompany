package harvest.ui.product;

import harvest.model.Product;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import harvest.database.ProductDAO;
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
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    public static ObservableList<String> PRODUCT_NAME_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<String> PRODUCT_TYPE_LIVE_DATA = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> fxProductName;
    @FXML
    private ComboBox<String> fxProductType;
    @FXML
    private TextField fxProductCode;
    @FXML
    private TextField fxProductPrice1;
    @FXML
    private TextField fxProductPrice2;
    @FXML
    private AnchorPane fxAddProductUI;


    private Boolean isEditStatus = Boolean.FALSE;
    private final Product mProduct = new Product();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final AlertMaker alert = new AlertMaker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxProductName.setItems(PRODUCT_NAME_LIVE_DATA);
        fxProductType.setItems(PRODUCT_TYPE_LIVE_DATA);
        mProductDAO.updateLiveData();
    }

    @FXML void handleClearFieldsButton(){
        fxProductName.valueProperty().set(null);
        fxProductType.valueProperty().set(null);
        fxProductCode.setText("");
        fxProductPrice1.setText("");
        fxProductPrice2.setText("");
    }


    @FXML
    private void handleSaveButton() {
        Product product = new Product();
        if (Validation.isEmpty(fxProductName.getSelectionModel().getSelectedItem(),
                fxProductType.getSelectionModel().getSelectedItem(),
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
            handleEditOperation(mProduct);
        }else {
            product.setProductName(fxProductName.getSelectionModel().getSelectedItem());
            product.setProductType(fxProductType.getSelectionModel().getSelectedItem());
            product.setProductCode(fxProductCode.getText());
            product.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
            product.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
            if (mProductDAO.addData(product)){
                mProductDAO.updateLiveData();
                handleClearFieldsButton();
                alert.saveItem("Product", true);
            }else {
                alert.saveItem("product", false);
            }
        }
    }

    private void handleEditOperation(Product product) {
        product.setProductName(fxProductName.getSelectionModel().getSelectedItem());
        product.setProductType(fxProductType.getSelectionModel().getSelectedItem());
        product.setProductCode(fxProductCode.getText());
        product.setProductFirstPrice(Double.parseDouble(fxProductPrice1.getText().trim()));
        product.setProductSecondPrice(Double.parseDouble(fxProductPrice2.getText().trim()));
        if (mProductDAO.editData(product)){
            handleClearFieldsButton();
            mProductDAO.updateProductListByName(product.getProductName());
            alert.updateItem("Product", true);
        }else {
            alert.updateItem("Product", false);
        }
        isEditStatus = Boolean.FALSE;
        handleCancelButton();
    }

    @FXML
    private void handleCancelButton() {
        Stage stage = (Stage) fxAddProductUI.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Product product){
        fxProductName.getSelectionModel().select(product.getProductName());
        fxProductType.getSelectionModel().select(product.getProductType());
        fxProductCode.setText(product.getProductCode());
        fxProductPrice1.setText(String.valueOf(product.getProductFirstPrice()));
        fxProductPrice2.setText(String.valueOf(product.getProductSecondPrice()));
        isEditStatus = Boolean.TRUE;
        mProduct.setProductId(product.getProductId());
    }
}
