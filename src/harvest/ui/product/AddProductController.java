package harvest.ui.product;

import harvest.model.Product;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import harvest.viewmodel.ProductDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.util.ResourceBundle;

import static harvest.viewmodel.ProductDAO.*;

public class AddProductController implements Initializable {

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
    private final ProductDAO mProductDAO = new ProductDAO();
    private final AlertMaker alert = new AlertMaker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxProductName.setItems(PRODUCT_NAME_LIVE_DATA);
        fxProductType.setItems(PRODUCT_TYPE_LIVE_DATA);
        mProductDAO.updateAddLivedata();
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
        if (isEditStatus){
            handleEditOperation(mProduct);
            return;
        }

        if(checkInputs()){
            if (mProductDAO.addProduct(
                    fxProductName.getSelectionModel().getSelectedItem().trim(),
                    fxProductType.getSelectionModel().getSelectedItem().trim(),
                    fxProductCode.getText().trim(),
                    Double.parseDouble(fxProductPrice1.getText().trim()),
                    Double.parseDouble(fxProductPrice2.getText().trim())
            )){
                mProductDAO.updateLivedata();
                handleClearFieldsButton();
                alert.saveItem("Product", true);
            }else {
                alert.saveItem("product", false);
            }
        }
    }

    private void handleEditOperation(Product product) {
        if (mProductDAO.updateProduct(
                product.getProductId(),
                fxProductName.getSelectionModel().getSelectedItem().trim(),
                fxProductCode.getText().trim(),
                fxProductType.getSelectionModel().getSelectedItem().trim(),
                Double.parseDouble(fxProductPrice1.getText().trim()),
                Double.parseDouble(fxProductPrice2.getText().trim())
        )){
            handleClearFieldsButton();
            mProductDAO.updateLivedata();
            alert.updateItem("Product", true);
        }else {
            alert.updateItem("Product", false);
        }
        isEditStatus = Boolean.FALSE;
        handleCancelButton();
    }


    private boolean checkInputs(){
        if (
                fxProductName.getSelectionModel().getSelectedItem().isEmpty()
                && fxProductType.getSelectionModel().getSelectedItem().isEmpty()
                && fxProductCode.getText().isEmpty()
                && fxProductPrice1.getText().isEmpty()
                && fxProductPrice2.getText().isEmpty()
        ){
            alert.show("Required fields are missing", "Name, Code, Type and Price fields cannot be empty!", AlertType.INFORMATION);
            return false;
        }else if(fxProductName.getSelectionModel().getSelectedItem().isEmpty()){
            alert.show("Required fields are missing", "Please enter product name", AlertType.INFORMATION);
            return false;
        }else if (fxProductCode.getText().isEmpty()){
            alert.show("Required fields are missing", "Please enter product code", AlertType.INFORMATION);
            return false;
        }else if (fxProductType.getSelectionModel().getSelectedItem().isEmpty()){
            alert.show("Required fields are missing", "Please enter product type", AlertType.INFORMATION);
            return false;
        }else if (fxProductPrice1.getText().isEmpty()){
            alert.show("Required fields are missing", "Please enter product price", AlertType.INFORMATION);
            return false;
        }else if (fxProductPrice2.getText().isEmpty()){
            alert.show("Required fields are missing", "Please enter product price", AlertType.INFORMATION);
            return false;
        }
        return Validation.isValidDouble(fxProductPrice1.getText()) && Validation.isValidDouble(fxProductPrice2.getText());
    }

    @FXML
    private void handleCancelButton() {
        Stage stage = (Stage) fxAddProductUI.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Product product){
        fxProductName.getSelectionModel().select(product.getProductName());
        fxProductCode.setText(product.getProductCode());
        fxProductType.getSelectionModel().select(product.getProductType());
        fxProductPrice1.setText(String.valueOf(product.getProductFirstPrice()));
        fxProductPrice2.setText(String.valueOf(product.getProductSecondPrice()));
        isEditStatus = Boolean.TRUE;
        mProduct.setProductId(product.getProductId());
    }



}
