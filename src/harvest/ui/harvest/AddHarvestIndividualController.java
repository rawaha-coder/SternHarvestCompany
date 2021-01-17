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

public class AddHarvestIndividualController implements Initializable {

    public static ObservableList<HarvestIndividual> HARVEST_INDIVIDUAL_LIST_LIVE_DATA = FXCollections.observableArrayList();

    private final Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
    private final Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    private final HarvestIndividualDAO mHarvestIndividualDAO = HarvestIndividualDAO.getInstance();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    private final HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
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
    private TableView<HarvestIndividual> fxAddHarvestHoursTable;
    @FXML
    private TableColumn<HarvestIndividual, Boolean> fxEmployeeSelectColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxEmployeeFullNameColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxAllQuantityColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxBadQualityColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxGoodQualityColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxPriceColumn;
    @FXML
    private TableColumn<HarvestIndividual, Boolean> fxTransportSelectColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxCreditColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxNetAmountColumn;
    @FXML
    private TableColumn<HarvestIndividual, String> fxRemarqueColumn;
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
        fxAddHarvestHoursTable.setItems(HARVEST_INDIVIDUAL_LIST_LIVE_DATA);
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
        HARVEST_INDIVIDUAL_LIST_LIVE_DATA.clear();
        try {
            HARVEST_INDIVIDUAL_LIST_LIVE_DATA.setAll(mHarvestIndividualDAO.getHarvestIndividualData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeEmployeeSelectColumn() {
        fxEmployeeSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxEmployeeSelectColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            BooleanProperty booleanProperty = harvestIndividual.employeeStatusProperty();
            booleanProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (mEmployeeDAO.updateEmployeeStatusById(harvestIndividual.getEmployeeId(), harvestIndividual.isEmployeeStatus())) {
                    harvestIndividual.setEmployeeStatus(newValue);
                } else {
                    alert.show("Error", "something wrong happened", Alert.AlertType.ERROR);
                }
            });
            return booleanProperty;
        });
    }

    //Observe and update All Quantity Column Change
    private void observeAllQuantityColumnChange() {
        fxAllQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxAllQuantityColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            fxAllQuantityColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestIndividual, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestIndividual.setAllQuantity(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestIndividual.getAllQuantity());
                            harvestIndividual.getGoodQuality();
                        }else {
                            alert.missingInfo("Error");
                            observeAllQuantityColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestIndividual.getAllQuantity()));
        });
    }

    //Observe and update Bad Quality Column Change
    private void observeBadQualityColumnChange() {
        fxBadQualityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxBadQualityColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            fxBadQualityColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestIndividual, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestIndividual.setBadQuality(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestIndividual.getBadQuality());
                            harvestIndividual.getGoodQuality();
                        }else {
                            alert.missingInfo("Error");
                            observeBadQualityColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestIndividual.getBadQuality()));
        });
    }

    //Observe and update Price Column Change
    private void observePriceColumnChange() {
        fxPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxPriceColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            fxPriceColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestIndividual, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestIndividual.setPrice(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestIndividual.getPrice());
                        }else {
                            alert.missingInfo("Error");
                            observePriceColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestIndividual.getPrice()));
        });
    }

    //Observe and update Transport Column Change
    private void observeTransportSelectColumn() {
        fxTransportSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportSelectColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            harvestIndividual.transportStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue){
                    harvestIndividual.setTransportAmount(10.0);
                }else {
                    harvestIndividual.setTransportAmount(0.0);
                }
                harvestIndividual.setTransportStatus(newValue);
                System.out.println(harvestIndividual.transportStatusProperty().get());
                System.out.println(harvestIndividual.getTransportAmount());
            });
            return harvestIndividual.transportStatusProperty();
        });
    }

    //Observe and update Credit Column Change
    private void observeCreditColumnChange() {
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxCreditColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            fxCreditColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestIndividual, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestIndividual.setCreditAmount(Double.parseDouble(t.getNewValue()));
                        }else {
                            alert.missingInfo("Error");
                            observeCreditColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestIndividual.getCreditAmount()));
        });
    }

    //Observe and update Net Amount Column Change
    private void observeNetAmountColumnChange() {
        fxNetAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxNetAmountColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            fxNetAmountColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestIndividual, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvestIndividual.setNetAmount(Double.parseDouble(t.getNewValue()));
                            System.out.println(harvestIndividual.getNetAmount());
                        }else {
                            alert.missingInfo("Error");
                            observeBadQualityColumnChange();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvestIndividual.getNetAmount()));
        });
    }

    //Observe and update Remarque Column Change
    private void observeRemarqueColumnChange() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellValueFactory(cellData -> {
            HarvestIndividual harvestIndividual = cellData.getValue();
            fxRemarqueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<HarvestIndividual, String> t) ->
                    {
                        harvestIndividual.setHarvestRemarque(t.getNewValue());
                        System.out.println(harvestIndividual.getHarvestRemarque());
                    }
            );
            return harvestIndividual.harvestRemarqueProperty();
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
        if (fxHarvestDate.getValue() == null
                || fxSupplierList.getValue() == null
                || fxFarmList.getValue() == null
                || fxProductList.getValue() == null
                || fxProductCodeList.getValue() == null)
        {
            alert.missingInfo("Harvest");
            return;
        }

        HarvestIndividualDAO harvestIndividualDAO = HarvestIndividualDAO.getInstance();
        Harvest harvest = new Harvest();
        harvest.setHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        harvest.setSupplier(mSupplierMap.get(fxSupplierList.getValue()));
        harvest.setFarm(mFarmMap.get(fxFarmList.getValue()));
        harvest.setProduct(mProductMap.get(fxProductList.getValue()));
        harvest.setProductDetail(mProductDetailMap.get(fxProductCodeList.getValue()));

        if (mHarvestDAO.isExists(harvest) == 0){
            if (mHarvestDAO.addHarvestDate(harvest)){
                harvest.setHarvestID(mHarvestDAO.getHarvestId(harvest));

            }
        }else{
            harvest.setHarvestID(mHarvestDAO.getHarvestId(harvest));
        }

        System.out.println("HJarvest id: " + harvest.getHarvestID());
        int count = 0;
        double allQuantity = 0.0;
        double badQuality = 0.0;
        double netAmount = 0.0;
        if (harvest.getHarvestID() != 0){
            for (HarvestIndividual item : HARVEST_INDIVIDUAL_LIST_LIVE_DATA){
                if (item.isEmployeeStatus()){
                    item.setHarvestDate(harvest.getHarvestDate());
                    item.setHarvestType(getHarvestType());
                    item.setHarvestID(harvest.getHarvestID());
                    item.setFarmId(harvest.getFarm().getFarmId());
                    if (harvestIndividualDAO.addHarvesters(item)){
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

        mHarvestProduction.setHarvest(harvest);
        mHarvestProduction.setHarvestID(harvest.getHarvestID());
        mHarvestProduction.setHarvestProductionDate(harvest.getHarvestDate());
        mHarvestProduction.setHarvestProductionPrice1(harvest.getProductDetail().getProductFirstPrice());
        mHarvestProduction.setHarvestProductionPrice2(harvest.getProductDetail().getProductSecondPrice());
        mHarvestProduction.setHarvestProductionTotalEmployee(count);
        mHarvestProduction.setHarvestProductionTotalTransport(Double.parseDouble(fxTotalTransport.getText()));
        mHarvestProduction.setHarvestProductionTotalCredit(Double.parseDouble(fxTotalCredit.getText()));
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
        for (HarvestIndividual harvestIndividual : HARVEST_INDIVIDUAL_LIST_LIVE_DATA){
            d += harvestIndividual.getTransportAmount();
        }

        return d;
    }

    private double getTotalCredit(){
        double d = 0.0;
        for (HarvestIndividual harvestIndividual : HARVEST_INDIVIDUAL_LIST_LIVE_DATA){
            d += harvestIndividual.getCreditAmount();
        }
        return d;
    }

}
