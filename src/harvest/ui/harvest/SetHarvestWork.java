package harvest.ui.harvest;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.beans.property.*;
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
import java.util.*;

public class SetHarvestWork implements Initializable {

    public static ObservableList<HarvestWork> HARVEST_WORK_LIVE_LIST = FXCollections.observableArrayList();

    private final Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
    private final Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    private final HarvestWorkDAO mHarvestWorkDAO = HarvestWorkDAO.getInstance();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    private final HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();
    ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    ObservableList<String> observableProductCode = FXCollections.observableArrayList();
    private final HarvestProduction mHarvestProduction = new HarvestProduction();

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
    private RadioButton fxIndividual;
    @FXML
    private RadioButton fxGroup;
    @FXML
    private TextField fxProductPrice;
    @FXML
    private TextField fxCalculateResult;
    @FXML
    private TableView<HarvestWork> fxAddHarvestHoursTable;
    @FXML
    private TableColumn<HarvestWork, Boolean> fxEmployeeSelectColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxEmployeeFullNameColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxAllQuantityColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxBadQualityColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxGoodQualityColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxPriceColumn;
    @FXML
    private TableColumn<HarvestWork, Boolean> fxTransportSelectColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxCreditColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxNetAmountColumn;
    @FXML
    private TableColumn<HarvestWork, String> fxRemarqueColumn;
    @FXML
    private TextField fxAllQuantity;
    @FXML
    private TextField fxBadQuality;
    @FXML
    private TextField fxGoodQuality;
    @FXML
    private TextField fxTotalCredit;
    @FXML
    private TextField fxTotalEmployee;
    @FXML
    private TextField fxTotalTransport;
    @FXML
    private TextField fxTotalNet;

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
        fxIndividual.setSelected(true);
        fxNetAmountColumn.setEditable(true);
        fxAllQuantity.setText("0.0");
        fxBadQuality.setText("0.0");
        fxGoodQuality.setText("0.0");
        fxProductPrice.setText("0.0");
        fxTotalCredit.setText("0.0");
        fxTotalEmployee.setText("0");
        fxTotalTransport.setText("0.0");
        fxTotalNet.setText("0.0");

    }

    //Initialization employee table Columns
    public void initTable(){
        updateLiveData();
        fxAddHarvestHoursTable.setItems(HARVEST_WORK_LIVE_LIST);
        fxEmployeeSelectColumn.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        fxEmployeeFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));
        fxAllQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxBadQualityColumn.setCellValueFactory(new PropertyValueFactory<>("badQuality"));
        fxGoodQualityColumn.setCellValueFactory(new PropertyValueFactory<>("goodQuality"));
        fxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        fxTransportSelectColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxNetAmountColumn.setCellValueFactory(new PropertyValueFactory<>("netAmount"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        observeEmployeeSelectColumn();
        observeAllQuantityColumnChange();
        observeBadQualityColumnChange();
        observePriceColumnChange();
        observeTransportSelectColumn();
        observeCreditColumnChange();
        observeNetAmountColumnChange();
    }

    public void updateLiveData(){
        HARVEST_WORK_LIVE_LIST.clear();
        try {
            HARVEST_WORK_LIVE_LIST.setAll(mHarvestWorkDAO.getHarvestWorkData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeEmployeeSelectColumn() {
        fxEmployeeSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxEmployeeSelectColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            harvestWork.getEmployee().employeeStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (mEmployeeDAO.updateEmployeeStatusById(harvestWork.getEmployee().getEmployeeId(), harvestWork.getEmployee().isEmployeeStatus())) {
                    harvestWork.getEmployee().setEmployeeStatus(newValue);
                } else {
                    alert.show("Error", "something wrong happened", Alert.AlertType.ERROR);
                }
            });
            return harvestWork.getEmployee().employeeStatusProperty();
        });
    }

    //Observe and update All Quantity Column Change
    private void observeAllQuantityColumnChange() {
        fxAllQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxAllQuantityColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            fxAllQuantityColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestWork, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestWork.setAllQuantity(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestWork.getAllQuantity());
                            harvestWork.getGoodQuality();
                        }else {
                            alert.missingInfo("Error");
                            observeAllQuantityColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestWork.getAllQuantity()));
        });
    }

    //Observe and update Bad Quality Column Change
    private void observeBadQualityColumnChange() {
        fxBadQualityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxBadQualityColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            fxBadQualityColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestWork, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestWork.setBadQuality(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestWork.getBadQuality());
                            harvestWork.getGoodQuality();
                        }else {
                            alert.missingInfo("Error");
                            observeBadQualityColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestWork.getBadQuality()));
        });
    }

    //Observe and update Price Column Change
    private void observePriceColumnChange() {
        fxPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxPriceColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            fxPriceColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestWork, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestWork.setProductPrice(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestWork.getProductPrice());
                        }else {
                            alert.missingInfo("Error");
                            observePriceColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestWork.getProductPrice()));
        });
    }

    //Observe and update Transport Column Change
    private void observeTransportSelectColumn() {
        fxTransportSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportSelectColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            harvestWork.transportStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue){
                    harvestWork.getTransport().setTransportAmount(10.0);
                }else {
                    harvestWork.getTransport().setTransportAmount(0.0);
                }
                harvestWork.setTransportStatus(newValue);
            });
            return harvestWork.transportStatusProperty();
        });
    }

    //Observe and update Credit Column Change
    private void observeCreditColumnChange() {
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxCreditColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            fxCreditColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestWork, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestWork.getCredit().setCreditAmount(Double.parseDouble(t.getNewValue()));
                        }else {
                            alert.missingInfo("Error");
                            observeCreditColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestWork.getCreditAmount()));
        });
    }

    //Observe and update Net Amount Column Change
    private void observeNetAmountColumnChange() {
        fxNetAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxNetAmountColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            fxNetAmountColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestWork, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestWork.setNetAmount(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestWork.getNetAmount());
                        }else {
                            alert.missingInfo("Error");
                            observeBadQualityColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestWork.getNetAmount()));
        });
    }

    //Observe and update Remarque Column Change
    private void observeRemarqueColumnChange() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellValueFactory(cellData -> {
            HarvestWork harvestWork = cellData.getValue();
            fxRemarqueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestWork, String> t) ->
                    {
                        harvestWork.setHarvestRemarque(t.getNewValue());
                        System.out.println(harvestWork.getHarvestRemarque());
                    }
            );
            return harvestWork.harvestRemarqueProperty();
        });
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

    private int getHarvestType(){
        if(fxGroup.isSelected()){
            return 1;
        }else {
            return 0;
        }
    }

    @FXML
    void handleClearButton() {
        getSupplierList();
        getFarmList();
        getProductList();
        fxHarvestDate.getEditor().setText("");
    }

    @FXML
    void handleSaveButton() {
        if (checkInput()) {
            alert.missingInfo("Harvest Work");
            return;
        }

        HarvestWorkDAO harvestWorkDAO = HarvestWorkDAO.getInstance();
        HarvestProduction harvestProduction = new HarvestProduction();
        harvestProduction.setHarvestProductionDate(Date.valueOf(fxHarvestDate.getValue()));
        harvestProduction.setHarvestProductionHarvestType(1);
        harvestProduction.setSupplier(mSupplierMap.get(fxSupplierList.getValue()));
        harvestProduction.setFarm(mFarmMap.get(fxFarmList.getValue()));
        harvestProduction.setProduct(mProductMap.get(fxProductList.getValue()));
        harvestProduction.setProductDetail(mProductDetailMap.get(fxProductCodeList.getValue()));

        if (mHarvestProductionDAO.isExists(harvestProduction) == 0){
            if (mHarvestProductionDAO.addHarvestProduction(harvestProduction)){
                harvestProduction.setHarvestProductionID(mHarvestProductionDAO.getHarvestProductionId(harvestProduction));

            }
        }else{
            harvestProduction.setHarvestProductionID(mHarvestProductionDAO.getHarvestProductionId(harvestProduction));
        }

        int count = 0;
        double allQuantity = 0.0;
        double badQuality = 0.0;
        double netAmount = 0.0;
        if (harvestProduction.getHarvestProductionID() != 0){
            for (HarvestWork item : HARVEST_WORK_LIVE_LIST){
                if (item.getEmployee().isEmployeeStatus()){
                    item.setHarvestDate(harvestProduction.getHarvestProductionDate());
                    item.setHarvestType(getHarvestType());
                    item.setHarvestProductionID(harvestProduction.getHarvestProductionID());
                    item.getFarm().setFarmId(harvestProduction.getFarm().getFarmId());
                    if (harvestWorkDAO.addHarvesters(item)){
                        count ++;
                        allQuantity += item.getAllQuantity();
                        badQuality += item.getBadQuality();
                        netAmount += item.getNetAmount();
                    }else{
                        alert.missingInfo("Error");
                    }
                }
            }
        }

        fxTotalEmployee.setText(String.valueOf(count));
        fxAllQuantity.setText(String.valueOf(allQuantity));
        fxBadQuality.setText(String.valueOf(badQuality));
        fxGoodQuality.setText(String.valueOf(allQuantity - badQuality));
        fxTotalNet.setText(String.valueOf(netAmount));

        fxTotalCredit.setText(String.valueOf(getTotalCredit()));
        fxTotalTransport.setText(String.valueOf(getTotalTransport()));

//        mHarvestProduction.setHarvest(harvest);
//        mHarvestProduction.setHarvestID(harvest.getHarvestID());
//        mHarvestProduction.setHarvestProductionDate(harvest.getHarvestDate());
//        mHarvestProduction.setHarvestProductionPrice1(harvest.getProductDetail().getProductFirstPrice());
//        mHarvestProduction.setHarvestProductionPrice2(harvest.getProductDetail().getProductSecondPrice());
//        mHarvestProduction.setHarvestProductionTotalEmployee(count);
//        mHarvestProduction.setHarvestProductionTotalTransport(Double.parseDouble(fxTotalTransport.getText()));
//        mHarvestProduction.setHarvestProductionTotalCredit(Double.parseDouble(fxTotalCredit.getText()));
        System.out.println("Employee added: " + count);
    }

    @FXML
    public void handleApplyButton(){
        if (fxTotalEmployee.getText().isEmpty()
                || fxAllQuantity.getText().isEmpty()
                || fxBadQuality.getText().isEmpty()
                || fxGoodQuality.getText().isEmpty()
                || fxProductPrice.getText().isEmpty()
                || fxTotalTransport.getText().isEmpty()
                || fxTotalCredit.getText().isEmpty()
                || fxTotalNet.getText().isEmpty()
                )
        {
            alert.missingInfo("Harvest");
            return;
        }
        mHarvestProduction.setHarvestProductionTotalAmount(Double.parseDouble(fxTotalCredit.getText()));
        mHarvestProduction.setHarvestProductionTotalTransport(Double.parseDouble(fxTotalTransport.getText()));
        HarvestProductionDAO harvestProductionDAO = HarvestProductionDAO.getInstance();
        alert.saveItem("Harvest Production", harvestProductionDAO.addHarvestProduction(mHarvestProduction));
    }

    @FXML
    void handleCalculateResultButton() {
        double result = 0;
        //result =((getTotalMilliSeconds()/1000) * (Double.parseDouble(fxHourPrice.getText()) * 3600)) - (getTotalTransport() + getTotalCredit());
        fxCalculateResult.setText(String.valueOf(result));
    }

    private double getTotalTransport(){
        double d = 0.0;
        for (HarvestWork harvestWork : HARVEST_WORK_LIVE_LIST){
            d += harvestWork.getTransportAmount();
        }

        return d;
    }

    private double getTotalCredit(){
        double d = 0.0;
        for (HarvestWork harvestWork : HARVEST_WORK_LIVE_LIST){
            d += harvestWork.getCreditAmount();
        }
        return d;
    }

    private boolean checkInput(){
        return (
                fxHarvestDate.getValue() == null ||
                fxSupplierList.getValue() == null ||
                fxFarmList.getValue() == null ||
                fxProductList.getValue() == null ||
                fxProductCodeList.getValue() == null
        );
    }
}
