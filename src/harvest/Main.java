package harvest;

import harvest.database.DBHandler;
import harvest.viewmodel.EmployeeDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Stern harvest Company");
        primaryStage.setScene(new Scene(root, 1400, 900));
        primaryStage.show();
        EmployeeDAO.createEmployeeTable();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        DBHandler.dbConnect();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DBHandler.dbDisconnect();
    }
}
