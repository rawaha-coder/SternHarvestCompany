package harvest.util;

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

    public static String getFullName(String first, String last){
        return first + " " + last;
    }
}
