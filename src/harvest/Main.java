package harvest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
   // CreditDAO mCreditDAO = CreditDAO.getInstance();
    //FarmDAO mFarmDAO = FarmDAO.getInstance();
    //SeasonDAO mSeasonDAO = SeasonDAO.getInstance();
    //ProductDAO mProductDAO = ProductDAO.getInstance();
    //ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    //SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    //SupplyDAO mSupplyDAO = SupplyDAO.getInstance();
    //TransportDAO mTransportDAO = TransportDAO.getInstance();
    //HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
    //HarvestHoursDAO mHarvestHoursDAO = HarvestHoursDAO.getInstance();
    //HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();
    //HarvestIndividualDAO mHarvestIndividualDAO = HarvestIndividualDAO.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        // Create a ScrollPane
        //ScrollPane scrollPane = new ScrollPane();
        //scrollPane.setStyle("style.css");
        // Set content for ScrollPane
        //scrollPane.setContent(root);
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        //scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        primaryStage.setTitle("Stern harvest Company");
        primaryStage.setScene(new Scene(root, 1300, 700));
        primaryStage.show();
        //mCreditDAO.createCreditTable();
        //mEmployeeDAO.createEmployeeTable();
        //mProductDAO.createProductTable();
        //mProductDetailDAO.createProductDetailTable();
        //mFarmDAO.createFarmTable();
        //mSeasonDAO.createSeasonTable();
        //mSupplierDAO.createSupplierTable();
        //mSupplyDAO.createSupplyTable();
        //mTransportDAO.createTransportTable();
        //mHarvestDAO.createHarvestTable();
        //mHarvestHoursDAO.createHarvestTable();
        //mHarvestProductionDAO.createHarvestTable();
        //mHarvestIndividualDAO.createHarvestTable();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
