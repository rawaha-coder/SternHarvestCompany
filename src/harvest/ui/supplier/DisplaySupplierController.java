package harvest.ui.supplier;

import harvest.model.Supplier;
import harvest.util.AlertMaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DisplaySupplierController implements Initializable {

    public static ObservableList<Supplier> SUPPLIER_LIST_LIVE_DATA = FXCollections.observableArrayList();

    @FXML
    private TableView<Supplier> fxSupplierTable;

    @FXML
    private TableColumn<Supplier, String> fxSupplierName;

    @FXML
    private TableColumn<Supplier, String> fxSupplierFirstName;

    @FXML
    private TableColumn<Supplier, String> fxSupplierLastName;

    @FXML
    private TableColumn<Supplier, String> fxSupplierProduct;

    private final AlertMaker alert = new AlertMaker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
    }

    private void initColumns() {
        fxSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fxSupplierFirstName.setCellValueFactory(new PropertyValueFactory<>("supplierFirstname"));
        fxSupplierLastName.setCellValueFactory(new PropertyValueFactory<>("supplierLastname"));
        fxSupplierProduct.setCellValueFactory(new PropertyValueFactory<>("SupplierProduct"));
        fxSupplierTable.setItems(SUPPLIER_LIST_LIVE_DATA);
    }

    @FXML
    void deleteItem() {
        Supplier supplier = fxSupplierTable.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            alert.selectDeleteItem("Supplier");
        }
    }

    @FXML
    void editItem() {
        Supplier supplier = fxSupplierTable.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            alert.selectEditItem("Supplier");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/ui/supplier/add_supplier.fxml"));
            Stage stage = new Stage();
            Parent parent = loader.load();
            AddSupplierController controller = loader.getController();
            controller.inflateUI(supplier);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
