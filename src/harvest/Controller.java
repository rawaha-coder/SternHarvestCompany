package harvest;

import harvest.util.AlertMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    private final Stage stage = new Stage(StageStyle.DECORATED);
    private final AlertMaker alert = new AlertMaker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // Add Buttons methods to display the add views in UI
    @FXML
    void loadUIAddEmployee(ActionEvent event) {
        loadAddWindow("/harvest/ui/employee/add_employee.fxml", "Add New Employee");
    }

    private void loadAddWindow(String location, String title){
        try {
            Parent parent = FXMLLoader.load(Controller.class.getResource(location));
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
