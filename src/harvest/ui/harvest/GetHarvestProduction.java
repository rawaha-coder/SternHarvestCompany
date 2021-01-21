package harvest.ui.harvest;

import harvest.MainController;
import harvest.database.HarvestProductionDAO;
import harvest.database.HarvestWorkDAO;
import harvest.model.HarvestProduction;
import harvest.model.HarvestWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GetHarvestProduction implements Initializable {

    public static ObservableList<HarvestProduction> HARVEST_PRODUCTION_LIVE_LIST = FXCollections.observableArrayList();
    HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();

    @FXML
    private TableView<HarvestProduction> fxHarvestTable;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        updateData();
        constructTable();
    }

    private void initTable() {
        fxHarvestTable.setItems(HARVEST_PRODUCTION_LIVE_LIST);
        fxHarvestDate.setCellValueFactory(new PropertyValueFactory<>("harvestProductionDate"));
        fxHarvestSupplier.setCellValueFactory(it -> it.getValue().getSupplier().supplierNameProperty());
        fxHarvestFarm.setCellValueFactory(it -> it.getValue().getFarm().farmNameProperty());
        fxHarvestProduct.setCellValueFactory(it -> it.getValue().getProduct().productNameProperty());
        fxHarvestCodeProduct.setCellValueFactory(it -> it.getValue().getProductDetail().productCodeProperty());
        fxHarvestEmployee.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalEmployee"));
        fxHarvestQuantity.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalQuantity"));
        fxHarvestPrice.setCellValueFactory(new PropertyValueFactory<>("harvestProductionPrice"));
        fxHarvestCost.setCellValueFactory(new PropertyValueFactory<>("harvestProductionTotalCost"));
    }

    public void updateData() {
        HARVEST_PRODUCTION_LIVE_LIST.clear();
        try {
            HARVEST_PRODUCTION_LIVE_LIST.setAll(mHarvestProductionDAO.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSearch(){
        //TODO
    }

    @FXML
    void handleReloadUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/main.fxml"));
            loader.load();
            MainController controller = loader.getController();
            controller.getHarvestProduction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constructTable() {
        fxHarvestTable.setRowFactory(tv -> new TableRow<>() {
            Node detailsPane;

            {
                this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        detailsPane = constructSubTable(getItem());
                        this.getChildren().add(detailsPane);
                    } else {
                        this.getChildren().remove(detailsPane);
                    }
                    this.requestLayout();
                });

            }

            @Override
            protected double computePrefHeight(double width) {
                if (isSelected()) {
                    return super.computePrefHeight(width) + detailsPane.prefHeight(60);
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

    private TableView<HarvestWork> constructSubTable(HarvestProduction harvestProduction) {

        TableView<HarvestWork> subTable = new TableView<>();
        subTable.getStylesheets().add(this.getClass().getResource("harvestStyle.css").toExternalForm());
        subTable.setMinWidth(1300);
        TableColumn<HarvestWork, Double> harvestDate = new TableColumn<>("Date");
        harvestDate.setMinWidth(140);
        harvestDate.setCellValueFactory(new PropertyValueFactory<>("harvestDate"));

        TableColumn<HarvestWork, String> employeeName = new TableColumn<>("Employee");
        employeeName.setMinWidth(200);
        employeeName.setCellValueFactory(it -> it.getValue().getEmployee().employeeFullNameProperty());

        TableColumn<HarvestWork, Double> allQuantity = new TableColumn<>("All Quantity");
        allQuantity.setMinWidth(120);
        allQuantity.setCellValueFactory(new PropertyValueFactory<>("allQuantity"));

        TableColumn<HarvestWork, Double> badQuality = new TableColumn<>("Bad Quality");
        badQuality.setMinWidth(120);
        badQuality.setCellValueFactory(new PropertyValueFactory<>("badQuality"));

        TableColumn<HarvestWork, Double> goodQuality = new TableColumn<>("Good Quality");
        goodQuality.setMinWidth(120);
        goodQuality.setCellValueFactory(new PropertyValueFactory<>("goodQuality"));

        TableColumn<HarvestWork, Double> productPrice = new TableColumn<>("Product Price");
        productPrice.setMinWidth(120);
        productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

        TableColumn<HarvestWork, Double> netAmount = new TableColumn<>("Net Amount");
        netAmount.setMinWidth(120);
        netAmount.setCellValueFactory(new PropertyValueFactory<>("netAmount"));

        HarvestWorkDAO harvestWorkDAO = HarvestWorkDAO.getInstance();
        ObservableList<HarvestWork> HARVEST_WORK_LIVE = FXCollections.observableArrayList();
        try {
            HARVEST_WORK_LIVE.setAll(harvestWorkDAO.getData(harvestProduction.getHarvestProductionDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        subTable.setItems(HARVEST_WORK_LIVE);
        subTable.getColumns().addAll(harvestDate, employeeName, allQuantity, badQuality, goodQuality, productPrice, netAmount);
        subTable.setPrefHeight(45 + (HARVEST_WORK_LIVE.size() * 30));
        subTable.setStyle("-fx-border-color: #42bff4;");
        return subTable;
    }
}
