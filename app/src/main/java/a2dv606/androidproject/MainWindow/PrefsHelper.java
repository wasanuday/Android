package a2dv606.androidproject.MainWindow;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import a2dv606.androidproject.R;
import a2dv606.androidproject.Settings.PreferenceKey;

/**
 * Created by Hussain on 5/2/2017.
 */

public class PrefsHelper {

    public static void updateCongDialogPref(Context mContext, boolean bln) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("show_dialog", bln);
        editor.commit();
    }

    public static int getWaterNeedPrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PreferenceKey.PREF_WATER_NEED,0);
    }

    public static boolean getNotificationsPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED,true);

    }
    public static boolean getCongDialogPrefs(Context context)
    {   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
       return  preferences.getBoolean("show_dialog", true);

    }

    public static boolean getSoundsPrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PreferenceKey.PREF_SOUND,false);
    }

    public static String getGlassSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
         return prefs.getString(PreferenceKey.PREF_GLASS_SIZE,"250");

    }
    public static String getBottleSizePrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

          return  prefs.getString(PreferenceKey.PREF_BOTTLE_SIZE, "1500");
    }

    public static boolean getFirstTimeRunPrefs(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
      return pref.getBoolean("first_time_run", true) ;
    }

    public static void setFirstTimeRunPrefs(Context context, boolean b) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("first_time_run", b);
        editor.commit();
    }

    public static boolean getSleepModePrefs(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
       return pref.getBoolean("sleep_mode", false);
    }

    public static void setSleepModePrefs(Context context, boolean bln) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("sleep_mode", bln);
        editor.commit();

    }
}