module SternHarvestCompany {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires org.apache.poi.poi;
    requires org.apache.poi.scratchpad;
    requires commons.math3;
    requires org.apache.commons.collections4;
    requires org.apache.commons.codec;
    requires org.apache.commons.compress;
    requires org.slf4j;
    requires org.apache.poi.ooxml;

    opens harvest.model;
    opens harvest.util;
    opens harvest.res.layout;
    opens harvest.view;

    opens harvest;
}