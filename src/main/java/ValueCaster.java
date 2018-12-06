import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by e080868 on 11/29/2018.
 */
public class ValueCaster {

    public static Object cast(String data){
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = simpleDateFormat.parse(data);
            if(date != null) {
                return date;
            }
        } catch (ParseException e) {
        }
        try {
            Integer i = Integer.parseInt(data);
            if(i != null) {
                return i;
            }
        } catch (NumberFormatException e){
        }
        return data;
    }
}
