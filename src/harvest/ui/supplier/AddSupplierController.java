package harvest.ui.supplier;

import harvest.database.ProductDAO;
import harvest.model.Product;
import harvest.model.Supplier;
import harvest.util.AlertMaker;
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
    private final Map<String, Product> mProductMap = new LinkedHashMap<>();

    private boolean isEditStatus = false;
    private final AlertMaker alert = new AlertMaker();
    private final Supplier mSupplier = new Supplier();
    private final Product mProduct = new Product();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();

    @FXML
    private AnchorPane fxAddItemUI;

    @FXML
    private TextField fxSupplierName;

    @FXML
    private TextField fxSupplierFirstname;

    @FXML
    private TextField fxSupplierLastname;

    @FXML
    private ChoiceBox<String> fxChoiceSupplierProduct;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getProductList();
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
            handleAddOperation(mSupplier);
        }


    }

    private void handleAddOperation(Supplier supplier){

    }

    private void handleEditOperation(Supplier supplier){
    }


    @FXML
    void handleCancelButton(ActionEvent event) {
        Stage stage = (Stage) fxAddItemUI.getScene().getWindow();
        stage.close();
        System.out.println("Cancel...");
    }

    @FXML
    void handleClearButton(ActionEvent event) {
        fxSupplierName.setText("");
        fxSupplierFirstname.setText("");
        fxSupplierLastname.setText("");
        getProductList();
    }


    public void inflateUI(Supplier supplier) {
        fxSupplierName.setText(supplier.getSupplierName());
        fxSupplierFirstname.setText(supplier.getSupplierFirstname());
        fxSupplierLastname.setText(supplier.getSupplierLastname());
        getProductList();
        fxChoiceSupplierProduct.getSelectionModel().select(supplier.getSupplierProduct().getProductName());
        isEditStatus = true;
        mSupplier.setSupplierId(supplier.getSupplierId());
        mSupplier.setSupplierName(supplier.getSupplierName());
        mSupplier.setSupplierFirstname(supplier.getSupplierFirstname());
        mSupplier.setSupplierLastname(supplier.getSupplierLastname());
        mSupplier.setSupplierProduct(supplier.getSupplierProduct());
    }
}
