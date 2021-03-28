package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;

public class Quantity {

    public enum HarvestCategory {
        GROUPE, INDIVIDUAL, UNKNOWN
    };

    private final IntegerProperty quantityID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> harvestDate = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty allQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty badQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty goodQuantity = new SimpleDoubleProperty();
    private final SimpleDoubleProperty productPrice = new SimpleDoubleProperty();
    private final SimpleDoubleProperty penaltyGeneral = new SimpleDoubleProperty();
    private final SimpleDoubleProperty damageGeneral = new SimpleDoubleProperty();
    private final IntegerProperty harvestType = new SimpleIntegerProperty();
    private final SimpleBooleanProperty transportStatus = new SimpleBooleanProperty();
    private final SimpleDoubleProperty payment = new SimpleDoubleProperty();
    private final Employee employee = new Employee();
    private final Transport transport = new Transport();
    private final Credit credit = new Credit();
    private final SimpleStringProperty remarque = new SimpleStringProperty();
    private final Production production = new  Production();

    public int getQuantityID() {
        return quantityID.get();
    }

    public IntegerProperty quantityIDProperty() {
        return quantityID;
    }

    public void setQuantityID(int quantityID) {
        this.quantityID.set(quantityID);
    }

    public Date getHarvestDate() {
        return harvestDate.get();
    }

    public ObjectProperty<Date> harvestDateProperty() {
        return harvestDate;
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate.set(harvestDate);
    }

    public double getAllQuantity() {
        return allQuantity.get();
    }

    public SimpleDoubleProperty allQuantityProperty() {
        return allQuantity;
    }

    public void setAllQuantity(double allQuantity) {
        this.allQuantity.set(allQuantity);
    }

    public double getBadQuantity() {
        return badQuantity.get();
    }

    public SimpleDoubleProperty badQuantityProperty() {
        return badQuantity;
    }

    public void setBadQuantity(double badQuantity) {
        this.badQuantity.set(badQuantity);
    }

    public double getGoodQuantity() {
        this.goodQuantity.set(getAllQuantity() - getBadQuantity());
        return goodQuantity.get();
    }

    public SimpleDoubleProperty goodQuantityProperty() {
        this.goodQuantity.set(getAllQuantity() - getBadQuantity());
        return goodQuantity;
    }

    public void setGoodQuantity(double goodQuantity) {
        this.goodQuantity.set(goodQuantity);
    }

    public double getProductPrice() {
        return productPrice.get();
    }

    public SimpleDoubleProperty productPriceProperty() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice.set(productPrice);
    }

    public double getPenaltyGeneral() {
        return penaltyGeneral.get();
    }

    public SimpleDoubleProperty penaltyGeneralProperty() {
        return penaltyGeneral;
    }

    public void setPenaltyGeneral(double penaltyGeneral) {
        this.penaltyGeneral.set(penaltyGeneral);
    }

    public double getDamageGeneral() {
        return damageGeneral.get();
    }

    public SimpleDoubleProperty damageGeneralProperty() {
        return damageGeneral;
    }

    public void setDamageGeneral(double damageGeneral) {
        this.damageGeneral.set(damageGeneral);
    }

    public int getHarvestType() {
        return harvestType.get();
    }

    public IntegerProperty harvestTypeProperty() {
        return harvestType;
    }

    public void setHarvestType(int harvestType) {
        this.harvestType.set(harvestType);
    }

    public boolean isTransportStatus() {
        return transportStatus.get();
    }

    public SimpleBooleanProperty transportStatusProperty() {
        return transportStatus;
    }

    public void setTransportStatus(boolean transportStatus) {
        this.transportStatus.set(transportStatus);
    }

    public double getPayment() {
        double pay = 0;
        try{
            pay = ((getAllQuantity() - getBadQuantity() - getPenaltyGeneral() - getDamageGeneral())  * getProductPrice()) - getTransportAmount() - getCreditAmount();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.payment.set(pay);
        return payment.get();
    }

    public SimpleDoubleProperty paymentProperty() {
        getPayment();
        return payment;
    }

    public void setPayment(double payment) {
        this.payment.set(payment);
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getEmployeeName(){
        return getEmployee().getEmployeeFullName();
    }

    public Transport getTransport() {
        return transport;
    }

    public double getTransportAmount(){
        return getTransport().getTransportAmount();
    }

    public String getTransportString(){
        return String.valueOf(getTransport().getTransportAmount());
    }

    public Credit getCredit() {
        return credit;
    }

    public double getCreditAmount(){
        return getCredit().getCreditAmount();
    }

    public String getCreditString(){
        return String.valueOf(getCredit().getCreditAmount());
    }

    public String getRemarque() {
        return remarque.get();
    }

    public SimpleStringProperty remarqueProperty() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque.set(remarque);
    }

    public Production getProduction() {
        return production;
    }

    public HarvestCategory  getHarvestCategory () {
        if (getHarvestType() == 1) {
            return HarvestCategory.INDIVIDUAL;
        } else if (getHarvestType() == 2) {
            return HarvestCategory.GROUPE;
        } else {
            return HarvestCategory.UNKNOWN;
        }
    }
}
