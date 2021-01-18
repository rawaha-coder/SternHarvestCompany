package harvest.ui.harvest;

import harvest.database.HarvestWorkDAO;
import harvest.model.HarvestWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GetHarvestWork implements Initializable {

    public static ObservableList<HarvestWork> HARVEST_WORK_LIST_LIVE_DATA = FXCollections.observableArrayList();

    @FXML
    private TableView<HarvestWork> fxDisplayHarvestWorkTable;

    @FXML
    private TableColumn<HarvestWork, String> fxEmployeeNameColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxAllQuantityColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxBadQualityColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxGoodQualityColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxProductPriceColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxTransportColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxCreditColumn;

    @FXML
    private TableColumn<HarvestWork, Double> fxNetAmountColumn;

    @FXML
    private TableColumn<HarvestWork, String> fxRemarqueColumn;

    HarvestWorkDAO mHarvestWorkDAO = HarvestWorkDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
    }

    //Initialization employee table Columns
    public void initTable(){
        updateLiveData();
        fxDisplayHarvestWorkTable.setItems(HARVEST_WORK_LIST_LIVE_DATA);
        fxEmployeeNameColumn.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());
        fxAllQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxBadQualityColumn.setCellValueFactory(new PropertyValueFactory<>("badQuality"));
        fxGoodQualityColumn.setCellValueFactory(new PropertyValueFactory<>("goodQuality"));
        fxProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        fxTransportColumn.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxNetAmountColumn.setCellValueFactory(new PropertyValueFactory<>("netAmount"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
    }

    public void updateLiveData(){
        LocalDate date = LocalDate.now();
        HARVEST_WORK_LIST_LIVE_DATA.clear();
        try {
            HARVEST_WORK_LIST_LIVE_DATA.setAll(mHarvestWorkDAO.getHarvestWorkData(Date.valueOf(date)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
