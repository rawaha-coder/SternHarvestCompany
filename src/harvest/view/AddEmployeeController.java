package harvest.view;

import harvest.model.Employee;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import harvest.database.EmployeeDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {

    private final Employee mEmployee = new Employee();
    private final AlertMaker alert = new AlertMaker();
    private final EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();

    @FXML private TextField fxFirstName;
    @FXML private TextField fxLastName;
    @FXML private DatePicker fxHireDate;
    @FXML private DatePicker fxFireDate;
    @FXML private DatePicker fxPermissionDate;
    @FXML private CheckBox fxEmployeeStatus;
    @FXML private AnchorPane fxAddEmployeeUI;
    private boolean isEditStatus = false;

    @FXML
    void handleSaveButton() {
        if (isEditStatus) {
            handleEditOperation(mEmployee);
        }else {
            handleAddOperation();
        }
    }

    private void handleEditOperation(Employee employee) {
        employee.setEmployeeStatus(fxEmployeeStatus.isSelected());
        employee.setEmployeeFirstName(fxFirstName.getText().trim());
        employee.setEmployeeLastName(fxLastName.getText().trim());
        employee.setEmployeeHireDate(Date.valueOf(fxHireDate.getValue()));
        employee.setEmployeeFireDate(Date.valueOf(fxFireDate.getValue()));
        employee.setEmployeePermissionDate(Date.valueOf(fxPermissionDate.getValue()));
        if (mEmployeeDAO.editData(employee)) {
            clearFieldsButton();
            mEmployeeDAO.updateLiveData();
            alert.updateItem("Employee", true);
        } else {
            alert.updateItem("Employee", false);
        }
        handleCancelButton();
    }

    private void handleAddOperation() {
        if (validInput())
        {
            alert.missingInfo("Employee");
            return;
        }
        Employee employee = new Employee();
        employee.setEmployeeStatus(fxEmployeeStatus.isSelected());
        employee.setEmployeeFirstName(fxFirstName.getText().trim());
        employee.setEmployeeLastName(fxLastName.getText().trim());
        employee.setEmployeeHireDate(Date.valueOf(fxHireDate.getValue()));
        employee.setEmployeeFireDate(Date.valueOf(fxFireDate.getValue()));
        employee.setEmployeePermissionDate(Date.valueOf(fxPermissionDate.getValue()));
        if (mEmployeeDAO.addData(employee))
        {
            mEmployeeDAO.updateLiveData();
            clearFieldsButton();
            alert.saveItem("Employee", true);
        } else {
            alert.saveItem("Employee", false);
        }
    }

    private boolean validInput(){
        return Validation.isEmpty(
                fxFirstName.getText(),
                fxLastName.getText(),
                fxHireDate.getEditor().getText(),
                fxFireDate.getEditor().getText(),
                fxPermissionDate.getEditor().getText());
    }

    @FXML
    private void handleCancelButton() {
        isEditStatus = false;
        Stage stage = (Stage) fxAddEmployeeUI.getScene().getWindow();
        stage.close();
    }

    @FXML
    void clearFieldsButton() {
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
