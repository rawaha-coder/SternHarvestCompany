package harvest.ui.group;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class AddGroupController {

    public static ObservableList<Harvest> HARVEST_WORK_LIVE_LIST = FXCollections.observableArrayList();

    private final Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
    private final Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    //private final HarvestWorkDAO mHarvestWorkDAO = HarvestWorkDAO.getInstance();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    //private final HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();
    ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    ObservableList<String> observableProductCode = FXCollections.observableArrayList();
    //private final HarvestProduction mHarvestProduction = new HarvestProduction();

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
    @FXML private TableColumn<Harvest, Double> fxTransportSelectColumn;
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

    @FXML void clearButton(ActionEvent event) {

    }

    @FXML
    void handleApplyButton(ActionEvent event) {

    }

    @FXML
    void handleSaveButton(ActionEvent event) {

    }

}
