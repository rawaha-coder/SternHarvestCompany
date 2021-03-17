package harvest.controller;

import harvest.database.FarmDAO;
import harvest.database.SeasonDAO;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.*;

public class AddFarmController implements Initializable {

    private Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    FarmDAO mFarmDAO = FarmDAO.getInstance();
    SeasonDAO mSeasonDAO = SeasonDAO.getInstance();
    Farm mFarm = new Farm();
    Season mSeason = new Season();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    private boolean isEditFarm = false;
    private boolean isEditSeason = false;

    @FXML private AnchorPane fxUIAddItem;
    @FXML private ComboBox<String> fxFarmComboBox;
    @FXML private TextField fxFarmAddress;
    @FXML private DatePicker fxPlantingDate;
    @FXML private DatePicker fxHarvestDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxPlantingDate.setEditable(false);
        fxHarvestDate.setEditable(false);
        getFarmList();
        observeSelectFarm();
    }

    //fill the ChoiceBox by farm name list
    private void getFarmList() {
        observableFarmList.clear();
        mFarmMap.clear();
        try {
            mFarmMap = mFarmDAO.getFarmMap();
            observableFarmList.setAll(mFarmMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxFarmComboBox.setItems(observableFarmList);
    }

    private void observeSelectFarm() {
        fxFarmComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if (mFarmMap.get(new_val) != null)
                        fxFarmAddress.setText(mFarmMap.get(new_val).getFarmAddress());
                }
        );
    }

    @FXML
    void handleSaveButton() {
        if (isEditFarm) {
            handleEditFarmData(mFarm);
        } else if (isEditSeason) {
            handleEditSeasonData(mSeason);
        } else {
            handleAddFarmData();
        }
    }

    //handle Add Farm and season Data
    private void handleAddFarmData() {
        if (Validation.isEmpty(fxFarmComboBox.getEditor().getText(), fxFarmAddress.getText())) {
            alert.missingInfo("Champ");
            return;
        }
        Farm oldFarm = mFarmMap.get(fxFarmComboBox.getValue());
        if (oldFarm == null && fxPlantingDate.getValue() != null && fxHarvestDate.getValue() != null) {
            addFarmCheck(addNewFarmWithSeason());
        } else if (oldFarm == null && fxPlantingDate.getValue() == null && fxHarvestDate.getValue() == null) {
            addFarmCheck(addNewFarmWithoutSeason());
        } else if (!Validation.isEmpty(fxPlantingDate.getValue().toString(), fxHarvestDate.getValue().toString())) {
            addFarmCheck(addSeasonToOldFarm(oldFarm));
        }
    }

    private void addFarmCheck(boolean b){
        if (b) {
            mFarmDAO.updateLiveData();
            handleClearButton();
            getFarmList();
            alert.saveItem("Champ", true);
        } else {
            alert.saveItem("Champ", false);
        }
    }

    //Add new farm without season date
    private boolean addNewFarmWithoutSeason(){
        Farm newFarm = new Farm();
        newFarm.setFarmName(fxFarmComboBox.getValue());
        newFarm.setFarmAddress(fxFarmAddress.getText());
        return mFarmDAO.addFarmData(newFarm);
    }

    //Add new farm with season date
    private boolean addNewFarmWithSeason(){
        Season season = new Season();
        season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
        season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        season.getFarm().setFarmName(fxFarmComboBox.getValue());
        season.getFarm().setFarmAddress(fxFarmAddress.getText());
        return mSeasonDAO.addFarmSeasonData(season);
    }

    //Add season date to old farm
    private boolean addSeasonToOldFarm(Farm oldFarm){
        Season season = new Season();
        season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
        season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        assert oldFarm != null;
        season.getFarm().setFarmId(oldFarm.getFarmId());
        season.getFarm().setFarmName(oldFarm.getFarmName());
        season.getFarm().setFarmAddress(oldFarm.getFarmAddress());
        SeasonDAO seasonDAO = SeasonDAO.getInstance();
        return seasonDAO.addSeasonData(season);
    }

    //handle Edit Farm Data
    private void handleEditFarmData(Farm farm) {
        if (observableFarmList.contains(fxFarmComboBox.getSelectionModel().getSelectedItem())){
            alert.show("Nom en double", "Le champ existe déjà choisissez un autre nom", Alert.AlertType.INFORMATION);
            return;
        }
        farm.setFarmName(fxFarmComboBox.getValue());
        farm.setFarmAddress(fxFarmAddress.getText());
        alert.updateItem("Farm", mFarmDAO.editFarmData(farm));
        handleCancelButton();
    }

    //handle Edit Season Data
    private void handleEditSeasonData(Season season) {
        season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
        season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        SeasonDAO seasonDAO = SeasonDAO.getInstance();
        if (seasonDAO.editSeasonData(season)) {
            seasonDAO.updateSeasonListByFarm(season.getFarm());
            alert.updateItem("Saison", true);
        } else {
            alert.updateItem("Saison", false);
        }
        handleCancelButton();
    }

    @FXML
    void handleCancelButton() {
        isEditFarm = false;
        isEditSeason = false;
        Stage stage = (Stage) fxUIAddItem.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleClearButton() {
        getFarmList();
        fxFarmComboBox.valueProperty().set(null);
        fxFarmAddress.setText("");
        fxPlantingDate.getEditor().setText("");
        fxHarvestDate.getEditor().setText("");
    }

    public void inflateFarmUI(Farm farm) {
        fxFarmComboBox.getEditor().setText(farm.getFarmName());
        fxFarmAddress.setText(farm.getFarmAddress());
        fxPlantingDate.setDisable(true);
        fxHarvestDate.setDisable(true);
        isEditFarm = true;
        mFarm.setFarmId(farm.getFarmId());
    }

    public void inflateSeasonUI(Season season) {
        fxFarmComboBox.getEditor().setText(season.getFarm().getFarmName());
        fxFarmAddress.setText(season.getFarm().getFarmAddress());
        fxPlantingDate.setValue(season.getFarmPlantingDate().toLocalDate());
        fxHarvestDate.setValue(season.getFarmHarvestDate().toLocalDate());
        isEditSeason = true;
        fxFarmComboBox.setDisable(true);
        fxFarmAddress.setDisable(true);
        mSeason.setSeasonId(season.getSeasonId());
        mSeason.getFarm().setFarmId(season.getFarm().getFarmId());
        mSeason.getFarm().setFarmName(season.getFarm().getFarmName());
        mSeason.getFarm().setFarmAddress(season.getFarm().getFarmAddress());
    }
}
