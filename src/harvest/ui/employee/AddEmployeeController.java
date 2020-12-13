package harvest.ui.employee;

import harvest.model.Employee;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import harvest.viewmodel.EmployeeDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;


public class AddEmployeeController implements Initializable {

    private final Employee mEmployee = new Employee();
    private final AlertMaker alert = new AlertMaker();
    private final EmployeeDAO mEmployeeDAO = new EmployeeDAO();
    public Button fxSaveButton;
    @FXML
    private TextField fxFirstName;
    @FXML
    private TextField fxLastName;
    @FXML
    private DatePicker fxHireDate;
    @FXML
    private DatePicker fxFireDate;
    @FXML
    private DatePicker fxPermissionDate;
    @FXML
    private CheckBox fxEmployeeStatus;
    @FXML
    private AnchorPane fxAddEmployeeUI;
    private Boolean isEditStatus = Boolean.FALSE;

    @FXML
    void handleSaveButton() {
        if (isEditStatus) {
            handleEditOperation(mEmployee);
            return;
        }

        if (checkInputs()) {
            if (mEmployeeDAO.addEmployee(
                    getIntEmployeeStatus(fxEmployeeStatus.isSelected()),
                    fxFirstName.getText().trim(),
                    fxLastName.getText().trim(),
                    Date.valueOf(fxHireDate.getValue()) ,
                    Date.valueOf(fxFireDate.getValue()) ,
                    Date.valueOf(fxPermissionDate.getValue())
            )
            ) {
                mEmployeeDAO.updateLivedata();
                handleClearFieldsButton();
                alert.saveItem("Employee", true);
            } else {
                alert.saveItem("Employee", false);
            }
        }
    }

    private void handleEditOperation(Employee employee) {
        if (mEmployeeDAO.updateEmployee(
                employee.getEmployeeId(),
                getIntEmployeeStatus(fxEmployeeStatus.isSelected()),
                fxFirstName.getText().trim(),
                fxLastName.getText().trim(),
                Date.valueOf(fxHireDate.getValue()),
                Date.valueOf(fxFireDate.getValue()),
                Date.valueOf(fxPermissionDate.getValue())
        )) {
            handleClearFieldsButton();
            mEmployeeDAO.updateLivedata();
            alert.updateItem("Employee", true);
        } else {
            alert.updateItem("Employee", false);
        }
        isEditStatus = Boolean.FALSE;
        handleCancelButton();
    }

    private int getIntEmployeeStatus(boolean status) {
        if (status) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean checkInputs() {
        if (fxFirstName.getText().isEmpty() && fxLastName.getText().isEmpty() && fxHireDate.getEditor().getText().isEmpty()
                && fxFireDate.getEditor().getText().isEmpty() && fxPermissionDate.getEditor().getText().isEmpty()) {
            alert.show("Required fields are missing", "Please enter required fields!", AlertType.INFORMATION);
            return false;
        } else if (fxFirstName.getText().isEmpty()) {
            alert.show("Required fields are missing", "Please enter Employee first name", AlertType.INFORMATION);
            return false;
        } else if (fxLastName.getText().isEmpty()) {
            alert.show("Required fields are missing", "Please enter Employee last name", AlertType.INFORMATION);
            return false;
        } else if (!Validation.isValidDate(fxHireDate.getEditor().getText())) {
            alert.show("Required fields are missing", "Please enter a correct hire date", AlertType.INFORMATION);
            return false;
        } else if (!Validation.isValidDate(fxFireDate.getEditor().getText())) {
            alert.show("Required fields are missing", "Please enter a correct fire date", AlertType.INFORMATION);
            return false;
        } else if (!Validation.isValidDate(fxPermissionDate.getEditor().getText())) {
            alert.show("Required fields are missing", "Please enter a Correct expiration date of the residence certificate", AlertType.INFORMATION);
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancelButton() {
        Stage stage = (Stage) fxAddEmployeeUI.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleClearFieldsButton() {
        fxFirstName.setText("");
        fxLastName.setText("");
        fxHireDate.getEditor().setText("");
        fxFireDate.getEditor().setText("");
        fxPermissionDate.getEditor().setText("");
        fxEmployeeStatus.setSelected(false);
    }

    public void inflateUI(Employee employee) {
        fxFirstName.setText(employee.getEmployeeFirstName());
        fxLastName.setText(employee.getEmployeeLastName());
        fxHireDate.setValue(employee.getHireDate());
        fxFireDate.setValue(employee.getFireDate());
        fxPermissionDate.setValue(employee.getPermissionDate());
        fxEmployeeStatus.setSelected(employee.isEmployeeSelected());
        isEditStatus = Boolean.TRUE;
        mEmployee.setEmployeeId(employee.getEmployeeId());
        mEmployee.setEmployeeStatus(employee.getEmployeeStatus());
        mEmployee.setEmployeeFirstName(employee.getEmployeeFirstName());
        mEmployee.setEmployeeLastName(employee.getEmployeeLastName());
        mEmployee.setEmployeeFullName(employee.getEmployeeFullName());
        mEmployee.setEmployeeHireDate(employee.getEmployeeHireDate());
        mEmployee.setEmployeeFireDate(employee.getEmployeeFireDate());
        mEmployee.setEmployeePermissionDate(employee.getEmployeePermissionDate());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxHireDate.setEditable(false);
        fxFireDate.setEditable(false);
        fxPermissionDate.setEditable(false);
    }
}
