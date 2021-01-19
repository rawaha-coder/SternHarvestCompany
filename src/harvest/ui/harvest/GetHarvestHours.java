package harvest.ui.harvest;

import harvest.database.HarvestHoursDAO;
import harvest.model.HarvestHours;
import harvest.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;

//This class will show Harvest working hours by day for every employee
public class GetHarvestHours implements Initializable {

    public static ObservableList<HarvestHours> HARVEST_HOURS_LIVE_LIST = FXCollections.observableArrayList();
    HarvestHoursDAO mHarvestHoursDAO = HarvestHoursDAO.getInstance();

    @FXML
    private TableView<HarvestHours> fxHarvestHoursTable;
    @FXML
    private TableColumn<HarvestHours, String> fxEmployee;
    @FXML
    private TableColumn<HarvestHours, Time> fxStartMorning;
    @FXML
    private TableColumn<HarvestHours, Time> fxEndMorning;
    @FXML
    private TableColumn<HarvestHours, Time> fxStartNoon;
    @FXML
    private TableColumn<HarvestHours, Time> fxEndNoon;
    @FXML
    private TableColumn<HarvestHours, Time> fxTotalHours;
    @FXML
    private TableColumn<HarvestHours, String> fxTransport;
    @FXML
    private TableColumn<HarvestHours, String> fxCredit;
    @FXML
    private TableColumn<HarvestHours, String> fxRemarque;
    @FXML
    private DatePicker fxDatePicker;
    @FXML
    private Label fxTotalWorkingHours;
    @FXML
    private Label fxTotalTransport;
    @FXML
    private Label fxTotalCredit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date = LocalDate.now();
        mHarvestHoursDAO.updateLiveData(Date.valueOf(date));
        fxDatePicker.setValue(date);
        initTable();
        observeDatePicker();
        fxTotalWorkingHours.setText(getTotalHours());
        fxTotalTransport.setText(getTotalTransport());
        fxTotalCredit.setText(getTotalCredit());
    }

    //Initialization employee table Columns
    public void initTable(){
        fxStartMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorning.setCellValueFactory(new PropertyValueFactory<>("endMorning"));
        fxStartNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoon.setCellValueFactory(new PropertyValueFactory<>("endNoon"));
        fxTotalHours.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        fxEmployee.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());
        fxTransport.setCellValueFactory(it -> it.getValue().transportAmountProperty());
        fxCredit.setCellValueFactory(it -> it.getValue().creditAmountProperty());
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        fxHarvestHoursTable.setItems(HARVEST_HOURS_LIVE_LIST);
    }

    private void observeDatePicker(){
        fxDatePicker.valueProperty().addListener((ob, ov, nv) -> {
            mHarvestHoursDAO.updateLiveData(Date.valueOf(nv));
            fxTotalWorkingHours.setText(getTotalHours());
            fxTotalTransport.setText(getTotalTransport());
            fxTotalCredit.setText(getTotalCredit());
        });
    }

    private String getTotalHours(){
        long hours = 0;
                for (HarvestHours harvestHours : HARVEST_HOURS_LIVE_LIST){
                    hours += harvestHours.getTotalHours() ;
                }
        return Validation.timeToStringTime(hours);
    }

    private String getTotalTransport(){
        double d = 0.0;
        for (HarvestHours harvestHours : HARVEST_HOURS_LIVE_LIST){
            d += harvestHours.getTransport().getTransportAmount();
        }
        System.out.println(d);
        return String.valueOf(d);
    }

    private String getTotalCredit(){
        double d = 0.0;
        for (HarvestHours harvestHours : HARVEST_HOURS_LIVE_LIST){
            d += harvestHours.getCredit().getCreditAmount();
        }
        return String.valueOf(d);
    }
}
