package harvest.ui.credit;

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
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AddCreditController implements Initializable {

    private final Map<String, Integer> employeeNameId = new LinkedHashMap<>();
    private final Credit mCredit = new Credit();
    private final AlertMaker alert = new AlertMaker();
    private final CreditDAO mCreditDAO = CreditDAO.getInstance();

    @FXML
    private AnchorPane fxAddItemUI;
    @FXML
    private DatePicker fxCreditDate;
    @FXML
    private ChoiceBox<String> fxEmployeeList;
    @FXML
    private TextField fxCreditAmount;
    private Boolean isEditStatus = Boolean.FALSE;
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxCreditDate.setValue(LocalDate.now());
        fxCreditDate.setEditable(false);
        getEmployeeList();
    }

    //fill the ChoiceBox by employee list
    private void getEmployeeList() {
        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        try {
            List<Employee> mList = new ArrayList<>(mEmployeeDAO.getData());
            for (Employee employee : mList) {
                stringObservableList.add(employee.getEmployeeFullName());
                employeeNameId.put(employee.getEmployeeFullName(), employee.getEmployeeId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxEmployeeList.setItems(stringObservableList);
    }

    @FXML
    void handleClearFieldsButton() {
        fxCreditDate.getEditor().setText("");
        getEmployeeList();
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
            credit.setEmployeeId(employeeNameId.get(fxEmployeeList.getValue()));
            if (mCreditDAO.addData(credit)) {
                handleClearFieldsButton();
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
        isEditStatus = Boolean.FALSE;
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
        getEmployeeList();
        fxEmployeeList.getSelectionModel().select(credit.getCreditEmployee());
        fxCreditAmount.setText(String.valueOf(credit.getCreditAmount()));
        isEditStatus = Boolean.TRUE;
        fxEmployeeList.setDisable(true);
        mCredit.setCreditId(credit.getCreditId());

    }
}
