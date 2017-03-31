package a2dv606.androidproject.Settings;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

import java.util.Calendar;

import a2dv606.androidproject.R;

/**
 * Created by Abeer on 3/28/2017.
 */

public class NotificationsFragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String PREF_START_TIME = "key_from_time";
    private final static String PREF_END_TIME ="key_to_time";
    private final static String PREF_INTERVAL="key_notif_interval_time";
    private final static String PREF_IS_ENABLED="key_notif_enable";
    private final static String PREF_SOUND="key_notif_sound";
    private final static String FROM_KEY= "from";
    private final static String TO_KEY= "to";

    private SettingsActivity mActivity;
    private Context context;
    private Preference intervalPref;
    private Preference soundPref;
    private Preference notiEnablePref;
    private Preference fromTimePrf;
    private Preference toTimePref;
    private Calendar fromC = Calendar.getInstance();
    private Calendar toC = Calendar.getInstance();



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =mActivity.getApplicationContext();
        addPreferencesFromResource(R.xml.notification_prefs);
        notiEnablePref= findPreference(PREF_IS_ENABLED);
        soundPref= findPreference(PREF_SOUND);
        intervalPref=findPreference(PREF_INTERVAL);
        fromTimePrf =  findPreference(PREF_START_TIME);
        toTimePref =  findPreference(PREF_END_TIME);
        notiEnablePref.setOnPreferenceChangeListener(this);
        intervalPref.setOnPreferenceChangeListener(this);
        soundPref.setOnPreferenceChangeListener(this);
        initSummaries();



        fromTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
              showTimeDialog(FROM_KEY);
                return false;
            }
        });
        toTimePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showTimeDialog(TO_KEY);
                return false;
            }
        });

    }

    private void initSummaries() {
       boolean isNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PREF_IS_ENABLED, false);
        boolean isSoundEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PREF_SOUND, false);
        int intervalNum = getPreferenceManager().getSharedPreferences().getInt(PREF_INTERVAL,2);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
         String from = sharedPreferences.getString(FROM_KEY,"");
        String to = sharedPreferences.getString(TO_KEY,"");

        fromTimePrf.setSummary(from+R.string.AM);
        toTimePref.setSummary(to+R.string.PM);
        notiEnablePref.setSummary(getString(isNotifEnabled));
        soundPref.setSummary(getString(isSoundEnabled));
        intervalPref.setSummary(Integer.toString(intervalNum));

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity!=null) {
            mActivity=(SettingsActivity)activity;
        }

    }

    private void showTimeDialog(String command) {
        if (command.equals(FROM_KEY)){
        int hour = fromC.get(Calendar.HOUR_OF_DAY);
        int minutes = fromC.get(Calendar.MINUTE);
          TimePickerDialog datePickerDialog =
                  new TimePickerDialog(getActivity(),timeFrom ,hour,minutes, true);
          datePickerDialog.show();
        }
        else if (command.equals(TO_KEY)){
            int hour = toC.get(Calendar.HOUR_OF_DAY);
            int minutes = toC.get(Calendar.MINUTE);
            TimePickerDialog datePickerDialog =
                    new TimePickerDialog(getActivity(),timeTo ,hour,minutes, true);
            datePickerDialog.show();

    }
    }
    TimePickerDialog.OnTimeSetListener timeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            fromC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            fromC.set(Calendar.MINUTE,minute);
            fromTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+R.string.AM);
            setPref(FROM_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));


        }

    };
    TimePickerDialog.OnTimeSetListener timeTo = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            toC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            toC.set(Calendar.MINUTE,minute);
            toTimePref.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+R.string.PM);
            setPref(TO_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }

    };
    private  String getString(boolean b){
        if (b)
            return String.valueOf(R.string.ON);
        else
        return  String.valueOf(R.string.OFF);

    }
    private void setPref(String command, String str){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(command, str);
        editor.commit();

    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PREF_IS_ENABLED:
                if(newValue.equals(true))
                preference.setSummary(R.string.ON);
                else
                    preference.setSummary(R.string.OFF);
                break;
            case PREF_SOUND:
                if(newValue.equals(true))
                    preference.setSummary(R.string.ON);
                else
                    preference.setSummary(R.string.OFF);
                break;
            case PREF_INTERVAL:
                preference.setSummary(newValue.toString());
                break;

        }

        return true;
    }






}
