package a2dv606.androidproject.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import a2dv606.androidproject.R;

/**
 * Created by Hussain on 3/28/2017.
 */

public class WaterCalculationFragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private final static String PREF_GENDER = "key_gender";
    private final static String PREF_WEIGHT_NUMBER = "key_weight_number";
    private final static String PREF_TRAINING="key_training";
    private final static String PREF_WATER_RECOM="key_water_recommendation";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.water_calculation_prefs);
        // attach change listeners to the preference widgets
   /*     findPreference(PREF_GENDER).setOnPreferenceChangeListener(this);
        findPreference(PREF_WEIGHT_NUMBER).setOnPreferenceChangeListener(this);
        findPreference(PREF_TRAINING).setOnPreferenceChangeListener(this);
        findPreference(PREF_WATER_RECOM).setOnPreferenceChangeListener(this);
            // init the summary of the 'Give me a number' preference widget with its value
            int aNumber = getPreferenceManager().getSharedPreferences().getInt(PREF_WEIGHT_NUMBER, 0);
            Preference giveMeANumberPreference = findPreference(PREF_WEIGHT_NUMBER);
            giveMeANumberPreference.setSummary(Integer.toString(aNumber));
            */
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // show that a preference is about to be updated
        //  Log.i(TAG, String.format("onPreferenceChange(key: %s, newValue: %s)", preference.getKey(), newValue.toString()));

        // return whether to allow or block the update
        switch (preference.getKey()) {
            case PREF_GENDER:
                // This preference should always be persisted

                return true;
            case PREF_WEIGHT_NUMBER:
                //  findPreference(PREF_WATER_RECOM).setEnabled(true);
                //   findPreference(PREF_WATER_RECOM).setSummary(newValue.toString());
                preference.setSummary(newValue.toString());
                return true;
            case PREF_TRAINING:
                return true;
            case PREF_WATER_RECOM:
                // we want this preference to only be persisted when PREF_PERSIST_PREFERENCES is set to true.
                //   int weight = getPreferenceManager().getSharedPreferences().getInt(PREF_WEIGHT_NUMBER,0);
                //   boolean train=   getPreferenceManager().getSharedPreferences().getBoolean(PREF_TRAINING, false);
                //    System.out.println(weight);
                //   if (weight!=0) {
                // update the summary of the 'Give me a number' preference widget with its new value
                //      preference.setSummary(newValue.toString());
                //      return true;
                //     }
                //       return allowChange;
            default:
                return true;
        }
    }}