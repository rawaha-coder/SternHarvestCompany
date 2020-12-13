package harvest.ui.employee;

import harvest.model.Employee;
import harvest.util.AlertMaker;
import harvest.viewmodel.EmployeeDAO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import static harvest.viewmodel.EmployeeDAO.EMPLOYEE_GRAPH_LIVE_DATA;
import static harvest.viewmodel.EmployeeDAO.EMPLOYEE_LIST_LIVE_DATA;

public class DisplayEmployeeController implements Initializable {

    private final AlertMaker alert = new AlertMaker();
    private final EmployeeDAO mEmployeeDAO = new EmployeeDAO();
    public AnchorPane fxEmployeeTableUI;
    public MenuItem fxEditEmployee;
    public MenuItem fxDeleteEmployee;
    @FXML
    private TableView<Employee> fxEmployeeTable;
    @FXML
    private TableColumn<Employee, Boolean> fxEmployeeSelectColumn;
    @FXML
    private TableColumn<Employee, String> fxEmployeeFullNameColumn;
    @FXML
    private TableColumn<Employee, String> fxEmployeeFirstNameColumn;
    @FXML
    private TableColumn<Employee, String> fxEmployeeLastNameColumn;
    @FXML
    private TableColumn<Employee, String> fxEmployeeHireDateColumn;
    @FXML
    private TableColumn<Employee, String> fxEmployeeFireDateColumn;
    @FXML
    private TableColumn<Employee, String> fxEmployeePermissionDaleColumn;
    @FXML
    private PieChart employeePieChat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initEmployeeTable();
        initEmployeeGraph();
        mEmployeeDAO.updateLivedata();
    }

    //Initialization PieChart
    private void initEmployeeGraph() {
        employeePieChat.setAnimated(false);
        employeePieChat.getData().clear();
        employeePieChat.setLabelsVisible(true);
        employeePieChat.setData(EMPLOYEE_GRAPH_LIVE_DATA);
    }


    //Initialization employee table Columns
    private void initEmployeeTable() {
        fxEmployeeSelectColumn.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        fxEmployeeFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFirstName"));
        fxEmployeeLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeLastName"));
        fxEmployeeFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));
        fxEmployeeHireDateColumn.setCellValueFactory(new PropertyValueFactory<>("employeeHireDate"));
        fxEmployeeFireDateColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFireDate"));
        fxEmployeePermissionDaleColumn.setCellValueFactory(new PropertyValueFactory<>("employeePermissionDate"));
        observeEmployeeSelectColumn();
        fxEmployeeTable.setItems(EMPLOYEE_LIST_LIVE_DATA);
    }


    //Add CheckBox To EmployeeSelectColumn and observe the change
    private void observeEmployeeSelectColumn() {
        fxEmployeeSelectColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxEmployeeSelectColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            BooleanProperty booleanProperty = employee.employeeStatusProperty();
            booleanProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (mEmployeeDAO.updateEmployeeStatusById(employee.getEmployeeId(), employee.isEmployeeSelected())) {
                    mEmployeeDAO.updateLivedata();
                } else {
                    alert.show("Error", "something wrong happened", AlertType.ERROR);
                }
            });
            return booleanProperty;
        });
    }

    @FXML
    void editEmployee() {
        Employee employee = fxEmployeeTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
            alert.selectEditItem("Employee");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/employee/add_employee.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddEmployeeController controller = loader.getController();
            controller.inflateUI(employee);
            stage.setTitle("Edit Employee");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteEmployee() {
        Employee employee = fxEmployeeTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
            alert.selectDeleteItem("Employee");
            return;
        }
        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Employee");

        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            if (mEmployeeDAO.deleteById(employee.getEmployeeId())) {
                mEmployeeDAO.updateLivedata();
                alert.deleteItem("Employee", true);
            } else {
                alert.deleteItem("Employee", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }
    }
}
