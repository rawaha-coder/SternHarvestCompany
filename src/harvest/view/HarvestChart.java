package harvest.view;

import harvest.database.ProductionDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;

import java.sql.Date;


public class HarvestChart{
    ProductionDAO mProductionDAO = ProductionDAO.getInstance();
    @FXML
    private AreaChart<String, Number> fxHarvestChart;

    public void getProductionChart(Date d1, Date d2){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            series = mProductionDAO.harvestProductionGraph(d1, d2);
        }catch (Exception e){
            e.printStackTrace();
        }
        fxHarvestChart.getData().add(series);
    }
}
