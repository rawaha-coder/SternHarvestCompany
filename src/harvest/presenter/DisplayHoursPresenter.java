package harvest.presenter;

import harvest.view.DisplayHoursController;
import harvest.database.HoursDAO;
import harvest.model.Hours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class DisplayHoursPresenter {

    public static ObservableList<Hours> DISPLAY_HOURS_LIVE_DATA = FXCollections.observableArrayList();

    DisplayHoursController mDisplayHoursController;

    HoursDAO mHoursDAO = HoursDAO.getInstance();
    LocalDate date = LocalDate.now();
    Thread thread = new Thread(){
        public void run(){
            System.out.println("Thread Running");
            updateDisplayHoursLiveData(Date.valueOf(date));
        }
    };

    public DisplayHoursPresenter(DisplayHoursController displayHoursController) {
        mDisplayHoursController = displayHoursController;
        initList();
        observeDatePicker();
        thread.start();
    }

    public void initList(){
        mDisplayHoursController.fxDatePicker.setValue(date);
        mDisplayHoursController.fxTotalWorkingHours.setText(getTotalHours());
        mDisplayHoursController.fxTotalTransport.setText(getTotalTransport());
        mDisplayHoursController.fxTotalCredit.setText(getTotalCredit());
    }

    private void observeDatePicker(){
        mDisplayHoursController.fxDatePicker.valueProperty().addListener((ob, ov, nv) -> {
            updateDisplayHoursLiveData(Date.valueOf(nv));
            mDisplayHoursController.fxTotalWorkingHours.setText(getTotalHours());
            mDisplayHoursController.fxTotalTransport.setText(getTotalTransport());
            mDisplayHoursController.fxTotalCredit.setText(getTotalCredit());
        });
    }

    public void updateDisplayHoursLiveData(Date date) {
        DISPLAY_HOURS_LIVE_DATA.clear();
        try {
            DISPLAY_HOURS_LIVE_DATA.setAll(mHoursDAO.getData(date));
            System.out.println(" Update list size: " + DISPLAY_HOURS_LIVE_DATA.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getTotalHours(){
        long totalMinutes = 0;
        for (Hours harvestHours : DISPLAY_HOURS_LIVE_DATA){
            totalMinutes += harvestHours.getTotalMinutes() ;
        }
        return String.valueOf(totalMinutes);
    }

    private String getTotalTransport(){
        double totalTransport = 0.0;
        for (Hours hours : DISPLAY_HOURS_LIVE_DATA){
            totalTransport += hours.getTransport().getTransportAmount();
        }
        return String.valueOf(totalTransport);
    }

    private String getTotalCredit(){
        double totalCredit = 0.0;
        for (Hours hours : DISPLAY_HOURS_LIVE_DATA){
            totalCredit += hours.getCredit().getCreditAmount();
        }
        return String.valueOf(totalCredit);
    }
}
