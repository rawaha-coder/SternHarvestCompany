package harvest.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;

public class Season {

    private final SimpleIntegerProperty seasonId = new SimpleIntegerProperty();
    private final ObjectProperty<Date> farmPlantingDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> farmHarvestDate = new SimpleObjectProperty<>();
    private final Farm farm = new Farm();

    public int getSeasonId() {
        return seasonId.get();
    }

    public void setSeasonId(int seasonId) {
        this.seasonId.set(seasonId);
    }

    public Date getFarmPlantingDate() {
        return farmPlantingDate.get();
    }

    public void setFarmPlantingDate(Date farmPlantingDate) {
        this.farmPlantingDate.set(farmPlantingDate);
    }

    public Date getFarmHarvestDate() {
        return farmHarvestDate.get();
    }

    public void setFarmHarvestDate(Date farmHarvestDate) {
        this.farmHarvestDate.set(farmHarvestDate);
    }

    public Farm getFarm() {
        return farm;
    }
}
