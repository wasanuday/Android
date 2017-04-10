package a2dv606.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.Settings.PreferenceKey;

/**
 * Created by Abeer on 3/29/2017.
 */

public class DBBroadcastReceiver extends BroadcastReceiver {

     private DrinkDataSource db;



    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int waterNeed= prefs.getInt(PreferenceKey.PREF_WATER_RECOM,0);
        db= new DrinkDataSource(context);
        db.open();
        db.create(0,waterNeed,new Date().toString());
      //  db.create()
        System.out.println("db alarm fired");
        System.out.println("new record inserted"+new Date()+"water need: "+waterNeed+" Water drank: "+0);
    }
}
