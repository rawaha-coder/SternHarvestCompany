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
    private Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private Map<String, Product> mProductMap = new LinkedHashMap<>();

    private int operation = 0;
    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final SupplyDAO mSupplyDAO = SupplyDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final Supplier mSupplier = new Supplier();
    private final Supply mSupply = new Supply();

    @FXML private AnchorPane fxAddItemUI;
    @FXML private TextField fxSupplierFirstname;
    @FXML private TextField fxSupplierLastname;
    @FXML private ComboBox<String> fxChoiceSupplier;
    @FXML private ChoiceBox<String> fxChoiceFarm;
    @FXML private ChoiceBox<String> fxChoiceProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initList();
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

    private void initList(){
        getSupplierList();
        getFarmList();
        getProductList();
    }

    //fill the ChoiceBox by Supplier list
    private void getSupplierList() {
        observableSupplierList.clear();
        mSupplierMap.clear();
        try {
            mSupplierMap = mSupplierDAO.getSupplierMap();
            observableSupplierList.setAll(mSupplierMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fxChoiceSupplier.setItems(observableSupplierList);
    }

    //fill the ChoiceBox by Farm list
    private void getFarmList() {
        observableFarmList.clear();
        mFarmMap.clear();
        try {
            mFarmMap = mFarmDAO.getFarmMap();
            observableFarmList.setAll( mFarmMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxChoiceFarm.setItems(observableFarmList);
    }

    //fill the ChoiceBox by Product list
    private void getProductList() {
        observableProductList.clear();
        mProductMap.clear();
        try {
            mProductMap = mProductDAO.getProductMap();
            observableProductList.setAll(mProductMap.keySet());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        fxChoiceProduct.setItems(observableProductList);
    }

    @FXML
    void handleSaveButton() {
            switch (operation){
                case 1:
                    editSupplier();
                    break;
                case 2:
                    editSupply();
                    break;
                default:
                    addSupplierOperation();
            }
    }

    private void addSupplierOperation(){
        if (Validation.isEmpty(fxChoiceSupplier.getValue()
                , fxSupplierFirstname.getText()
                , fxSupplierLastname.getText()
                , fxChoiceFarm.getValue()
                , fxChoiceProduct.getValue()))
        {
            alert.missingInfo("Fournisseur");
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
            //supply.setProduct(mProductMap.get(fxChoiceProduct.getValue()));
            supply.getProduct().setProductId(mProductMap.get(fxChoiceProduct.getValue()).getProductId());
            supply.getProduct().setProductName(mProductMap.get(fxChoiceProduct.getValue()).getProductName());
            isAdded = mSupplierDAO.addSupplierSupplyData(supply);
        }else{
            Supply supply = new Supply();
            supply.setSupplier(mSupplierMap.get(fxChoiceSupplier.getValue()));
            supply.setFarm(mFarmMap.get(fxChoiceFarm.getValue()));
            //supply.setProduct(mProductMap.get(fxChoiceProduct.getValue()));
            supply.getProduct().setProductId(mProductMap.get(fxChoiceProduct.getValue()).getProductId());
            supply.getProduct().setProductName(mProductMap.get(fxChoiceProduct.getValue()).getProductName());
            isAdded = mSupplyDAO.addData(supply);
        }

        if (isAdded) {
            mSupplierDAO.updateLiveData();
            mSupplyDAO.updateLiveData();
            handleClearButton();
            getSupplierList();
            getFarmList();
            getProductList();
            alert.saveItem("Fournisseur", true);
        } else {
            alert.saveItem("Fournisseur", false);
        }
    }

    private void editSupplier(){
        mSupplier.setSupplierName(fxChoiceSupplier.getValue());
        mSupplier.setSupplierFirstname(fxSupplierFirstname.getText());
        mSupplier.setSupplierLastname(fxSupplierLastname.getText());
        if (mSupplierDAO.editData(mSupplier)){
            alert.updateItem("Fournisseur", true);
            mSupplierDAO.updateLiveData();
        }else {
            alert.updateItem("Fournisseur", false);
        }
        handleCloseButton();
    }

    public void editSupply(){
        mSupply.setSupplier(mSupplierMap.get(fxChoiceSupplier.getValue()));
        mSupply.setFarm(mFarmMap.get(fxChoiceFarm.getValue()));
        //mSupply.setProduct(mProductMap.get(fxChoiceProduct.getValue()));
        mSupply.getProduct().setProductId(mProductMap.get(fxChoiceProduct.getValue()).getProductId());
        mSupply.getProduct().setProductName(mProductMap.get(fxChoiceProduct.getValue()).getProductName());
        if (mSupplyDAO.editData(mSupply)){
            mSupplierDAO.updateLiveData();
            mSupplyDAO.updateLiveData(mSupplierMap.get(fxChoiceSupplier.getValue()));
            alert.saveItem("Champ", true );
        }else {
            alert.saveItem("Champ", false);
        }
        handleCloseButton();
    }

    public void inflateSupplierUI(Supplier supplier) {
        getSupplierList();
        fxChoiceSupplier.setValue(supplier.getSupplierName());
        fxSupplierFirstname.setText(supplier.getSupplierFirstname());
        fxSupplierLastname.setText(supplier.getSupplierLastname());
        fxChoiceFarm.setDisable(true);
        fxChoiceProduct.setDisable(true);
        operation = 1;
        mSupplier.setSupplierId(supplier.getSupplierId());
    }

    public void inflateSupplyUI(Supply supply) {
        initList();
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
        fxChoiceSupplier.setItems(null);
        initList();
    }

    @FXML
    void handleCloseButton() {
        operation = 0;
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
    }
}
