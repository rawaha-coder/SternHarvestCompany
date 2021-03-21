package harvest.view;

import harvest.database.HarvestDAO;
import harvest.database.PreferencesDAO;
import harvest.model.Harvest;
import harvest.model.Preferences;
import harvest.model.ProductDetail;
import harvest.util.Calculation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DisplayGroupController implements Initializable {

    public static ObservableList<Harvest> HARVEST_GROUP_LIVE_LIST = FXCollections.observableArrayList();

    private final HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
    private Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
    @FXML private AnchorPane displayHarvestWorkUI;
    @FXML private TableView<Harvest> fxHarvestWorkTable;
    @FXML private TableColumn<Harvest, Date> fxDateColumn;
    @FXML private TableColumn<Harvest, String> fxEmployeeNameColumn;
    @FXML private TableColumn<Harvest, Double> fxAllQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxDefectiveQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxGoodQuantityColumn;
    @FXML private TableColumn<Harvest, Double> fxPriceColumn;
    @FXML private TableColumn<Harvest, Double> fxTransportAmountColumn;
    @FXML private TableColumn<Harvest, Double> fxCreditColumn;
    @FXML private TableColumn<Harvest, Double> fxAmountPayableColumn;
    @FXML private TableColumn<Harvest, String> fxRemarqueColumn;

    @FXML private TextField fxTotalEmployee;
    @FXML private TextField fxAllQuantity;
    @FXML private TextField fxBadQuality;
    @FXML private TextField fxGoodQuality;
    @FXML private TextField fxProductPriceCompany;
    @FXML private TextField fxTotalCredit;
    @FXML private TextField fxTotalTransport;
    @FXML private Label fxCompanyCharge;
    @FXML private Label fxDayFrom;
    @FXML private DatePicker fxDateFrom;
    @FXML private Label fxDayTo;
    @FXML private DatePicker fxDateTo;

    @FXML
    private Button fxSearch;

    @FXML
    void searchByDate() {
        if (fxDateFrom.getValue() == null || fxDateTo.getValue() == null) { return; }
        Date dateFrom = Date.valueOf(fxDateFrom.getValue());
        Date dateTo = Date.valueOf(fxDateTo.getValue());
        HARVEST_GROUP_LIVE_LIST.clear();
        try {
            HARVEST_GROUP_LIVE_LIST.setAll(mHarvestDAO.getHarvestGroupByDate(dateFrom, dateTo));
        }catch (Exception e){
            e.printStackTrace();
        }
        totalInput();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLiveData();
        initTable();
        totalInput();
    }

    //Initialization employee table Columns
    public void initTable(){
        fxHarvestWorkTable.setItems(HARVEST_GROUP_LIVE_LIST);
        fxDateColumn.setCellValueFactory(new PropertyValueFactory<>("harvestDate"));
        fxEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        fxAllQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxDefectiveQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("defectiveQuantity"));
        fxGoodQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("goodQuantity"));
        fxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        fxTransportAmountColumn.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxAmountPayableColumn.setCellValueFactory(new PropertyValueFactory<>("amountPayable"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
    }

    public void updateLiveData(){
        HARVEST_GROUP_LIVE_LIST.clear();
        try {
            HARVEST_GROUP_LIVE_LIST.setAll(mHarvestDAO.getHarvestGroupData(Date.valueOf(LocalDate.now())));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void totalInput() {
        double allQuantity = 0.0;
        double defectiveQuantity = 0.0;
        double totalPrice = 0.0;
        double penaltyGeneral = getPreferences().getPenaltyGeneral();
        double defectiveGeneral = getPreferences().getDefectiveGeneral();
        double totalTransport = 0.0;
        double totalCredit = 0.0;
        int totalEmployee = 0;
        if (HARVEST_GROUP_LIVE_LIST.size() > 0){
            totalEmployee = HARVEST_GROUP_LIVE_LIST.size();
            for(Harvest harvest: HARVEST_GROUP_LIVE_LIST){
                allQuantity += harvest.getAllQuantity();
                defectiveQuantity += harvest.getDefectiveQuantity();
                totalTransport += harvest.getTransportAmount();
                totalCredit += harvest.getCreditAmount();
                totalPrice += harvest.getProductPrice();
            }
        }
        double price = totalPrice / totalEmployee;
        fxTotalEmployee.setText(String.valueOf(totalEmployee));
        fxAllQuantity.setText(String.valueOf(allQuantity));
        fxBadQuality.setText(String.valueOf(defectiveQuantity));
        fxGoodQuality.setText(String.valueOf(allQuantity - defectiveQuantity));
        fxProductPriceCompany.setText(String.valueOf(price));
        fxTotalTransport.setText(String.valueOf(totalTransport));
        fxTotalCredit.setText(String.valueOf(totalCredit));
        double calculation = Calculation.employeeCharge(allQuantity, defectiveQuantity , penaltyGeneral, defectiveGeneral, price, totalCredit, totalTransport);
        fxCompanyCharge.setText(String.valueOf(calculation));
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
