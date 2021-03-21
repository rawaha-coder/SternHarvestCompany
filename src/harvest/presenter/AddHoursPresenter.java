package harvest.presenter;

import harvest.controller.AddHoursController;
import harvest.database.HoursDAO;
import harvest.database.PreferencesDAO;
import harvest.model.Hours;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Time;


public class AddHoursPresenter {

    public static ObservableList<Hours> ADD_HOURS_LIVE_DATA = FXCollections.observableArrayList();
    public final HoursDAO mHoursDAO = HoursDAO.getInstance();
    AddHoursController mAddHoursController;
    ListPresenter listPresenter = new ListPresenter();
    public AddHoursPresenter(AddHoursController addHoursController) {
        mAddHoursController = addHoursController;
    }

    public void initList(){
        mAddHoursController.fxSupplierList.setItems(listPresenter.getSupplierList());
        mAddHoursController.fxFarmList.setItems(listPresenter.getFarmList());
        mAddHoursController.fxProductList.setItems(listPresenter.getProductList());
        observeChoiceProduct();
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

    public void updateHoursDataLive(){
        mHoursDAO.updateLiveData();
    }

    public boolean applyAddHoursWork() {
        boolean trackInsert = false;
        for (Hours item : ADD_HOURS_LIVE_DATA){
            trackInsert = mHoursDAO.addHarvestHours(item);
            if (!trackInsert) break;
        }
        return trackInsert;
    }

    public void validateAddHoursWork(){
        PreferencesDAO preferencesDAO = PreferencesDAO.getInstance();
        double price = preferencesDAO.getHourPrice();
        double totalMinute = 0.0;
        double totalTransport = 0.0;
        double totalCredit = 0.0;
        double totalPayment = 0.0;

        for(Hours hours: ADD_HOURS_LIVE_DATA){
            hours.setHarvestDate(Date.valueOf(mAddHoursController.fxHarvestDate.getValue()));
            hours.getSupplier().setSupplierId(listPresenter.getSupplierMap().get(mAddHoursController.fxSupplierList.getValue()).getSupplierId());
            hours.getFarm().setFarmId(listPresenter.getFarmMap().get(mAddHoursController.fxFarmList.getValue()).getFarmId());
            hours.getProduct().setProductId(listPresenter.getProductMap().get(mAddHoursController.fxProductList.getValue()).getProductId());
            hours.getProductDetail().setProductDetailId(listPresenter.getProductDetailMap().get(mAddHoursController.fxProductCodeList.getValue()).getProductDetailId());
            hours.setStartMorning(Time.valueOf(mAddHoursController.fxStartMorningTime.getText()));
            hours.setEndMorning(Time.valueOf(mAddHoursController.fxEndMorningTime.getText()));
            hours.setStartNoon(Time.valueOf(mAddHoursController.fxStartNoonTime.getText()));
            hours.setEndNoon(Time.valueOf(mAddHoursController.fxEndNoonTime.getText()));
            hours.setHourPrice(price);
            hours.setEmployeeType(getEmployeeType());
            totalMinute += hours.getTotalMinutes();
            totalTransport += hours.getTransport().getTransportAmount();
            totalCredit += hours.getCredit().getCreditAmount();
            totalPayment += hours.getPayment();
        }

        mAddHoursController.fxTotalEmployee.setText(String.valueOf(ADD_HOURS_LIVE_DATA.size()));
        mAddHoursController.fxTotalHours.setText(String.valueOf(totalMinute));
        mAddHoursController.fxHourPrice.setText(String.valueOf(price));
        mAddHoursController.fxTotalTransport.setText(String.valueOf(totalTransport));
        mAddHoursController.fxTotalCredit.setText(String.valueOf(totalCredit));
        mAddHoursController.fxTotalPayment.setText(String.valueOf(totalPayment));
    }

    private int getEmployeeType() {
        if (mAddHoursController.fxController.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }


}
