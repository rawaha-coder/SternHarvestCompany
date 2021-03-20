package harvest.controller;

import harvest.database.HoursDAO;
import harvest.model.Hours;
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

public class DisplayHoursController implements Initializable {

    public static ObservableList<Hours> DISPLAY_HOURS_LIVE_DATA = FXCollections.observableArrayList();
    HoursDAO mHoursDAO = HoursDAO.getInstance();

    @FXML private TableView<Hours> fxHarvestHoursTable;
    @FXML private TableColumn<Hours, String> fxEmployee;
    @FXML private TableColumn<Hours, Time> fxStartMorning;
    @FXML private TableColumn<Hours, Time> fxEndMorning;
    @FXML private TableColumn<Hours, Time> fxStartNoon;
    @FXML private TableColumn<Hours, Time> fxEndNoon;
    @FXML private TableColumn<Hours, Time> fxTotalHours;
    @FXML private TableColumn<Hours, String> fxTransport;
    @FXML private TableColumn<Hours, String> fxCredit;
    @FXML private TableColumn<Hours, String> fxRemarque;
    @FXML private DatePicker fxDatePicker;
    @FXML private Label fxTotalWorkingHours;
    @FXML private Label fxTotalTransport;
    @FXML private Label fxTotalCredit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date = LocalDate.now();
        mHoursDAO.updateLiveData(Date.valueOf(date));
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
//        fxEmployee.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());
//        fxTransport.setCellValueFactory(it -> it.getValue().transportAmountProperty());
//        fxCredit.setCellValueFactory(it -> it.getValue().creditAmountProperty());
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        fxHarvestHoursTable.setItems(DISPLAY_HOURS_LIVE_DATA);
    }

    private void observeDatePicker(){
        fxDatePicker.valueProperty().addListener((ob, ov, nv) -> {
            mHoursDAO.updateLiveData(Date.valueOf(nv));
            fxTotalWorkingHours.setText(getTotalHours());
            fxTotalTransport.setText(getTotalTransport());
            fxTotalCredit.setText(getTotalCredit());
        });
    }

    private String getTotalHours(){
        long hours = 0;
//                for (Hours harvestHours : HARVEST_HOURS_LIVE_LIST){
//                    hours += harvestHours.getTotalHours() ;
//                }
        return Validation.timeToStringTime(hours);
    }

    private String getTotalTransport(){
        double d = 0.0;
//        for (Hours hours : HARVEST_HOURS_LIVE_LIST){
//            d += hours.getTransport().getTransportAmount();
//        }
        System.out.println(d);
        return String.valueOf(d);
    }

    private String getTotalCredit(){
        double d = 0.0;
//        for (Hours hours : HARVEST_HOURS_LIVE_LIST){
//            d += hours.getCredit().getCreditAmount();
//        }
        return String.valueOf(d);
    }
}
