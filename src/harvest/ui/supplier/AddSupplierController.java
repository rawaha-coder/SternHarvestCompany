package harvest.ui.supplier;

import harvest.database.FarmDAO;
import harvest.database.ProductDAO;
import harvest.database.SupplierDAO;
import harvest.database.SupplyDAO;
import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Supplier;
import harvest.model.Supply;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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

    private boolean isEditStatus = false;
    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final SupplyDAO mSupplyDAO = SupplyDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final Supplier mSupplier = new Supplier();
    private final Supply mSupply = new Supply();
    private final Farm mFarm = new Farm();
    private final Product mProduct = new Product();


    @FXML
    private AnchorPane fxAddItemUI;
    @FXML
    private TextField fxSupplierName;
    @FXML
    private TextField fxSupplierFirstname;
    @FXML
    private TextField fxSupplierLastname;
    @FXML
    private ChoiceBox<String> fxChoiceSupplier;
    @FXML
    private ChoiceBox<String> fxChoiceSupplierFarm;
    @FXML
    private ChoiceBox<String> fxChoiceSupplierProduct;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSupplierList();
        getFarmList();
        getProductList();
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
        fxChoiceSupplierFarm.setItems(observableFarmList);
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
        fxChoiceSupplierProduct.setItems(observableProductList);
    }


    @FXML
    void handleSaveSupplierButton() {
        if (isEditStatus){
            handleEditSupplierOperation(mSupplier);
        }else{
            handleAddSupplierOperation();
        }
    }

    private void handleAddSupplierOperation(){
        if (Validation.isEmpty(fxSupplierName.getText()
                , fxSupplierFirstname.getText()
                , fxSupplierLastname.getText())
        ){
            alert.show("Supplier");
        }else {
            Supplier supplier = new Supplier();
            supplier.setSupplierName(fxSupplierName.getText());
            supplier.setSupplierFirstname(fxSupplierFirstname.getText());
            supplier.setSupplierLastname(fxSupplierLastname.getText());
            if (mSupplierDAO.addData(supplier)){
                alert.saveItem("Supplier", true);
                handleClearSupplierButton();
            }else{
                alert.saveItem("Supplier", false);
            }
        }
    }

    private void handleEditSupplierOperation(Supplier supplier){
        mSupplier.setSupplierName(fxSupplierName.getText());
        mSupplier.setSupplierFirstname(fxSupplierFirstname.getText());
        mSupplier.setSupplierLastname(fxSupplierLastname.getText());
        alert.updateItem("Supplier", mSupplierDAO.editData(supplier));
        isEditStatus = false;
        handleCloseButton();
    }


    @FXML
    void handleCloseButton() {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
        System.out.println("Cancel...");
    }

    @FXML
    void handleClearSupplierButton() {
        fxSupplierName.setText("");
        fxSupplierFirstname.setText("");
        fxSupplierLastname.setText("");
    }

    public void inflateSupplierUI(Supplier supplier) {
        fxSupplierName.setText(supplier.getSupplierName());
        fxSupplierFirstname.setText(supplier.getSupplierFirstname());
        fxSupplierLastname.setText(supplier.getSupplierLastname());
        getProductList();
        isEditStatus = true;
        mSupplier.setSupplierId(supplier.getSupplierId());
    }

    public void handleClearSupplyButton() {
        getSupplierList();
        getFarmList();
        getProductList();
    }

    public void handleSaveSupplyButton(ActionEvent actionEvent) {
        if (isEditStatus){
            handleEditSupplyOperation(mSupply);
        }else{
            handleAddSupplyOperation();
        }
    }

    public void  handleAddSupplyOperation(){
        if (Validation.isEmpty(fxChoiceSupplier.getValue(), fxChoiceSupplierFarm.getValue(), fxChoiceSupplierProduct.getValue())){
            alert.show("Supplier");
        }else {
            Supply supply = new Supply();
            supply.setSupplier(mSupplierMap.get(fxChoiceSupplier.getValue()));
            supply.setFarm(mFarmMap.get(fxChoiceSupplierFarm.getValue()));
            supply.setProduct(mProductMap.get(fxChoiceSupplierProduct.getValue()));
            alert.saveItem("Supplier", mSupplyDAO.addData(supply));
            handleClearSupplyButton();
        }
    }

    public void handleEditSupplyOperation(Supply supply){

    }

}
