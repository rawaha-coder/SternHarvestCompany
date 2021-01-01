package harvest.ui.harvest;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import javafx.beans.property.BooleanProperty;
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
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

import static harvest.ui.employee.DisplayEmployeeController.EMPLOYEE_LIST_LIVE_DATA;

public class AddHarvestHoursController implements Initializable {

    public static ObservableList<Employee> HARVEST_HOURS_LIST = FXCollections.observableArrayList();

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
    private TextField fxStartMorningTime;

    @FXML
    private TextField fxEndMorningTime;

    @FXML
    private TextField fxStartNoonTime;

    @FXML
    private TextField fxEndNoonTime;

    @FXML
    private RadioButton fxHarvester;

    @FXML
    private RadioButton fxController;

    @FXML
    private Button fxSaveButton;

    @FXML
    private Button fxClearButton;

    @FXML
    private TableView<Employee> fxAddHarvestHoursTable;

    @FXML
    private TableColumn<Employee, Boolean> fxEmployeeSelectColumn;

    @FXML
    private TableColumn<Employee, String> fxEmployeeFullNameColumn;

    @FXML
    private TableColumn<String, String> fxRemarqueColumn;

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
        initHarvestHoursTable();
        fxRemarqueColumn.setEditable(true);
        observeRemarqueColumn();
    }

    //Initialization employee table Columns
    private void initHarvestHoursTable() {
        fxAddHarvestHoursTable.setItems(EMPLOYEE_LIST_LIVE_DATA);
        fxEmployeeSelectColumn.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        fxEmployeeFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));
        observeEmployeeSelectColumn();

    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeRemarqueColumn() {
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<String, String> t) ->
                        System.out.println(t.getNewValue())
        );
    }

    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeEmployeeSelectColumn() {
        fxEmployeeSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxEmployeeSelectColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            BooleanProperty booleanProperty = employee.employeeStatusProperty();
            booleanProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (mEmployeeDAO.updateEmployeeStatusById(employee.getEmployeeId(), employee.isEmployeeStatus())) {
                    mEmployeeDAO.updateLiveData();
                } else {
                    alert.show("Error", "something wrong happened", Alert.AlertType.ERROR);
                }
            });
            return booleanProperty;
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
        Harvest harvest = new Harvest();
        harvest.setHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        harvest.setSupplier(mSupplierMap.get(fxSupplierList.getValue()));
        harvest.setFarm(mFarmMap.get(fxFarmList.getValue()));
        harvest.setProduct(mProductMap.get(fxProductList.getValue()));
        harvest.setProductDetail(mProductDetailMap.get(fxProductCodeList.getValue()));
        alert.saveItem("Harvest", mHarvestDAO.addHarvestDate(harvest));
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


}
