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
        getEmployeeList();
    }

    //fill the ChoiceBox by employee list
    private void getEmployeeList() {
        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        try {
            List<Employee> mList = new ArrayList<>(mEmployeeDAO.getData());
            for (Employee employee : mList) {
                stringObservableList.add(Validation.getFullName(employee.getEmployeeFirstName(), employee.getEmployeeLastName()));
                employeeNameId.put(Validation.getFullName(employee.getEmployeeFirstName(), employee.getEmployeeLastName()), employee.getEmployeeId());
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
        if (
                Validation.isEmpty(fxCreditDate.getValue().toString(), fxCreditAmount.getText(), employeeNameId.get(fxEmployeeList.getValue()).toString(), fxEmployeeList.getValue())
                || !Validation.isDouble(fxCreditAmount.getText())
        ){
            alert.show("Required fields are missing", "Please enter correct data in required fields!", AlertType.INFORMATION);
            return;
        }
        if (isEditStatus) {
            handleEditOperation(mCredit);
        }else {
            Credit credit = new Credit();
            credit.setCreditDate(fxCreditDate.getValue());
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
        credit.setCreditDate(fxCreditDate.getValue());
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
        System.out.println("Cancel...");
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
