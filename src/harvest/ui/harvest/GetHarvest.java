package harvest.ui.harvest;

import harvest.Main;
import harvest.MainController;
import harvest.database.HarvestProductionDAO;
import harvest.database.HarvestWorkDAO;
import harvest.model.HarvestHours;
import harvest.model.HarvestProduction;
import harvest.model.HarvestWork;
import harvest.ui.employee.AddEmployeeController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GetHarvest implements Initializable {

    public static ObservableList<HarvestProduction> HARVEST_PRODUCTION_LIVE_LIST = FXCollections.observableArrayList();

    HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();

    @FXML
    private AnchorPane fxGetHarvestUI;
    @FXML
    private VBox fxVBoxHarvestProduction;
    @FXML
    private TableView<HarvestProduction> fxHarvestTable;
    @FXML
    private TableColumn<HarvestProduction, Button> fxExtendColumn;
    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestDate;
    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestSupplier;
    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestFarm;
    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestProduct;
    @FXML
    private TableColumn<HarvestProduction, String> fxHarvestCodeProduct;
    @FXML
    private TableColumn<HarvestProduction, Integer> fxHarvestEmployee;
    @FXML
    private TableColumn<HarvestProduction, Double> fxHarvestQuantity;
    @FXML
    private TableColumn<HarvestProduction, Double> fxHarvestPrice;
    @FXML
    private TableColumn<HarvestProduction, Double> fxHarvestCost;

    @FXML
    private Label fxDate;
    @FXML
    private DatePicker fxDatePicker;

    boolean first = true;
    double detailsPaneHeight = 0;
    Callback<TableView<HarvestProduction>, TableRow<HarvestProduction>> rowFactory
            = new Callback<TableView<HarvestProduction>, TableRow<HarvestProduction>>() {
        @Override
        public TableRow<HarvestProduction> call(TableView<HarvestProduction> harvestProductionTableView) {
            return new TableRow<>(){

                Node detailsPane = null;
                {
                    this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event){
                            if(selectedProperty().getValue() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 2){
                                detailsPane = constructSubTable(getItem());
                                getChildren().add(detailsPane);
                                detailsPaneHeight = detailsPane.prefHeight(60);
                            }else{
                                getChildren().remove(detailsPane);
                                detailsPaneHeight = 0;
                            }
                            requestLayout();
                        }
                    });

                }

                @Override
                protected double computePrefHeight(double width) {
                    if (detailsPane != null) {
                        return super.computePrefHeight(width) +detailsPaneHeight;
                    } else {
                        return super.computePrefHeight(width);
                    }
                }

                @Override
                protected void layoutChildren() {
                    super.layoutChildren();
                    if (detailsPane != null) {
                        double width = getWidth();
                        double paneHeight = detailsPaneHeight;
                        detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
                    }
                }

            };
        }
    };

    Callback<TableColumn<HarvestProduction, Button>, TableCell<HarvestProduction, Button>> cellFactory
            = //
            new Callback<TableColumn<HarvestProduction, Button>, TableCell<HarvestProduction, Button>>() {
                @Override
                public TableCell call(final TableColumn<HarvestProduction, Button> param) {
                    final TableCell<HarvestProduction, Button> cell = new TableCell<HarvestProduction, Button>() {

                        final Button btn = new Button("+");

                        @Override
                        public void updateItem(Button item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                btn.setOnMouseClicked(event -> {
                                    if(event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1 && first){
                                        System.out.println("Mouse Entered on Click Me Two " + first);
                                        btn.setText("-");
                                        first = false;
                                    }else if (event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1 && !first){
                                        System.out.println("Mouse Entered on Click Me Two " + first);
                                        btn.setText("+");
                                        reLoadUI();
                                        first = true;
                                    }
                                });
                                setGraphic(btn);
                            }
                            setText(null);
                        }
                    };
                    return cell;
                }
            };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        updateData();
        fxHarvestTable.setRowFactory(rowFactory);
        //constructTable();

        //fxExtendColumn.setCellFactory(cellFactory);
    }


    private void initTable() {
        fxHarvestTable.setItems(HARVEST_PRODUCTION_LIVE_LIST);
        fxExtendColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        fxHarvestDate.setCellValueFactory(new PropertyValueFactory<>("harvestProductionDate"));
        fxHarvestSupplier.setCellValueFactory(it -> it.getValue().getSupplier().supplierNameProperty());
        fxHarvestFarm.setCellValueFactory(it -> it.getValue().getFarm().farmNameProperty());
        fxHarvestProduct.setCellValueFactory(it -> it.getValue().getProduct().productNameProperty() );
        fxHarvestCodeProduct.setCellValueFactory(it -> it.getValue().getProductDetail().productCodeProperty());
        fxHarvestEmployee.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalEmployee"));
        fxHarvestQuantity.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalQuantity"));
        fxHarvestPrice.setCellValueFactory(new PropertyValueFactory<>("harvestProductionPrice"));
        fxHarvestCost.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalCost"));
    }

    public void updateData(){
        HARVEST_PRODUCTION_LIVE_LIST.clear();
        try {
            HARVEST_PRODUCTION_LIVE_LIST.setAll(mHarvestProductionDAO.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reLoadUI(){
        System.out.println("reLoadUI");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/main.fxml"));
            Parent parent = loader.load();
            MainController controller = loader.getController();
            controller.getHarvestProduction();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private Button fxReLoadUI;

    @FXML
    void handleReloadUI() {
        System.out.println("handleReloadUI");
        reLoadUI();
    }

    private void constructTable() {
        fxHarvestTable.setRowFactory(rowFactory);
//        fxHarvestTable.setRowFactory(tv -> new TableRow<>() {
//            Node detailsPane;
//            {
//                this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
//                    if (isNowSelected) {
//                        System.out.println(this.toString());
//                        detailsPane = constructSubTable(getItem());
//                        this.getChildren().add(detailsPane);
//                    } else {
//                        this.getChildren().remove(detailsPane);
//                    }
//                    this.requestLayout();
//                });
//
//            }
//
//            @Override
//            protected double computePrefHeight(double width) {
//                if (isSelected()) {
//                    return super.computePrefHeight(width) + detailsPane.prefHeight(60);
//                } else {
//                    return super.computePrefHeight(width);
//                }
//            }
//
//            @Override
//            protected void layoutChildren() {
//                super.layoutChildren();
//                if (isSelected()) {
//                    double width = getWidth();
//                    double paneHeight = detailsPane.prefHeight(width);
//                    detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
//                }
//            }
//        });
    }

    private TableView<HarvestWork> constructSubTable(HarvestProduction harvestProduction) {

        List<HarvestProduction> list = new ArrayList<>();
        list.add(harvestProduction);

        TableView<HarvestWork> subTable = new TableView<>();
        subTable.getStylesheets().add(this.getClass().getResource("harvestStyle.css").toExternalForm());
        subTable.setMinWidth(1000);
        TableColumn<HarvestWork, Double> harvestDate = new TableColumn<>("Date");
        harvestDate.setMinWidth(200);
        harvestDate.setCellValueFactory(new PropertyValueFactory<>("harvestDate"));

        TableColumn<HarvestWork, String> employeeName = new TableColumn<>("Employee");
        employeeName.setMinWidth(200);
        employeeName.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());

        TableColumn<HarvestWork, Double> allQuantity = new TableColumn<>("All Quantity");
        allQuantity.setMinWidth(200);
        allQuantity.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));

        TableColumn<HarvestWork, Double> badQuality = new TableColumn<>("Bad Quality");
        badQuality.setMinWidth(200);
        badQuality.setCellValueFactory(new PropertyValueFactory<>("badQuality"));

        HarvestWorkDAO harvestWorkDAO = HarvestWorkDAO.getInstance();
        ObservableList<HarvestWork> HARVEST_WORK_LIVE = FXCollections.observableArrayList();
        try {
            HARVEST_WORK_LIVE.setAll(harvestWorkDAO.getData(harvestProduction.getHarvestProductionDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        subTable.setItems(HARVEST_WORK_LIVE);
        subTable.getColumns().addAll(harvestDate, employeeName, allQuantity, badQuality);
        subTable.setPrefHeight(45 + (HARVEST_WORK_LIVE.size() * 30));
        subTable.setStyle("-fx-border-color: #42bff4;");
        return subTable;
    }




}
