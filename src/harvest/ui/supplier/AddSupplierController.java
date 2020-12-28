package harvest.ui.supplier;

import harvest.database.*;
import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Supplier;
import harvest.model.Supply;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class AddSupplierController implements Initializable {

    ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    private final Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();

    private int operation = 0;
    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final SupplyDAO mSupplyDAO = SupplyDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final CommonDAO mCommonDAO = CommonDAO.getInstance();
    private final Supplier mSupplier = new Supplier();
    private final Supply mSupply = new Supply();



    @FXML
    private AnchorPane fxAddItemUI;
    @FXML
    private TextField fxSupplierFirstname;
    @FXML
    private TextField fxSupplierLastname;
    @FXML
    private ComboBox<String> fxChoiceSupplier;
    @FXML
    private ChoiceBox<String> fxChoiceFarm;
    @FXML
    private ChoiceBox<String> fxChoiceProduct;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSupplierList();
        getFarmList();
        getProductList();
        observeChoiceSupplier();
    }

    private void observeChoiceSupplier(){
        fxChoiceSupplier.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            if (mSupplierMap.get(new_val) != null){
                fxSupplierFirstname.setText(mSupplierMap.get(new_val).getSupplierFirstname());
                fxSupplierLastname.setText(mSupplierMap.get(new_val).getSupplierLastname());
            }
        });
    }

    //fill the ChoiceBox by employee list
    private void getSupplierList() {
        observableSupplierList.clear();
        try {
            List<Supplier> suppliers = new ArrayList<>(mSupplierDAO.getData());
            if (suppliers.size() > 0) {
                for (Supplier supplier: suppliers) {
                    observableSupplierList.add(supplier.getSupplierName());
                    mSupplierMap.put(supplier.getSupplierName(), supplier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxChoiceSupplier.setItems(observableSupplierList);
    }

    //fill the ChoiceBox by employee list
    private void getFarmList() {
        observableFarmList.clear();
        try {
            List<Farm> farms = new ArrayList<>(mFarmDAO.getFarmData());
            if (farms.size() > 0) {
                for (Farm farm: farms) {
                    observableFarmList.add(farm.getFarmName());
                    mFarmMap.put(farm.getFarmName(), farm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxChoiceFarm.setItems(observableFarmList);
    }

    //fill the ChoiceBox by employee list
    private void getProductList() {
        observableProductList.clear();
        try {
            List<Product> products = new ArrayList<>(mProductDAO.getData());
            if (products.size() > 0) {
                for (Product product: products) {
                    observableProductList.add(product.getProductName());
                    mProductMap.put(product.getProductName(), product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxChoiceProduct.setItems(observableProductList);
    }


    @FXML
    void handleSaveButton() {
            switch (operation){
                case 1:
                    handleEditSupplierOperation(mSupplier);
                    break;
                case 2:
                    handleEditSupplyOperation(mSupply);
                    break;
                default:
                    handleAddSupplierOperation();
            }
    }

    private void handleAddSupplierOperation(){
        if (Validation.isEmpty(fxChoiceSupplier.getValue()
                , fxSupplierFirstname.getText()
                , fxSupplierLastname.getText()
                , fxChoiceFarm.getValue()
                , fxChoiceProduct.getValue()))
        {
            alert.show("Supplier");
            return;
        }
        boolean isAdded;
        if (mSupplierMap.get(fxChoiceSupplier.getValue()) == null){
            Supplier supplier = new Supplier();
            supplier.setSupplierName(fxChoiceSupplier.getValue());
            supplier.setSupplierFirstname(fxSupplierFirstname.getText());
            supplier.setSupplierLastname(fxSupplierLastname.getText());
            Supply supply = new Supply();
            supply.setSupplier(supplier);
            supply.setFarm(mFarmMap.get(fxChoiceFarm.getValue()));
            System.out.println(" *********** " + mFarmMap.get(fxChoiceFarm.getValue()).getFarmId());
            supply.setProduct(mProductMap.get(fxChoiceProduct.getValue()));
            System.out.println(" *********** " + mProductMap.get(fxChoiceProduct.getValue()).getProductId());
            isAdded = mCommonDAO.addSupplierSupplyData(supply);
        }else{
            Supply supply = new Supply();
            supply.setSupplier(mSupplierMap.get(fxChoiceSupplier.getValue()));
            supply.setFarm(mFarmMap.get(fxChoiceFarm.getValue()));
            supply.setProduct(mProductMap.get(fxChoiceProduct.getValue()));
            isAdded = mSupplyDAO.addData(supply);
        }

        if (isAdded) {
            mSupplierDAO.updateLiveData();
            mSupplyDAO.updateLiveData();
            handleClearButton();
            getSupplierList();
            getFarmList();
            getProductList();
            alert.saveItem("Supplier", true);
        } else {
            alert.saveItem("Supplier", false);
        }
    }

    private void handleEditSupplierOperation(Supplier supplier){
        supplier.setSupplierName(fxChoiceSupplier.getValue());
        supplier.setSupplierFirstname(fxSupplierFirstname.getText());
        supplier.setSupplierLastname(fxSupplierLastname.getText());
        if (mSupplierDAO.editData(supplier)){
            alert.updateItem("Supplier", true);
            mSupplierDAO.updateLiveData();
        }else {
            alert.updateItem("Supplier", false);
        }
        operation = 0;
        handleCloseButton();
    }

    public void handleEditSupplyOperation(Supply supply){
        supply.setSupplier(mSupplierMap.get(fxChoiceSupplier.getValue()));
        supply.setFarm(mFarmMap.get(fxChoiceFarm.getValue()));
        supply.setProduct(mProductMap.get(fxChoiceProduct.getValue()));
        if (mSupplyDAO.editData(supply)){
            mSupplierDAO.updateLiveData();
            mSupplyDAO.updateLiveData(mSupplierMap.get(fxChoiceSupplier.getValue()));
            alert.saveItem("Supply", true );
        }else {
            alert.saveItem("Supply", false);
        }
        operation = 0;
        handleCloseButton();
    }

    public void inflateSupplierUI(Supplier supplier) {
        getSupplierList();
        fxChoiceSupplier.setValue(supplier.getSupplierName());
        fxSupplierFirstname.setText(supplier.getSupplierFirstname());
        fxSupplierLastname.setText(supplier.getSupplierLastname());
        fxChoiceFarm.setDisable(true);
        fxChoiceProduct.setDisable(true);
        getProductList();
        operation = 1;
        mSupplier.setSupplierId(supplier.getSupplierId());
    }

    public void inflateSupplyUI(Supply supply) {
        getSupplierList();
        getFarmList();
        getProductList();
        fxChoiceSupplier.setValue(supply.getSupplierName());
        fxChoiceSupplier.setDisable(true);
        fxSupplierFirstname.setDisable(true);
        fxSupplierLastname.setDisable(true);
        fxChoiceFarm.setValue(supply.getFarmName());
        fxChoiceProduct.setValue(supply.getProductName());
        operation = 2;
        mSupply.setSupplyId(supply.getSupplyId());

    }

    @FXML
    void handleClearButton() {
        fxSupplierFirstname.setText("");
        fxSupplierLastname.setText("");
        getSupplierList();
        getFarmList();
        getProductList();
    }

    @FXML
    void handleCloseButton() {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
        System.out.println("Cancel...");
    }
}
