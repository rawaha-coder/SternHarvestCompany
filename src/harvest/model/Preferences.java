package harvest.model;

public class Preferences {

    private Double penaltyEmployee = 0.0;
    private Double penaltyGeneral = 0.0;
    private Double hourPrice = 1.0;
    private Double transportPrice = 0.0;

    public Double getPenaltyEmployee() {
        return penaltyEmployee;
    }

    public void setPenaltyEmployee(Double penaltyEmployee) {
        this.penaltyEmployee = penaltyEmployee;
    }

    public Double getPenaltyGeneral() {
        return penaltyGeneral;
    }

    public void setPenaltyGeneral(Double penaltyGeneral) {
        this.penaltyGeneral = penaltyGeneral;
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
