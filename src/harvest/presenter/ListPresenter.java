package harvest.presenter;

import harvest.database.*;
import harvest.model.Farm;
import harvest.model.Product;
import harvest.model.ProductDetail;
import harvest.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListPresenter {

    private final SupplierDAO mSupplierDAO = SupplierDAO.getInstance();
    private final FarmDAO mFarmDAO = FarmDAO.getInstance();
    private final ProductDAO mProductDAO = ProductDAO.getInstance();
    private final ProductDetailDAO mProductDetailDAO = ProductDetailDAO.getInstance();
    private final HoursDAO mHoursDAO = HoursDAO.getInstance();

    private Map<String, Supplier> mSupplierMap = new LinkedHashMap<>();
    private Map<String, Farm> mFarmMap = new LinkedHashMap<>();
    private Map<String, Product> mProductMap = new LinkedHashMap<>();
    private Map<String, ProductDetail> mProductDetailMap = new LinkedHashMap<>();

    public Map<String, Supplier> getSupplierMap() {
        return mSupplierMap;
    }

    public Map<String, Farm> getFarmMap() {
        return mFarmMap;
    }

    public Map<String, Product> getProductMap() {
        return mProductMap;
    }

    public Map<String, ProductDetail> getProductDetailMap() {
        return mProductDetailMap;
    }

    //fill the ChoiceBox by supplier name
    public ObservableList<String> getSupplierList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mSupplierMap = mSupplierDAO.getSupplierMap();
            list.setAll(mSupplierMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //fill the ChoiceBox by Farm name
    public ObservableList<String> getFarmList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mFarmMap = mFarmDAO.getFarmMap();
            list.setAll(mFarmMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //fill the ChoiceBox by product name
    public ObservableList<String> getProductList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mProductMap = mProductDAO.getProductMap();
            list.setAll(mProductMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //fill the ChoiceBox by Product Code
    public ObservableList<String> getProductCode(Product product) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            mProductDetailMap = mProductDetailDAO.getProductDetailMap(product);
            list.setAll(mProductDetailMap.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
