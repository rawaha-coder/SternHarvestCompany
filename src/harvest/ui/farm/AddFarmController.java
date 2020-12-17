package harvest.ui.farm;

import harvest.database.FarmDAO;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class AddFarmController implements Initializable {

    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    FarmDAO mFarmDAO = FarmDAO.getInstance();
    Farm mFarm = new Farm();
    Farm selectedFarm = new Farm();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane fxUIAddItem;
    @FXML
    private ComboBox<String> fxFarmComboBox;
    @FXML
    private TextField fxFarmAddress;
    @FXML
    private DatePicker fxPlantingDate;
    @FXML
    private DatePicker fxHarvestDate;
    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxPlantingDate.setEditable(false);
        fxHarvestDate.setEditable(false);
        getFarmList();
        observeSelectFarm();
    }

    //fill the ChoiceBox by employee list
    private void getFarmList() {
        observableFarmList.clear();
        try {
            List<Farm> farms = new ArrayList<>(mFarmDAO.getFarmData());
            if (farms.size() > 0) {
                for (Farm farm : farms) {
                    observableFarmList.add(farm.getFarmName());
                    mFarmMap.put(farm.getFarmName(), farm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxFarmComboBox.setItems(observableFarmList);
    }

    private void observeSelectFarm() {
        fxFarmComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                        if(mFarmMap.get(new_val) != null)
                            fxFarmAddress.setText(mFarmMap.get(new_val).getFarmAddress());

                }
        );
    }

    @FXML
    void handleSaveButton() {
        if (Validation.isEmpty(fxFarmComboBox.getValue(), fxFarmAddress.getText()))
        {
            alert.show("Required fields are missing", "Please enter correct data in required fields!", AlertType.INFORMATION);
            return;
        }
        boolean isAdded = false;
        Farm oldFarm = mFarmMap.get(fxFarmComboBox.getValue());
        Season season = new Season();
        if (oldFarm == null && fxPlantingDate.getValue() != null && fxHarvestDate.getValue() != null) {
            Farm newFarm = new Farm();
            newFarm.setFarmName(fxFarmComboBox.getValue());
            newFarm.setFarmAddress(fxFarmAddress.getText());
            season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
            season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
            season.setSeasonFarm(newFarm);
            try {
                isAdded = mFarmDAO.addData(season);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(oldFarm == null && fxPlantingDate.getValue() == null && fxHarvestDate.getValue() == null){
            Farm newFarm = new Farm();
            newFarm.setFarmName(fxFarmComboBox.getValue());
            newFarm.setFarmAddress(fxFarmAddress.getText());
            try {
                isAdded = mFarmDAO.addFarmData(newFarm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(!Validation.isEmpty(fxPlantingDate.getValue().toString(), fxHarvestDate.getValue().toString())){
            season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
            season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
            season.setSeasonFarm(oldFarm);
            isAdded = mFarmDAO.addSeasonData(season);
        }
        if (isAdded) {
            mFarmDAO.updateLiveData();
            handleClearButton();
            getFarmList();
            alert.saveItem("Farm", true);
        } else {
            alert.saveItem("Farm", false);
        }

    }

    @FXML
    void handleCancelButton() {
        Stage stage = (Stage) fxUIAddItem.getScene().getWindow();
        stage.close();
        System.out.println("Cancel...");
    }

    @FXML
    void handleClearButton() {
        getFarmList();
        fxFarmAddress.setText("");
        fxPlantingDate.getEditor().setText("");
        fxHarvestDate.getEditor().setText("");

    }

}
