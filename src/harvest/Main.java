package harvest;

import harvest.database.EmployeeDAO;
import harvest.database.ProductDAO;
import harvest.database.ProductDetailDAO;
import harvest.database.SupplierDAO;
import harvest.model.Employee;
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

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Stern harvest Company");
        primaryStage.setScene(new Scene(root, 1400, 900));
        primaryStage.show();
        //mCreditDAO.createCreditTable();
        //mEmployeeDAO.createEmployeeTable();
        //mProductDAO.createProductTable();
        //mProductDetailDAO.createProductDetailTable();
        //mFarmDAO.createFarmTable();
        //mSeasonDAO.createSeasonTable();
        //mSupplierDAO.createSupplierTable();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
