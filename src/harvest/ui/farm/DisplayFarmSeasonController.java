package harvest.ui.farm;

import harvest.database.FarmDAO;
import harvest.database.SeasonDAO;
import harvest.model.Farm;
import harvest.model.Season;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayFarmSeasonController implements Initializable {

    public static ObservableList<Farm> FARM_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<Season> SEASON_LIST_LIVE_DATA = FXCollections.observableArrayList();
    private final AlertMaker alert = new AlertMaker();
    FarmDAO mFarmDAO = FarmDAO.getInstance();

    @FXML
    private TableView<Farm> fxFarmTable;

    @FXML
    private TableColumn<Farm, String> fxFarmNameColumn;

    @FXML
    private TableColumn<Farm, String> fxFarmAddressColumn;

    @FXML
    private TableView<Season> fxSeasonTable;

    @FXML
    private TableColumn<Season, String> fxPlantingDateColumn;

    @FXML
    private TableColumn<Season, String > fxHarvestDateColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxFarmNameColumn.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxFarmAddressColumn.setCellValueFactory(new PropertyValueFactory<>("farmAddress"));
        fxPlantingDateColumn.setCellValueFactory(new PropertyValueFactory<>("farmPlantingDate"));
        fxHarvestDateColumn.setCellValueFactory(new PropertyValueFactory<>("farmHarvestDate"));
        mFarmDAO.updateLiveData();
        fxFarmTable.setItems(FARM_LIST_LIVE_DATA);
        fxFarmTable.getSelectionModel().selectFirst();
        fxSeasonTable.setItems(SEASON_LIST_LIVE_DATA);
        fxSeasonTable.getSelectionModel().selectFirst();
        observeSelectProduct();
        mFarmDAO.updateSeasonListByFarm(FARM_LIST_LIVE_DATA.get(0));

    }

    private void observeSelectProduct(){
        fxFarmTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Farm> ov, Farm old_val, Farm new_val) -> {
                    if (new_val != null)
                    mFarmDAO.updateSeasonListByFarm(new_val);
                }
        );
    }


    //***********************************************************************************
    //Farm operation
    //***********************************************************************************
    @FXML
    void deleteFarm() {
        Farm farm = fxFarmTable.getSelectionModel().getSelectedItem();
        if (farm == null) {
            alert.selectDeleteItem("Farm");
            return;
        }

        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Farm");

        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            if (mFarmDAO.deleteDataById(farm.getFarmId())) {
                mFarmDAO.updateLiveData();
                alert.deleteItem("Farm", true);
            } else {
                alert.deleteItem("Farm", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }

        try {
            mFarmDAO.deleteDataById(farm.getFarmId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void editFarm(ActionEvent event) {

    }

    //**************************************************************************************
    // Season operations
    //**************************************************************************************
    @FXML
    void deleteSeason(ActionEvent event) {

    }

    @FXML
    void editSeason(ActionEvent event) {

    }


}
