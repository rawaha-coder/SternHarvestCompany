package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Farm {

    private final SimpleIntegerProperty farmId = new SimpleIntegerProperty();
    private final SimpleStringProperty farmName = new SimpleStringProperty();
    private final SimpleStringProperty farmAddress = new SimpleStringProperty();

    public int getFarmId() {
        return farmId.get();
    }

    public void setFarmId(int farmId) {
        this.farmId.set(farmId);
    }

    public String getFarmName() {
        return farmName.get().toUpperCase();
    }

    public SimpleStringProperty farmNameProperty() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName.set(farmName);
    }

    public String getFarmAddress() {
        return farmAddress.get().toUpperCase();
    }

    public void setFarmAddress(String farmAddress) {
        this.farmAddress.set(farmAddress);
    }
}
