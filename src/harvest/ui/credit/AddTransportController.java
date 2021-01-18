package harvest.ui.credit;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.*;

public class AddTransportController implements Initializable {

    private final Map<String, Employee> employeeMap = new LinkedHashMap<>();
    private final Map<String, Farm> farmMap = new LinkedHashMap<>();

    @FXML
    private AnchorPane fxAddItemUI;

    @FXML
    private DatePicker fxTransportDate;

    @FXML
    private ChoiceBox<String> fxEmployeeList;

    @FXML
    private TextField fxTransportAmount;

    @FXML
    private ChoiceBox<String> fxFarmList;

    private Boolean isEditStatus = Boolean.FALSE;
    private final AlertMaker alert = new AlertMaker();
    private final Transport mTransport = new Transport();
    private final TransportDAO mTransportDAO = TransportDAO.getInstance();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeList();
        farmList();
    }

    private void farmList(){
        ObservableList<String> observableList = FXCollections.observableArrayList();

        try {
            List<Farm> list = new ArrayList<>(mFarmDAO.getFarmData());
            for (Farm farm : list){
                observableList.add(farm.getFarmAddress());
                farmMap.put(farm.getFarmAddress(), farm);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        fxFarmList.setItems(observableList);

    }

    private void employeeList() {
        ObservableList<String> observableList = FXCollections.observableArrayList();

        try {
            List<Employee> list = new ArrayList<>(mEmployeeDAO.getData());
            for (Employee employee : list){
                observableList.add(employee.getEmployeeFullName());
                employeeMap.put(employee.getEmployeeFullName(), employee);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        fxEmployeeList.setItems(observableList);
    }

    @FXML
    void handleSaveButton() {
        if (isEditStatus){
            handleEditOperation(mTransport);
        }else {
            handleAddOperation();
        }

    }

    private void handleAddOperation() {
        if (Validation.isEmpty(
                fxTransportDate.getEditor().getText()
                , fxEmployeeList.getValue()
                , fxTransportAmount.getText()
                , fxFarmList.getValue()
        )){
            alert.missingInfo("Transport");
            return;
        }
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

    public void handleEditOperation(Transport transport) {
        transport.setTransportDate(Date.valueOf(fxTransportDate.getValue()));
        transport.getEmployee().setEmployeeId(employeeMap.get(fxEmployeeList.getValue()).getEmployeeId());
        transport.getFarm().setFarmId(farmMap.get(fxFarmList.getValue()).getFarmId());
        transport.setTransportAmount(Double.parseDouble(fxTransportAmount.getText()));
        if (mTransportDAO.editData(transport)){
            mTransportDAO.updateLiveData();
            alert.updateItem("Transport", true);
        }else {
            alert.updateItem("Transport", false);
        }
        isEditStatus = false;
        handleCloseButton();
    }

    @FXML
    void handleClearButton() {
        fxTransportDate.getEditor().setText("");
        employeeList();
        fxTransportAmount.setText("");
        farmList();
    }

    @FXML
    void handleCloseButton() {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
        System.out.println("Cancel...");
    }

    public void inflateUI(Transport transport) {
        fxTransportDate.setValue(transport.getTransportDate().toLocalDate());
        employeeList();
        fxEmployeeList.setValue(transport.getEmployeeName());
        fxTransportAmount.setText(String.valueOf(transport.getTransportAmount()));
        farmList();
        fxFarmList.setValue(transport.getFarm().getFarmAddress());
        isEditStatus = true;
        mTransport.setTransportId(transport.getTransportId());
    }
}
