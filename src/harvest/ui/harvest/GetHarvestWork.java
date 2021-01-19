package harvest.ui.harvest;

import harvest.database.HarvestWorkDAO;
import harvest.model.HarvestWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static harvest.ui.harvest.SetHarvestWork.HARVEST_WORK_LIVE_LIST;

public class GetHarvestWork implements Initializable {

    public static ObservableList<HarvestWork> HARVEST_WORK_LIVE_LIST = FXCollections.observableArrayList();
    HarvestWorkDAO mHarvestWorkDAO = HarvestWorkDAO.getInstance();

    @FXML
    private TableView<HarvestWork> fxHarvestWorkTable;
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

    @FXML
    private DatePicker fxDatePicker;
    @FXML
    private Label fxTotalTransport;
    @FXML
    private Label fxTotalCredit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date = LocalDate.now();
        updateData(date);
        fxDatePicker.setValue(date);
        initTable();
        observeDatePicker();
        fxTotalTransport.setText(getTotalTransport());
        fxTotalCredit.setText(getTotalCredit());
    }

    //Initialization employee table Columns
    public void initTable() {
        fxEmployeeNameColumn.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());
        fxAllQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxBadQualityColumn.setCellValueFactory(new PropertyValueFactory<>("badQuality"));
        fxGoodQualityColumn.setCellValueFactory(new PropertyValueFactory<>("goodQuality"));
        fxProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        fxTransportColumn.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxNetAmountColumn.setCellValueFactory(new PropertyValueFactory<>("netAmount"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        fxHarvestWorkTable.setItems(HARVEST_WORK_LIVE_LIST);
    }

    public void updateData(LocalDate date){
        HARVEST_WORK_LIVE_LIST.clear();
        try {
            HARVEST_WORK_LIVE_LIST.setAll(mHarvestWorkDAO.getData(Date.valueOf(date)));
            System.out.println(" Update list size: " + HARVEST_WORK_LIVE_LIST.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void observeDatePicker() {
        fxDatePicker.valueProperty().addListener((ob, ov, nv) -> {
            updateData(nv);
            fxTotalTransport.setText(getTotalTransport());
            fxTotalCredit.setText(getTotalCredit());
        });
    }

    private String getTotalTransport() {
        double d = 0.0;
        for (HarvestWork harvestWork : HARVEST_WORK_LIVE_LIST) {
            d += harvestWork.getTransport().getTransportAmount();
        }
        System.out.println(d);
        return String.valueOf(d);
    }

    private String getTotalCredit() {
        double d = 0.0;
        for (HarvestWork harvestWork : HARVEST_WORK_LIVE_LIST) {
            d += harvestWork.getCredit().getCreditAmount();
        }
        return String.valueOf(d);
    }

//    public void updateLiveData(){
//        LocalDate date = LocalDate.now();
//        HARVEST_WORK_LIVE_LIST.clear();
//        try {
//            HARVEST_WORK_LIVE_LIST.setAll(mHarvestWorkDAO.getHarvestWorkData(Date.valueOf(date)));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
