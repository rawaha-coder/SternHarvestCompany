package harvest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javafx.scene.control.Alert.AlertType;

public class Validation {

    private static AlertMaker alert = new AlertMaker();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public static boolean isValidDate(String date){
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String getText) {
        try {
            Double.parseDouble(getText);
            return true;
        } catch (Exception e) {
            alert.show("Error", "Enter value should be a decimal number (eg: 40, 10.5)", AlertType.ERROR);
            return false;
        }
    }

    public static String getFullName(String first, String last){
        return first + " " + last;
    }

    public static int getStatus(boolean status){
        if (status){
            return 1;
        }else {
            return 0;
        }
    }
}
