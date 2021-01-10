package harvest.util;

import java.sql.Time;
import java.text.ParseException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Validation {
    public static boolean isEmpty(String... text){
        for (String s:text){
            if (s.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public static boolean isDouble(String getText) {
        try {
            Double.parseDouble(getText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public static String getFullName(String first, String last){
//        return first + " " + last;
//    }

    public void Duree(){
        Duration duration = Duration.ofSeconds(3000);
        long hours = duration.toHours();
        int minutes = (int) ((duration.getSeconds() % (60 * 60)) / 60);
        int seconds = (int) (duration.getSeconds() % 60);
        System.out.println(hours + ":" + minutes + ":" + seconds);
    }

    public static Time convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return Time.valueOf(String.format("%d:%02d:%02d", h,m,s));
    }

}
