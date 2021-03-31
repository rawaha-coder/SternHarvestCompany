package harvest.presenter;

import harvest.database.HoursDAO;
import harvest.database.ProductionDAO;
import harvest.database.QuantityDAO;
import harvest.model.Hours;
import harvest.model.Production;
import harvest.model.Quantity;
import harvest.util.AlertMaker;
import harvest.view.DisplayQuantityProduction;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class DisplayQuantityProductionPresenter {
    public static ObservableList<Production> QUANTITY_PRODUCTION_LIVE_DATA = FXCollections.observableArrayList();
    ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    DisplayQuantityProduction mDisplayQuantityProduction;
    public final AlertMaker alert = new AlertMaker();

    Thread thread = new Thread(){
        public void run(){
            System.out.println("Thread Running");
            initHoursProductionLiveData();
        }
    };

    public DisplayQuantityProductionPresenter(DisplayQuantityProduction displayQuantityProduction) {
        mDisplayQuantityProduction = displayQuantityProduction;
        mDisplayQuantityProduction.fxProductionTable.setItems(QUANTITY_PRODUCTION_LIVE_DATA);
        thread.start();
        nestedTable();
    }

    public void initHoursProductionLiveData(){
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(30);
        mDisplayQuantityProduction.fxDatePickerFrom.setValue(fromDate);
        mDisplayQuantityProduction.fxDatePickerTo.setValue(toDate);
        updateData(fromDate, toDate);
    }

    public void searchByDate() {
        if (mDisplayQuantityProduction.fxDatePickerFrom.getValue() != null && mDisplayQuantityProduction.fxDatePickerTo.getValue() != null){
            LocalDate fromDate = mDisplayQuantityProduction.fxDatePickerFrom.getValue();
            LocalDate toDate = mDisplayQuantityProduction.fxDatePickerTo.getValue();
            updateData(fromDate, toDate);
        }
    }

    public void updateData(LocalDate fromDate, LocalDate toDate){
        QUANTITY_PRODUCTION_LIVE_DATA.clear();
        try {
            QUANTITY_PRODUCTION_LIVE_DATA.setAll(mProductionDAO.searchHoursProductionData(Date.valueOf(fromDate), Date.valueOf(toDate), 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nestedTable(){
        mDisplayQuantityProduction.fxProductionTable.setRowFactory(tv -> new TableRow<Production>() {
            Node detailsPane ;
            {
                selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        getChildren().add(detailsPane);
                    } else {
                        getChildren().remove(detailsPane);
                    }
                    this.requestLayout();
                });
                detailsPane = constructSubTable(itemProperty());
            }

            @Override
            protected double computePrefHeight(double width) {
                if (isSelected()) {
                    return super.computePrefHeight(width) + detailsPane.prefHeight(getWidth());
                } else {
                    return super.computePrefHeight(width);
                }
            }

            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                if (isSelected()) {
                    double width = getWidth();
                    double paneHeight = detailsPane.prefHeight(width);
                    detailsPane.resizeRelocate(0, getHeight()-paneHeight, width, paneHeight);
                }
            }
        });
    }

    private TableView<Quantity> constructSubTable(ObjectProperty<Production> production) {

        TableView<Quantity> subTable = new TableView<>();
        subTable.getStylesheets().add(this.getClass().getResource("/harvest/res/style/subTableStyle.css").toExternalForm());
        subTable.maxWidth(1000);
        TableColumn<Quantity, String> fxEmployee = new TableColumn<>("Employee");
        TableColumn<Quantity, Double> fxAllQuantity = new TableColumn<>("All Quantity");
        TableColumn<Quantity, Double> fxBadQuantity = new TableColumn<>("Bad Quantity");
        TableColumn<Quantity, Double> fxGoodQuantity = new TableColumn<>("Good Quantity");
        TableColumn<Quantity, Double> fxPrice = new TableColumn<>("Price");
        TableColumn<Quantity, String> fxTransport = new TableColumn<>("Transport");
        TableColumn<Quantity, String> fxCredit = new TableColumn<>("Credit");
        TableColumn<Quantity, Double> fxPayment = new TableColumn<>("Payment");
        TableColumn<Quantity, String> fxCategory = new TableColumn<>("Category");
        TableColumn<Quantity, String> fxRemarque = new TableColumn<>("Remarque");

        fxEmployee.setMinWidth(180);
        fxAllQuantity.setMinWidth(80);
        fxBadQuantity.setMinWidth(80);
        fxGoodQuantity.setMinWidth(80);
        fxPrice.setMinWidth(80);
        fxPayment.setMinWidth(80);
        fxTransport.setMinWidth(80);
        fxCredit.setMinWidth(80);
        fxCategory.setMinWidth(120);
        fxRemarque.setMinWidth(220);

        fxEmployee.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        fxAllQuantity.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));
        fxBadQuantity.setCellValueFactory(new PropertyValueFactory<>("badQuantity"));
        fxGoodQuantity.setCellValueFactory(new PropertyValueFactory<>("goodQuantity"));
        fxPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        fxPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
        fxTransport.setCellValueFactory(new PropertyValueFactory<>("transportString"));
        fxCredit.setCellValueFactory(new PropertyValueFactory<>("creditString"));
        fxCategory.setCellValueFactory(new PropertyValueFactory<>("harvestCategory"));
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("remarque"));

        subTable.getColumns().addAll(
                fxEmployee, fxAllQuantity, fxBadQuantity, fxGoodQuantity, fxPrice,
                fxTransport, fxCredit, fxPayment, fxCategory, fxRemarque);

        QuantityDAO mQuantityDAO = QuantityDAO.getInstance();
        ObservableList<Quantity> QUANTITY_WORK_LIVE = FXCollections.observableArrayList();
        production.addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                try {
                    QUANTITY_WORK_LIVE.clear();
                    QUANTITY_WORK_LIVE.setAll(mQuantityDAO.getQuantityDataByProductionId(newItem));
                    subTable.setItems(QUANTITY_WORK_LIVE);
                    subTable.setPrefHeight(100 + (QUANTITY_WORK_LIVE.size() * 30));
                    subTable.setStyle("-fx-border-color: #151819;");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return subTable;
    }

    public void deleteProduction() {
        Production production = mDisplayQuantityProduction.fxProductionTable.getSelectionModel().getSelectedItem();
        if (production == null) {
            alert.missingInfo("Credit");
            return;
        }
        QuantityDAO quantityDAO = QuantityDAO.getInstance();
        ObservableList<Quantity> list = FXCollections.observableArrayList();
        try {
            list.setAll(quantityDAO.getQuantityDataByProductionId(production));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertMaker alertDelete = new AlertMaker();
        Optional<ButtonType> result = alertDelete.deleteConfirmation("Production");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            boolean trackInsert = false;
            for (Quantity quantity : list){
                trackInsert = mProductionDAO.deleteQuantityProductionData(production, quantity);
                if (!trackInsert) break;
            }
            alert.deleteItem("Production", trackInsert);
            searchByDate();
        } else {
            alert.cancelOperation("Delete");
        }
    }

}
