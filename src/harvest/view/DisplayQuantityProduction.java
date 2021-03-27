package harvest.view;

import harvest.model.Production;
import harvest.presenter.DisplayHoursProductionPresenter;
import harvest.presenter.DisplayQuantityProductionPresenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayQuantityProduction implements Initializable {

    @FXML
    public TableView<Production> fxHoursProductionTable;
    @FXML
    public TableColumn<Production, String> fxHarvestDate;
    @FXML
    public TableColumn<Production, String> fxSupplierName;
    @FXML
    public TableColumn<Production, String> fxFarmName;
    @FXML
    public TableColumn<Production, String> fxProductName;
    @FXML
    public TableColumn<Production, String> fxProductCode;
    @FXML
    public TableColumn<Production, Integer> fxTotalEmployee;
    @FXML
    public TableColumn<Production, Long> fxTotalMinutes;
    @FXML
    public TableColumn<Production, Double> fxProductionPrice;
    @FXML
    public TableColumn<Production, Double> fxProductionCost;

    @FXML
    public Label fxFromDate;
    @FXML
    public DatePicker fxDatePickerFrom;
    @FXML
    public Label fxToDate;
    @FXML
    public DatePicker fxDatePickerTo;
    @FXML
    public Button fxSearch;

    DisplayQuantityProductionPresenter mDisplayQuantityProductionPresenter;

    @FXML
    void handleSearchButton(ActionEvent event) {
        mDisplayQuantityProductionPresenter.searchByDate();
    }

    @FXML
    void handleHarvestChartButton(ActionEvent event) {
        fxHoursProductionTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        mDisplayQuantityProductionPresenter = new DisplayQuantityProductionPresenter(this);
    }

    private void initTable() {
        fxHarvestDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));
        fxSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fxFarmName.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        fxProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        fxTotalEmployee.setCellValueFactory(new PropertyValueFactory<>("totalEmployee"));
        fxTotalMinutes.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        fxProductionPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        fxProductionCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }
}
