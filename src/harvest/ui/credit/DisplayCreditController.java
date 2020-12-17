package harvest.ui.credit;

import harvest.database.CreditDAO;
import harvest.database.TransportDAO;
import harvest.model.Credit;
import harvest.model.Transport;
import harvest.util.AlertMaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayCreditController implements Initializable {

    public static ObservableList<Credit> CREDIT_LIST_LIVE_DATA = FXCollections.observableArrayList();

    private final AlertMaker alert = new AlertMaker();
    private final CreditDAO mCreditDAO = CreditDAO.getInstance();
    private final TransportDAO mTransportDAO = new TransportDAO();

    public AnchorPane fxTableUI;
    public MenuItem fxEditTransport;
    public MenuItem fxDeleteTransport;

    @FXML
    private TableView<Credit> fxCreditTable;
    @FXML
    private TableColumn<Credit, String> fxCreditDateColumn;
    @FXML
    private TableColumn<Credit, String> fxCreditEmployeeColumn;
    @FXML
    private TableColumn<Credit, Double> fxCreditAmountColumn;
    @FXML
    private TableView<Transport> fxTransportTable;
    @FXML
    private TableColumn<Transport, String> fxTransportDateColumn;
    @FXML
    private TableColumn<Transport, String> fxTransportEmployeeColumn;
    @FXML
    private TableColumn<Transport, Double> fxTransportAmountColumn;
    @FXML
    private TableColumn<Transport, String> fxTransportDestinationColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCreditColumns();
        //initTransportColumns();
    }

    //*********************************************************************************************
    //Credit table model section
    //*********************************************************************************************
    public void initCreditColumns() {
        fxCreditDateColumn.setCellValueFactory(new PropertyValueFactory<>("creditDate"));
        fxCreditEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("creditEmployee"));
        fxCreditAmountColumn.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        mCreditDAO.updateLiveData();
        fxCreditTable.setItems(CREDIT_LIST_LIVE_DATA);
    }

    @FXML
    void editCredit() {
        Credit credit = fxCreditTable.getSelectionModel().getSelectedItem();
        if (credit == null) {
            alert.show("Required selected credit", "Please select an transport_credit", AlertType.INFORMATION);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/credit/add_credit.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddCreditController controller = loader.getController();
            controller.inflateUI(credit);
            stage.setTitle("Edit Credit");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCreditDAO.updateLiveData();
    }

    @FXML
    void deleteCredit() {
        Credit credit = fxCreditTable.getSelectionModel().getSelectedItem();
        if (credit == null) {
            alert.selectDeleteItem("Credit");
            return;
        }
        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Credit");

        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            if (mCreditDAO.deleteDataById(credit.getCreditId())) {
                mCreditDAO.updateLiveData();
                alert.deleteItem("Credit", true);
            } else {
                alert.deleteItem("Credit", false);
            }
        } else {
            alert.show("Deletion cancelled", "Deletion process cancelled", AlertType.INFORMATION);
        }

    }
/*
    //*********************************************************************************************
    //Transport View model section
    //*********************************************************************************************
    private void initTransportColumns() {
        fxTransportDateColumn.setCellValueFactory(new PropertyValueFactory<>("transportDate"));
        fxTransportEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("transportEmployee"));
        fxTransportAmountColumn.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxTransportDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("transportDestination"));
        mTransportDAO.observeLivedata();
        fxTransportTable.setItems(TRANSPORT_LIST_LIVE_DATA);
    }

    @FXML
    void editTransport() {
        Transport transport = fxTransportTable.getSelectionModel().getSelectedItem();
        if (transport == null) {
            alert.show("Required selected transport", "Please select an transport", AlertType.INFORMATION);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/ui/add/transport/add_transport.fxml"));
            Stage stage = new Stage();
            Parent parent = loader.load();
            AddTransportController controller = loader.getController();
            controller.inflateUI(transport);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTransportDAO.observeLivedata();
    }

    @FXML
    void deleteTransport() {
        Transport transport = fxTransportTable.getSelectionModel().getSelectedItem();
        if (transport == null) {
            alert.show("Required selected transport", "Please select a transport from the table for deletion", AlertType.INFORMATION);
            return;
        }

        Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmation.setTitle("Deletion");
        deleteConfirmation.setHeaderText("Delete Port Confirmation");
        deleteConfirmation.setContentText("Press OK to delete this Port!");

        Optional<ButtonType> result = deleteConfirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                mTransportDAO.deleteTransportWithId(transport.getTransportId());
            } catch (SQLException e) {
                alert.show("Error", "Failed to delete the Port", AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            alert.show("Deletion cancelled", "Deletion process cancelled", AlertType.INFORMATION);
        }
        mTransportDAO.observeLivedata();
        System.out.println("Delete Port...");
    }

 */
}
