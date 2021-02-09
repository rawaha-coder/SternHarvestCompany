package harvest;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController{

    @FXML AnchorPane fxMainStage;
    @FXML private AnchorPane mainCenterPane;
    @FXML AnchorPane fxProgressPane;
    @FXML HBox fxProgressHBox;
    ProgressBar pBar = new ProgressBar();

    @FXML
    public void getProduction() { displayCenterView("/harvest/ui/harvest/get_harvest_production.fxml"); }
    @FXML
    public void getHours() { displayCenterView("/harvest/ui/harvest/get_harvest_hours.fxml"); }
    @FXML
    void getWork() { displayCenterView("/harvest/ui/harvest/get_harvest_work.fxml"); }
    @FXML
    void getFarmSeason(){ displayCenterView("/harvest/ui/farm/display_farm_season.fxml"); }
    @FXML
    void getCredit() { displayCenterView("/harvest/ui/credit/display_credit.fxml"); }
    @FXML
    void getEmployee() { displayCenterView("/harvest/ui/employee/display_employee.fxml"); }
    @FXML
    void getSupplier(){ displayCenterView("/harvest/ui/supplier/display_supplier.fxml"); }
    @FXML
    void getProduct(){ displayCenterView("/harvest/ui/product/display_product.fxml"); }

    public void displayCenterView(String location){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource(location));
            AnchorPane view = loader.load();
            mainCenterPane.getChildren().setAll(view);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void addWorkByHours(){ loadAddWindow("/harvest/ui/harvest/set_harvest_hours.fxml", "Add hours work"); }
    @FXML
    void addWorkByIndividual(){ loadAddWindow("/harvest/ui/harvest/set_harvest_work.fxml", "Add individual work "); }
    @FXML
    void addWorkByGroup(){ loadAddWindow("/harvest/ui/harvest/set_harvest_work.fxml", "Add group work"); }
    @FXML
    void addFarmSeason(){ loadAddWindow("/harvest/ui/farm/add_farm.fxml", "Add Champ"); }
    @FXML
    void addCredit(){ loadAddWindow("/harvest/ui/credit/add_credit.fxml", "Add Credit"); }
    @FXML
    void addTransport(){ loadAddWindow("/harvest/ui/credit/add_transport.fxml", "Add Transport"); }
    @FXML
    void addEmployee(){ loadAddWindow("/harvest/ui/employee/add_employee.fxml", "Add Employee"); }
    @FXML
    void addSupplier(){ loadAddWindow("/harvest/ui/supplier/add_supplier.fxml", "Add Fournisseur"); }
    @FXML
    void addProduct(){ loadAddWindow("/harvest/ui/product/add_product.fxml", "Add Produit"); }

    private void loadAddWindow(String location, String title){
        Stage subStage = new Stage(StageStyle.DECORATED);
        Stage mainStage = (Stage) fxMainStage.getScene().getWindow();
        try {
            Parent parent = FXMLLoader.load(MainController.class.getResource(location));
            subStage.setTitle(title);
            subStage.setScene(new Scene(parent));
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.initOwner(mainStage);
            subStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void showProgress(){
//        pBar.setMinWidth(fxProgressHBox.getWidth());
//        fxProgressHBox.getChildren().setAll(pBar);
//    }
//
//    private void hideProgress(){
//        pBar.setMinWidth(fxProgressHBox.getWidth());
//        fxProgressHBox.getChildren().removeAll(pBar);
//    }

}
