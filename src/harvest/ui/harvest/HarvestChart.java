package harvest.ui.harvest;


import harvest.database.HarvestProductionDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class HarvestChart implements Initializable {

    @FXML
    private AnchorPane fxGetHarvestUI;

    @FXML
    private AreaChart<?, ?> fxHarvestChart;

    @FXML
    private CategoryAxis fxHarvestChartDate;

    @FXML
    private NumberAxis fxHarvestChartQuantity;

    HarvestProductionDAO mHarvestProductionDAO = HarvestProductionDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate d1 = LocalDate.now();

        LocalDate d2 = d1.minus(Period.ofDays(30));

        XYChart.Series series = new XYChart.Series();
        try {
            series = mHarvestProductionDAO.harvestProductionGraph(Date.valueOf(d2), Date.valueOf(d1));
        }catch (Exception e){
            e.printStackTrace();
        }

        fxHarvestChart.getData().add(series);

    }
}
