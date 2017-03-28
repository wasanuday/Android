package a2dv606.androidproject.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import a2dv606.androidproject.R;

/**
 * Fragment for demoing module alobar-preference
 */
public class UnitsFragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = UnitsFragmentPrefs.class.getSimpleName();

    // must be the same keys as used in /res/xml/water_calculation_prefs.xmlnk_prefs.xml


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.units_prefs);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Called when a Preference has been changed by the user. This is called before the state of the
     * Preference is about to be updated and before the state is persisted. Return true to persist
     * the changed value, or false to ignore the change.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        // show that a preference is about to be updated
        Log.i(TAG, String.format("onPreferenceChange(key: %s, newValue: %s)",
                preference.getKey(), newValue.toString()));
return true;
    }
    /**
     * Called when a shared preference is changed, added, or removed. This may be called even if a
     * preference is set to its existing value.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // show when a preference is actually being persisted.
        Log.i(TAG, String.format("onSharedPreferenceChanged(key: %s)", key));
    }
}