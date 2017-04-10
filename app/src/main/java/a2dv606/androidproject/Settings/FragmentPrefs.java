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

import java.text.DecimalFormat;
import java.util.Calendar;

import a2dv606.androidproject.R;

/**
 * Created by Abeer on 3/28/2017.
 */

public class FragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private SettingsActivity mActivity;
    private Context context;
    private Preference glassSizePref, bottleSizePref, intervalPref, soundPref,
            notiEnablePref,fromTimePrf, toTimePref,weightPref,trainingPref,waterRecomPref;
    private Calendar fromC = Calendar.getInstance();
    private Calendar toC = Calendar.getInstance();



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =mActivity.getApplicationContext();
        addPreferencesFromResource(R.xml.fragment_prefs);
        notiEnablePref= findPreference(PreferenceKey.PREF_IS_ENABLED);
        soundPref= findPreference(PreferenceKey.PREF_SOUND);
        intervalPref=findPreference(PreferenceKey.PREF_INTERVAL);
        fromTimePrf =  findPreference(PreferenceKey.PREF_START_TIME);
        toTimePref =  findPreference(PreferenceKey.PREF_END_TIME);
        weightPref =findPreference(PreferenceKey.PREF_WEIGHT_NUMBER);
        trainingPref= findPreference(PreferenceKey.PREF_TRAINING);
        waterRecomPref = findPreference(PreferenceKey.PREF_WATER_RECOM);
        glassSizePref =  findPreference(PreferenceKey.PREF_GLASS_SIZE);
        bottleSizePref =  findPreference(PreferenceKey.PREF_BOTTLE_SIZE);


        trainingPref.setOnPreferenceChangeListener(this);
        weightPref.setOnPreferenceChangeListener(this);
        waterRecomPref.setOnPreferenceChangeListener(this);
        notiEnablePref.setOnPreferenceChangeListener(this);
        intervalPref.setOnPreferenceChangeListener(this);
        soundPref.setOnPreferenceChangeListener(this);
        glassSizePref.setOnPreferenceChangeListener(this);
        bottleSizePref.setOnPreferenceChangeListener(this);
        initSummaries();



        fromTimePrf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
              showTimeDialog(PreferenceKey.FROM_KEY);
                return false;
            }
        });
        toTimePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showTimeDialog(PreferenceKey.TO_KEY);
                return false;
            }
        });

    }
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity!=null) {
            mActivity=(SettingsActivity)activity;
        }

    }

    private void initSummaries() {
        boolean isNotifEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_IS_ENABLED, false);
        boolean isSoundEnabled = getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_SOUND, false);
        int intervalNum = getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_INTERVAL,2);
        int weight = getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_WEIGHT_NUMBER, 0);
        boolean training= getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_TRAINING,false);
        int water_reco= getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_WATER_RECOM,0);
        String glassSize = getPreferenceManager().getSharedPreferences().getString(PreferenceKey.PREF_GLASS_SIZE,"");
        String bottleSize = getPreferenceManager().getSharedPreferences().getString(PreferenceKey.PREF_BOTTLE_SIZE,"");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String from = sharedPreferences.getString(PreferenceKey.FROM_KEY,"");
        String to = sharedPreferences.getString(PreferenceKey.TO_KEY,"");


        weightPref.setSummary(Integer.toString(weight)+" kg");
        trainingPref.setSummary(getString(training));
        waterRecomPref.setSummary(Integer.toString(water_reco)+" ml");
        glassSizePref.setSummary(glassSize+ " ml");
        bottleSizePref.setSummary(bottleSize+ " ml");
        if(!from.equals(""))
           fromTimePrf.setSummary(from+" am");
        if (!to.equals(""))
        toTimePref.setSummary(to+" pm");
        notiEnablePref.setSummary(getString(isNotifEnabled));
        soundPref.setSummary(getString(isSoundEnabled));
        intervalPref.setSummary(Integer.toString(intervalNum)+" hours");

    }


    private void showTimeDialog(String command) {
        if (command.equals(PreferenceKey.FROM_KEY)){
        int hour = fromC.get(Calendar.HOUR_OF_DAY);
        int minutes = fromC.get(Calendar.MINUTE);
          TimePickerDialog datePickerDialog =
                  new TimePickerDialog(getActivity(),timeFrom ,hour,minutes, true);
          datePickerDialog.show();
        }
        else if (command.equals(PreferenceKey.TO_KEY)){
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
            fromTimePrf.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+" am");
            setTimePref(PreferenceKey.FROM_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));


        }

    };
    TimePickerDialog.OnTimeSetListener timeTo = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            toC.set(Calendar.HOUR_OF_DAY,hourOfDay);
            toC.set(Calendar.MINUTE,minute);
            toTimePref.setSummary(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+" pm");
            setTimePref(PreferenceKey.TO_KEY,String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }

    };
    private  String getString(boolean b){
        if (b)
            return "ON";
        return "OFF";

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PreferenceKey.PREF_WEIGHT_NUMBER:
                   handleWeightPref( newValue.toString(), preference);
                return true;
            case PreferenceKey.PREF_TRAINING:
                  handleTrainingPrefs(newValue.toString(),preference);
                return true;

            case PreferenceKey.PREF_WATER_RECOM:
                preference.setSummary(newValue.toString()+ " ml");
                return true;

            case  PreferenceKey.PREF_GLASS_SIZE:
                preference.setSummary(newValue.toString()+ " ml");
                return true;

            case  PreferenceKey.PREF_BOTTLE_SIZE:
                preference.setSummary(newValue.toString()+ " ml");
                return true;

            case PreferenceKey.PREF_IS_ENABLED:
              setSwichPrefSummaries(newValue.toString(), preference);
                return true;
            case PreferenceKey.PREF_SOUND:
                setSwichPrefSummaries(newValue.toString(), preference);
                return true;
            case PreferenceKey.PREF_INTERVAL:
                preference.setSummary(newValue.toString()+" hours");
                return true;

        }

        return true;
    }

    private void handleTrainingPrefs(String s, Preference preference) {
        boolean value = Boolean.valueOf(s.toString());
        setTrainingToPref(value);
        int waterNeedValue =calculateWaterReco();
        setWaterRecomToPref(waterNeedValue);
        preference.setSummary(getString(value));
        waterRecomPref.setSummary(String.valueOf(waterNeedValue));
    }

    private void handleWeightPref(String s, Preference preference) {
        setWeightToPref(Integer.valueOf(s.toString()));
        int value =calculateWaterReco();
        System.out.println(value);
        setWaterRecomToPref(value);
        preference.setSummary(s.toString());
        waterRecomPref.setSummary(String.valueOf(value));
    }

    private void setSwichPrefSummaries(String newValue, Preference preference) {
        if(newValue.equals("true"))
            preference.setSummary("ON ");
        else
            preference.setSummary("OFF ");
    }


    private void setTrainingToPref(Boolean aBoolean) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PreferenceKey.PREF_TRAINING, aBoolean);
        editor.commit();
    }

    private void setWeightToPref(int v) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferenceKey.PREF_WEIGHT_NUMBER, v);
        editor.commit();
    }
    private void setWaterRecomToPref(int v) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferenceKey.PREF_WATER_RECOM, v);
        editor.commit();
    }
    private void setTimePref(String command, String str){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(command, str);
        editor.commit();

    }

    private int calculateWaterReco() {
        int weight = getPreferenceManager().getSharedPreferences().getInt(PreferenceKey.PREF_WEIGHT_NUMBER, 0);
        boolean training= getPreferenceManager().getSharedPreferences().getBoolean(PreferenceKey.PREF_TRAINING,false);
        double waterRecom= weight/ 0.030 ;
        if (training)
            waterRecom= waterRecom+300;

        return (int)waterRecom;




    }



}
