package harvest.ui.harvest;

import harvest.database.HarvestProductionDAO;
import harvest.model.HarvestProduction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GetHarvestProduction implements Initializable {

    public static ObservableList<HarvestProduction> HARVEST_PRODUCTION_LIVE_LIST = FXCollections.observableArrayList();
    HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();

    @FXML
    private AnchorPane fxGetHarvestProductionUI;

    @FXML
    private TableView<HarvestProduction> fxHarvestProductionTable;

    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestProductionDate;

    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestProductionSupplier;

    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestProductionFarm;

    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestProductionProduct;

    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestProductionCodeProduct;

    @FXML
    private TableColumn<HarvestProduction, Integer> fxHarvestProductionNumberEmployee;

    @FXML
    private TableColumn<HarvestProduction, Double> fxHarvestProductionQuantity;

    @FXML
    private TableColumn<HarvestProduction, Double> fxHarvestProductionPrice;

    @FXML
    private TableColumn<HarvestProduction, Double> fxHarvestProductionCost;

    @FXML
    private Label fxDate;

    @FXML
    private DatePicker fxDatePicker;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        updateData();
    }

    private void initTable() {
        fxHarvestProductionTable.setItems(HARVEST_PRODUCTION_LIVE_LIST);
        fxHarvestProductionDate.setCellValueFactory(new PropertyValueFactory<>("harvestProductionDate"));
        fxHarvestProductionSupplier.setCellValueFactory(it -> it.getValue().getSupplier().supplierNameProperty());
        fxHarvestProductionFarm.setCellValueFactory(it -> it.getValue().getFarm().farmNameProperty());
        fxHarvestProductionProduct.setCellValueFactory(it -> it.getValue().getProduct().productNameProperty() );
        fxHarvestProductionCodeProduct.setCellValueFactory(it -> it.getValue().getProductDetail().productCodeProperty());
        fxHarvestProductionNumberEmployee.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalEmployee"));
        fxHarvestProductionQuantity.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalQuantity"));
        fxHarvestProductionPrice.setCellValueFactory(new PropertyValueFactory<>("harvestProductionPrice"));
        fxHarvestProductionCost.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalCost"));
    }

    public void updateData(){
        HARVEST_PRODUCTION_LIVE_LIST.clear();
        try {
            HARVEST_PRODUCTION_LIVE_LIST.setAll(mHarvestProductionDAO.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
