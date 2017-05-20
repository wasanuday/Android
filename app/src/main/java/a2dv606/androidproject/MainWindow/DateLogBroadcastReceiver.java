package a2dv606.androidproject.MainWindow;

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

public class DateLogBroadcastReceiver extends BroadcastReceiver {

    private DrinkDataSource db;
    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext =context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotiEnable= prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED,true);
                insertDateLog();
                PrefsHelper.updateCongDialogPref(context,true);
                  if(isNotiEnable)
                     AlarmHelper.setNotificationsAlarm(context);

    }



    private void insertDateLog() {

        int waterNeed= PrefsHelper.getWaterNeedPrefs(mContext);
        db= new DrinkDataSource(mContext);
        db.open();
        db.createDateLog(0,waterNeed, DateHandler.getCurrentDate());
        System.out.println("db alarm fired "+ DateHandler.getCurrentDate());
        System.out.println("new record inserted: "+new Date()+" water need: "+waterNeed+" Water drank: "+0);

        Intent local = new Intent();
        local.setAction("com.update.view.action");
        mContext.sendBroadcast(local);

    }

}
