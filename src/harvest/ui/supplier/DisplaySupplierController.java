package harvest.ui.supplier;

import harvest.database.SupplierDAO;
import harvest.database.SupplyDAO;
import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Supplier;
import harvest.model.Supply;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplaySupplierController implements Initializable {


    public static ObservableList<Supplier> SUPPLIER_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<Farm> SUPPLIER_FARM_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<Product> SUPPLIER_PRODUCT_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<Supply> SUPPLY_LIST_LIVE_DATA = FXCollections.observableArrayList();
    @FXML
    private AnchorPane fxTableUI;
    @FXML
    private TableView<Supplier> fxSupplierTable;
    @FXML
    private TableColumn<Supplier, String> fxSupplierName;
    @FXML
    private TableColumn<Supplier, String> fxSupplierFirstName;
    @FXML
    private TableColumn<Supplier, String> fxSupplierLastName;
    @FXML
    private TableView<Supply> fxSupplyTable;
    @FXML
    private TableColumn<Supply, String> fxSupplyFarm;
    @FXML
    private TableColumn<Supply, String> fxSupplyProduct;

    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final SupplyDAO mSupplyDAO = SupplyDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        mSupplierDAO.updateLiveData();
        fxSupplierTable.setItems(SUPPLIER_LIST_LIVE_DATA);
        mSupplyDAO.updateLiveData();
        fxSupplyTable.setItems(SUPPLY_LIST_LIVE_DATA);
        observeSelectSupplier();
        fxSupplierTable.getSelectionModel().selectFirst();

    }

    private void initColumns() {
        fxSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fxSupplierFirstName.setCellValueFactory(new PropertyValueFactory<>("supplierFirstname"));
        fxSupplierLastName.setCellValueFactory(new PropertyValueFactory<>("supplierLastname"));
        fxSupplyFarm.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxSupplyProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
    }

    private void observeSelectSupplier(){
        fxSupplierTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Supplier> ov, Supplier old_val, Supplier new_val) -> {
                    if (new_val != null){
                        try{
                            mSupplyDAO.updateLiveData(new_val);
                            fxSupplyTable.getSelectionModel().selectFirst();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void selectItem(Supplier supplier){
        fxSupplierTable.getSelectionModel().select(supplier); // no effect
        mSupplyDAO.updateLiveData(supplier);
        fxSupplyTable.getSelectionModel().selectFirst(); // no effect
    }

    @FXML
    void deleteFarm() {

    }

    @FXML
    void deleteProduct() {

    }

    @FXML
    void editSupplier(ActionEvent event) {
        Supplier supplier = fxSupplierTable.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            alert.selectEditItem("Supplier");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/supplier/add_supplier.fxml"));
            Stage stage = new Stage();
            Parent parent = loader.load();
            AddSupplierController controller = loader.getController();
            controller.inflateSupplierUI(supplier);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteSupplier() {
        Supplier supplier = fxSupplierTable.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            alert.selectDeleteItem("Supplier");
        }
        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Supplier");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            assert supplier != null;
            if (mSupplierDAO.deleteDataById(supplier.getSupplierId())){
                mSupplierDAO.updateLiveData();
                mSupplyDAO.updateLiveData();
                alert.deleteItem("Product detail", true);
            }else {
                alert.deleteItem("Product detail", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }
        System.out.println("Delete product...");
    }

    @FXML
    void editFarm(ActionEvent event) {

    }

    @FXML
    void editProduct(ActionEvent event) {

    }

    public void editSupply(ActionEvent actionEvent) {
        Supply supply = fxSupplyTable.getSelectionModel().getSelectedItem();
        if (supply == null) {
            alert.selectEditItem("Supply");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/supplier/add_supplier.fxml"));
            Stage stage = new Stage();
            Parent parent = loader.load();
            AddSupplierController controller = loader.getController();
            controller.inflateSupplyUI(supply);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSupply(ActionEvent actionEvent) {
    }
}
