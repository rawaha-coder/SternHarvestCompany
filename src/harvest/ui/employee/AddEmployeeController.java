package harvest.ui.employee;

import harvest.model.Employee;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import harvest.database.EmployeeDAO;
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
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
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
    private boolean isEditStatus = false;

    @FXML
    void handleSaveButton() {
        Employee employee = new Employee();
        if (Validation.isEmpty(fxFirstName.getText(), fxLastName.getText(), fxHireDate.getEditor().getText(), fxFireDate.getEditor().getText(), fxPermissionDate.getEditor().getText()))
        {
            alert.missingInfo("Employee");
            return;
        }

        if (isEditStatus) {
            handleEditOperation(mEmployee);
        }else {
            employee.setEmployeeStatus(fxEmployeeStatus.isSelected());
            employee.setEmployeeFirstName(fxFirstName.getText());
            employee.setEmployeeLastName(fxLastName.getText());
            employee.setEmployeeHireDate(Date.valueOf(fxHireDate.getValue()));
            employee.setEmployeeFireDate(Date.valueOf(fxFireDate.getValue()));
            employee.setEmployeePermissionDate(Date.valueOf(fxPermissionDate.getValue()));
            if (mEmployeeDAO.addData(employee))
            {
                mEmployeeDAO.updateLiveData();
                handleClearFieldsButton();
                alert.saveItem("Employee", true);
            } else {
                alert.saveItem("Employee", false);
            }
        }
    }

    private void handleEditOperation(Employee employee) {
        employee.setEmployeeStatus(fxEmployeeStatus.isSelected());
        employee.setEmployeeFirstName(fxFirstName.getText());
        employee.setEmployeeLastName(fxLastName.getText());
        employee.setEmployeeHireDate(Date.valueOf(fxHireDate.getValue()));
        employee.setEmployeeFireDate(Date.valueOf(fxFireDate.getValue()));
        employee.setEmployeePermissionDate(Date.valueOf(fxPermissionDate.getValue()));
        if (mEmployeeDAO.editData(employee)) {
            handleClearFieldsButton();
            mEmployeeDAO.updateLiveData();
            alert.updateItem("Employee", true);
        } else {
            alert.updateItem("Employee", false);
        }
        isEditStatus = Boolean.FALSE;
        handleCancelButton();
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
        fxHireDate.setValue(employee.getEmployeeHireDate().toLocalDate());
        fxFireDate.setValue(employee.getEmployeeFireDate().toLocalDate());
        fxPermissionDate.setValue(employee.getEmployeePermissionDate().toLocalDate());
        fxEmployeeStatus.setSelected(employee.isEmployeeStatus());
        isEditStatus = true;
        mEmployee.setEmployeeId(employee.getEmployeeId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxHireDate.setEditable(false);
        fxFireDate.setEditable(false);
        fxPermissionDate.setEditable(false);
    }
}
