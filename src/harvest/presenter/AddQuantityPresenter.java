package harvest.presenter;

import harvest.database.PreferencesDAO;
import harvest.database.ProductionDAO;
import harvest.database.QuantityDAO;
import harvest.model.*;
import harvest.util.AlertMaker;
import harvest.view.AddQuantityController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;


public class AddQuantityPresenter {

    public static ObservableList<Quantity> ADD_QUANTITY_LIVE_DATA = FXCollections.observableArrayList();
    private final QuantityDAO mQuantityDAO = QuantityDAO.getInstance();
    private final ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    AddQuantityController mAddQuantityController;
    ListPresenter listPresenter = new ListPresenter();
    Production production = new Production();
    public final AlertMaker alert = new AlertMaker();

    public AddQuantityPresenter(AddQuantityController addHoursController) {
        mAddQuantityController = addHoursController;
    }

    public void initList(){
        mAddQuantityController.fxSupplierList.setItems(listPresenter.getSupplierList());
        mAddQuantityController.fxFarmList.setItems(listPresenter.getFarmList());
        mAddQuantityController.fxProductList.setItems(listPresenter.getProductList());
        observeProductList();
    }

    public void updateAddQuantityDataLive() {
        Thread thread = new Thread(){
            public void run(){
                ADD_QUANTITY_LIVE_DATA.clear();
                try {
                    ADD_QUANTITY_LIVE_DATA.setAll(mQuantityDAO.getAddQuantityData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void observeProductList() {
        mAddQuantityController.fxProductList.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if (listPresenter.getProductMap().get(new_val) != null) {
                        mAddQuantityController.fxProductCodeList.setItems(listPresenter.getProductCode(listPresenter.getProductMap().get(new_val)));
                    }else {
                        mAddQuantityController.fxProductCodeList.getSelectionModel().clearSelection();
                    }
                });
    }

    public void applyProductionToDatabase() {
        production.setProductionDate(Date.valueOf(mAddQuantityController.fxHarvestDate.getValue()));
        production.setProductionType(2);
        production.getSupplier().setSupplierId(listPresenter.getSupplierMap().get(mAddQuantityController.fxSupplierList.getValue()).getSupplierId());
        production.getFarm().setFarmId(listPresenter.getFarmMap().get(mAddQuantityController.fxFarmList.getValue()).getFarmId());
        production.getProduct().setProductId(listPresenter.getProductMap().get(mAddQuantityController.fxProductList.getValue()).getProductId());
        production.getProductDetail().setProductDetailId(listPresenter.getProductDetailMap().get(mAddQuantityController.fxProductCodeList.getValue()).getProductDetailId());
        production.setTotalEmployee(Integer.parseInt(mAddQuantityController.fxTotalEmployee.getText()));
        production.setTotalQuantity(Double.parseDouble(mAddQuantityController.fxTotalGoodQuantity.getText()));
        production.setPrice(Double.parseDouble(mAddQuantityController.fxProductPriceCompany.getText()));
        int productionId = mProductionDAO.addProductionAndGetId(production);
        if (productionId != -1){
            production.setProductionID(productionId);
            boolean added = addHoursWorkToDatabase();
            if (added){
                mAddQuantityController.clearButton();
                alert.saveItem("Production" , added);
            }else {
                alert.saveItem("Production" , added);
            }

        }
    }

    private boolean addHoursWorkToDatabase() {
        boolean trackInsert = false;
        if (production.getProductionID() > 0){
            for (Quantity item : ADD_QUANTITY_LIVE_DATA){
                item.setHarvestDate(production.getProductionDate());
                item.getProduction().setProductionID(production.getProductionID());
                trackInsert = mQuantityDAO.addHarvestQuantity(item);
                if (!trackInsert) break;
            }
        }
        return trackInsert;
    }

    public void validateAddHarvestQuantity(){
        double allQuantity = Double.parseDouble(mAddQuantityController.fxInputAllQuantity.getText());
        double badQuantity = Double.parseDouble(mAddQuantityController.fxInputBadQuantity.getText());
        double allQuantityEmp = allQuantity / ADD_QUANTITY_LIVE_DATA.size();
        double badQuantityEmp = badQuantity / ADD_QUANTITY_LIVE_DATA.size();
        double priceCompany = listPresenter.getProductDetailMap().get(mAddQuantityController.fxProductCodeList.getValue()).getPriceCompany();
        double priceEmployee = listPresenter.getProductDetailMap().get(mAddQuantityController.fxProductCodeList.getValue()).getPriceEmployee();
        double penaltyEmployee = getPreferences().getPenaltyGeneral();
        double penaltyGeneral = getPreferences().getDefectiveGeneral();
        double totalGoodQuantity = 0.0;
        double totalTransport = 0.0;
        double totalCredit = 0.0;
        double totalPayment = 0.0;

        for(Quantity quantity: ADD_QUANTITY_LIVE_DATA){
            quantity.setAllQuantity(allQuantityEmp);
            quantity.setBadQuantity(badQuantityEmp);
            quantity.setPenaltyGeneral(penaltyEmployee);
            quantity.setDamageGeneral(penaltyGeneral);
            quantity.setProductPrice(priceEmployee);
            quantity.setHarvestType(getHarvestType());
            totalGoodQuantity += quantity.getGoodQuantity();
            totalTransport += quantity.getTransportAmount();
            totalCredit += quantity.getCreditAmount();
            totalPayment += quantity.getPayment();
        }

        mAddQuantityController.fxTotalEmployee.setText(String.valueOf(ADD_QUANTITY_LIVE_DATA.size()));
        mAddQuantityController.fxTotalAllQuantity.setText(String.valueOf(allQuantity));
        mAddQuantityController.fxTotalBadQuantity.setText(String.valueOf(badQuantity));
        mAddQuantityController.fxTotalGoodQuantity.setText(String.valueOf(totalGoodQuantity));
        mAddQuantityController.fxProductPriceEmployee.setText(String.valueOf(priceEmployee));
        mAddQuantityController.fxTotalTransport.setText(String.valueOf(totalTransport));
        mAddQuantityController.fxTotalCredit.setText(String.valueOf(totalCredit));
        mAddQuantityController.fxTotalPayment.setText(String.valueOf(totalPayment));
        mAddQuantityController.fxProductPriceCompany.setText(String.valueOf(priceCompany));
        mAddQuantityController.fxCompanyCharge.setText(String.valueOf(priceCompany * totalGoodQuantity));
    }

    private int getHarvestType() {
        if (mAddQuantityController.fxHarvestByIndividual.isSelected()) {
            return 2;
        } else {
            return 1;
        }
    }

    private Preferences getPreferences(){
        PreferencesDAO preferencesDAO = PreferencesDAO.getInstance();
        Preferences preferences = new Preferences();
        try {
            preferences = preferencesDAO.getPreferences();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return preferences;
    }

    public void clearField() {
        initList();
        mAddQuantityController.fxHarvestDate.getEditor().setText("");

        for(Quantity quantity: ADD_QUANTITY_LIVE_DATA){
            quantity.setAllQuantity(0.0);
            quantity.setBadQuantity(0.0);
            quantity.setGoodQuantity(0.0);
            quantity.setHarvestType(1);
            quantity.setProductPrice(0.0);
            quantity.setTransportStatus(false);
            quantity.setRemarque("");
            quantity.getCredit().setCreditAmount(0.0);
            quantity.getTransport().setTransportAmount(0.0);
            quantity.setPayment(0.0);
        }
        mAddQuantityController.initField();
        mAddQuantityController.fxHarvestQuantityTable.refresh();
    }
}
