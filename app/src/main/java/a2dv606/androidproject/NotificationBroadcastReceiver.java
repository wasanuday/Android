package a2dv606.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Calendar;

import a2dv606.androidproject.Notifications.NotificationReciever;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Hussain on 3/31/2017.
 */

public class NotificationBroadcastReceiver
        extends BroadcastReceiver {

private Context context;
    private String PREF_SCHEDULE = "schedule_notification";
    private String PREF_STOP= "stop_notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getExtras().getString("action");
        if(action.equals(PREF_SCHEDULE))
            scheduleNotificationAlarm();
      if (action.equals(PREF_STOP))
          stopNotificationAlarm();


    }
    private void scheduleNotificationAlarm() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);

        Calendar calendar = Calendar.getInstance();
        /*
        set start on time for the repeating alarm
         */

        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,45);
        calendar.set(Calendar.SECOND,0);
        Intent mIntent = new Intent(context,NotificationReciever.class);
       PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context,101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        /*
        set repeating time to every 2 minutes
         */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 2, pendingIntent);
        Toast.makeText(context,"repeating alarm has been scheduled",Toast.LENGTH_LONG).show();
    }

    public void stopNotificationAlarm(){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent mIntent = new Intent(context,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context,101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmManager!=null)
        {  alarmManager.cancel(pendingIntent);
            String msg = "Repeating alarm has been unscheduled";
            Toast.makeText(context, msg,Toast.LENGTH_LONG).show();}
    }
}
