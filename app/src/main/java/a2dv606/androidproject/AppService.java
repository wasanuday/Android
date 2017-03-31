package a2dv606.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Calendar;

import a2dv606.androidproject.Notifications.NotificationReciever;

public class AppService extends Service {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String NOTIFICATION_ACTION= "reset_scheduled_time";
    public AppService() {
    }

    /**
     * Binding
     */
    private final IBinder binder = new AppBinder();

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBindCommand(...)");
        return binder;

    }


    public class AppBinder extends Binder {

        public AppService getService() {
            System.out.println("ongetServiceCommand(...)");
            return AppService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand(...)");
        boolean isOn = intent.getExtras().getBoolean("notification_enable");
        boolean insertRecord = intent.getExtras().getBoolean("insert_record");
    //    String reschedule = intent.getExtras().getString()
        if (insertRecord)
            insertNewRecordInDatabase();

        if(isOn)
          setUpNotificationAlarm();
        return Service.START_NOT_STICKY;
    }

    private void insertNewRecordInDatabase() {
        Toast.makeText(getApplicationContext(),"new record insert in database",Toast.LENGTH_LONG).show();
    }

    private void setUpNotificationAlarm() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

        Calendar calendar = Calendar.getInstance();
        /*
        set start on time for the repeating alarm
         */

        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,45);
        calendar.set(Calendar.SECOND,0);
        Intent mIntent = new Intent(getApplicationContext(),NotificationReciever.class);
         pendingIntent = PendingIntent.
                getBroadcast(getApplicationContext(),101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        /*
        set repeating time to every 2 minutes
         */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 2, pendingIntent);
        Toast.makeText(getApplicationContext(),"repeating alarm has been scheduled",Toast.LENGTH_LONG).show();
    }

    public void stopNotificationAlarm(){

         if(alarmManager!=null)
         {  alarmManager.cancel(pendingIntent);
        String msg = "Repeating alarm has been unscheduled";
        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();}
    }

}
