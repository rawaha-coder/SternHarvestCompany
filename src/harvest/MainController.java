package harvest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
    private ToggleButton fxEmployeeBtn;
    @FXML
    private ToggleButton fxProductBtn;
    @FXML
    private Button fxAddEmployeeButton;
    @FXML
    private Button fxAddProductButton;
    @FXML
    private HBox fxHBoxTopMenu;

    private final Stage stage = new Stage(StageStyle.DECORATED);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void loadUIAddEmployee() {
        loadAddWindow("/harvest/ui/employee/add_employee.fxml", "Add New Employee", fxAddEmployeeButton);
    }
    @FXML
    void setDisplayEmployee() {
        setDisplayView("/harvest/ui/employee/display_employee.fxml", fxEmployeeBtn);
    }

    @FXML
    void loadUIAddProduct() {
        loadAddWindow("/harvest/ui/product/add_product.fxml", "Add New Product", fxAddProductButton);

    }
    @FXML
    void setDisplayProduct(){
        setDisplayView("/harvest/ui/product/display_product.fxml", fxProductBtn);
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
