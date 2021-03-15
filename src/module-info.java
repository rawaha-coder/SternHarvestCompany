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


    opens harvest.ui.farm;
    opens harvest.ui.supplier;
    opens harvest.model;
    opens harvest.ui.individual;
    opens harvest.util;
    opens harvest.ui.menu;
    opens harvest.ui.group;
    opens harvest.ui.production;
    opens harvest.ui.hours;
    opens harvest.ui.product;
    opens harvest.ui.credit;
    opens harvest.res.layout;
    opens harvest.controller;


    opens harvest;
}