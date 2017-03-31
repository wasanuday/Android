package a2dv606.androidproject.Settings;

import android.content.Intent;
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
    private static final int NotificationSettingsCode=1;
    private int UnitsSettingsCode=2;
    private int waterCalculationCode=3;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case NotificationSettingsCode:
                resetNotificationAlarm();
                break;

        }

    }

    private void resetNotificationAlarm() {

    }


}
