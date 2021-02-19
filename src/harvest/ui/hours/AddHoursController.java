package harvest.ui.hours;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.TimeTextField;
import harvest.util.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class AddHoursController implements Initializable {

    public static ObservableList<Hours> HARVEST_HOURS_LIVE_LIST = FXCollections.observableArrayList();

    private final Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
    private final Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    //private final HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();
    private final HoursDAO mHoursDAO = HoursDAO.getInstance();
    ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    ObservableList<String> observableProductCode = FXCollections.observableArrayList();
    //private final HarvestProduction mHarvestProduction = new HarvestProduction();

    @FXML
    private AnchorPane addHarvestHoursUI;
    @FXML
    private DatePicker fxHarvestDate;
    @FXML
    private ChoiceBox<String> fxSupplierList;
    @FXML
    private ChoiceBox<String> fxFarmList;
    @FXML
    private ChoiceBox<String> fxProductList;
    @FXML
    private ChoiceBox<String> fxProductCodeList;
    @FXML
    private TimeTextField fxStartMorningTime;
    @FXML
    private TimeTextField fxEndMorningTime;
    @FXML
    private TimeTextField fxStartNoonTime;
    @FXML
    private TimeTextField fxEndNoonTime;
    @FXML
    private RadioButton fxHarvester;
    @FXML
    private RadioButton fxController;
    @FXML
    private TextField fxHourPrice;
    @FXML
    private TextField fxCalculateResult;
    @FXML
    private TableView<Hours> fxAddHarvestHoursTable;
    @FXML
    private TableColumn<Hours, Boolean> fxEmployeeSelectColumn;
    @FXML
    private TableColumn<Hours, String> fxEmployeeFullNameColumn;
    @FXML
    private TableColumn<Hours, Boolean> fxTransportSelectColumn;
    @FXML
    private TableColumn<Hours, String> fxCreditColumn;
    @FXML
    private TableColumn<Hours, String> fxRemarqueColumn;
    @FXML
    private TextField fxTotalHours;
    @FXML
    private TextField fxTotalCredit;
    @FXML
    private TextField fxTotalEmployee;
    @FXML
    private TextField fxTotalTransport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSupplierList();
        getFarmList();
        getProductList();
        observeChoiceProduct();
        mEmployeeDAO.updateLiveData();
        initTable();
        fxRemarqueColumn.setEditable(true);
        observeRemarqueColumnChange();
        fxHarvester.setSelected(true);
        fxTotalHours.setText("0.0");
        fxTotalCredit.setText("0.0");
        fxTotalEmployee.setText("0");
        fxTotalTransport.setText("0.0");
        fxHourPrice.setText("0.0");
    }

    //Initialization employee table Columns
    public void initTable(){
        updateLiveData();
        fxAddHarvestHoursTable.setItems(HARVEST_HOURS_LIVE_LIST);
        fxEmployeeSelectColumn.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        fxEmployeeFullNameColumn.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());
        fxTransportSelectColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        observeEmployeeSelectColumn();
        observeTransportSelectColumn();
        observeCreditColumnChange();
    }

    public void updateLiveData(){
        HARVEST_HOURS_LIVE_LIST.clear();
        try {
            HARVEST_HOURS_LIVE_LIST.setAll(mHoursDAO.getHarvestHoursData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeEmployeeSelectColumn() {
        fxEmployeeSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxEmployeeSelectColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            hours.getEmployee().employeeStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (mEmployeeDAO.updateEmployeeStatusById(hours.getEmployee().getEmployeeId(), hours.getEmployee().isEmployeeStatus())) {
                    hours.getEmployee().setEmployeeStatus(newValue);
                } else {
                    alert.show("Error", "something wrong happened", Alert.AlertType.ERROR);
                }
            });
            return hours.getEmployee().employeeStatusProperty();
        });
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeTransportSelectColumn() {
        fxTransportSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportSelectColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            hours.transportStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                hours.setTransportStatus(newValue);
                hours.getTransport().setTransportAmount(10.0);
            });
            return hours.transportStatusProperty();
        });
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeCreditColumnChange() {
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxCreditColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            fxCreditColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Hours, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            hours.getCredit().setCreditAmount(Double.parseDouble(t.getNewValue()));
                        }else {
                            alert.missingInfo("Error");
                            observeCreditColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(hours.getCredit().getCreditAmount()));
        });
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeRemarqueColumnChange() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            StringProperty stringProperty = hours.harvestRemarqueProperty();
            fxRemarqueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Hours, String> t) ->
                    {
                        hours.setHarvestRemarque(t.getNewValue());
                        System.out.println(hours.getHarvestRemarque());
                    }
            );
            return stringProperty;
        });
    }

    @FXML
    void handleClearButton() {
        getSupplierList();
        getFarmList();
        getProductList();
        fxHarvestDate.getEditor().setText("");
    }

    @FXML
    void handleCloseButton() {
        Stage stage = (Stage) addHarvestHoursUI.getScene().getWindow();
        stage.close();
    }

    private void getSupplierList() {
        observableSupplierList.clear();
        try {
            List<Supplier> suppliers = new ArrayList<>(mSupplierDAO.getData());
            if (suppliers.size() > 0) {
                for (Supplier supplier : suppliers) {
                    observableSupplierList.add(supplier.getSupplierName());
                    mSupplierMap.put(supplier.getSupplierName(), supplier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxSupplierList.setItems(observableSupplierList);
    }

    //fill the ChoiceBox by employee list
    private void getFarmList() {
        observableFarmList.clear();
        try {
            List<Farm> farms = new ArrayList<>(mFarmDAO.getFarmData());
            if (farms.size() > 0) {
                for (Farm farm : farms) {
                    observableFarmList.add(farm.getFarmName());
                    mFarmMap.put(farm.getFarmName(), farm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxFarmList.setItems(observableFarmList);
    }

    //fill the ChoiceBox by employee list
    private void getProductList() {
        observableProductList.clear();
        try {
            List<Product> products = new ArrayList<>(mProductDAO.getData());
            if (products.size() > 0) {
                for (Product product : products) {
                    observableProductList.add(product.getProductName());
                    mProductMap.put(product.getProductName(), product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxProductList.setItems(observableProductList);
    }

    private void observeChoiceProduct() {
        fxProductList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            if (mProductMap.get(new_val) != null) {
                getProductCode(mProductMap.get(new_val));
            }
        });
    }


    //fill the ChoiceBox by employee list
    private void getProductCode(Product product) {
        observableProductCode.clear();
        try {
            List<ProductDetail> productDetails = new ArrayList<>(mProductDetailDAO.getProductDetail(product));
            if (productDetails.size() > 0) {
                for (ProductDetail productDetail : productDetails) {
                    observableProductCode.add(productDetail.getProductCode());
                    mProductDetailMap.put(productDetail.getProductCode(), productDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxProductCodeList.setItems(observableProductCode);
    }

    private int getEmployeeType(){
        if(fxController.isSelected()){
            return 1;
        }else {
            return 0;
        }
    }


    @FXML
    void handleSaveButton() {
//        if (checkInput())
//        {
//            alert.missingInfo("Harvest");
//            return;
//        }
//
//        HoursDAO hoursDAO = HoursDAO.getInstance();
////        mHarvestProduction.setHarvestProductionDate(Date.valueOf(fxHarvestDate.getValue()));
////        mHarvestProduction.setHarvestProductionHarvestType(3);
////        mHarvestProduction.getSupplier().setSupplierId(mSupplierMap.get(fxSupplierList.getValue()).getSupplierId());
////        mHarvestProduction.getFarm().setFarmId(mFarmMap.get(fxFarmList.getValue()).getFarmId());
////        mHarvestProduction.getProduct().setProductId(mProductMap.get(fxProductList.getValue()).getProductId());
////        mHarvestProduction.getProductDetail().setProductDetailId(mProductDetailMap.get(fxProductCodeList.getValue()).getProductDetailId());
//
//        /*if (mHarvestProductionDAO.isExists(mHarvestProduction) == 0){
////            if (mHarvestProductionDAO.addHarvestProduction(mHarvestProduction)){
////                mHarvestProduction.setHarvestProductionID(mHarvestProductionDAO.getHarvestProductionId(mHarvestProduction));
////            }
//        }else{
//            mHarvestProduction.setHarvestProductionID(mHarvestProductionDAO.getHarvestProductionId(mHarvestProduction));
//        }*/
//
//        int count = 0;
//        if (mHarvestProduction.getHarvestProductionID() != 0){
//            for (Hours item : HARVEST_HOURS_LIVE_LIST){
//                if (item.getEmployee().isEmployeeStatus()){
//                    item.setHarvestDate(mHarvestProduction.getHarvestProductionDate());
//                    item.setStartMorning(Time.valueOf(fxStartMorningTime.getText()));
//                    item.setEndMorning(Time.valueOf(fxEndMorningTime.getText()));
//                    item.setStartNoon(Time.valueOf(fxStartNoonTime.getText()));
//                    item.setEndNoon(Time.valueOf(fxEndNoonTime.getText()));
//                    item.setEmployeeType(getEmployeeType());
//                    item.getHarvestProduction().setHarvestProductionID(mHarvestProduction.getHarvestProductionID());
//                    item.getTransport().setFarmId(mHarvestProduction.getFarm().getFarmId());
//                    if (hoursDAO.addHarvesters(item)){
//                        count ++;
//                    }
//                }
//            }
//        }
//
//        fxTotalEmployee.setText(String.valueOf(count));
//        long time = getTotalSecondWork(HARVEST_HOURS_LIVE_LIST);
//        fxTotalHours.setText(timeToStringTime(time));
//        fxTotalCredit.setText(String.valueOf(getTotalCredit()));
//        fxTotalTransport.setText(String.valueOf(getTotalTransport()));
//        mHarvestProduction.setHarvestProductionID(mHarvestProduction.getHarvestProductionID());
//        System.out.println("Employee added: " + count);
    }

    private long getTotalSecondWork(ObservableList<Hours> list){
        long totalSeconds = 0;
        for (Hours item : list){
            if (item.getEmployee().isEmployeeStatus()){
              totalSeconds +=  item.getEndMorning().getTime() - item.getStartMorning().getTime();
              totalSeconds += item.getEndNoon().getTime() - item.getStartNoon().getTime();
            }
        }
        return (int) totalSeconds;
    }

    private String timeToStringTime(long time) {
        int sec = (int) time/1000;
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }

    @FXML
    public void handleApplyButton(){
        if (fxTotalCredit.getText().isEmpty()
                || fxTotalTransport.getText().isEmpty()
                || fxTotalEmployee.getText().isEmpty()
                || fxTotalHours.getText().isEmpty())
        {
            alert.missingInfo("Harvest");
            return;
        }
//        mHarvestProduction.setHarvestProductionTotalCost(Double.parseDouble(fxTotalCredit.getText()));
//        mHarvestProduction.setHarvestProductionTotalTransport(Double.parseDouble(fxTotalTransport.getText()));
//        //alert.saveItem("Harvest Production", mHarvestProductionDAO.updateHarvestProduction(mHarvestProduction));
    }

    @FXML
    void handleCalculateResultButton() {
        double result;
        result =((getTotalMilliSeconds()/1000) * (Double.parseDouble(fxHourPrice.getText()) * 3600)) - (getTotalTransport() + getTotalCredit());
        fxCalculateResult.setText(String.valueOf(result));
    }

    private double getTotalMilliSeconds(){
        long hours = 0;
        for (Hours harvestHours : HARVEST_HOURS_LIVE_LIST){
            hours += harvestHours.getTotalHours() ;
        }
        return (double) hours;
    }

    private double getTotalTransport(){
        double d = 0.0;
        for (Hours hours : HARVEST_HOURS_LIVE_LIST){
            d += hours.getTransport().getTransportAmount();
        }

        return d;
    }

    private double getTotalCredit(){
        double d = 0.0;
        for (Hours hours : HARVEST_HOURS_LIVE_LIST){
            d += hours.getCredit().getCreditAmount();
        }
        return d;
    }

    private boolean checkInput(){
        return (fxHarvestDate.getValue() == null ||
                fxSupplierList.getValue() == null ||
                fxFarmList.getValue() == null ||
                fxProductList.getValue() == null ||
                fxProductCodeList.getValue() == null);
    }
}
