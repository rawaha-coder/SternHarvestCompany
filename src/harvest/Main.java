package harvest;

import harvest.database.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //EmployeeDAO mEmployeeDAO = EmployeeDAO.getInstance();
    //ProductDAO mProductDAO = ProductDAO.getInstance();
    //ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    //CreditDAO mCreditDAO = CreditDAO.getInstance();
    //FarmDAO mFarmDAO = FarmDAO.getInstance();
    //SeasonDAO mSeasonDAO = SeasonDAO.getInstance();
    //SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    //SupplyDAO mSupplyDAO = SupplyDAO.getInstance();
    //TransportDAO mTransportDAO = TransportDAO.getInstance();
    //HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
    //HarvestHoursDAO mHarvestHoursDAO = HarvestHoursDAO.getInstance();
    //HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();
    //HarvestIndividualDAO mHarvestIndividualDAO = HarvestIndividualDAO.getInstance();
    //PreferencesDAO mPreferencesDAO = PreferencesDAO.getInstance();
    //ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    //HoursDAO mHoursDAO = HoursDAO.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Stern individual Company");
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
        //mPreferencesDAO.createPreferencesTable();
        //mPreferencesDAO.initPreferencesTable();
        //mProductionDAO.createProductionTable();
        //mHoursDAO.createTable();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
