package harvest.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class FarmSeason {
    private final SimpleIntegerProperty farmId;
    private final SimpleStringProperty farmName;
    private final SimpleStringProperty farmAddress;
    private final SimpleIntegerProperty seasonId;
    private final ObjectProperty<Date> farmPlantingDate;
    private final ObjectProperty<Date> farmHarvestDate;

    public FarmSeason() {
        this.farmId = new SimpleIntegerProperty();
        this.farmName = new SimpleStringProperty();
        this.farmAddress = new SimpleStringProperty();
        this.seasonId = new SimpleIntegerProperty();
        this.farmPlantingDate = new SimpleObjectProperty<>();
        this.farmHarvestDate = new SimpleObjectProperty<>();
    }

    public int getFarmId() {
        return farmId.get();
    }

    public SimpleIntegerProperty farmIdProperty() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId.set(farmId);
    }

    public String getFarmName() {
        return farmName.get();
    }

    public SimpleStringProperty farmNameProperty() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
    }

    public String getFarmAddress() {
        return farmAddress.get();
    }

    public SimpleStringProperty farmAddressProperty() {
        return farmAddress;
    }

    public void setFarmAddress(String farmAddress) {
        this.farmAddress.set(farmAddress);
    }

    public int getSeasonId() {
        return seasonId.get();
    }

    public SimpleIntegerProperty seasonIdProperty() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId.set(seasonId);
    }

    public Date getFarmPlantingDate() {
        return farmPlantingDate.get();
    }

    public ObjectProperty<Date> farmPlantingDateProperty() {
        return farmPlantingDate;
    }

    public void setFarmPlantingDate(Date farmPlantingDate) {
        this.farmPlantingDate.set(farmPlantingDate);
    }

    public Date getFarmHarvestDate() {
        return farmHarvestDate.get();
    }

    public ObjectProperty<Date> farmHarvestDateProperty() {
        return farmHarvestDate;
    }

    public void setFarmHarvestDate(Date farmHarvestDate) {
        this.farmHarvestDate.set(farmHarvestDate);
    }
}
