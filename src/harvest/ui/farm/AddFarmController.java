package harvest.ui.farm;

import harvest.database.FarmDAO;
import harvest.database.SeasonDAO;
import harvest.model.*;
import harvest.util.AlertMaker;
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

import java.net.URL;
import java.sql.Date;
import java.util.*;

public class AddFarmController implements Initializable {

    //public static ObservableList<String> FARM_NAME_LIVE_DATA = FXCollections.observableArrayList();
    private final Map<String, Integer> farmNameId = new LinkedHashMap<>();
    FarmDAO mFarmDAO = FarmDAO.getInstance();
    SeasonDAO mSeasonDAO = SeasonDAO.getInstance();
    List<Farm> mFarmList = new ArrayList<>();
    Farm mFarm = new Farm();
    Farm selectedFarm = new Farm();
    ObservableList<String> farmNameList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane fxUIAddItem;

    @FXML
    private ComboBox<String> fxFarmList;

    @FXML
    private TextField fxFarmName;

    @FXML
    private TextField fxFarmAddress;

    @FXML
    private DatePicker fxPlantingDate;

    @FXML
    private DatePicker fxHarvestDate;

    @FXML
    private Button fxClearFarmButton;

    @FXML
    private Button fxSaveFarmButton;

    @FXML
    private Button fxCancelFarmButton;

    private final AlertMaker alert = new AlertMaker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getFarmList();
        observeSelectFarm();
    }

    //fill the ChoiceBox by employee list
    private void getFarmList() {

        try {
            mFarmList.addAll(mFarmDAO.getData());
            if (mFarmList.size() >0){
                for (Farm farm : mFarmList) {
                    farmNameList.add(farm.getFarmName());
                    farmNameId.put(farm.getFarmName(), farm.getFarmId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxFarmList.setItems(farmNameList);
    }

    private void observeSelectFarm(){
        fxFarmList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    for (Farm f : mFarmList){
                        if (f.getFarmName().equals(new_val)){
                            fxFarmAddress.setText(f.getFarmAddress());
                            selectedFarm.setFarmId(f.getFarmId());
                            selectedFarm.setFarmName(f.getFarmName());
                            selectedFarm.setFarmAddress(f.getFarmAddress());
                        }else{
                            selectedFarm.setFarmId(0);
                            selectedFarm.setFarmName(fxFarmList.getValue());
                            selectedFarm.setFarmAddress(fxFarmAddress.getText());
                        }
                    }
                }
        );
    }

    @FXML
    void handleSaveButton() {
        Farm farm = new Farm();
        Season season = new Season();
        if (!farmNameId.isEmpty()){
            farm.setFarmId(farmNameId.get(fxFarmList.getValue()));
        }
        farm.setFarmName(fxFarmName.getText());
        farm.setFarmAddress(fxFarmAddress.getText());
        season.setFarmPlantingDate(Date.valueOf(fxPlantingDate.getValue()));
        season.setFarmHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
        season.setSeasonFarm(farm);
        boolean justSeason = false;
        for (String s : farmNameList){
            if (s.equals(farm.getFarmName())){
                justSeason = true;
            }
        }
        if (justSeason){
            if (mSeasonDAO.addData(season)){
                mSeasonDAO.updateLiveData();
                handleClearButton();
                alert.saveItem("Season", true);
            }else {
                alert.saveItem("Season", false);
            }
        }else{
            boolean added = false;
            try {
               added = mFarmDAO.addFarmSeason(farm, season);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (added){
                mSeasonDAO.updateLiveData();
                mFarmDAO.updateLiveData();
                handleClearButton();
                alert.saveItem("Farm", true);
            }else {
                alert.saveItem("Farm", false);
            }
        }

    }

    @FXML
    void handleCancelButton() {

    }

    @FXML
    void handleClearButton() {
        getFarmList();
        fxFarmName.setText("");
        fxFarmAddress.setText("");
        fxPlantingDate.getEditor().setText("");
        fxHarvestDate.getEditor().setText("");

    }

}
