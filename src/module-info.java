module SternHarvestCompany {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;

    opens harvest.ui.employee;

    opens harvest;
}