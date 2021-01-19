package harvest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {



    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ToggleButton fxHarvestProductionMenuBtn;
    @FXML
    private ToggleButton fxEmployeeMenuBtn;
    @FXML
    private ToggleButton fxSupplierMenuBtn;
    @FXML
    private ToggleButton fxProductMenuBtn;
    @FXML
    private ToggleButton fxTransportCreditMenuBtn;
    @FXML
    private ToggleButton fxFarmSeasonMenuBtn;
    @FXML
    public ToggleButton fxHarvestHoursMenuBtn;
    @FXML
    public ToggleButton fxHarvestWorkMenuBtn;

    @FXML
    private Button fxAddEmployeeButton;
    @FXML
    private Button fxAddProductButton;
    @FXML
    private Button fxAddTransportCreditButton;
    @FXML
    private Button fxAddTransportButton;
    @FXML
    private Button fxAddSupplierButton;
    @FXML
    private Button  fxAddFarmSeasonButton;
    @FXML
    public Button fxAddHarvestButton;
    @FXML
    public Button fxAddHarvestHoursButton;
    @FXML
    public Button fxSetHarvestWorkButton;
    @FXML
    private HBox fxHBoxTopMenu;

    private final Stage stage = new Stage(StageStyle.DECORATED);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getHarvestProduction();
    }


    @FXML
    public void getHarvestProduction() {
        setDisplayView("/harvest/ui/harvest/get_harvest_production.fxml", fxHarvestProductionMenuBtn);
    }

    @FXML
    void setHarvestProduction() {
        loadAddWindow("/harvest/ui/harvest/set_harvest_production.fxml", "Add Harvest", fxAddHarvestButton);
    }

    @FXML
    void setHarvestHours() {
        loadAddWindow("/harvest/ui/harvest/set_harvest_hours.fxml", "Add Harvest Hours", fxAddHarvestHoursButton);
    }

    @FXML
    void getHarvestHours(){
        setDisplayView("/harvest/ui/harvest/get_harvest_hours.fxml", fxHarvestHoursMenuBtn);
    }

    @FXML
    void setHarvestWork() {
        loadAddWindow("/harvest/ui/harvest/set_harvest_work.fxml", "Add Harvest Work", fxSetHarvestWorkButton);
    }

    @FXML
    void getHarvestWork(){
        setDisplayView("/harvest/ui/harvest/get_harvest_work.fxml", fxHarvestWorkMenuBtn);
    }

    @FXML
    void setFarmSeason() {
        loadAddWindow("/harvest/ui/farm/add_farm.fxml", "Add Farm / Season", fxAddFarmSeasonButton);
    }
    @FXML
    void getFarmSeason(){
        setDisplayView("/harvest/ui/farm/display_farm_season.fxml", fxFarmSeasonMenuBtn);
    }

    @FXML
    void setCredit() {
        loadAddWindow("/harvest/ui/credit/add_credit.fxml", "Add Credit", fxAddTransportCreditButton);
    }
    @FXML
    void getCredit() {
        setDisplayView("/harvest/ui/credit/display_credit.fxml", fxTransportCreditMenuBtn);
    }

    @FXML
    void setTransport() {
        loadAddWindow("/harvest/ui/credit/add_transport.fxml", "Add Transport", fxAddTransportButton);
    }

    @FXML
    void setEmployee() {
        loadAddWindow("/harvest/ui/employee/add_employee.fxml", "Add New Employee", fxAddEmployeeButton);
    }
    @FXML
    void getEmployee() {
        setDisplayView("/harvest/ui/employee/display_employee.fxml", fxEmployeeMenuBtn);
    }

    @FXML
    void setSupplier() {
        loadAddWindow("/harvest/ui/supplier/add_supplier.fxml", "Add supplier", fxAddSupplierButton);
    }
    @FXML
    void getSupplier(){
        setDisplayView("/harvest/ui/supplier/display_supplier.fxml", fxSupplierMenuBtn);
    }

    @FXML
    void setProduct() {
        loadAddWindow("/harvest/ui/product/add_product.fxml", "Add New Product", fxAddProductButton);

    }
    @FXML
    void getProduct(){
        setDisplayView("/harvest/ui/product/display_product.fxml", fxProductMenuBtn);
    }

    public void setDisplayView(String location, ToggleButton fxButton){
        try {
            for (Node node: fxHBoxTopMenu.getChildren()){
                node.setDisable(false);
            }
            fxButton.setDisable(true);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource(location));
            AnchorPane view = loader.load();
            mainBorderPane.setCenter(view);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void loadAddWindow(String location, String title, Button fxButton){
        try {
            fxButton.disableProperty().bind(stage.showingProperty());
            Parent parent = FXMLLoader.load(MainController.class.getResource(location));
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
