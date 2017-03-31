package a2dv606.androidproject.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import a2dv606.androidproject.MainActivity;
import a2dv606.androidproject.R;

/**
 * Created by Abeer on 3/29/2017.
 */

public class NotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder =setupNotification(context);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(100, builder.build());
        Toast.makeText(context,"notification alarm fired",Toast.LENGTH_LONG).show();

    }

    private  Notification.Builder setupNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.drop_notification_icon) .setContentTitle("Water Tracker")
                .setContentText("Time to drink water")
                .setContentInfo("add a drink!").setAutoCancel(true);
     return builder;

    }

}
