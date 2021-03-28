package harvest.presenter;

import harvest.database.HoursDAO;
import harvest.database.ProductionDAO;
import harvest.model.Hours;
import harvest.model.Production;
import harvest.model.Quantity;
import harvest.view.DisplayHoursProduction;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class DisplayHoursProductionPresenter {

    public static ObservableList<Production> HOURS_PRODUCTION_LIVE_DATA = FXCollections.observableArrayList();
    ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    DisplayHoursProduction mDisplayHoursProduction;

    Thread thread = new Thread(){
        public void run(){
            System.out.println("Thread Running");
            initHoursProductionLiveData();
        }
    };

    public DisplayHoursProductionPresenter(DisplayHoursProduction displayHoursProduction) {
        mDisplayHoursProduction = displayHoursProduction;
        mDisplayHoursProduction.fxHoursProductionTable.setItems(HOURS_PRODUCTION_LIVE_DATA);
        thread.start();
        nestedTable();
    }

    public void initHoursProductionLiveData(){
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(30);
        mDisplayHoursProduction.fxDatePickerFrom.setValue(fromDate);
        mDisplayHoursProduction.fxDatePickerTo.setValue(toDate);
        updateData(fromDate, toDate);
    }

    public void searchByDate() {
        if (mDisplayHoursProduction.fxDatePickerFrom.getValue() != null && mDisplayHoursProduction.fxDatePickerTo.getValue() != null){
            LocalDate fromDate = mDisplayHoursProduction.fxDatePickerFrom.getValue();
            LocalDate toDate = mDisplayHoursProduction.fxDatePickerTo.getValue();
            updateData(fromDate, toDate);
        }
    }

    public void updateData(LocalDate fromDate, LocalDate toDate){
        HOURS_PRODUCTION_LIVE_DATA.clear();
        try {
            HOURS_PRODUCTION_LIVE_DATA.setAll(mProductionDAO.searchHoursProductionData(Date.valueOf(fromDate), Date.valueOf(toDate), 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nestedTable(){
        mDisplayHoursProduction.fxHoursProductionTable.setRowFactory(tv -> new TableRow<Production>() {
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

    private TableView<Hours> constructSubTable(ObjectProperty<Production> production) {

        TableView<Hours> subTable = new TableView<>();
        subTable.getStylesheets().add(this.getClass().getResource("/harvest/res/style/subTableStyle.css").toExternalForm());
        subTable.maxWidth(1000);
        TableColumn<Hours, String> fxEmployee = new TableColumn<>("Employee");
        TableColumn<Hours, Time> fxStartMorning = new TableColumn<>("D.M");
        TableColumn<Hours, Time> fxEndMorning = new TableColumn<>("F.M");
        TableColumn<Hours, Time> fxStartNoon = new TableColumn<>("D.S");
        TableColumn<Hours, Time> fxEndNoon = new TableColumn<>("F.S");
        TableColumn<Hours, String> totalMinutes = new TableColumn<>("Minutes");
        TableColumn<Hours, Double> fxPrice = new TableColumn<>("Price");
        TableColumn<Hours, String> fxTransport = new TableColumn<>("Transport");
        TableColumn<Hours, String> fxCredit = new TableColumn<>("Credit");
        TableColumn<Hours, Double> fxPayment = new TableColumn<>("Payment");
        TableColumn<Hours, String> fxCategory = new TableColumn<>("Category");
        TableColumn<Hours, String> fxRemarque = new TableColumn<>("Remarque");

        fxEmployee.setMinWidth(180);
        fxStartMorning.setMinWidth(80);
        fxEndMorning.setMinWidth(80);
        fxStartNoon.setMinWidth(80);
        fxEndNoon.setMinWidth(80);
        totalMinutes.setMinWidth(80);
        fxPrice.setMinWidth(80);
        fxTransport.setMinWidth(80);
        fxCredit.setMinWidth(80);
        fxPayment.setMinWidth(80);
        fxCategory.setMinWidth(120);
        fxRemarque.setMinWidth(220);

        fxEmployee.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        fxStartMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxStartNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        totalMinutes.setCellValueFactory(new PropertyValueFactory<>("TotalMinutesString"));
        fxPrice.setCellValueFactory(new PropertyValueFactory<>("hourPrice"));
        fxTransport.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCredit.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
        fxCategory.setCellValueFactory(new PropertyValueFactory<>("employeeCategory"));
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("remarque"));

        subTable.getColumns().addAll(
                fxEmployee, fxStartMorning, fxEndMorning, fxStartNoon, fxEndNoon,
                totalMinutes, fxPrice, fxTransport, fxCredit, fxPayment, fxCategory, fxRemarque);

        HoursDAO mHoursDAO = HoursDAO.getInstance();
        ObservableList<Hours> HOURS_WORK_LIVE = FXCollections.observableArrayList();
        production.addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                try {
                    HOURS_WORK_LIVE.clear();
                    HOURS_WORK_LIVE.setAll(mHoursDAO.getHoursDataByProductionId(newItem));
                    subTable.setItems(HOURS_WORK_LIVE);
                    subTable.setPrefHeight(100 + (HOURS_WORK_LIVE.size() * 30));
                    subTable.setStyle("-fx-border-color: #151819;");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return subTable;
    }
}
