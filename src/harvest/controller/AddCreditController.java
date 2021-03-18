package harvest.controller;

import harvest.database.CreditDAO;
import harvest.database.EmployeeDAO;
import harvest.model.Credit;
import harvest.model.Employee;
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
import java.time.LocalDate;
import java.util.*;

public class AddCreditController implements Initializable {

    private Map<String, Employee> mapNameEmployee = new LinkedHashMap<>();
    private final Credit mCredit = new Credit();
    private final AlertMaker alert = new AlertMaker();
    private final CreditDAO mCreditDAO = CreditDAO.getInstance();

    @FXML private AnchorPane fxAddItemUI;
    @FXML private DatePicker fxCreditDate;
    @FXML private ChoiceBox<String> fxEmployeeList;
    @FXML private TextField fxCreditAmount;

    private boolean isEditStatus = false;
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxCreditDate.setValue(LocalDate.now());
        fxCreditDate.setEditable(false);
        getEmployeeName();
    }

    //fill the ChoiceBox by employee list
    private void getEmployeeName() {
        ObservableList<String> employeeNameList = FXCollections.observableArrayList();
        mapNameEmployee.clear();
        try {
            mapNameEmployee = mEmployeeDAO.getEmployeeMap();
            employeeNameList.setAll(mapNameEmployee.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxEmployeeList.setItems(employeeNameList);
    }

    @FXML
    void handleClearButton() {
        fxCreditDate.getEditor().setText("");
        getEmployeeName();
        fxCreditAmount.setText("");
    }

    @FXML
    void handleSaveButton() {
        if (fxCreditDate.getValue() == null
                || fxCreditAmount.getText().isEmpty()
                || fxEmployeeList.getValue() == null
                || fxEmployeeList.getValue() == null
                || !Validation.isDouble(fxCreditAmount.getText())
        ){
            alert.missingInfo("Credit");
            return;
        }
        if (isEditStatus) {
            handleEditOperation(mCredit);
        }else {
            Credit credit = new Credit();
            credit.setCreditDate(Date.valueOf(fxCreditDate.getValue()));
            credit.setCreditAmount(Double.parseDouble(fxCreditAmount.getText()));
            credit.setEmployeeId(mapNameEmployee.get(fxEmployeeList.getValue()).getEmployeeId());
            credit.setEmployeeName(mapNameEmployee.get(fxEmployeeList.getValue()).getEmployeeFullName());
            if (mCreditDAO.addData(credit)) {
                handleClearButton();
                mCreditDAO.updateLiveData();
                alert.saveItem("Credit", true);
            } else {
                alert.saveItem("Credit", false);
            }
        }
    }

    private void handleEditOperation(Credit credit) {
        credit.setCreditDate(Date.valueOf(fxCreditDate.getValue()));
        credit.setCreditAmount(Double.parseDouble(fxCreditAmount.getText()));
        if (mCreditDAO.editData(credit)) {
            mCreditDAO.updateLiveData();
            alert.updateItem ("Credit", true);
        } else {
            alert.updateItem ("Credit", false);
        }
        isEditStatus = false;
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
    }


    @FXML
    void handleCancelButton() {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Credit credit) {
        fxCreditDate.setValue(credit.getCreditDate().toLocalDate());
        getEmployeeName();
        fxEmployeeList.getSelectionModel().select(credit.getEmployeeName());
        fxCreditAmount.setText(String.valueOf(credit.getCreditAmount()));
        isEditStatus = true;
        fxEmployeeList.setDisable(true);
        mCredit.setCreditId(credit.getCreditId());
        mCredit.setEmployeeName(credit.getEmployeeName());
    }
}
