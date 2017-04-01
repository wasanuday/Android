package a2dv606.androidproject.Model;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Abeer on 3/23/2017.
 */

public class TimeLog {
    long ID;
    private String time;
    private int amount;
    private String date;
    Date d = new Date();

    public String getDate() {
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        Date today = Calendar.getInstance().getTime();
        date=df.format(today);
        return date;

    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {

        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm a", Locale.getDefault());

        time=dateFormat.format(d);
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }


}

