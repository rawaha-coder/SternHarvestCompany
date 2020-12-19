package harvest.ui.product;


import harvest.database.ProductDAO;
import harvest.database.ProductDetailDAO;
import harvest.model.Product;
import harvest.model.ProductDetail;
import harvest.util.AlertMaker;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayProductController implements Initializable {

    public static ObservableList<Product> PRODUCT_NAME_LIVE_DATA = FXCollections.observableArrayList();
    public static ObservableList<ProductDetail> PRODUCT_DETAIL_LIVE_DATA = FXCollections.observableArrayList();

    @FXML
    private TableView<harvest.model.Product> fxProductTable;
    @FXML
    private TableColumn<harvest.model.Product, String> fxProductNameColumn;
    @FXML
    private TableView<ProductDetail> fxProductDetailTable;
    @FXML
    private TableColumn<ProductDetail, String> fxProductTypeColumn;
    @FXML
    private TableColumn<ProductDetail, String> fxProductCodeColumn;
    @FXML
    private TableColumn<ProductDetail, Double> fxProductSecondPriceColumn;
    @FXML
    private TableColumn<ProductDetail, Double> fxProductFirstPriceColumn;

    private final AlertMaker alert = new AlertMaker();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        updateLiveData();
        observeSelectProduct();
    }

    public void updateLiveData(){
        mProductDAO.updateLiveData();
        mProductDetailDAO.updateLiveData(PRODUCT_NAME_LIVE_DATA.get(0));
    }

    public void initColumns() {
        fxProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        fxProductTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        fxProductCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        fxProductFirstPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productFirstPrice"));
        fxProductSecondPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productSecondPrice"));
        fxProductTable.setItems(PRODUCT_NAME_LIVE_DATA);
        fxProductDetailTable.setItems(PRODUCT_DETAIL_LIVE_DATA);

    }

    private void observeSelectProduct(){
        fxProductTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Product> ov, Product old_val, Product new_val) -> {
                    if (new_val != null){
                        try{
                            mProductDetailDAO.updateLiveData(new_val);
                            fxProductDetailTable.getSelectionModel().selectFirst();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    @FXML
    void editProduct() {
        Product product = fxProductTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            alert.show("Required selected product code", "Please select a product code", AlertType.INFORMATION);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/product/add_product.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddProductController controller = loader.getController();
            controller.inflateProductUI(product);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteProduct() {
        Product product = fxProductTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            alert.show("Required selected product", "Please select a product from the second table", AlertType.INFORMATION);
            return;
        }
        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Product");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            alert.deleteItem("Product", mProductDAO.deleteDataById(product.getProductId()));
        } else {
            alert.cancelOperation("Delete");
        }
        mProductDAO.updateLiveData();
        fxProductTable.getSelectionModel().selectFirst();
        fxProductDetailTable.getSelectionModel().selectFirst();
        System.out.println("Delete product...");
    }

    @FXML
    void editProductDetail(){
        ProductDetail productDetail = fxProductDetailTable.getSelectionModel().getSelectedItem();
        if (productDetail == null) {
            alert.show("Required selected product code", "Please select a product code", AlertType.INFORMATION);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/harvest/ui/product/add_product.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent parent = loader.load();
            AddProductController controller = loader.getController();
            controller.inflateProductDetailUI(productDetail);
            stage.setTitle("Edition");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteProductDetail(){
        ProductDetail productDetail = fxProductDetailTable.getSelectionModel().getSelectedItem();
        if (productDetail== null) {
            alert.show("Required selected product", "Please select a product from the second table", AlertType.INFORMATION);
            return;
        }
        AlertMaker alertDelete = new AlertMaker();

        Optional<ButtonType> result = alertDelete.deleteConfirmation("Product Detail");
        assert result.isPresent();
        if (result.get() == ButtonType.OK && result.get() != ButtonType.CLOSE) {
            mProductDetailDAO.deleteDataById(productDetail.getProductDetailId());
        } else {
            alert.show("Deletion cancelled", "Deletion process cancelled", AlertType.INFORMATION);
        }
        updateLiveData();
        System.out.println("Delete product...");
    }
}