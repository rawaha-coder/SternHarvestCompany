package harvest.ui.credit;

import harvest.database.EmployeeDAO;
import harvest.database.TransportDAO;
import harvest.model.Employee;
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
import java.sql.SQLException;
import java.util.*;

public class AddTransportController {
//
//    private final Map<String, Integer> employeeIdAndName = new LinkedHashMap<>();
//    private final Map<String, Integer> farmIdAndDestination = new LinkedHashMap<>();
//    @FXML
//    private AnchorPane fxAddItemUI;
//
//    @FXML
//    private DatePicker fxTransportDate;
//
//    @FXML
//    private ChoiceBox<String> fxEmployeeList;
//
//    @FXML
//    private TextField fxTransportAmount;
//
//    @FXML
//    private ChoiceBox<String> fxFarmList;
//
//    private Boolean isEditStatus = Boolean.FALSE;
//    private final AlertMaker alert = new AlertMaker();
//    private final Transport mTransport = new Transport();
//    private final TransportDAO mTransportDAO = new TransportDAO();
//    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
//    private final FarmSeasonDAO mFarmSeasonDAO = new FarmSeasonDAO();
//    @FXML
//    void handleCancelButton() {
//        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
//        stage.close();
//        System.out.println("Cancel...");
//    }
//
//    @FXML
//    void handleClearFieldsButton() {
//        fxTransportDate.getEditor().setText("");
//        employeeList();
//        fxTransportAmount.setText("");
//        farmList();
//    }
//
//    private void farmList(){
//        ObservableList<String> observableList = FXCollections.observableArrayList();
//        try {
//            List<Farm> list = new ArrayList<>(mFarmSeasonDAO.getFarmIdAndAddress());
//            for (Farm farm : list){
//                observableList.add(farm.getFarmAddress());
//                farmIdAndDestination.put(farm.getFarmAddress(), farm.getFarmId());
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        fxFarmList.setItems(observableList);
//
//    }
//
//    private void employeeList() {
//        ObservableList<String> observableList = FXCollections.observableArrayList();
//        try {
//            List<Employee> list = new ArrayList<>(mEmployeeDAO.getEmployeeIdAndName());
//            for (Employee employee : list){
//                observableList.add(employee.getEmployeeFullName());
//                employeeIdAndName.put(employee.getEmployeeFullName(), employee.getEmployeeId());
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        fxEmployeeList.setItems(observableList);
//    }
//
//    @FXML
//    void handleSaveButton() {
//        if (isEditStatus){
//            handleEditOperation(mTransport);
//            Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
//            stage.close();
//            System.out.println("Close...");
//            return;
//        }
//
//        if (checkInputs()){
//            if (mTransportDAO.addTransport(
//                    fxTransportDate.getEditor().getText(),
//                    Double.parseDouble(fxTransportAmount.getText()),
//                    employeeIdAndName.get(fxEmployeeList.getValue()),
//                    farmIdAndDestination.get(fxFarmList.getValue()))
//            ){
//                alert.show("Transport" + TITLE_SAVED, "Transport" + MESSAGE_SAVED, AlertType.INFORMATION);
//            }else {
//                alert.show("Transport" + TITLE_NOT_SAVED, "Transport" + MESSAGE_NOT_SAVED, AlertType.ERROR);
//            }
//            mTransportDAO.observeLivedata();
//            handleClearFieldsButton();
//        }
//    }
//
//    private void handleEditOperation(Transport transport) {
//        if (mTransportDAO.updateTransport(
//                transport.getTransportId(),
//                fxTransportDate.getEditor().getText(),
//                Double.parseDouble(fxTransportAmount.getText()),
//                transport.getEmployeeId(),
//                transport.getFarmId()))
//        {
//            alert.show("Transport" + TITLE_UPDATE, "Transport" + MESSAGE_UPDATE, AlertType.INFORMATION);
//
//        }else{
//            alert.show("Transport" + TITLE_NOT_UPDATE , "Transport" + MESSAGE_NOT_UPDATE, AlertType.INFORMATION);
//        }
//        isEditStatus = Boolean.FALSE;
//        mTransportDAO.observeLivedata();
//        handleCancelButton();
//    }
//
//    private boolean checkInputs(){
//        if (!Validation.isValidDate(fxTransportDate.getEditor().getText()) && fxEmployeeList.getSelectionModel().isEmpty() && fxTransportAmount.getText().isEmpty() && fxFarmList.getSelectionModel().isEmpty()) {
//            alert.show("Required fields are missing", "fields cannot be empty!", AlertType.INFORMATION);
//            return false;
//        }else if (!Validation.isValidDate(fxTransportDate.getEditor().getText())){
//            alert.show("Required fields are missing", "Please enter a valid Port Date", AlertType.INFORMATION);
//            return false;
//        }else if (fxEmployeeList.getSelectionModel().isEmpty()){
//            alert.show("Required fields are missing", "Please choice employee", AlertType.INFORMATION);
//            return false;
//        }else if (fxTransportAmount.getText().isEmpty()){
//            alert.show("Required fields are missing", "Please enter montant", AlertType.INFORMATION);
//            return false;
//        }else if (fxFarmList.getSelectionModel().isEmpty()){
//            alert.show("Required fields are missing", "Please choice champ", AlertType.INFORMATION);
//            return false;
//        }
//        return Validation.isValidDouble(fxTransportAmount.getText());
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        employeeList();
//        farmList();
//    }
//
//    public void inflateUI(Transport transport) {
//        fxTransportDate.getEditor().setText(transport.getTransportDate());
//        employeeList();
//        fxEmployeeList.getSelectionModel().select(transport.getTransportEmployee());
//        fxTransportAmount.setText(String.valueOf(transport.getTransportAmount()));
//        farmList();
//        fxFarmList.getSelectionModel().select(transport.getTransportDestination());
//        isEditStatus = Boolean.TRUE;
//        fxEmployeeList.setDisable(true);
//        fxFarmList.setDisable(true);
//        mTransport.setTransportId(transport.getTransportId());
//        mTransport.setTransportDate(transport.getTransportDate());
//        mTransport.setTransportAmount(transport.getTransportAmount());
//        mTransport.setEmployeeId(transport.getEmployeeId());
//        mTransport.setTransportEmployee(transport.getTransportEmployee());
//        mTransport.setFarmId(transport.getFarmId());
//        mTransport.setTransportDestination(transport.getTransportDestination());
//    }
}
