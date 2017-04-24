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
import java.util.Date;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.Notifications.NotificationReciever;
import a2dv606.androidproject.Settings.PreferenceKey;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Abeer on 3/29/2017.
 */

public class AppBroadcastReceiver extends BroadcastReceiver {

     private DrinkDataSource db;
    private Context mContext;
    private final String ACTION_SETUP = "setup";
    private final String ACTION_SCHEDULE = "schedule_notifications";
    private final String ACTION_STOP= "stop_notifications";



    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getExtras().getString("action");
        mContext =context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isNotiEnable= prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED,true);
        switch (action){
            case ACTION_SETUP:
                insertDateLog();
                  if(isNotiEnable)
                      scheduleNotificationAlarm();
                break;
            case ACTION_SCHEDULE:
               scheduleNotificationAlarm();
                break;
            case ACTION_STOP:
                stopNotificationAlarm();
                break;
        }

    }



    private void insertDateLog() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int waterNeed= prefs.getInt(PreferenceKey.PREF_WATER_NEED,0);
        db= new DrinkDataSource(mContext);
        db.open();
        db.create(0,waterNeed,DateHandler.getCurrentDate());
        System.out.println("db alarm fired "+ DateHandler.getCurrentDate());
        System.out.println("new record inserted: "+new Date()+" water need: "+waterNeed+" Water drank: "+0);

        Intent local = new Intent();
        local.setAction("com.update.view.action");
        mContext.sendBroadcast(local);

    }


    private void scheduleNotificationAlarm() {
        System.out.println("notifications sech");
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(mContext);
             String from = prefs.getString(PreferenceKey.FROM_KEY,"");
             int interval = prefs.getInt(PreferenceKey.PREF_INTERVAL,2);
             String[] values= from.split(":");
              String hr= values[0];
              String mt= values[1];

        Calendar calendar = Calendar.getInstance();
        /*
        set start on time for the repeating alarm
         */
        Calendar now =Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE,Integer.valueOf(mt));
        calendar.set(Calendar.SECOND,0);
        Intent mIntent = new Intent(mContext,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
        /*  set repeating time to every 2 hr  */
        long time= calendar.getTimeInMillis() ;
        if(calendar.before(now)){
            time = System.currentTimeMillis()+500000;}
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time, 1000 * 60 *60*interval, pendingIntent);



        Toast.makeText(mContext,"repeating alarm has been scheduled",Toast.LENGTH_LONG).show();
    }


    public void stopNotificationAlarm(){
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