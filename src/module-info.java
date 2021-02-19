module SternHarvestCompany {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;


    opens harvest.ui.employee;
    opens harvest.ui.product;
    opens harvest.ui.credit;
    opens harvest.ui.farm;
    opens harvest.ui.supplier;
    opens harvest.model;
    opens harvest.ui.individual;
    opens harvest.util;
    opens harvest.ui.menu;
    opens harvest.ui.group;
    opens harvest.ui.production;

    opens harvest;
}