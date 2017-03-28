package a2dv606.androidproject.Settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import a2dv606.androidproject.R;

public class SettingsActivity extends PreferenceActivity {

    static Toolbar bar ;
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
      bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBuildHeaders(List<Header> target) {
     loadHeadersFromResource(R.xml.pref_headers, target);

    }
    @Override
    protected boolean isValidFragment (String fragmentName) {
        return (UnitsFragmentPrefs.class.getName().equals(fragmentName)
                ||NotificationsFragmentPrefs.class.getName().equals(fragmentName)||
                WaterCalculationFragmentPrefs.class.getName().equals(fragmentName));
    }

}
     /*
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new NotificationFragmentPreferences())
                .commit();
    }
}
*/