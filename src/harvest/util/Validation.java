package harvest.util;

import java.time.Duration;

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

}
