package harvest.ui.harvest;

import harvest.database.*;
import harvest.model.*;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.*;

public class AddHarvestController{
//
//    private final Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
//    private final Map<String, Farm> mFarmMap = new LinkedHashMap<>();
//    private final Map<String, Product> mProductMap = new LinkedHashMap<>();
//    private final Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();
//    private final AlertMaker alert = new AlertMaker();
//    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
//    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
//    private final ProductDAO mProductDAO = ProductDAO.getInstance();
//    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
//    private final HarvestDAO mHarvestDAO = HarvestDAO.getInstance();
//    ObservableList<String> observableSupplierList = FXCollections.observableArrayList();
//    ObservableList<String> observableFarmList = FXCollections.observableArrayList();
//    ObservableList<String> observableProductList = FXCollections.observableArrayList();
//    ObservableList<String> observableProductCode = FXCollections.observableArrayList();
//
//    @FXML
//    private AnchorPane addHarvestUI;
//    @FXML
//    private DatePicker fxHarvestDate;
//    @FXML
//    private ChoiceBox<String> fxSupplierList;
//    @FXML
//    private ChoiceBox<String> fxFarmList;
//    @FXML
//    private ChoiceBox<String> fxProductList;
//    @FXML
//    private ChoiceBox<String> fxProductCodeList;
//    @FXML
//    private Button fxSaveButton;
//    @FXML
//    private Button fxClearButton;
//    @FXML
//    private Button fxCloseButton;
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        getSupplierList();
//        getFarmList();
//        getProductList();
//        observeChoiceProduct();
//    }
//
//    private void getSupplierList() {
//        observableSupplierList.clear();
//        try {
//            List<Supplier> suppliers = new ArrayList<>(mSupplierDAO.getData());
//            if (suppliers.size() > 0) {
//                for (Supplier supplier : suppliers) {
//                    observableSupplierList.add(supplier.getSupplierName());
//                    mSupplierMap.put(supplier.getSupplierName(), supplier);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fxSupplierList.setItems(observableSupplierList);
//    }
//
//    //fill the ChoiceBox by employee list
//    private void getFarmList() {
//        observableFarmList.clear();
//        try {
//            List<Farm> farms = new ArrayList<>(mFarmDAO.getFarmData());
//            if (farms.size() > 0) {
//                for (Farm farm : farms) {
//                    observableFarmList.add(farm.getFarmName());
//                    mFarmMap.put(farm.getFarmName(), farm);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fxFarmList.setItems(observableFarmList);
//    }
//
//    //fill the ChoiceBox by employee list
//    private void getProductList() {
//        observableProductList.clear();
//        try {
//            List<Product> products = new ArrayList<>(mProductDAO.getData());
//            if (products.size() > 0) {
//                for (Product product : products) {
//                    observableProductList.add(product.getProductName());
//                    mProductMap.put(product.getProductName(), product);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fxProductList.setItems(observableProductList);
//    }
//
//    private void observeChoiceProduct() {
//        fxProductList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
//            if (mProductMap.get(new_val) != null) {
//                getProductCode(mProductMap.get(new_val));
//            }
//        });
//    }
//
//
//    //fill the ChoiceBox by employee list
//    private void getProductCode(Product product) {
//        observableProductCode.clear();
//        try {
//            List<ProductDetail> productDetails = new ArrayList<>(mProductDetailDAO.getProductDetail(product));
//            if (productDetails.size() > 0) {
//                for (ProductDetail productDetail : productDetails) {
//                    observableProductCode.add(productDetail.getProductCode());
//                    mProductDetailMap.put(productDetail.getProductCode(), productDetail);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fxProductCodeList.setItems(observableProductCode);
//    }
//
//    @FXML
//    void handleClearButton() {
//        getSupplierList();
//        getFarmList();
//        getProductList();
//        fxHarvestDate.getEditor().setText("");
//    }
//
//    @FXML
//    void handleCloseButton() {
//        Stage stage = (Stage) addHarvestUI.getScene().getWindow();
//        stage.close();
//    }
//
//    @FXML
//    void handleSaveButton() {
//        if (fxHarvestDate.getValue() == null
//                || fxSupplierList.getValue() == null
//                || fxFarmList.getValue() == null
//                || fxProductList.getValue() == null
//                || fxProductCodeList.getValue() == null)
//        {
//            alert.missingInfo("Harvest");
//            return;
//        }
//        Harvest harvest = new Harvest();
//        harvest.setHarvestDate(Date.valueOf(fxHarvestDate.getValue()));
//        harvest.setSupplier(mSupplierMap.get(fxSupplierList.getValue()));
//        harvest.setFarm(mFarmMap.get(fxFarmList.getValue()));
//        harvest.setProduct(mProductMap.get(fxProductList.getValue()));
//        harvest.setProductDetail(mProductDetailMap.get(fxProductCodeList.getValue()));
//
//        if (mHarvestDAO.isExists(harvest) == 0){
//            alert.saveItem("Harvest", mHarvestDAO.addHarvestDate(harvest));
//        }else{
//            System.out.println(mHarvestDAO.getHarvestId(harvest));
//        }
//    }
}
