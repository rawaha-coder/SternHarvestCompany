package harvest.model;

public class Preferences {

    private Double penaltyGeneral = 0.0;
    private Double defectiveGeneral = 0.0;
    private Double hourPrice = 1.0;
    private Double transportPrice = 0.0;

    public Double getPenaltyGeneral() {
        return penaltyGeneral;
    }

    public void setPenaltyGeneral(Double penaltyGeneral) {
        this.penaltyGeneral = penaltyGeneral;
    }

    public Double getDefectiveGeneral() {
        return defectiveGeneral;
    }

    public void setDefectiveGeneral(Double defectiveGeneral) {
        this.defectiveGeneral = defectiveGeneral;
    }

    public Double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Double hourPrice) {
        this.hourPrice = hourPrice;
    }

    public Double getTransportPrice() {
        return transportPrice;
    }

    public void setTransportPrice(Double transportPrice) {
        this.transportPrice = transportPrice;
    }
}
