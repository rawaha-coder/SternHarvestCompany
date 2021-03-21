package harvest.view;

import harvest.model.Hours;
import harvest.presenter.DisplayHoursPresenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

import static harvest.presenter.DisplayHoursPresenter.DISPLAY_HOURS_LIVE_DATA;

public class DisplayHoursController implements Initializable {

    @FXML public TableView<Hours> fxHarvestHoursTable;
    @FXML public TableColumn<Hours, String> fxEmployee;
    @FXML public TableColumn<Hours, Time> fxStartMorning;
    @FXML public TableColumn<Hours, Time> fxEndMorning;
    @FXML public TableColumn<Hours, Time> fxStartNoon;
    @FXML public TableColumn<Hours, Time> fxEndNoon;
    @FXML public TableColumn<Hours, Time> fxTotalHours;
    @FXML public TableColumn<Hours, String> fxTransport;
    @FXML public TableColumn<Hours, String> fxCredit;
    @FXML public TableColumn<Hours, String> fxCategory;
    @FXML public TableColumn<Hours, String> fxRemarque;
    @FXML public DatePicker fxDatePicker;
    @FXML public Label fxTotalWorkingHours;
    @FXML public Label fxTotalTransport;
    @FXML public Label fxTotalCredit;

    DisplayHoursPresenter mDisplayHoursPresenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        mDisplayHoursPresenter = new DisplayHoursPresenter(this);
        //mDisplayHoursPresenter.initList();
    }

    //Initialization employee table Columns
    public void initTable(){
        fxStartMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorning.setCellValueFactory(new PropertyValueFactory<>("endMorning"));
        fxStartNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoon.setCellValueFactory(new PropertyValueFactory<>("endNoon"));
        fxTotalHours.setCellValueFactory(new PropertyValueFactory<>("totalMinutes"));
        fxEmployee.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        fxTransport.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCredit.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxCategory.setCellValueFactory(new PropertyValueFactory<>("employeeCategory"));
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        fxHarvestHoursTable.setItems(DISPLAY_HOURS_LIVE_DATA);
    }
    
}
