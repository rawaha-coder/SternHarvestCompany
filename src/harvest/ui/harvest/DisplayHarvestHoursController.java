package harvest.ui.harvest;

import harvest.database.HarvestHoursDAO;
import harvest.model.Employee;
import harvest.model.HarvestHours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DisplayHarvestHoursController implements Initializable {

    public static ObservableList<HarvestHours> HARVEST_HOURS_LIST_LIVE_DATA = FXCollections.observableArrayList();
    HarvestHoursDAO mHarvestHoursDAO = HarvestHoursDAO.getInstance();

    @FXML
    private AnchorPane displayHarvestHoursUI;

    @FXML
    private TableView<HarvestHours> fxHarvestHoursTable;

    @FXML
    private TableColumn<HarvestHours, String> fxEmployee;

    @FXML
    private TableColumn<HarvestHours, Time> fxStartMorning;

    @FXML
    private TableColumn<HarvestHours, Time> fxEndMorning;

    @FXML
    private TableColumn<HarvestHours, Time> fxStartNoon;

    @FXML
    private TableColumn<HarvestHours, Time> fxEndNoon;

    @FXML
    private TableColumn<HarvestHours, Time> fxTotalHours;

    @FXML
    private TableColumn<HarvestHours, Double> fxTransport;

    @FXML
    private TableColumn<HarvestHours, Double> fxCredit;

    @FXML
    private TableColumn<HarvestHours, String> fxRemarque;

    LocalDate toDay = LocalDate.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mHarvestHoursDAO.updateLiveData(Date.valueOf("2021-01-25"));
        initTable();
    }

    //Initialization employee table Columns
    public void initTable(){
        fxStartMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorning.setCellValueFactory(new PropertyValueFactory<>("endMorning"));
        fxStartNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoon.setCellValueFactory(new PropertyValueFactory<>("endNoon"));
        fxEmployee.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));
        fxTransport.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCredit.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("harvestRemarque"));
        fxHarvestHoursTable.setItems(HARVEST_HOURS_LIST_LIVE_DATA);
    }

}
