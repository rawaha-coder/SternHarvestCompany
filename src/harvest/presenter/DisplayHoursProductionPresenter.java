package harvest.presenter;

import harvest.database.HoursDAO;
import harvest.database.ProductionDAO;
import harvest.model.Hours;
import harvest.model.Production;
import harvest.util.AlertMaker;
import harvest.view.AddHoursController;
import harvest.view.DisplayHoursProduction;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Optional;

public class DisplayHoursProductionPresenter {

    public static ObservableList<Production> HOURS_PRODUCTION_LIVE_DATA = FXCollections.observableArrayList();
    ObservableList<Hours> HOURS_WORK_LIVE = FXCollections.observableArrayList();
    ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    DisplayHoursProduction mDisplayHoursProduction;
    public final AlertMaker alert = new AlertMaker();


    public DisplayHoursProductionPresenter(DisplayHoursProduction displayHoursProduction) {
        mDisplayHoursProduction = displayHoursProduction;
        mDisplayHoursProduction.fxProductionTable.setItems(HOURS_PRODUCTION_LIVE_DATA);
        initHoursProductionLiveData();
        nestedTable();
    }

    public void initHoursProductionLiveData(){
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(30);
        mDisplayHoursProduction.fxDatePickerFrom.setValue(fromDate);
        mDisplayHoursProduction.fxDatePickerTo.setValue(toDate);
        Thread thread = new Thread(() -> {
            System.out.println("Thread Running");
            updateData(fromDate, toDate);
        });
        thread.start();
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
        mDisplayHoursProduction.fxProductionTable.setRowFactory(tv -> new TableRow<>() {
            Node detailsPane;

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
                    detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
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
        fxEndMorning.setCellValueFactory(new PropertyValueFactory<>("endMorning"));
        fxStartNoon.setCellValueFactory(new PropertyValueFactory<>("startNoon"));
        fxEndNoon.setCellValueFactory(new PropertyValueFactory<>("endNoon"));
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
        ObservableList<Hours> HOURS_WORK = FXCollections.observableArrayList();
        production.addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                try {
                    HOURS_WORK.clear();
                    HOURS_WORK.setAll(mHoursDAO.getHoursDataByProductionId(newItem));
                    subTable.setItems(HOURS_WORK);
                    subTable.setPrefHeight(100 + (HOURS_WORK.size() * 30));
                    subTable.setStyle("-fx-border-color: #151819;");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        HOURS_WORK_LIVE.clear();
        HOURS_WORK_LIVE.setAll(HOURS_WORK);
        return subTable;
    }

    public void editProduction() {
        Production production = mDisplayHoursProduction.fxProductionTable.getSelectionModel().getSelectedItem();
        if (production == null) {
            alert.missingInfo("Production");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/res/layout/add_hours.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddHoursController controller = loader.getController();
            controller.inflateUI(production, this);
            stage.setTitle("Edit Production");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduction() {
        Production production = mDisplayHoursProduction.fxProductionTable.getSelectionModel().getSelectedItem();
        if (production == null) {
            alert.missingInfo("Credit");
            return;
        }
        HoursDAO mHoursDAO = HoursDAO.getInstance();
        ObservableList<Hours> listHours = FXCollections.observableArrayList();
        try {
            listHours.setAll(mHoursDAO.getHoursDataByProductionId(production));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertMaker alertDelete = new AlertMaker();
        Optional<ButtonType> result = alertDelete.deleteConfirmation("Production");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            boolean trackInsert = false;
            for (Hours hours : listHours){
                trackInsert = mProductionDAO.deleteHoursProductionData(production, hours);
                if (!trackInsert) break;
            }
            alert.deleteItem("Production", trackInsert);
            searchByDate();
        } else {
            alert.cancelOperation("Delete");
        }
    }

}
