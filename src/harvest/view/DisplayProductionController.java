package harvest.view;

import harvest.database.ProductionDAO;
import harvest.model.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLiveData();
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

    public void updateLiveData(){
        PRODUCTION_LIVE_LIST.clear();
        try {
            PRODUCTION_LIVE_LIST.setAll(mProductionDAO.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleSearch() {
        if (fxDatePickerFrom.getValue() != null && fxDatePickerTo.getValue() != null){
            LocalDate fromDate = fxDatePickerFrom.getValue();
            LocalDate toDate = fxDatePickerTo.getValue();
            updateData(fromDate, toDate);
        }
    }

    public void updateData(LocalDate fromDate, LocalDate toDate){
        PRODUCTION_LIVE_LIST.clear();
        try {
            PRODUCTION_LIVE_LIST.setAll(mProductionDAO.searchDataByDate(Date.valueOf(fromDate), Date.valueOf(toDate)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void harvestChart() {
        if (fxDatePickerFrom.getValue() != null && fxDatePickerTo.getValue() != null){
            LocalDate fromDate = fxDatePickerFrom.getValue();
            LocalDate toDate = fxDatePickerTo.getValue();
            final Stage stage = new Stage();
            try {
                String location = "/harvest/res/layout/harvest_chart.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
                Parent parent = loader.load();
                HarvestChart controller = loader.getController();
                controller.getProductionChart(Date.valueOf(fromDate), Date.valueOf(toDate));
                stage.setTitle("Harvest Chart Production");
                stage.setScene(new Scene(parent));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML void handleReloadUI(ActionEvent event) {

    }

}
