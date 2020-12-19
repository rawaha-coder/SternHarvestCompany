package harvest;

import harvest.database.ProductDAO;
import harvest.database.ProductDetailDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

   // CreditDAO mCreditDAO = CreditDAO.getInstance();
    //FarmDAO mFarmDAO = FarmDAO.getInstance();
    //SeasonDAO mSeasonDAO = SeasonDAO.getInstance();
    //ProductDAO mProductDAO = ProductDAO.getInstance();
    //ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Stern harvest Company");
        primaryStage.setScene(new Scene(root, 1400, 900));
        primaryStage.show();
        //mCreditDAO.createCreditTable();
        //EmployeeDAO.createEmployeeTable();
        //mProductDAO.createProductTable();
        //mProductDetailDAO.createProductDetailTable();
        //mFarmDAO.createFarmTable();
        //mSeasonDAO.createSeasonTable();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
