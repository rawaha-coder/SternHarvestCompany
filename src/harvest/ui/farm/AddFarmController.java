package harvest.ui.farm;

import harvest.database.CommonDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class AddFarmController implements Initializable {

    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final AlertMaker alert = new AlertMaker();
    FarmDAO mFarmDAO = FarmDAO.getInstance();
    Farm mFarm = new Farm();
    Season mSeason = new Season();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    private boolean isEditFarm = false;
    private boolean isEditSeason = false;

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
        if (isEditFarm){
            handleEditFarmOperation(mFarm);
        }else if (isEditSeason){
            handleEditSeasonOperation(mSeason);
        } else{
            handleAddFarmOperation();
        }
    }

    private void handleAddFarmOperation(){
        if (Validation.isEmpty(fxFarmComboBox.getEditor().getText(), fxFarmAddress.getText()))
        {
            alert.show("Farm");
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
            CommonDAO commonDAO = CommonDAO.getInstance();
            try {
                isAdded = commonDAO.addFarmSeasonData(season);
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
            assert oldFarm != null;
            season.setSeasonFarm(oldFarm);
            SeasonDAO seasonDAO = SeasonDAO.getInstance();
            isAdded = seasonDAO.addSeasonData(season);
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

    private void handleEditFarmOperation(Farm farm){
            farm.setFarmName(fxFarmComboBox.getValue());
            farm.setFarmAddress(fxFarmAddress.getText());
        alert.updateItem("Farm", mFarmDAO.editFarmData(farm));
        isEditFarm = false;
        handleCancelButton();
    }

    private void handleEditSeasonOperation(Season season){
        season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
        season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        SeasonDAO seasonDAO = SeasonDAO.getInstance();
        if (seasonDAO.editSeasonData(season)) {
            seasonDAO.updateSeasonListByFarm(season.getSeasonFarm());
            alert.updateItem("Season", true);
        }else {
            alert.updateItem("Season", false);
        }
        isEditSeason = false;
        handleCancelButton();
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

    public void inflateFarmUI(Farm farm) {
        fxFarmComboBox.getEditor().setText(farm.getFarmName());
        fxFarmAddress.setText(farm.getFarmAddress());
        fxPlantingDate.setDisable(true);
        fxHarvestDate.setDisable(true);
        isEditFarm = true;
        mFarm.setFarmId(farm.getFarmId());
    }

    public void inflateSeasonUI(Season season) {
        fxFarmComboBox.getEditor().setText(season.getSeasonFarm().getFarmName());
        fxFarmAddress.setText(season.getSeasonFarm().getFarmAddress());
        fxPlantingDate.setValue(season.getFarmPlantingDate().toLocalDate());
        fxHarvestDate.setValue(season.getFarmHarvestDate().toLocalDate());
        isEditSeason = true;
        fxFarmComboBox.setDisable(true);
        fxFarmAddress.setDisable(true);
        mSeason.setSeasonId(season.getSeasonId());
        mSeason.setSeasonFarm(season.getSeasonFarm());
    }
}
