package a2dv606.androidproject;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hussain on 4/12/2017.
 */

public class DateHandler {
    public static String getCurrentDate(){
        return  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm a", Locale.getDefault());
        return dateFormat.format(new Date());
    }
    public static String getDateFormat(String date)  {
        System.out.println(date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(d);
    }

}
