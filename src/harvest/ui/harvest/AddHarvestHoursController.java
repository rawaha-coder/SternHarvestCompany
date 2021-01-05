package harvest.ui.harvest;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.TimeTextField;
import javafx.beans.property.BooleanProperty;
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
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.util.*;

public class AddHarvestHoursController implements Initializable {

    public static ObservableList<AddHarvestHours> HARVEST_HOURS_LIST_LIVE_DATA = FXCollections.observableArrayList();

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
    private final HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
    private final CommonDAO commonDAO = CommonDAO.getInstance();
    ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    ObservableList<String> observableProductCode = FXCollections.observableArrayList();

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
    private Button fxSaveButton;

    @FXML
    private Button fxClearButton;

    @FXML
    private TableView<AddHarvestHours> fxAddHarvestHoursTable;

    @FXML
    private TableColumn<AddHarvestHours, Boolean> fxEmployeeSelectColumn;

    @FXML
    private TableColumn<AddHarvestHours, String> fxEmployeeFullNameColumn;

    @FXML
    private TableColumn<AddHarvestHours, Boolean> fxTransportSelectColumn;

    @FXML
    private TableColumn<AddHarvestHours, String> fxRemarqueColumn;

    @FXML
    private TextField fxTotalHours;

    @FXML
    private TextField fxTotalAmount;

    @FXML
    private TextField fxTotalEmployee;

    @FXML
    private TextField fxTotalTransport;

    @FXML
    private Button fxApplyButton;

    @FXML
    private Button fxCloseButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSupplierList();
        getFarmList();
        getProductList();
        observeChoiceProduct();
        mEmployeeDAO.updateLiveData();
        initTable();
        fxRemarqueColumn.setEditable(true);
        //observeRemarqueColumn();
        observeRemarqueColumnChange();
        fxHarvester.setSelected(true);
    }

    //Initialization employee table Columns
    public void initTable(){
        updateLiveData();
        fxAddHarvestHoursTable.setItems(HARVEST_HOURS_LIST_LIVE_DATA);
        fxEmployeeSelectColumn.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        fxEmployeeFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));
        fxTransportSelectColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        observeEmployeeSelectColumn();
        observeTransportSelectColumn();
    }

    public void updateLiveData(){
        HARVEST_HOURS_LIST_LIVE_DATA.clear();
        try {
            HARVEST_HOURS_LIST_LIVE_DATA.setAll(commonDAO.getHarvestHoursData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeRemarqueColumn() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<AddHarvestHours, String> t) ->
                        System.out.println(t.getNewValue())
        );
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeEmployeeSelectColumn() {
        fxEmployeeSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxEmployeeSelectColumn.setCellValueFactory(cellData -> {
            AddHarvestHours harvestHours = cellData.getValue();
            BooleanProperty booleanProperty = harvestHours.employeeStatusProperty();
            booleanProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (mEmployeeDAO.updateEmployeeStatusById(harvestHours.getEmployeeId(), harvestHours.isEmployeeStatus())) {
                    updateLiveData();
                } else {
                    alert.show("Error", "something wrong happened", Alert.AlertType.ERROR);
                }
            });
            return booleanProperty;
        });
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeTransportSelectColumn() {
        fxTransportSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportSelectColumn.setCellValueFactory(cellData -> {
            AddHarvestHours harvestHours = cellData.getValue();
            BooleanProperty booleanProperty = harvestHours.transportStatusProperty();
            booleanProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                harvestHours.setTransportStatus(newValue);
                System.out.println(harvestHours.isTransportStatus());
                System.out.println(harvestHours.getEmployeeFullName());
            });
            return booleanProperty;
        });
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeRemarqueColumnChange() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellValueFactory(cellData -> {
            AddHarvestHours harvestHours = cellData.getValue();
            StringProperty stringProperty = harvestHours.harvestRemarqueProperty();
            fxRemarqueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<AddHarvestHours, String> t) ->
                    {
                        harvestHours.setHarvestRemarque(t.getNewValue());
                        System.out.println(harvestHours.getHarvestRemarque());
                    }
            );
            return stringProperty;
        });

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

        HarvestHoursDAO harvestHoursDAO = HarvestHoursDAO.getInstance();
        HarvestHours harvestHours = new HarvestHours();
        Harvest harvest = new Harvest();
        AddHarvestHours addHarvestHours = new AddHarvestHours();

        harvest.setHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        harvest.setSupplier(mSupplierMap.get(fxSupplierList.getValue()));
        harvest.setFarm(mFarmMap.get(fxFarmList.getValue()));
        harvest.setProduct(mProductMap.get(fxProductList.getValue()));
        harvest.setProductDetail(mProductDetailMap.get(fxProductCodeList.getValue()));

        int id = 0;
        if (mHarvestDAO.isExists(harvest) == 0){
            if (mHarvestDAO.addHarvestDate(harvest)){
                id = mHarvestDAO.getHarvestId(harvest);
            }
        }else{
            id = mHarvestDAO.getHarvestId(harvest);
        }

        int count = 0;
        if (id != 0){
            for (AddHarvestHours item : HARVEST_HOURS_LIST_LIVE_DATA){
                if (item.isEmployeeStatus()){
                    item.setStartMorning(Time.valueOf(fxStartMorningTime.getText()));
                    item.setEndMorning(Time.valueOf(fxEndMorningTime.getText()));
                    item.setStartNoon(Time.valueOf(fxStartNoonTime.getText()));
                    item.setEndNoon(Time.valueOf(fxEndNoonTime.getText()));
                    item.setEmployeeType(getEmployeeType());
                    item.setHarvestID(id);
                    item.setHarvestDate(harvest.getHarvestDate());
                    if (harvestHoursDAO.addHarvesters(item)){
                        count ++;
                    }
                }
                break;
            }
        }

        System.out.println("Employee added: " + count);
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

}
