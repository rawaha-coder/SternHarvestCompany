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

    public static String timeToStringTime(long time) {
        int sec = (int) time/1000;
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }

}
