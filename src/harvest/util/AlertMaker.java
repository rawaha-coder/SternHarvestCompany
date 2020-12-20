package harvest.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertMaker {
    Alert alert = new Alert(Alert.AlertType.NONE);

    public void show(String title, String message, AlertType alertType){
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();
    }

    public void show(String title){
        alert.setTitle(title + " info missing");
        alert.setHeaderText("Required fields are missing");
        alert.setContentText("Please enter correct data in required fields!");
        alert.setAlertType(AlertType.INFORMATION);
        alert.showAndWait();
    }

    public Optional<ButtonType> deleteConfirmation(String item){
        alert.setTitle(item + " Delete Confirmation");
        alert.setHeaderText("This will erase ALL " + item + " information from \nthe database, NOT JUST from this table");
        alert.setContentText("Press OK to confirm delete of this " + item);
        alert.setAlertType(AlertType.CONFIRMATION);
        return alert.showAndWait();
    }

    public void selectDeleteItem(String item){
        alert.setTitle("Selected Required");
        alert.setHeaderText("Required selected " + item);
        alert.setContentText("Please select an "+ item + " from the table for deletion");
        alert.setAlertType(AlertType.INFORMATION);
        alert.showAndWait();
    }
    public void selectEditItem(String item){
        alert.setTitle("Selected Required");
        alert.setHeaderText("Required selected " + item);
        alert.setContentText("Please select an "+ item + " from the table for edition");
        alert.setAlertType(AlertType.INFORMATION);
        alert.showAndWait();
    }

    public void saveItem(String item, boolean isUpdated){

        if (isUpdated){
            alert.setTitle("Save Confirmation");
            alert.setHeaderText(item + " saved");
            alert.setContentText(item + " information success to be saved");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
        }else{
            alert.setTitle("Save Confirmation");
            alert.setHeaderText(item + " not saved");
            alert.setContentText(item + " information failed to be saved");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
        }
    }

    public void updateItem(String item, boolean isUpdated){
        if (isUpdated){
            alert.setTitle("Updated Confirmation");
            alert.setHeaderText(item + " updated");
            alert.setContentText(item + " information success to be updated");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
        }else{
            alert.setTitle("Updated Confirmation");
            alert.setHeaderText(item + " not updated");
            alert.setContentText(item + " information failed to be updated");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
        }
    }

    public void deleteItem(String item, boolean isDeleted){
        if (isDeleted){
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(item + " deleted");
            alert.setContentText(item + " information success to be deleted");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
        }else{
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(item + " not deleted");
            alert.setContentText(item + " information failed to be deleted");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
        }
    }

    public void cancelOperation(String operation){
            alert.setTitle("Operation cancelled");
            alert.setHeaderText(operation + " operation cancelled");
            alert.setContentText(operation+ " operation process has been cancelled");
            alert.setAlertType(AlertType.INFORMATION);
            alert.showAndWait();
    }

}
