package harvest.view;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AddCreditController implements Initializable {


    private Map<String, Employee> mEmployeeMap = new LinkedHashMap<>();
    private final Credit mCredit = new Credit();
    private final AlertMaker alert = new AlertMaker();
    private final CreditDAO mCreditDAO = CreditDAO.getInstance();

    @FXML private AnchorPane fxAddItemUI;
    @FXML private DatePicker fxCreditDate;
    @FXML private ComboBox<String> fxEmployeeList;
    @FXML private TextField fxCreditAmount;

    private boolean isEditStatus = false;
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxCreditDate.setValue(LocalDate.now());
        fxCreditDate.setEditable(false);
        getEmployeeName();
    }

    //fill the ComboBox by employee list
    private void getEmployeeName() {
        ObservableList<String> employeeNameList = FXCollections.observableArrayList();
        mEmployeeMap.clear();
        try {
            mEmployeeMap = mEmployeeDAO.getEmployeeMap();
            employeeNameList.setAll(mEmployeeMap.keySet());
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
        if (validateInput()){
            alert.missingInfo("Credit");
            return;
        }
        if (isEditStatus) {
            editCredit(mCredit);
        }else {
            addNewCredit();
        }
    }

    private boolean validateInput() {
        return fxCreditDate.getValue() == null
                || fxCreditAmount.getText().isEmpty()
                || fxEmployeeList.getValue() == null
                || fxEmployeeList.getValue() == null
                || !Validation.isDouble(fxCreditAmount.getText());
    }

    private void addNewCredit() {
        Credit credit = new Credit();
        credit.setCreditDate(Date.valueOf(fxCreditDate.getValue()));
        credit.setCreditAmount(Double.parseDouble(fxCreditAmount.getText()));
        credit.getEmployee().setEmployeeId(mEmployeeMap.get(fxEmployeeList.getValue()).getEmployeeId());
        credit.getEmployee().setEmployeeFirstName(mEmployeeMap.get(fxEmployeeList.getValue()).getEmployeeFirstName());
        credit.getEmployee().setEmployeeLastName(mEmployeeMap.get(fxEmployeeList.getValue()).getEmployeeLastName());
        if (mCreditDAO.addData(credit)) {
            handleClearButton();
            mCreditDAO.updateLiveData();
            alert.saveItem("Credit", true);
        } else {
            alert.saveItem("Credit", false);
        }
    }

    private void editCredit(Credit credit) {
        credit.setCreditDate(Date.valueOf(fxCreditDate.getValue()));
        credit.setCreditAmount(Double.parseDouble(fxCreditAmount.getText()));
        if (mCreditDAO.editData(credit)) {
            mCreditDAO.updateLiveData();
            alert.updateItem ("Credit", true);
        } else {
            alert.updateItem ("Credit", false);
        }
        isEditStatus = false;
        handleCancelButton();
    }

    @FXML
    void handleCancelButton() {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Credit credit) {
        fxCreditDate.setValue(credit.getCreditDate().toLocalDate());
        fxCreditAmount.setText(String.valueOf(credit.getCreditAmount()));
        isEditStatus = true;
        fxEmployeeList.setItems(null);
        fxEmployeeList.setValue(credit.getEmployeeName());
        fxEmployeeList.setDisable(true);
        mCredit.setCreditId(credit.getCreditId());
        mCredit.getEmployee().setEmployeeId(credit.getEmployee().getEmployeeId());
        mCredit.getEmployee().setEmployeeFirstName(credit.getEmployee().getEmployeeFirstName());
        mCredit.getEmployee().setEmployeeLastName(credit.getEmployee().getEmployeeLastName());
    }
}
