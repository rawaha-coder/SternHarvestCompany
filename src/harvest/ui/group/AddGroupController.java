package harvest.ui.group;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
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
import java.sql.SQLException;
import java.util.*;
import java.sql.Date;

public class AddGroupController implements Initializable {

    public static ObservableList<Harvest> HARVEST_WORK_LIVE_LIST = FXCollections.observableArrayList();

    private Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private Map<String, Product> mProductMap = new LinkedHashMap<>();
    private Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();

    private final AlertMaker alert = new AlertMaker();

    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    private final PreferencesDAO mPreferencesDAO = PreferencesDAO.getInstance();
    private final ProductionDAO mProductionDAO = ProductionDAO.getInstance();

    Production production = new Production();

    @FXML private DatePicker fxHarvestDate;
    @FXML private ChoiceBox<String> fxSupplierList;
    @FXML private ChoiceBox<String> fxFarmList;
    @FXML private ChoiceBox<String> fxProductList;
    @FXML private ChoiceBox<String> fxProductCodeList;
    @FXML private TextField fxAllQuantityByG;
    @FXML private TextField fxBadQuantityByG;
    @FXML private Button fxClearButton;
    @FXML private Button fxSaveButton;
    @FXML private TableView<Harvest> fxHarvestWorkTable;
    @FXML private TableColumn<Harvest, String> fxEmployeeNameColumn;
    @FXML private TableColumn<Harvest, Double> fxAllQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxBadQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxGoodQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxPriceColumn;
    @FXML private TableColumn<Harvest, Boolean> fxTransportStatusColumn;
    @FXML private TableColumn<Harvest, String> fxCreditColumn;
    @FXML private TableColumn<Harvest, Double> fxAmountPayableColumn;
    @FXML private TableColumn<Harvest, String> fxRemarqueColumn;
    @FXML private TextField fxTotalAllQuantity;
    @FXML private TextField fxTotalBadQuantity;
    @FXML private TextField fxTotalGoodQuantity;
    @FXML private TextField fxProductPriceCompany;
    @FXML private TextField fxTotalCredit;
    @FXML private TextField fxTotalEmployee;
    @FXML private TextField fxTotalTransport;
    @FXML private Label fxCompanyCharge;
    @FXML private Button fxApplyButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxSupplierList.setItems(getSupplierList());
        fxFarmList.setItems(getFarmList());
        fxProductList.setItems(getProductList());
        observeChoiceProduct();
        updateLiveData();
        initTable();
        observeTransportStatus();
        observeCreditColumn();
        observeRemarqueColumn();
    }

    //Initialization employee table Columns
    public void initTable(){
        fxHarvestWorkTable.setItems(HARVEST_WORK_LIVE_LIST);
        fxEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        fxAllQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxBadQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("badQuantity"));
        fxGoodQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("goodQuantity"));
        fxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        fxTransportStatusColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxAmountPayableColumn.setCellValueFactory(new PropertyValueFactory<>("amountPayable"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
    }

    public void updateLiveData(){
        HARVEST_WORK_LIVE_LIST.clear();
        try {
            HARVEST_WORK_LIVE_LIST.setAll(mHarvestDAO.getHarvestData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Observe and update Transport Column Change
    private void observeTransportStatus() {
        fxTransportStatusColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportStatusColumn.setCellValueFactory(cellData -> {
            Harvest harvest = cellData.getValue();
            harvest.transportStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue){
                    if (mPreferencesDAO.getTransportPrice() == -1){
                        newValue = false;
                    }
                    harvest.setTransportAmount(mPreferencesDAO.getTransportPrice());
                }else {
                    harvest.setTransportAmount(0.0);
                }
                harvest.setTransportStatus(newValue);
            });
            return harvest.transportStatusProperty();
        });
    }

    private ObservableList<String> getSupplierList() {
        ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
        mSupplierMap.clear();
        try {
            mSupplierMap = mSupplierDAO.getSupplierMap();
            observableSupplierList.setAll(mSupplierMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableSupplierList;
    }

    private ObservableList<String> getFarmList() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        mFarmMap.clear();
        try {
            mFarmMap = mFarmDAO.getFarmMap();
            observableList.setAll(mFarmMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return observableList;
    }


    private ObservableList<String> getProductList(){
        ObservableList<String> observableList = FXCollections.observableArrayList();
        mProductMap.clear();
        try {
            mProductMap = mProductDAO.getProductMap();
            observableList.setAll(mProductMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return observableList;
    }

    private void observeChoiceProduct() {
        fxProductList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if (mProductMap.get(new_val) != null) {
                        fxProductCodeList.setItems(getProductCode(mProductMap.get(new_val)));
                    }
                });
    }

    private ObservableList<String> getProductCode(Product product) {
        ObservableList<String> observableProductCode = FXCollections.observableArrayList();
        mProductDetailMap.clear();
        try {
            mProductDetailMap = mProductDetailDAO.getProductDetailMap(product);
            observableProductCode.setAll(mProductDetailMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableProductCode;
    }

    //Observe and update Credit Column Change
    private void observeCreditColumn() {
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxCreditColumn.setCellValueFactory(cellData -> {
            Harvest harvest = cellData.getValue();
            fxCreditColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Harvest, String> t) ->
                    {
                        if (Validation.isDouble(t.getNewValue())){
                            harvest.setCreditAmount(Double.parseDouble(t.getNewValue()));
                        }else {
                            alert.missingInfo("Error");
                            observeCreditColumn();
                        }
                    }
            );
            return new SimpleStringProperty(String.valueOf(harvest.getCreditAmount()));
        });
    }

    //Observe and update Remarque Column Change
    private void observeRemarqueColumn() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellValueFactory(cellData -> {
            Harvest harvest = cellData.getValue();
            fxRemarqueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Harvest, String> t) ->
                    {
                        harvest.setRemarque(t.getNewValue());
                    }
            );
            return harvest.remarqueProperty();
        });
    }

    @FXML void clearButton() {
        fxSupplierList.setItems(getSupplierList());
        fxFarmList.setItems(getFarmList());
        fxProductList.setItems(getProductList());
        fxProductCodeList.setItems(FXCollections.emptyObservableList());
        fxHarvestDate.getEditor().setText("");
        fxAllQuantityByG.setText("");
    }

    @FXML
    void applyButton() {

        production.setProductionDate(Date.valueOf(fxHarvestDate.getValue()));
        production.setSupplierID(mSupplierMap.get(fxSupplierList.getValue()).getSupplierId());
        production.setSupplierName(mSupplierMap.get(fxSupplierList.getValue()).getSupplierName());
        production.setFarmID(mFarmMap.get(fxFarmList.getValue()).getFarmId());
        production.setFarmName(mFarmMap.get(fxFarmList.getValue()).getFarmName());
        production.setProductID(mProductMap.get(fxProductList.getValue()).getProductId());
        production.setProductName(mProductMap.get(fxProductList.getValue()).getProductName());
        production.setProductCode(mProductDetailMap.get(fxProductCodeList.getValue()).getProductCode());
        production.setProductionPrice(mProductDetailMap.get(fxProductCodeList.getValue()).getPriceCompany());
        production.setTotalEmployee(HARVEST_WORK_LIVE_LIST.size());
        production.setGoodQuantity(Double.parseDouble(fxTotalGoodQuantity.getText()));
        production.setProductionCost(Double.parseDouble(fxCompanyCharge.getText()));

        if (mProductionDAO.getProductionId(production) != -1){
            production.setProductionID(mProductionDAO.getProductionId(production));
            alert.saveItem("Production" , addHarvestEmployeeWork());
        }
    }

    private boolean addHarvestEmployeeWork() {
        boolean trackInsert = false;
        if (production.getProductionID() > 0){
            for (Harvest item : HARVEST_WORK_LIVE_LIST){
                    item.setHarvestDate(production.getProductionDate());
                    item.setProductionID(production.getProductionID());
                    trackInsert = mHarvestDAO.addHarvesters(item);
                    if (!trackInsert) break;
            }
        }
        return trackInsert;
    }


    @FXML
    void validateButton() {
        if (checkInput()) {
            alert.missingInfo("Harvest Work");
            return;
        }
        validateInput();
    }

    private void validateInput() {
        double allQuantity = Double.parseDouble(fxAllQuantityByG.getText());
        double badQuantity = Double.parseDouble(fxBadQuantityByG.getText());
        double allQuantityEmp = allQuantity / HARVEST_WORK_LIVE_LIST.size();
        double badQuantityEmp = badQuantity / HARVEST_WORK_LIVE_LIST.size();
        double priceCompany = mProductDetailMap.get(fxProductCodeList.getValue()).getPriceCompany();
        double penaltyEmployee = getPreferences().getPenaltyEmployee();
        double penaltyGeneral = getPreferences().getPenaltyGeneral();
        double totalTransport = 0.0;
        double totalCredit = 0.0;
        for(Harvest harvest: HARVEST_WORK_LIVE_LIST){
            harvest.setAllQuantity(allQuantityEmp);
            harvest.setBadQuantity(badQuantityEmp);
            harvest.setPenaltyQuality(penaltyEmployee);
            harvest.setGeneralPenaltyQuality(penaltyGeneral);
            harvest.setGoodQuantity(allQuantityEmp - badQuantityEmp);
            harvest.setProductPrice(mProductDetailMap.get(fxProductCodeList.getValue()).getPriceEmployee());
            harvest.setFarmID(mFarmMap.get(fxFarmList.getValue()).getFarmId());
            harvest.setFarmName(mFarmMap.get(fxFarmList.getValue()).getFarmName());
            harvest.setAmountPayable((harvest.getGoodQuantity() * harvest.getProductPrice()) - (harvest.getTransportAmount() + harvest.getCreditAmount()));
            harvest.setHarvestType(1);
            totalTransport += harvest.getTransportAmount();
            totalCredit += harvest.getCreditAmount();
            System.out.println(harvest.getRemarque());
        }
        fxTotalEmployee.setText(String.valueOf(HARVEST_WORK_LIVE_LIST.size()));
        fxTotalAllQuantity.setText(String.valueOf(allQuantity));
        fxTotalBadQuantity.setText(String.valueOf(badQuantity));
        fxTotalGoodQuantity.setText(String.valueOf(allQuantity - badQuantity));
        fxProductPriceCompany.setText(String.valueOf(priceCompany));
        fxTotalTransport.setText(String.valueOf(totalTransport));
        fxTotalCredit.setText(String.valueOf(totalCredit));
        fxCompanyCharge.setText(String.valueOf(((allQuantity - badQuantity) * priceCompany) - (totalTransport + totalCredit)));
    }

    private boolean checkInput(){
        return (fxHarvestDate.getValue() == null ||
                        fxSupplierList.getValue() == null ||
                        fxFarmList.getValue() == null ||
                        fxProductList.getValue() == null ||
                        fxProductCodeList.getValue() == null ||
                        !Validation.isDouble(fxAllQuantityByG.getText()) ||
                        !Validation.isDouble(fxAllQuantityByG.getText())
        );
    }

    private Preferences getPreferences(){
        PreferencesDAO preferencesDAO = PreferencesDAO.getInstance();
        Preferences preferences = new Preferences();
        try {
            preferences = preferencesDAO.getPreferences();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return preferences;
    }
}
