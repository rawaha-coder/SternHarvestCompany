package harvest.view;

import harvest.database.SupplierDAO;
import harvest.database.SupplyDAO;
import harvest.model.Supplier;
import harvest.model.Supply;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplaySupplierController implements Initializable {

    public static ObservableList<Supplier> SUPPLIER_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<Supply> SUPPLY_LIST_LIVE_DATA = FXCollections.observableArrayList();

    @FXML private TableView<Supplier> fxSupplierTable;
    @FXML private TableColumn<Supplier, String> fxSupplierName;
    @FXML private TableColumn<Supplier, String> fxSupplierFirstName;
    @FXML private TableColumn<Supplier, String> fxSupplierLastName;
    @FXML private TableView<Supply> fxSupplyTable;
    @FXML private TableColumn<Supply, String> fxSupplyFarm;
    @FXML private TableColumn<Supply, String> fxSupplyProduct;

    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final SupplyDAO mSupplyDAO = SupplyDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        mSupplierDAO.updateLiveData();
        mSupplyDAO.updateLiveData();
        observeSelectSupplier();
    }

    private void initColumns() {
        fxSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fxSupplierFirstName.setCellValueFactory(new PropertyValueFactory<>("supplierFirstname"));
        fxSupplierLastName.setCellValueFactory(new PropertyValueFactory<>("supplierLastname"));
        fxSupplyFarm.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxSupplyProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        fxSupplierTable.setItems(SUPPLIER_LIST_LIVE_DATA);
        fxSupplyTable.setItems(SUPPLY_LIST_LIVE_DATA);
        fxSupplierTable.getSelectionModel().selectFirst();
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

    @FXML
    void editSupplier() {
        Supplier supplier = fxSupplierTable.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            alert.selectEditItem("Supplier");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/res/layout/add_supplier.fxml"));
            Stage stage = new Stage();
            Parent parent = loader.load();
            AddSupplierController controller = loader.getController();
            controller.inflateSupplierUI(supplier);
            stage.setTitle("Supplier Edition");
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
            alert.selectDeleteItem("Fournisseur");
            return;
        }
        AlertMaker alertDelete = new AlertMaker();
        Optional<ButtonType> result = alertDelete.deleteConfirmation("Fournisseur");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            if (mSupplierDAO.deleteSupplierData(supplier)){
                mSupplierDAO.updateLiveData();
                mSupplyDAO.updateLiveData();
                alert.deleteItem("Fournisseur", true);
            }else {
                alert.deleteItem("Fournisseur", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }
    }

    @FXML
    public void editSupply() {
        Supply supply = fxSupplyTable.getSelectionModel().getSelectedItem();
        if (supply == null) {
            alert.selectEditItem("Champ");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/res/layout/add_supplier.fxml"));
            Stage stage = new Stage();
            Parent parent = loader.load();
            AddSupplierController controller = loader.getController();
            controller.inflateSupplyUI(supply);
            stage.setTitle("Champ Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteSupply() {
        Supply supply = fxSupplyTable.getSelectionModel().getSelectedItem();
        if (supply == null) {
            alert.selectDeleteItem("Champ");
            return;
        }
        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Champ");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            if (mSupplyDAO.deleteDataById(supply)){
                mSupplierDAO.updateLiveData();
                mSupplyDAO.updateLiveData();
                alert.deleteItem("Champ", true);
            }else {
                alert.deleteItem("Champ", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }
    }
}
