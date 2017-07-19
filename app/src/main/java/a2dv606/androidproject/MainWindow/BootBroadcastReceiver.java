package a2dv606.androidproject.MainWindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import a2dv606.androidproject.Database.DrinkDataSource;

/**
 * Created by Hussain on 5/2/2017.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context pContext, Intent intent) {
        DrinkDataSource db= new DrinkDataSource(pContext);
        db.open();
        int waterNeed= PrefsHelper.getWaterNeedPrefs(pContext);
        db.createMissingDateLog(0,waterNeed);
        AlarmHelper.setDBAlarm(pContext);
    }
}


