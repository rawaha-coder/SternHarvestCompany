package harvest.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMaker {
    Alert alert = new Alert(Alert.AlertType.NONE);

    public void show(String title, String message, AlertType alertType){
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();
    }
}
