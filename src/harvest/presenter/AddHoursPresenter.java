package harvest.presenter;

import harvest.view.AddHoursController;
import harvest.database.HoursDAO;
import harvest.database.PreferencesDAO;
import harvest.database.ProductionDAO;
import harvest.model.Hours;
import harvest.model.Production;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.Time;

public class AddHoursPresenter {

    public static ObservableList<Hours> ADD_HOURS_LIVE_DATA = FXCollections.observableArrayList();
    public final HoursDAO mHoursDAO = HoursDAO.getInstance();
    private final ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    AddHoursController mAddHoursController;
    ListPresenter listPresenter = new ListPresenter();
    Production production = new Production();
    public final AlertMaker alert = new AlertMaker();
    private boolean isEditStatus = false;
    DisplayHoursProductionPresenter mDisplayHoursProductionPresenter;

    public AddHoursPresenter(AddHoursController addHoursController) {
        mAddHoursController = addHoursController;
    }

    public void initList(){
        mAddHoursController.fxSupplierList.setItems(listPresenter.getSupplierList());
        mAddHoursController.fxFarmList.setItems(listPresenter.getFarmList());
        mAddHoursController.fxProductList.setItems(listPresenter.getProductList());
        observeChoiceProduct();
    }

    public void updateAddHoursDataLive() {
        Thread thread = new Thread(() -> {
            ADD_HOURS_LIVE_DATA.clear();
            try {
                ADD_HOURS_LIVE_DATA.setAll(mHoursDAO.getAddHoursData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void observeChoiceProduct() {
        mAddHoursController.fxProductList.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if (listPresenter.getProductMap().get(new_val) != null) {
                        mAddHoursController.fxProductCodeList.setItems(listPresenter.getProductCode(listPresenter.getProductMap().get(new_val)));
                    }else {
                        mAddHoursController.fxProductCodeList.getSelectionModel().clearSelection();
                    }
                });
    }

    public void handleProductionData() {
        if (isEditStatus){ updateProductionDataInDatabase();
        }else { addProductionDataToDatabase(); }
    }

    private void addProductionDataToDatabase() {
        setProductionValueFromFields();
        int productionId = mProductionDAO.addProductionAndGetId(production);
        if (productionId != -1){
            production.setProductionID(productionId);
            boolean added = addHarvestHoursToDatabase();
            if (added){ mAddHoursController.handleClearButton(); }
            alert.saveItem("Production" , added);
        }
    }

    private boolean addHarvestHoursToDatabase() {
        boolean trackInsert = false;
        if (production.getProductionID() > 0){
            for (Hours item : ADD_HOURS_LIVE_DATA){
                item.setHarvestDate(production.getProductionDate());
                item.getProduction().getFarm().setFarmId(production.getFarm().getFarmId());
                item.getProduction().setProductionID(production.getProductionID());
                trackInsert = mHoursDAO.addHoursWork(item);
                if (!trackInsert) break;
            }
        }
        return trackInsert;
    }

    private void updateProductionDataInDatabase() {
        setProductionValueFromFields();
        if (mProductionDAO.updateProductionData(production)){
            alert.saveItem("Production" , updateHoursWorkInDatabase());
        }else {
            alert.saveItem("Production" , false);
        }
        mDisplayHoursProductionPresenter.searchByDate();
        isEditStatus = false;
        Stage stage = (Stage) mAddHoursController.addHarvestHoursUI.getScene().getWindow();
        stage.close();
    }
    private boolean updateHoursWorkInDatabase() {
        boolean trackInsert = false;
            for (Hours item : ADD_HOURS_LIVE_DATA){
                item.setHarvestDate(production.getProductionDate());
                item.getProduction().getFarm().setFarmId(production.getFarm().getFarmId());
                item.getProduction().setProductionID(production.getProductionID());
                trackInsert = mHoursDAO.updateHoursWork(item);
                if (!trackInsert) break;
            }
        return trackInsert;
    }

    private void setProductionValueFromFields(){
        production.setProductionDate(Date.valueOf(mAddHoursController.fxHarvestDate.getValue()));
        production.setProductionType(1);
        production.getSupplier().setSupplierId(listPresenter.getSupplierMap().get(mAddHoursController.fxSupplierList.getValue()).getSupplierId());
        production.getFarm().setFarmId(listPresenter.getFarmMap().get(mAddHoursController.fxFarmList.getValue()).getFarmId());
        production.getProduct().setProductId(listPresenter.getProductMap().get(mAddHoursController.fxProductList.getValue()).getProductId());
        production.getProductDetail().setProductDetailId(listPresenter.getProductDetailMap().get(mAddHoursController.fxProductCodeList.getValue()).getProductDetailId());
        production.setTotalEmployee(Integer.parseInt(mAddHoursController.fxTotalEmployee.getText()));
        production.setTotalMinutes(Long.parseLong(mAddHoursController.fxTotalMinutes.getText()));
        production.setPrice(Double.parseDouble(mAddHoursController.fxHourPrice.getText()));
    }

    //validate data before send them to database
    public void validateAddHoursWork(){
        PreferencesDAO preferencesDAO = PreferencesDAO.getInstance();
        double price = preferencesDAO.getHourPrice();
        long totalMinute = 0; double totalTransport = 0.0; double totalCredit = 0.0; double totalPayment = 0.0;

        for(Hours hours: ADD_HOURS_LIVE_DATA){
            hours.setStartMorning(Time.valueOf(mAddHoursController.fxStartMorningTime.getText()));
            hours.setEndMorning(Time.valueOf(mAddHoursController.fxEndMorningTime.getText()));
            hours.setStartNoon(Time.valueOf(mAddHoursController.fxStartNoonTime.getText()));
            hours.setEndNoon(Time.valueOf(mAddHoursController.fxEndNoonTime.getText()));
            hours.setEmployeeType(mAddHoursController.getEmployeeType());
            hours.setHourPrice(price);
            totalMinute += hours.getTotalMinutes();
            totalTransport += hours.getTransport().getTransportAmount();
            totalCredit += hours.getCredit().getCreditAmount();
            totalPayment += hours.getPayment();
        }

        mAddHoursController.fxTotalEmployee.setText(String.valueOf(ADD_HOURS_LIVE_DATA.size()));
        mAddHoursController.fxTotalMinutes.setText(String.valueOf(totalMinute));
        mAddHoursController.fxHourPrice.setText(String.valueOf(price));
        mAddHoursController.fxTotalTransport.setText(String.valueOf(totalTransport));
        mAddHoursController.fxTotalCredit.setText(String.valueOf(totalCredit));
        mAddHoursController.fxTotalPayment.setText(String.valueOf(totalPayment));
    }

    //initial value in addHours UI for editing hours production data
    public void updateProductionData(Production production, DisplayHoursProductionPresenter displayHoursProductionPresenter) {
        isEditStatus = true;
        mDisplayHoursProductionPresenter = displayHoursProductionPresenter;
        this.production.setProductionID(production.getProductionID());
        mAddHoursController.fxHarvestDate.setValue(production.getProductionDate().toLocalDate());
        mAddHoursController.fxSupplierList.getSelectionModel().select(production.getSupplierName());
        mAddHoursController.fxFarmList.getSelectionModel().select(production.getFarmName().toUpperCase());
        mAddHoursController.fxProductList.getSelectionModel().select(production.getProductName());
        mAddHoursController.fxProductCodeList.getSelectionModel().select(production.getProductCode());

        ADD_HOURS_LIVE_DATA.clear();
        try {
            ADD_HOURS_LIVE_DATA.setAll(mHoursDAO.getHoursDataByProductionId(production));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Hours firstEmployee = ADD_HOURS_LIVE_DATA.get(0);
        Time timeSM = firstEmployee.getStartMorning(); Time timeEM = firstEmployee.getEndMorning();
        Time timeSN = firstEmployee.getStartNoon(); Time timeEN = firstEmployee.getEndNoon();
        mAddHoursController.fxStartMorningTime.setText(timeSM.toString());
        mAddHoursController.fxEndMorningTime.setText(timeEM.toString());
        mAddHoursController.fxStartNoonTime.setText(timeSN.toString());
        mAddHoursController.fxEndNoonTime.setText(timeEN.toString());
        mAddHoursController.setEmployeeType(firstEmployee.getEmployeeType());
        for(Hours hours: ADD_HOURS_LIVE_DATA){
            hours.setTransportStatus(hours.getTransportStatusByAmount());
        }
        initTotalField();
    }

    private void initTotalField(){
        mAddHoursController.fxTotalEmployee.setText("0");
        mAddHoursController.fxTotalMinutes.setText("0");
        mAddHoursController.fxHourPrice.setText("0.0");
        mAddHoursController.fxTotalTransport.setText("0.0");
        mAddHoursController.fxTotalCredit.setText("0.0");
        mAddHoursController.fxTotalPayment.setText("0.0");
    }

    public void clearField() {
        initList();
        mAddHoursController.fxHarvestDate.getEditor().setText("");
        mAddHoursController.fxStartMorningTime.setText("00:00:00");
        mAddHoursController.fxEndMorningTime.setText("00:00:00");
        mAddHoursController.fxStartNoonTime.setText("00:00:00");
        mAddHoursController.fxEndNoonTime.setText("00:00:00");
        mAddHoursController.fxEmployeeType.selectToggle(mAddHoursController.fxHarvester);
        Time time = new Time(0);
        for(Hours hours: ADD_HOURS_LIVE_DATA){
            hours.setStartMorning(time);
            hours.setEndMorning(time);
            hours.setStartNoon(time);
            hours.setEndNoon(time);
            hours.setTotalMinutes(0);
            hours.setHourPrice(0.0);
            hours.setTransportStatus(false);
            hours.setRemarque("");
            hours.getCredit().setCreditAmount(0.0);
            hours.getTransport().setTransportAmount(0.0);
            hours.setPayment(0.0);
        }
        initTotalField();
        mAddHoursController.fxAddHarvestHoursTable.refresh();
    }
}
