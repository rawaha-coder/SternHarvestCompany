package harvest.ui.group;

import harvest.model.Harvest;
import harvest.model.Harvest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DisplayGroupController implements Initializable {


    @FXML
    private AnchorPane displayHarvestWorkUI;

    @FXML
    private TableView<Harvest> fxHarvestWorkTable;

    @FXML
    private TableColumn<Harvest, String> fxEmployeeNameColumn;

    @FXML
    private TableColumn<Harvest, Double> fxAllQuantityColumn;

    @FXML
    private TableColumn<Harvest, Double> fxBadQuantityColumn;

    @FXML
    private TableColumn<Harvest, Double> fxGoodQuantityColumn;

    @FXML
    private TableColumn<Harvest, Double> fxPriceColumn;

    @FXML
    private TableColumn<Harvest, Double> fxTransportSelectColumn;

    @FXML
    private TableColumn<Harvest, Double> fxCreditColumn;

    @FXML
    private TableColumn<Harvest, Double> fxAmountPayableColumn;

    @FXML
    private TableColumn<Harvest, String> fxRemarqueColumn;

    @FXML
    private TextField fxTotalEmployee;

    @FXML
    private TextField fxAllQuantity;

    @FXML
    private TextField fxBadQuality;

    @FXML
    private TextField fxGoodQuality;

    @FXML
    private TextField fxProductPriceCompany;

    @FXML
    private TextField fxTotalCredit;

    @FXML
    private TextField fxTotalTransport;

    @FXML
    private Label fxCompanyCharge;

    @FXML
    private Label fxDayFrom;

    @FXML
    private DatePicker fxDateFrom;

    @FXML
    private Label fxDayTo;

    @FXML
    private DatePicker fxDateTo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
