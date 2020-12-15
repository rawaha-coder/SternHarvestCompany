package harvest.ui.product;


import harvest.database.ProductDAO;
import harvest.model.Product;
import harvest.util.AlertMaker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static harvest.ui.product.AddProductController.PRODUCT_NAME_LIVE_DATA;

public class DisplayProductController implements Initializable {

    public static ObservableList<Product> PRODUCT_LIST_LIVE_DATA = FXCollections.observableArrayList();

    @FXML
    private TableView<String> fxProductNameTable;
    @FXML
    private TableColumn<String, String> fxProductNameColumn;

    @FXML
    private TableView<harvest.model.Product> fxProductTypeTable;

    @FXML
    private TableColumn<harvest.model.Product, String> fxProductTypeColumn;
    @FXML
    private TableColumn<harvest.model.Product, String> fxProductCodeColumn;
    @FXML
    private TableColumn<harvest.model.Product, Double> fxProductSecondPriceColumn;
    @FXML
    private TableColumn<harvest.model.Product, Double> fxProductFirstPriceColumn;

    private final AlertMaker alert = new AlertMaker();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mProductDAO.updateLiveData();
        initColumns();
        observeSelectProduct();

    }

    public void initColumns() {
        fxProductNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        fxProductTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        fxProductCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        fxProductFirstPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productFirstPrice"));
        fxProductSecondPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productSecondPrice"));
        fxProductNameTable.setItems(PRODUCT_NAME_LIVE_DATA);
        fxProductNameTable.getSelectionModel().selectFirst();
        fxProductTypeTable.setItems(PRODUCT_LIST_LIVE_DATA);
        fxProductTypeTable.getSelectionModel().selectFirst();
    }

    private void observeSelectProduct(){
        fxProductNameTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    mProductDAO.updateProductListByName(new_val);
                    fxProductTypeTable.getSelectionModel().selectFirst();
                });
    }

    @FXML
    void editItem(ActionEvent event) {
        Product product = fxProductTypeTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            alert.show("Required selected product code", "Please select a product code", AlertType.INFORMATION);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/product/add_product.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddProductController controller = loader.getController();
            controller.inflateUI(product);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
            mProductDAO.updateProductListByName(product.getProductName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteItem(ActionEvent event) {
        Product product = fxProductTypeTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            alert.show("Required selected product", "Please select a product from the second table", AlertType.INFORMATION);
            return;
        }
        Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmation.setTitle("Deletion");
        deleteConfirmation.setHeaderText("Product Delete Confirmation");
        deleteConfirmation.setContentText("Press OK to delete this product!");

        Optional<ButtonType> result = deleteConfirmation.showAndWait();
        if (result.get() == ButtonType.OK) {

                mProductDAO.deleteDataById(product.getProductId());

        } else {
            alert.show("Deletion cancelled", "Deletion process cancelled", AlertType.INFORMATION);
        }
        mProductDAO.updateLiveData();
        fxProductNameTable.getSelectionModel().selectFirst();
        fxProductTypeTable.getSelectionModel().selectFirst();
        System.out.println("Delete product...");
    }
}