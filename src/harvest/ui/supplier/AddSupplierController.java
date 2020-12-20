package harvest.ui.supplier;

import harvest.database.FarmDAO;
import harvest.database.ProductDAO;
import harvest.database.SupplierDAO;
import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.Supplier;
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
import java.sql.SQLException;
import java.util.*;

public class AddSupplierController implements Initializable {

    ObservableList<String> observableProductList = FXCollections.observableArrayList();
    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();

    private boolean isEditStatus = false;
    private final AlertMaker alert = new AlertMaker();
    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final Supplier mSupplier = new Supplier();
    private final Product mProduct = new Product();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final Farm mFarm = new Farm();

    @FXML
    private AnchorPane fxAddItemUI;
    @FXML
    private TextField fxSupplierName;
    @FXML
    private TextField fxSupplierFirstname;
    @FXML
    private TextField fxSupplierLastname;
    @FXML
    private ChoiceBox<String> fxChoiceSupplierFarm;
    @FXML
    private ChoiceBox<String> fxChoiceSupplierProduct;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getFarmList();
        getProductList();
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
    void handleSaveButton() {
        if (isEditStatus){
            handleEditOperation(mSupplier);
        }else{
            handleAddOperation();
        }


    }

    private void handleAddOperation(){
        if (Validation.isEmpty(fxSupplierName.getText()
                , fxSupplierFirstname.getText()
                , fxSupplierLastname.getText()
                , fxChoiceSupplierFarm.getSelectionModel().getSelectedItem()
                , fxChoiceSupplierProduct.getSelectionModel().getSelectedItem())
        ){
            alert.show("Supplier");
        }else {
            Supplier supplier = new Supplier();
            supplier.setSupplierName(fxSupplierName.getText());
            supplier.setSupplierFirstname(fxSupplierFirstname.getText());
            supplier.setSupplierLastname(fxSupplierLastname.getText());
            supplier.setSupplierFarm(mFarmMap.get(fxChoiceSupplierFarm.getSelectionModel().getSelectedItem()));
            supplier.setSupplierProduct(mProductMap.get(fxChoiceSupplierProduct.getSelectionModel().getSelectedItem()));
            if (mSupplierDAO.addData(supplier)){
                mSupplierDAO.updateLiveData();
                alert.saveItem("Supplier", true);
                handleClearButton();
            }else{
                alert.saveItem("Supplier", false);
            }
        }
    }

    private void handleEditOperation(Supplier supplier){
        mSupplier.setSupplierName(fxSupplierName.getText());
        mSupplier.setSupplierFirstname(fxSupplierFirstname.getText());
        mSupplier.setSupplierLastname(fxSupplierLastname.getText());
        mSupplier.setSupplierFarm(mFarmMap.get(fxChoiceSupplierFarm.getSelectionModel().getSelectedItem()));
        mSupplier.setSupplierProduct(mProductMap.get(fxChoiceSupplierProduct.getSelectionModel().getSelectedItem()));
        if (mSupplierDAO.editData(supplier)){
            mSupplierDAO.updateLiveData();
            alert.updateItem("Supplier", true);
        }else {
            alert.updateItem("Supplier", false);
        }
        isEditStatus = false;
        handleCancelButton();
    }


    @FXML
    void handleCancelButton() {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
        System.out.println("Cancel...");
    }

    @FXML
    void handleClearButton() {
        fxSupplierName.setText("");
        fxSupplierFirstname.setText("");
        fxSupplierLastname.setText("");
        getFarmList();
        getProductList();
    }

    public void inflateUI(Supplier supplier) {
        fxSupplierName.setText(supplier.getSupplierName());
        fxSupplierFirstname.setText(supplier.getSupplierFirstname());
        fxSupplierLastname.setText(supplier.getSupplierLastname());
        fxChoiceSupplierFarm.getSelectionModel().select(supplier.getSupplierFarm().getFarmName());
        fxChoiceSupplierProduct.getSelectionModel().select(supplier.getSupplierProduct().getProductName());
        getProductList();
        fxChoiceSupplierProduct.getSelectionModel().select(supplier.getSupplierProduct().getProductName());
        isEditStatus = true;
        mSupplier.setSupplierId(supplier.getSupplierId());
    }
}
