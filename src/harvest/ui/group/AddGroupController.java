package harvest.ui.group;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;

import java.net.URL;
import java.util.*;

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

    @FXML private DatePicker fxHarvestDate;
    @FXML private ChoiceBox<String> fxSupplierList;
    @FXML private ChoiceBox<String> fxFarmList;
    @FXML private ChoiceBox<String> fxProductList;
    @FXML private ChoiceBox<String> fxProductCodeList;
    @FXML private TextField fxQuantityByGroup;
    @FXML private Button fxClearButton;
    @FXML private Button fxSaveButton;
    @FXML private TableView<Harvest> fxHarvestWorkTable;
    @FXML private TableColumn<Harvest, String> fxEmployeeNameColumn;
    @FXML private TableColumn<Harvest, Double> fxAllQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxBadQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxGoodQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxPriceColumn;
    @FXML private TableColumn<Harvest, Boolean> fxTransportStatusColumn;
    @FXML private TableColumn<Harvest, Double> fxCreditColumn;
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

    @FXML void clearButton() {
        fxSupplierList.setItems(getSupplierList());
        fxFarmList.setItems(getFarmList());
        fxProductList.setItems(getProductList());
        fxProductCodeList.setItems(FXCollections.emptyObservableList());
        fxHarvestDate.getEditor().setText("");
        fxQuantityByGroup.setText("");
    }

    @FXML
    void applyButton() {

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
        Harvest harvest = new Harvest();
        harvest.setHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        harvest.setAllQuantity(100);

    }

    private boolean checkInput(){
        return (
                fxHarvestDate.getValue() == null ||
                        fxSupplierList.getValue() == null ||
                        fxFarmList.getValue() == null ||
                        fxProductList.getValue() == null ||
                        fxProductCodeList.getValue() == null ||
                        !Validation.isDouble(fxQuantityByGroup.getText())
        );
    }


}
