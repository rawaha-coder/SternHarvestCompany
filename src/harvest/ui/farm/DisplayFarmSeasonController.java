package harvest.ui.farm;

import harvest.database.FarmDAO;
import harvest.database.SeasonDAO;
import harvest.model.Farm;
import harvest.model.Season;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayFarmSeasonController implements Initializable {

    public static ObservableList<Farm> FARM_LIST_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<Season> SEASON_LIST_LIVE_DATA = FXCollections.observableArrayList();

    FarmDAO mFarmDAO = FarmDAO.getInstance();
    @FXML
    private TableView<Farm> fxFarmTable;

    @FXML
    private TableColumn<Farm, String> fxFarmNameColumn;

    @FXML
    private TableColumn<Farm, String> fxFarmAddressColumn;

    @FXML
    private MenuItem fxEditFarm;

    @FXML
    private MenuItem fxDeleteFarm;

    @FXML
    private TableView<Season> fxSeasonTable;

    @FXML
    private TableColumn<Season, String> fxPlantingDateColumn;

    @FXML
    private TableColumn<Season, String > fxHarvestDateColumn;

    @FXML
    private MenuItem fxEditSeason;

    @FXML
    private MenuItem fxDeleteSeason;

    private final SeasonDAO mSeasonDAO = SeasonDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxFarmNameColumn.setCellValueFactory(new PropertyValueFactory<>("farmName"));
        fxFarmAddressColumn.setCellValueFactory(new PropertyValueFactory<>("farmAddress"));
        fxPlantingDateColumn.setCellValueFactory(new PropertyValueFactory<>("farmPlantingDate"));
        fxHarvestDateColumn.setCellValueFactory(new PropertyValueFactory<>("farmHarvestDate"));
        fxFarmTable.setItems(FARM_LIST_LIVE_DATA);
        fxFarmTable.getSelectionModel().selectFirst();
        fxSeasonTable.setItems(SEASON_LIST_LIVE_DATA);
        mSeasonDAO.updateLiveData();
        observeSelectProduct();
    }

    private void observeSelectProduct(){
        fxFarmTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Farm> ov, Farm old_val, Farm new_val) -> {
                    mSeasonDAO.updateSeasonListByFarm(new_val);
                }
        );
    }


    @FXML
    void deleteFarm() {
        Farm farm = fxFarmTable.getSelectionModel().getSelectedItem();
        try {
            mFarmDAO.deleteFarmSeason(farm.getFarmId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void deleteItem(ActionEvent event) {

    }

    @FXML
    void editFarm(ActionEvent event) {

    }

    @FXML
    void editItem(ActionEvent event) {

    }


}
