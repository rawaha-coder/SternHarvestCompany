package harvest.controller;

import harvest.database.EmployeeDAO;
import harvest.database.FarmDAO;
import harvest.database.TransportDAO;
import harvest.model.Employee;
import harvest.model.Farm;
import harvest.model.Transport;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AddTransportController implements Initializable {

    private final AlertMaker alert = new AlertMaker();
    private final TransportDAO mTransportDAO = TransportDAO.getInstance();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();

    private Map<String, Employee> employeeMap = new LinkedHashMap<>();
    private Map<String, Farm> farmMap = new LinkedHashMap<>();
    private boolean isEditStatus = false;
    private Transport mTransport;

    @FXML private AnchorPane fxAddItemUI;
    @FXML private DatePicker fxTransportDate;
    @FXML private ComboBox<String> fxEmployeeList;
    @FXML private TextField fxTransportAmount;
    @FXML private ComboBox<String> fxFarmList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mTransport = new Transport();
        fxTransportDate.setValue(LocalDate.now());
        fxTransportDate.setEditable(false);
        setEmployeeList();
        setFarmList();
    }

    private void setFarmList() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        farmMap.clear();
        try {
            farmMap = mFarmDAO.getFarmMap();
            observableList.setAll(farmMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxFarmList.setItems(observableList);
        fxFarmList.getSelectionModel().clearSelection();
    }

    private void setEmployeeList() {
        ObservableList<String> employeeNameList = FXCollections.observableArrayList();
        employeeMap.clear();
        try {
            employeeMap = mEmployeeDAO.getEmployeeMap();
            employeeNameList.setAll(employeeMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxEmployeeList.setItems(employeeNameList);
        fxEmployeeList.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSaveButton() {
        if (validateInput()){
            alert.missingInfo("Transport");
            return;
        }
        if (isEditStatus){
            editTransport();
        }else {
            addNewTransport();
        }
    }

    private void addNewTransport() {
        Transport transport = new Transport();
        transport.setTransportDate(Date.valueOf(fxTransportDate.getValue()));
        transport.setTransportAmount(Double.parseDouble(fxTransportAmount.getText()));
        transport.getEmployee().setEmployeeId(employeeMap.get(fxEmployeeList.getValue()).getEmployeeId());
        transport.getFarm().setFarmId(farmMap.get(fxFarmList.getValue()).getFarmId());
        if (mTransportDAO.addData(transport)){
            mTransportDAO.updateLiveData();
            alert.saveItem("Transport", true);
        }else {
            alert.saveItem("Transport", false);
        }
        handleClearButton();
    }

    private boolean validateInput() {
        return Validation.isEmpty(
                fxTransportDate.getEditor().getText()
                , fxEmployeeList.getValue()
                , fxTransportAmount.getText()
                , fxFarmList.getValue()
        ) || !Validation.isDouble(fxTransportAmount.getText());
    }

    public void editTransport() {
        mTransport.setTransportDate(Date.valueOf(fxTransportDate.getValue()));
        mTransport.setTransportAmount(Double.parseDouble(fxTransportAmount.getText()));
        mTransport.getEmployee().setEmployeeId(employeeMap.get(fxEmployeeList.getValue()).getEmployeeId());
        mTransport.getFarm().setFarmId(farmMap.get(fxFarmList.getValue()).getFarmId());
        if (mTransportDAO.editData(mTransport)){
            mTransportDAO.updateLiveData();
            alert.updateItem("Transport", true);
        }else {
            alert.updateItem("Transport", false);
        }
        handleCloseButton();
    }

    @FXML
    void handleClearButton() {
        fxTransportDate.getEditor().setText("");
        setEmployeeList();
        fxTransportAmount.setText("");
        setFarmList();
    }

    @FXML
    void handleCloseButton() {
        isEditStatus = false;
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Transport transport) {
        fxTransportDate.setValue(transport.getTransportDate().toLocalDate());
        fxEmployeeList.getSelectionModel().select(transport.getEmployeeName());
        fxTransportAmount.setText(String.valueOf(transport.getTransportAmount()));
        fxFarmList.getSelectionModel().select(transport.getFarmName());
        isEditStatus = true;
        mTransport.setTransportId(transport.getTransportId());
        mTransport.getEmployee().setEmployeeId(transport.getEmployee().getEmployeeId());
        mTransport.getFarm().setFarmId(transport.getFarm().getFarmId());
    }
}
