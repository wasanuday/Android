package a2dv606.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Abeer on 3/29/2017.
 */

public class DBBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("db alarm fired");
        System.out.println("new record inserted"+new Date());
    }
}
