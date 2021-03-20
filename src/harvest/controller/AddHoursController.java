package harvest.controller;

import harvest.database.*;
import harvest.model.*;
import harvest.presenter.AddHoursPresenter;
import harvest.util.AlertMaker;
import harvest.util.TimeTextField;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.Time;
import java.util.*;

import static harvest.presenter.AddHoursPresenter.ADD_HOURS_LIVE_DATA;

public class AddHoursController implements Initializable {

    public final AlertMaker alert = new AlertMaker();
    AddHoursPresenter mAddHoursPresenter;

    @FXML public DatePicker fxHarvestDate;
    @FXML public ChoiceBox<String> fxSupplierList;
    @FXML public ChoiceBox<String> fxFarmList;
    @FXML public ChoiceBox<String> fxProductList;
    @FXML public ChoiceBox<String> fxProductCodeList;
    @FXML public TimeTextField fxStartMorningTime;
    @FXML public TimeTextField fxEndMorningTime;
    @FXML public TimeTextField fxStartNoonTime;
    @FXML public TimeTextField fxEndNoonTime;
    @FXML public ToggleGroup fxEmployeeType;
    @FXML public RadioButton fxHarvester;
    @FXML public RadioButton fxController;
    @FXML public TextField fxHourPrice;

    @FXML public TableView<Hours> fxAddHarvestHoursTable;
    @FXML public TableColumn<Hours, String> fxEmployeeColumn;
    @FXML public TableColumn<Hours, Time> fxStartMorningColumn;
    @FXML public TableColumn<Hours, Time> fxEndMorningColumn;
    @FXML public TableColumn<Hours, Time> fxStartNoonColumn;
    @FXML public TableColumn<Hours, Time> fxEndNoonColumn;
    @FXML public TableColumn<Hours, Long> fxDurationColumn;
    @FXML public TableColumn<Hours, Double> fxPriceColumn;
    @FXML public TableColumn<Hours, Boolean> fxTransportColumn;
    @FXML public TableColumn<Hours, String> fxCreditColumn;
    @FXML public TableColumn<Hours, Double> fxPaymentColumn;
    @FXML public TableColumn<Hours, String> fxRemarqueColumn;

    @FXML public TextField fxTotalHours;
    @FXML public TextField fxTotalCredit;
    @FXML public TextField fxTotalEmployee;
    @FXML public TextField fxTotalTransport;
    @FXML public TextField fxTotalPayment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mAddHoursPresenter = new AddHoursPresenter(this);
        mAddHoursPresenter.updateHoursDataLive();
        mAddHoursPresenter.initList();
        initTable();
        onChangeTransportCell();
        fxHarvester.setSelected(true);
        fxTotalHours.setText("0.0");
        fxTotalCredit.setText("0.0");
        fxTotalEmployee.setText("0");
        fxTotalTransport.setText("0.0");
        fxHourPrice.setText("0.0");
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void initTable() {
        fxEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        fxStartMorningColumn.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorningColumn.setCellValueFactory(new PropertyValueFactory<>("endMorning"));
        fxStartNoonColumn.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoonColumn.setCellValueFactory(new PropertyValueFactory<>("endNoon"));
        fxDurationColumn.setCellValueFactory(new PropertyValueFactory<>("totalMinutes"));
        fxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("hourPrice"));
        fxTransportColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditString"));
        fxPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        fxAddHarvestHoursTable.setItems(ADD_HOURS_LIVE_DATA);
    }

    private void onChangeTransportCell() {
        PreferencesDAO mPreferencesDAO = PreferencesDAO.getInstance();
        fxTransportColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportColumn.setCellValueFactory(cellData -> {
            Hours hours = cellData.getValue();
            hours.transportStatusProperty()
                    .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue) {
                            if (mPreferencesDAO.getTransportPrice() == -1) {
                                newValue = false;
                            }
                            hours.getTransport().setTransportAmount(mPreferencesDAO.getTransportPrice());
                            System.out.println(hours.getTransport().getTransportAmount());
                        } else {
                            hours.getTransport().setTransportAmount(0.0);
                        }
                        hours.setTransportStatus(newValue);
                    });
            return hours.transportStatusProperty();
        });
    }

    public void onChangeCreditCell(TableColumn.CellEditEvent cellEditEvent){
        Hours hoursSelected = fxAddHarvestHoursTable.getSelectionModel().getSelectedItem();
        hoursSelected.getCredit().setCreditAmount(Double.parseDouble(cellEditEvent.getNewValue().toString()));
    }

    public void onChangeRemarqueCell(TableColumn.CellEditEvent cellEditEvent){
        Hours hoursSelected = fxAddHarvestHoursTable.getSelectionModel().getSelectedItem();
        hoursSelected.setRemarque(cellEditEvent.getNewValue().toString());
    }

    @FXML
    void handleValidButton() {
        if (checkInput()) {
            alert.missingInfo("Harvest hours");
            return;
        }
        mAddHoursPresenter.validateAddHoursWork();
    }

    private boolean checkInput() {
        return (fxHarvestDate.getValue() == null ||
                fxSupplierList.getValue() == null ||
                fxFarmList.getValue() == null ||
                fxProductList.getValue() == null ||
                fxProductCodeList.getValue() == null);
    }

    @FXML
    public void handleApplyButton() {
        if (fxTotalCredit.getText().isEmpty()
                || fxTotalTransport.getText().isEmpty()
                || fxTotalEmployee.getText().isEmpty()
                || fxTotalHours.getText().isEmpty()
                || fxTotalPayment.getText().isEmpty()
        ) {
            alert.missingInfo("Hours");
            return;
        }
        alert.saveItem("Production" , mAddHoursPresenter.applyAddHoursWork());
    }

    @FXML
    void handleClearButton() {
        mAddHoursPresenter.initList();
        fxHarvestDate.getEditor().setText("");
        fxStartMorningTime.setText("00:00:00");
        fxEndMorningTime.setText("00:00:00");
        fxStartNoonTime.setText("00:00:00");
        fxEndNoonTime.setText("00:00:00");
        fxEmployeeType.selectToggle(fxHarvester);
        Time time = new Time(0);
        for(Hours hours: ADD_HOURS_LIVE_DATA){
            hours.setStartMorning(time);
            hours.setEndMorning(time);
            hours.setStartNoon(time);
            hours.setEndNoon(time);
            hours.setTotalMinutes(0);
            hours.setHourPrice(0.0);
            hours.setTransportStatus(false);
            hours.setRemarque("");
            hours.getCredit().setCreditAmount(0.0);
            hours.getTransport().setTransportAmount(0.0);
            hours.setPayment(0.0);
        }
        fxAddHarvestHoursTable.refresh();
    }
}
