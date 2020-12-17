package harvest.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;

public class Season {

    private final SimpleIntegerProperty seasonId;
    private final ObjectProperty<Date> farmPlantingDate;
    private final ObjectProperty<Date> farmHarvestDate;
    private final Farm seasonFarm;

    public Season() {
        this.seasonId = new SimpleIntegerProperty();
        this.farmPlantingDate = new SimpleObjectProperty<>();
        this.farmHarvestDate = new SimpleObjectProperty<>();
        this.seasonFarm = new Farm();
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

    public Farm getSeasonFarm() {
        return seasonFarm;
    }

    public void setSeasonFarm(Farm farm){
        this.seasonFarm.setFarmId(farm.getFarmId());
        this.seasonFarm.setFarmName(farm.getFarmName());
        this.seasonFarm.setFarmAddress(farm.getFarmAddress());
    }
}
