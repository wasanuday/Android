package a2dv606.androidproject.Model;

/**
 * Created by Abeer on 3/23/2017.
 */

public class TimeLog {
    private String time;
    private int amount;
    private String date;

    public String getDate() {
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

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TimeLog(String time, int amount, String date) {

        this.time= time;
        this.date= date;
        this.amount=amount;


    }
}

