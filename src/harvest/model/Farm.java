package harvest.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Farm {
    private final SimpleIntegerProperty farmId;
    private final SimpleStringProperty farmName;
    private final SimpleStringProperty farmAddress;

    public Farm() {
        this.farmId = new SimpleIntegerProperty();
        this.farmName = new SimpleStringProperty();
        this.farmAddress = new SimpleStringProperty();
    }

    public Farm(int i) {
        this.farmId = new SimpleIntegerProperty(i);
        this.farmName = new SimpleStringProperty();
        this.farmAddress = new SimpleStringProperty();
    }

    public Farm(int i, String s) {
        this.farmId = new SimpleIntegerProperty(i);
        this.farmName = new SimpleStringProperty(s);
        this.farmAddress = new SimpleStringProperty();
    }

    public Farm(int farmId, String farmName, String farmAddress) {
        this.farmId = new SimpleIntegerProperty();
        this.farmName = new SimpleStringProperty();
        this.farmAddress = new SimpleStringProperty();
        this.farmId.set(farmId);
        this.farmName.set(farmName);
        this.farmAddress.set(farmAddress);
    }

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
