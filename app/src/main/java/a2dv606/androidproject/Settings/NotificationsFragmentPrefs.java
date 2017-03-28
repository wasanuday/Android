package a2dv606.androidproject.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import a2dv606.androidproject.R;

/**
 * Created by Hussain on 3/28/2017.
 */

public class NotificationsFragmentPrefs extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private final static String PREF_HEADER_NAME_NOTIFICATION = "notifications_header";
    private final static String PREF_HEADER_NAME_WATER_CALCULATION = "water_calculation_header";
    private final static String PREF_HEADER_NAME_GLASS_SIZE = "glass_size_header";
    private final static String PREF_HEADER_NAME_SOUND = "sound_header";


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.notification_prefs);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
