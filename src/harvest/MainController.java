package harvest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController{

    @FXML AnchorPane fxMainStage;
    @FXML private AnchorPane mainCenterPane;
    @FXML AnchorPane fxProgressPane;
    @FXML HBox fxProgressHBox;
    ProgressBar pBar = new ProgressBar();

    @FXML
    public void displayProduction() { displayCenterView("/harvest/ui/production/display_production.fxml"); }
    @FXML
    public void displayHours() { displayCenterView("/harvest/res/layout/display_hours.fxml"); }
    @FXML
    public void displayGroup() { displayCenterView("/harvest/ui/group/display_group.fxml"); }
    @FXML
    public void displayFarmSeason(){ displayCenterView("/harvest/res/layout/display_farm_season.fxml"); }
    @FXML
    public void displayCredit() { displayCenterView("/harvest/res/layout/display_credit.fxml"); }
    @FXML
    public void displayEmployee() { displayCenterView("/harvest/res/layout/display_employee.fxml"); }
    @FXML
    public void displaySupplier(){ displayCenterView("/harvest/res/layout/display_supplier.fxml"); }
    @FXML
    public void displayProduct(){ displayCenterView("/harvest/res/layout/display_product.fxml"); }

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
    void addGroupWork(){ loadAddWindow("/harvest/ui/group/add_group.fxml", "Add group work"); }
    @FXML
    void addIndividualWork(){ loadAddWindow("/harvest/ui/individual/add_individual.fxml", "Add Individual Work"); }
    @FXML
    void addWorkHours(){ loadAddWindow("/harvest/res/layout/add_hours.fxml", "Add work hours"); }
    @FXML
    void addFarmSeason(){ loadAddWindow("/harvest/res/layout/add_farm.fxml", "Add Champ"); }
    @FXML
    void addCredit(){ loadAddWindow("/harvest/res/layout/add_credit.fxml", "Add Credit"); }
    @FXML
    void addTransport(){ loadAddWindow("/harvest/res/layout/add_transport.fxml", "Add Transport"); }
    @FXML
    void addEmployee(){ loadAddWindow("/harvest/res/layout/add_employee.fxml", "Add Employee"); }
    @FXML
    void addSupplier(){ loadAddWindow("/harvest/res/layout/add_supplier.fxml", "Add Fournisseur"); }
    @FXML
    void addProduct(){ loadAddWindow("/harvest/res/layout/add_product.fxml", "Add Produit"); }

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

    public void handlePreferences() {
        loadAddWindow("/harvest/ui/menu/preferences.fxml", "Preferences");
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
