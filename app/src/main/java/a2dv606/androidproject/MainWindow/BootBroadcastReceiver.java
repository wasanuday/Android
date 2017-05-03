package a2dv606.androidproject.MainWindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Hussain on 5/2/2017.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context pContext, Intent intent) {
        System.out.println("loooooooooooooooooooooooooooooooooooooooooooooool");
        AlarmHelper.setDBAlarm(pContext);
    }
}
