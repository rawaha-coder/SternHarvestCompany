package harvest.view;

import harvest.database.PreferencesDAO;
import harvest.model.Quantity;
import harvest.presenter.AddQuantityPresenter;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

import static harvest.presenter.AddQuantityPresenter.ADD_QUANTITY_LIVE_DATA;

public class AddQuantityController implements Initializable {

    //Add quantity UI Top input
    @FXML public DatePicker fxHarvestDate;
    @FXML public ChoiceBox<String> fxSupplierList;
    @FXML public ChoiceBox<String> fxFarmList;
    @FXML public ChoiceBox<String> fxProductList;
    @FXML public ChoiceBox<String> fxProductCodeList;
    @FXML public TextField fxInputAllQuantity;
    @FXML public TextField fxInputBadQuantity;

    //Add quantity UI Table input
    @FXML public TableView<Quantity> fxHarvestQuantityTable;
    @FXML public TableColumn<Quantity, String> fxEmployeeColumn;
    @FXML public TableColumn<Quantity, Double> fxAllQuantityColumn;
    @FXML public TableColumn<Quantity, Double> fxBadQuantityColumn;
    @FXML public TableColumn<Quantity, Double> fxGoodQuantityColumn;
    @FXML public TableColumn<Quantity, Double> fxPriceColumn;
    @FXML public TableColumn<Quantity, Boolean> fxTransportColumn;
    @FXML public TableColumn<Quantity, String> fxCreditColumn;
    @FXML public TableColumn<Quantity, Double> fxPaymentColumn;
    @FXML public TableColumn<Quantity, String> fxRemarqueColumn;

    //Add quantity UI Bottom input
    @FXML public TextField fxTotalEmployee;
    @FXML public TextField fxTotalAllQuantity;
    @FXML public TextField fxTotalBadQuantity;
    @FXML public TextField fxTotalGoodQuantity;
    @FXML public TextField fxProductPriceEmployee;
    @FXML public TextField fxTotalTransport;
    @FXML public TextField fxTotalCredit;
    @FXML public Label fxTotalPayment;

    //Add quantity UI right input
    @FXML public Button fxImportButton;
    @FXML public ToggleGroup fxHarvestType;
    @FXML public RadioButton fxHarvestByGroup;
    @FXML public RadioButton fxHarvestByIndividual;
    @FXML public Button fxValidateButton;
    @FXML public Button fxClearButton;
    @FXML public TextField fxProductPriceCompany;
    @FXML public Label fxCompanyCharge;
    @FXML public Button fxApplyButton;


    public final AlertMaker alert = new AlertMaker();
    AddQuantityPresenter mAddQuantityPresenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mAddQuantityPresenter = new AddQuantityPresenter(this);
        mAddQuantityPresenter.updateAddQuantityDataLive();
        mAddQuantityPresenter.initList();
        initTable();
        onChangeTransportCell();
        initField();
        fxCreditColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fxRemarqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        onChangeHarvestType();
    }

    //Initialization employee table Columns
    public void initTable(){
        fxHarvestQuantityTable.setItems(ADD_QUANTITY_LIVE_DATA);
        fxEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        fxAllQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxBadQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("badQuantity"));
        fxGoodQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("goodQuantity"));
        fxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        fxTransportColumn.setCellValueFactory(new PropertyValueFactory<>("transportStatus"));
        fxCreditColumn.setCellValueFactory(new PropertyValueFactory<>("creditString"));
        fxPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        fxRemarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
    }

    public  void initField(){
        fxTotalEmployee.setText("0");
        fxTotalAllQuantity.setText("0.0");
        fxTotalBadQuantity.setText("0.0");
        fxTotalGoodQuantity.setText("0.0");
        fxProductPriceEmployee.setText("0.0");
        fxTotalTransport.setText("0.0");
        fxTotalCredit.setText("0.0");
        fxTotalPayment.setText("0.0");
        fxHarvestByIndividual.setToggleGroup(fxHarvestType);
        fxHarvestByIndividual.setUserData("Individual");
        fxHarvestByGroup.setToggleGroup(fxHarvestType);
        fxHarvestByGroup.setUserData("Group");
        fxHarvestByGroup.setSelected(true);
        fxImportButton.setDisable(true);
        fxProductPriceCompany.setText("0.0");
    }

    private void onChangeTransportCell() {
        fxTransportColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        fxTransportColumn.setCellValueFactory(cellData -> {
            Quantity quantity = cellData.getValue();
            quantity.transportStatusProperty()
                    .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue) {
                            PreferencesDAO mPreferencesDAO = PreferencesDAO.getInstance();
                            double transportPrice = mPreferencesDAO.getTransportPrice();
                            if (transportPrice == -1) {
                                newValue = false;
                            }
                            quantity.getTransport().setTransportAmount(transportPrice);
                            System.out.println(quantity.getTransport().getTransportAmount());
                        } else {
                            quantity.getTransport().setTransportAmount(0.0);
                        }
                        quantity.setTransportStatus(newValue);
                    });
            return quantity.transportStatusProperty();
        });
    }

    private void onChangeHarvestType(){
        fxHarvestType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (fxHarvestType.getSelectedToggle() != null) {
                    System.out.println(fxHarvestType.getSelectedToggle().getUserData().toString());
                    fxImportButton.setDisable(fxHarvestType.getSelectedToggle().getUserData().equals("Group"));
                }
            }
        });
    }

    public void onChangeCreditCell(TableColumn.CellEditEvent cellEditEvent){
        Quantity quantity = fxHarvestQuantityTable.getSelectionModel().getSelectedItem();
        quantity.getCredit().setCreditAmount(Double.parseDouble(cellEditEvent.getNewValue().toString()));
    }

    public void onChangeRemarqueCell(TableColumn.CellEditEvent cellEditEvent){
        Quantity quantity = fxHarvestQuantityTable.getSelectionModel().getSelectedItem();
        quantity.setRemarque(cellEditEvent.getNewValue().toString());
    }

    @FXML
    void applyButton(ActionEvent event) {
        if (checkApplyButtonInput()) {
            alert.missingInfo("Hours");
            return;
        }
        mAddQuantityPresenter.applyProductionToDatabase();
    }

    private boolean checkApplyButtonInput() {
        return (fxTotalEmployee.getText().isEmpty() ||
                fxTotalAllQuantity.getText().isEmpty() ||
                fxTotalBadQuantity.getText().isEmpty() ||
                fxTotalGoodQuantity.getText().isEmpty() ||
                fxProductPriceEmployee.getText().isEmpty() ||
                fxTotalCredit.getText().isEmpty() ||
                fxTotalTransport.getText().isEmpty() ||
                fxTotalPayment.getText().isEmpty() ||
                fxProductPriceCompany.getText().isEmpty() ||
                fxCompanyCharge.getText().isEmpty()
        );
    }

    @FXML
    public void clearButton() {
        mAddQuantityPresenter.clearField();
    }

    @FXML
    void importButton(ActionEvent event) {

    }

    @FXML
    void validateButton(ActionEvent event) {
        if (checkValidButtonInput()) {
            alert.missingInfo("Harvest hours");
            return;
        }
        mAddQuantityPresenter.validateAddHarvestQuantity();
    }

    private boolean checkValidButtonInput() {
        return (fxHarvestDate.getValue() == null ||
                fxSupplierList.getValue() == null ||
                fxFarmList.getValue() == null ||
                fxProductList.getValue() == null ||
                fxProductCodeList.getValue() == null ||
                !Validation.isDouble(fxInputAllQuantity.getText()) ||
                !Validation.isDouble(fxInputBadQuantity.getText())
        );
    }

}
