package harvest.view;

import harvest.model.Production;
import harvest.presenter.DisplayHoursProductionPresenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayHoursProduction implements Initializable {

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

    DisplayHoursProductionPresenter mDisplayHoursProductionPresenter;

    @FXML
    void handleSearchButton(ActionEvent event) {
        mDisplayHoursProductionPresenter.searchByDate();
    }

    @FXML
    void handleHarvestChartButton(ActionEvent event) {
        fxHoursProductionTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        mDisplayHoursProductionPresenter = new DisplayHoursProductionPresenter(this);
    }

    private void initTable() {
        fxHarvestDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));
        fxSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fxFarmName.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        fxProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        fxTotalEmployee.setCellValueFactory(new PropertyValueFactory<>("totalEmployee"));
        fxTotalMinutes.setCellValueFactory(new PropertyValueFactory<>("totalMinutes"));
        fxProductionPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        fxProductionCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }
}
