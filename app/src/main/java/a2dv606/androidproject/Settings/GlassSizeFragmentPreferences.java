package a2dv606.androidproject.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import a2dv606.androidproject.R;

public class GlassSizeFragmentPreferences extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    //private final static String TAG = WaterCalcuationFragmentPreferences.class.getSimpleName();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.units_prefs);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}