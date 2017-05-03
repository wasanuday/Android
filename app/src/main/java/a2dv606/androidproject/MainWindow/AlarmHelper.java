package a2dv606.androidproject.MainWindow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Calendar;

import a2dv606.androidproject.Notifications.NotificationReciever;
import a2dv606.androidproject.Settings.PreferenceKey;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Hussain on 5/2/2017.
 */

public class AlarmHelper {


    public static void setDBAlarm(Context context) {
        boolean isNotWorking =(PendingIntent.getBroadcast(context, 90,
                new Intent(context,DateLogBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) == null);
        System.out.println("not working "+isNotWorking);
        if(isNotWorking) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Intent mIntent = new Intent(context,DateLogBroadcastReceiver.class);
            mIntent.putExtra(DateLogBroadcastReceiver.EXTRA, DateLogBroadcastReceiver.ACTION_SETUP);
            PendingIntent pendingIntent = PendingIntent.
                    getBroadcast(context,90,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent);
            System.out.println("db setup");
    }}
    public static void setNotificationsAlarm(Context mContext) {

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(mContext);
        String from = prefs.getString(PreferenceKey.FROM_KEY,"8:0");
        int interval = prefs.getInt(PreferenceKey.PREF_INTERVAL,2);
        String[] values= from.split(":");
        String hr= values[0];
        String mt= values[1];

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE,Integer.valueOf(mt));
        calendar.set(Calendar.SECOND,0);
        Intent mIntent = new Intent(mContext,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000 * 60 *60*interval, pendingIntent);
        Toast.makeText(mContext,"repeating alarm has been scheduled",Toast.LENGTH_LONG).show();
    }





    public static void stopNotificationsAlarm(Context mContext){
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
        Intent mIntent = new Intent(mContext,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(mContext,101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager!=null)
        {  alarmManager.cancel(pendingIntent);
            String msg = "Repeating alarm has been unscheduled";
            Toast.makeText(mContext, msg,Toast.LENGTH_LONG).show();}
    }

}

