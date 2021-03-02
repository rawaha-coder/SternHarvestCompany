package harvest.ui.hours;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.Calculation;
import harvest.util.TimeTextField;
import harvest.util.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class AddHoursController implements Initializable {

    public static ObservableList<Hours> HARVEST_HOURS_LIVE_LIST = FXCollections.observableArrayList();
    public TextField fxCalculateResult;

    private Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private Map<String, Product> mProductMap = new LinkedHashMap<>();
    private Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    private final HoursDAO mHoursDAO = HoursDAO.getInstance();

    @FXML private DatePicker fxHarvestDate;
    @FXML private ChoiceBox<String> fxSupplierList;
    @FXML private ChoiceBox<String> fxFarmList;
    @FXML private ChoiceBox<String> fxProductList;
    @FXML private ChoiceBox<String> fxProductCodeList;
    @FXML private TimeTextField fxStartMorningTime;
    @FXML private TimeTextField fxEndMorningTime;
    @FXML private TimeTextField fxStartNoonTime;
    @FXML private TimeTextField fxEndNoonTime;
    @FXML private RadioButton fxHarvester;
    @FXML private RadioButton fxController;
    @FXML private TextField fxHourPrice;
    @FXML private TableView<Hours> fxAddHarvestHoursTable;
    @FXML private TableColumn<Hours, String> fxEmployeeNameColumn;
    @FXML private TableColumn<Hours, Time> fxStartMorningColumn;
    @FXML private TableColumn<Hours, Time> fxEndMorningColumn;
    @FXML private TableColumn<Hours, Time> fxStartNoonColumn;
    @FXML private TableColumn<Hours, Time> fxEndNoonColumn;
    @FXML private TableColumn<Hours, Time> fxDurationColumn;
    @FXML private TableColumn<Hours, Double> fxPriceColumn;
    @FXML private TableColumn<Hours, Boolean> fxTransportSelectColumn;
    @FXML private TableColumn<Hours, String> fxCreditColumn;
    @FXML private TableColumn<Hours, String> fxRemarqueColumn;
    @FXML private TextField fxTotalHours;
    @FXML private TextField fxTotalCredit;
    @FXML private TextField fxTotalEmployee;
    @FXML private TextField fxTotalTransport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSupplierList();
        getFarmList();
        getProductList();
        observeChoiceProduct();
        updateLiveData();
        initTable();
        observeTransportColumn();
        observeCreditColumn();
        observeRemarqueColumn();
        fxHarvester.setSelected(true);
        fxTotalHours.setText("0.0");
        fxTotalCredit.setText("0.0");
        fxTotalEmployee.setText("0");
        fxTotalTransport.setText("0.0");
        fxHourPrice.setText("0.0");
    }

    //Initialization employee table Columns
    public void initTable() {
        fxAddHarvestHoursTable.setItems(HARVEST_HOURS_LIVE_LIST);
        fxEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        fxStartMorningColumn.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorningColumn.setCellValueFactory(new PropertyValueFactory<>("endMorning"));
        fxStartNoonColumn.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoonColumn.setCellValueFactory(new PropertyValueFactory<>("endNoon"));
        fxDurationColumn.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        fxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("hourPrice"));
        fxTransportSelectColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
    }

    public void updateLiveData() {
        HARVEST_HOURS_LIVE_LIST.clear();
        try {
            HARVEST_HOURS_LIVE_LIST.setAll(mHoursDAO.getAddHoursData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fill the ChoiceBox by supplier name
    private void getSupplierList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mSupplierMap = mSupplierDAO.getSupplierMap();
            list.setAll(mSupplierMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxSupplierList.setItems(list);
    }

    //fill the ChoiceBox by Farm name
    private void getFarmList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mFarmMap = mFarmDAO.getFarmMap();
            list.setAll(mFarmMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxFarmList.setItems(list);
    }

    //fill the ChoiceBox by product name
    private void getProductList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mProductMap = mProductDAO.getProductMap();
            list.setAll(mProductMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxProductList.setItems(list);
    }

    private void observeChoiceProduct() {
        fxProductList.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if (mProductMap.get(new_val) != null) {
                        getProductCode(mProductMap.get(new_val));
                    }else {
                        fxProductCodeList.getSelectionModel().clearSelection();
                    }
                });
    }

    //fill the ChoiceBox by Product Code
    private void getProductCode(Product product) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mProductDetailMap = mProductDetailDAO.getProductDetailMap(product);
            list.setAll(mProductDetailMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxProductCodeList.setItems(list);
    }

    private int getEmployeeType() {
        if (fxController.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    private void observeTransportColumn() {
        PreferencesDAO mPreferencesDAO = PreferencesDAO.getInstance();
        fxTransportSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportSelectColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            hours.transportStatusProperty()
                    .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue) {
                            if (mPreferencesDAO.getTransportPrice() == -1) {
                                newValue = false;
                            }
                            hours.setTransportAmount(mPreferencesDAO.getTransportPrice());
                        } else {
                            hours.setTransportAmount(0.0);
                        }
                        hours.setTransportStatus(newValue);
                    });
            return hours.transportStatusProperty();
        });
    }

    private void observeCreditColumn() {
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxCreditColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            fxCreditColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Hours, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            hours.setCreditAmount(Double.parseDouble(t.getNewValue()));
                        }else {
                            alert.missingInfo("Error");
                            observeCreditColumn();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(hours.getCreditAmount()));
        });
    }

    private void observeRemarqueColumn() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            fxRemarqueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Hours, String> t) ->
                    {
                        hours.setRemarque(t.getNewValue());
                    }
            );
            return hours.remarqueProperty();
        });
    }

    @FXML
    void handleValidButton() {
        if (checkInput()) {
            alert.missingInfo("Harvest hours");
            return;
        }
        validateInput();
    }

    private boolean checkInput() {
        return (fxHarvestDate.getValue() == null ||
                fxSupplierList.getValue() == null ||
                fxFarmList.getValue() == null ||
                fxProductList.getValue() == null ||
                fxProductCodeList.getValue() == null);
    }

    private void validateInput() {
        PreferencesDAO preferencesDAO = PreferencesDAO.getInstance();
        double price = preferencesDAO.getHourPrice();
        double totalMinute = 0.0;
        double totalTransport = 0.0;
        double totalCredit = 0.0;
        for(Hours hours: HARVEST_HOURS_LIVE_LIST){
            hours.setHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
            hours.setSupplierID(mSupplierMap.get(fxSupplierList.getValue()).getSupplierId());
            hours.setSupplierName(mSupplierMap.get(fxSupplierList.getValue()).getSupplierName());
            hours.setFarmID(mFarmMap.get(fxFarmList.getValue()).getFarmId());
            hours.setFarmName(mFarmMap.get(fxFarmList.getValue()).getFarmName());
            hours.setProductID(mProductMap.get(fxProductList.getValue()).getProductId());
            hours.setProductName(mProductMap.get(fxProductList.getValue()).getProductName());
            hours.setProductCode(mProductDetailMap.get(fxProductCodeList.getValue()).getProductCode());

            hours.setStartMorning(Time.valueOf(fxStartMorningTime.getText()));
            hours.setEndMorning(Time.valueOf(fxEndMorningTime.getText()));
            hours.setStartNoon(Time.valueOf(fxStartNoonTime.getText()));
            hours.setEndNoon(Time.valueOf(fxEndNoonTime.getText()));

            hours.setTotalHours(hours.getTotalMinute());
            hours.setHourPrice(price);
            hours.setEmployeeType(getEmployeeType());
            totalMinute += hours.getTotalHours();
            totalTransport += hours.getTransportAmount();
            totalCredit += hours.getCreditAmount();
        }
        fxTotalEmployee.setText(String.valueOf(HARVEST_HOURS_LIVE_LIST.size()));
        fxTotalHours.setText(String.valueOf(totalMinute));
        fxHourPrice.setText(String.valueOf(price));
        fxTotalTransport.setText(String.valueOf(totalTransport));
        fxTotalCredit.setText(String.valueOf(totalCredit));
        fxCalculateResult.setText(String.valueOf(Calculation.hoursCharge(totalMinute, price, totalCredit, totalTransport)));
    }

    @FXML
    public void handleApplyButton() {
        if (fxTotalCredit.getText().isEmpty()
                || fxTotalTransport.getText().isEmpty()
                || fxTotalEmployee.getText().isEmpty()
                || fxTotalHours.getText().isEmpty()) {
            alert.missingInfo("Hours");
            return;
        }
        alert.saveItem("Production" , addHarvestHoursWork());

    }

    private boolean addHarvestHoursWork() {
        boolean trackInsert = false;

            for (Hours item : HARVEST_HOURS_LIVE_LIST){
                trackInsert = mHoursDAO.addHarvestHours(item);
                if (!trackInsert) break;
            }

        return trackInsert;
    }


    @FXML
    void handleClearButton() {
        getSupplierList();
        getFarmList();
        getProductList();
        fxHarvestDate.getEditor().setText("");
        fxStartMorningTime.setText("00:00:00");
        fxEndMorningTime.setText("00:00:00");
        fxStartNoonTime.setText("00:00:00");
        fxEndNoonTime.setText("00:00:00");
        Time time = new Time(0);
        for(Hours hours: HARVEST_HOURS_LIVE_LIST){
            hours.setStartMorning(time);
            hours.setEndMorning(time);
            hours.setStartNoon(time);
            hours.setEndNoon(time);
            hours.setTotalHours(0);
            hours.setHourPrice(0.0);
            hours.setTransportStatus(false);
            hours.setCreditAmount(0.0);
            hours.setRemarque("");
        }
    }
}
