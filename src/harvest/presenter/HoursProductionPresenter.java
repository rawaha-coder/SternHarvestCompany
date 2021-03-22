package harvest.presenter;

import harvest.database.HoursDAO;
import harvest.database.ProductionDAO;
import harvest.model.Hours;
import harvest.model.Production;
import harvest.view.HoursProduction;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class HoursProductionPresenter {

    public static ObservableList<Production> HOURS_PRODUCTION_LIVE_DATA = FXCollections.observableArrayList();
    ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    HoursProduction mHoursProduction;

    Thread thread = new Thread(){
        public void run(){
            System.out.println("Thread Running");
            initHoursProductionLiveData();
        }
    };

    public HoursProductionPresenter(HoursProduction hoursProduction) {
        mHoursProduction = hoursProduction;
        mHoursProduction.fxHoursProductionTable.setItems(HOURS_PRODUCTION_LIVE_DATA);
        thread.start();
        nestedTable();
    }

    public void initHoursProductionLiveData(){
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(30);
        mHoursProduction.fxDatePickerFrom.setValue(fromDate);
        mHoursProduction.fxDatePickerTo.setValue(toDate);
        updateData(fromDate, toDate);
    }

    public void searchByDate() {
        if (mHoursProduction.fxDatePickerFrom.getValue() != null && mHoursProduction.fxDatePickerTo.getValue() != null){
            LocalDate fromDate = mHoursProduction.fxDatePickerFrom.getValue();
            LocalDate toDate = mHoursProduction.fxDatePickerTo.getValue();
            updateData(fromDate, toDate);
        }
    }

    public void updateData(LocalDate fromDate, LocalDate toDate){
        HOURS_PRODUCTION_LIVE_DATA.clear();
        try {
            HOURS_PRODUCTION_LIVE_DATA.setAll(mProductionDAO.searchHoursProductionData(Date.valueOf(fromDate), Date.valueOf(toDate)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nestedTable(){
        mHoursProduction.fxHoursProductionTable.setRowFactory(tv -> new TableRow<Production>() {
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
                    return super.computePrefHeight(width)+detailsPane.prefHeight(getWidth());
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
        subTable.maxWidth(900);
        TableColumn<Hours, String> fxEmployee = new TableColumn<>("Employee");
        TableColumn<Hours, Time> fxStartMorning = new TableColumn<>("D.M");
        TableColumn<Hours, Time> fxEndMorning = new TableColumn<>("F.M");
        TableColumn<Hours, Time> fxStartNoon = new TableColumn<>("D.S");
        TableColumn<Hours, Time> fxEndNoon = new TableColumn<>("F.S");
        TableColumn<Hours, Time> totalMinutes = new TableColumn<>("Minutes");
        TableColumn<Hours, String> fxTransport = new TableColumn<>("Transport");
        TableColumn<Hours, String> fxCredit = new TableColumn<>("Credit");
        TableColumn<Hours, String> fxCategory = new TableColumn<>("Category");
        TableColumn<Hours, String> fxRemarque = new TableColumn<>("Remarque");

        fxEmployee.setMinWidth(140);
        fxStartMorning.setMinWidth(80);
        fxEndMorning.setMinWidth(80);
        fxStartNoon.setMinWidth(80);
        fxEndNoon.setMinWidth(80);
        totalMinutes.setMinWidth(80);
        fxTransport.setMinWidth(80);
        fxCredit.setMinWidth(80);
        fxCategory.setMinWidth(120);
        fxRemarque.setMinWidth(220);

        fxEmployee.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        fxStartMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxEndMorning.setCellValueFactory(new PropertyValueFactory<>("startMorning"));
        fxStartNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        totalMinutes.setCellValueFactory(new PropertyValueFactory<>("totalMinutes"));
        fxTransport.setCellValueFactory(new PropertyValueFactory<>("transportAmount"));
        fxCredit.setCellValueFactory(new PropertyValueFactory<>("creditAmount"));
        fxCategory.setCellValueFactory(new PropertyValueFactory<>("employeeCategory"));
        fxRemarque.setCellValueFactory(new PropertyValueFactory<>("remarque"));


        subTable.getColumns().addAll(
                fxEmployee, fxStartMorning, fxEndMorning, fxStartNoon, fxEndNoon,
                totalMinutes, fxTransport, fxCredit, fxCategory, fxRemarque);

        HoursDAO mHoursDAO = HoursDAO.getInstance();
        Date date = Date.valueOf(LocalDate.now());
        ObservableList<Hours> HOURS_WORK_LIVE = FXCollections.observableArrayList();
        try {
            HOURS_WORK_LIVE.clear();
            HOURS_WORK_LIVE.setAll(mHoursDAO.getData(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        subTable.setItems(HOURS_WORK_LIVE);
        subTable.setPrefHeight(75 + (HOURS_WORK_LIVE.size() * 30));
        subTable.setStyle("-fx-border-color: #151819;");
        return subTable;
    }
}