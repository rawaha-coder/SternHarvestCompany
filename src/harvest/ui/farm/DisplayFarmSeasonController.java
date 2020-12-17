package harvest.ui.farm;

import harvest.database.FarmDAO;
import harvest.model.Farm;
import harvest.model.Season;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
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
    }

    private void observeSelectProduct(){
        fxFarmTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Farm> ov, Farm old_val, Farm new_val) -> {
                    if (new_val != null){
                        mFarmDAO.updateSeasonListByFarm(new_val);
                        fxSeasonTable.getSelectionModel().selectFirst();
                    }
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
                alert.deleteItem("Farm", true);
                fxFarmTable.getSelectionModel().selectFirst();
                fxSeasonTable.getSelectionModel().selectFirst();
            } else {
                alert.deleteItem("Farm", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }
    }

    @FXML
    void editFarm() {
        Farm farm = fxFarmTable.getSelectionModel().getSelectedItem();
        if (farm == null) {
            alert.selectEditItem("Farm");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/farm/add_farm.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddFarmController controller = loader.getController();
            controller.inflateFarmUI(farm);
            stage.setTitle("Edit farm");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //**************************************************************************************
    // Season operations
    //**************************************************************************************
    @FXML
    void deleteSeason() {
        Season season = fxSeasonTable.getSelectionModel().getSelectedItem();
        if (season == null) {
            alert.selectDeleteItem("Season");
            return;
        }

        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Season");

        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            if (mFarmDAO.deleteSeasonById(season.getSeasonId())) {
                alert.deleteItem("Season", true);
            } else {
                alert.deleteItem("Farm", false);
            }
        } else {
            alert.cancelOperation("Delete");
        }
    }

    @FXML
    void editSeason() {
        Season season = fxSeasonTable.getSelectionModel().getSelectedItem();
        if (season == null) {
            alert.selectDeleteItem("Season");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/farm/add_farm.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddFarmController controller = loader.getController();
            controller.inflateSeasonUI(season);
            stage.setTitle("Edit Season");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
