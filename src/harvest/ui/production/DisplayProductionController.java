package harvest.ui.production;

import harvest.database.ProductionDAO;
import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayProductionController implements Initializable {

    public static ObservableList<Production> PRODUCTION_LIVE_LIST = FXCollections.observableArrayList();
    ProductionDAO mProductionDAO = ProductionDAO.getInstance();

    @FXML private TableView<Production> fxHarvestTable;
    @FXML private TableColumn<Production, String> fxHarvestDate;
    @FXML private TableColumn<Production, String> fxSupplierName;
    @FXML private TableColumn<Production, String> fxFarmName;
    @FXML private TableColumn<Production, String> fxProductName;
    @FXML private TableColumn<Production, String> fxProductCode;
    @FXML private TableColumn<Production, Integer> fxTotalEmployee;
    @FXML private TableColumn<Production, Double> fxGoodQuantity;
    @FXML private TableColumn<Production, Double> fxProductionPrice;
    @FXML private TableColumn<Production, Double> fxProductionCost;

    @FXML private Button fxReLoadUI;
    @FXML private Label fxFromDate;
    @FXML private DatePicker fxDatePickerFrom;
    @FXML private Label fxToDate;
    @FXML private DatePicker fxDatePickerTo;
    @FXML private Button fxSearch;

    @FXML void handleReloadUI(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {

    }

    @FXML
    void harvestChart(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        fxHarvestTable.setItems(PRODUCTION_LIVE_LIST);
    }

    private void initTable() {
        fxHarvestDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));
        fxSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fxFarmName.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        fxProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        fxTotalEmployee.setCellValueFactory(new PropertyValueFactory<>("totalEmployee"));
        fxGoodQuantity.setCellValueFactory(new PropertyValueFactory<>("goodQuantity"));
        fxProductionPrice.setCellValueFactory(new PropertyValueFactory<>("productionPrice"));
        fxProductionCost.setCellValueFactory(new PropertyValueFactory<>("productionCost"));

    }
}
