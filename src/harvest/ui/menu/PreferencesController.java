package harvest.ui.menu;

import harvest.database.PreferencesDAO;
import harvest.util.AlertMaker;
import harvest.util.Validation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PreferencesController implements Initializable {

    @FXML
    private TextField fxPenalty;
    @FXML
    private TextField fxGeneralPenalty;
    @FXML
    private TextField fxHourPrice;
    @FXML
    private TextField fxTransportPrice;
    AlertMaker alertMaker = new AlertMaker();
    PreferencesDAO preferencesDAO = PreferencesDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFields();
    }

    private void initFields(){
        double[] values = new double[4];
        try {
            values = preferencesDAO.getData();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        fxPenalty.setText(String.valueOf(values[0]));
        fxGeneralPenalty.setText(String.valueOf(values[1]));
        fxHourPrice.setText(String.valueOf(values[2]));
        fxTransportPrice.setText(String.valueOf(values[3]));
    }

    public void handlePreferencesBtn() {
        if (!Validation.isDouble(fxPenalty.getText()) || !Validation.isDouble(fxGeneralPenalty.getText()) || !Validation.isDouble(fxHourPrice.getText()) ){
            alertMaker.missingInfo("Value");
            return;
        }
        alertMaker.updateItem("Value",
                preferencesDAO.editData(
                        Double.valueOf(fxPenalty.getText()),
                        Double.valueOf(fxGeneralPenalty.getText()),
                        Double.valueOf(fxHourPrice.getText()),
                        Double.valueOf(fxTransportPrice.getText()))
        );
    }
}
