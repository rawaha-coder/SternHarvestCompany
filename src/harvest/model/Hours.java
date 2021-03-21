package harvest.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;

public class Hours {

    public enum EmployeeCategory {
        RECOLTEUR, CONTROLEUR, UNKNOWN
    };

    private final IntegerProperty hoursID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> harvestDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> startMorning = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> endMorning = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> startNoon = new SimpleObjectProperty<>();
    private final ObjectProperty<Time> endNoon = new SimpleObjectProperty<>();
    private final LongProperty totalMinutes = new SimpleLongProperty();
    private final IntegerProperty employeeType = new SimpleIntegerProperty();
    private final SimpleDoubleProperty hourPrice = new SimpleDoubleProperty();
    private final SimpleBooleanProperty transportStatus = new SimpleBooleanProperty();
    private final SimpleDoubleProperty payment = new SimpleDoubleProperty();
    private final Employee employee = new Employee();
    private final Transport transport = new Transport();
    private final Credit credit = new Credit();
    private final SimpleStringProperty remarque = new SimpleStringProperty();
    private final Production production = new  Production();

    public long getTotalMinutes() {
        totalMinutes.set(calculateTotalMinutes(
                startMorning.getValue().getTime(), endMorning.getValue().getTime(),
                startNoon.getValue().getTime(), endNoon.getValue().getTime()));
        return totalMinutes.get();
    }

    public long calculateTotalMinutes(Long SM, Long EM, Long SN, Long EN){
        long totalMilliSeconds = 0;
        if (SM != null && EM != null && SN != null && EN != null)
        {
            totalMilliSeconds =  (EM - SM) + (EN - SN);
        }
        return totalMilliSeconds / (1000 * 60);
    }

    public LongProperty totalMinutesProperty() {
        return totalMinutes;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes.set(totalMinutes);
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

    public int getHoursID() {
        return hoursID.get();
    }

    public void setHoursID(int hoursID) {
        this.hoursID.set(hoursID);
    }

    public Date getHarvestDate() {
        return harvestDate.get();
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate.set(harvestDate);
    }

    public Time getStartMorning() {
        return startMorning.get();
    }

    public ObjectProperty<Time> startMorningProperty() {
        return startMorning;
    }

    public void setStartMorning(Time startMorning) {
        this.startMorning.set(startMorning);
    }

    public Time getEndMorning() {
        return endMorning.get();
    }

    public ObjectProperty<Time> endMorningProperty() {
        return endMorning;
    }

    public void setEndMorning(Time endMorning) {
        this.endMorning.set(endMorning);
    }

    public Time getStartNoon() {
        return startNoon.get();
    }

    public ObjectProperty<Time> startNoonProperty() {
        return startNoon;
    }

    public void setStartNoon(Time startNoon) {
        this.startNoon.set(startNoon);
    }

    public Time getEndNoon() {
        return endNoon.get();
    }

    public ObjectProperty<Time> endNoonProperty() {
        return endNoon;
    }

    public void setEndNoon(Time endNoon) {
        this.endNoon.set(endNoon);
    }

    public int getEmployeeType() {
        return employeeType.get();
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType.set(employeeType);
    }

    public double getHourPrice() {
        return hourPrice.get();
    }

    public SimpleDoubleProperty hourPriceProperty() {
        return hourPrice;
    }

    public void setHourPrice(double hourPrice) {
        this.hourPrice.set(hourPrice);
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

    public double getPayment() {
        this.payment.set(calculatePayment());
        return payment.get();
    }

    private double calculatePayment() {
        return  (getTotalMinutes() * (getHourPrice()/60)) - (getTransport().getTransportAmount() + getCreditAmount())  ;
    }

    public SimpleDoubleProperty paymentProperty() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment.set(payment);
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

    public EmployeeCategory getEmployeeCategory() {
        if (getEmployeeType() == 1) {
            return EmployeeCategory.RECOLTEUR;
        } else if (getEmployeeType() == 2) {
            return EmployeeCategory.CONTROLEUR;
        } else {
            return EmployeeCategory.UNKNOWN;
        }
    }

    public Production getProduction() {
        return production;
    }
}
