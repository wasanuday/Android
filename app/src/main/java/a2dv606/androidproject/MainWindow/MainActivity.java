package a2dv606.androidproject.MainWindow;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import a2dv606.androidproject.Database.DrinkDataSource;


import java.util.Calendar;

import a2dv606.androidproject.Chart.ChartActivity;
import a2dv606.androidproject.R;
import a2dv606.androidproject.Settings.PreferenceKey;
import a2dv606.androidproject.WaterDrankHistory.DateLogActivity;
import a2dv606.androidproject.Settings.SettingsActivity;
import a2dv606.androidproject.OutlinesFragments.OutlineActivity;

public class MainActivity extends Activity  implements View.OnClickListener {

    private ImageButton logButton, chartButton, settingButton,outlineButton;
    private Button addDrinkButton;
    private LinearLayout mainLayout;
    public static  DonutProgress circleProgress;
    public static  TextView choosenAmountTv;
    private DrinkDataSource db;
    private BroadcastReceiver updateUIReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new DrinkDataSource(this);
        db.open();
        setupAppAlarm();
        checkAppFirstTimeRun();
        setContentView(R.layout.main_page);
        mainLayout= (LinearLayout) findViewById(R.id.main_view);
        circleProgress = (DonutProgress) findViewById(R.id.donut_progress);
        initializeViews();
        updateView();
        registerUIBroadcastReceiver();
      }

    private void checkAppFirstTimeRun() {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (pref.getBoolean("first_time_run", true)) {
            db.createDateLog(0,getWaterNeedPrefs(), DateHandler.getCurrentDate());
            startActivityForResult(new Intent(getBaseContext(), SettingsActivity.class),0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("first_time_run", false);
            editor.commit();
        }
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(updateUIReciver);
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        switch (requestCode) {
            case 0:
                db.updateWaterNeedForTodayDateLog(getWaterNeedPrefs());
                updateView();
                loadNotificationsPrefs();
                break;
        }

    }

    private void loadNotificationsPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isNotEnable= prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED,true);
        Intent myIntent= new Intent(getApplicationContext(),AppBroadcastReceiver.class);
        if (isNotEnable){
            myIntent.putExtra(AppBroadcastReceiver.EXTRA,AppBroadcastReceiver.ACTION_SCHEDULE);
            sendBroadcast(myIntent);
        }else {
            myIntent.putExtra(AppBroadcastReceiver.EXTRA,AppBroadcastReceiver.ACTION_STOP);
            sendBroadcast(myIntent);
        }
    }
    private int getWaterNeedPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       return prefs.getInt(PreferenceKey.PREF_WATER_NEED,0);

    }
    private void setupAppAlarm() {
        boolean isNotWorking =(PendingIntent.getBroadcast(getApplicationContext(), 90,
                new Intent(getApplicationContext(),AppBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) == null);
        System.out.println("working "+isNotWorking);
        if(isNotWorking) {
            Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        Intent mIntent = new Intent(getApplicationContext(),AppBroadcastReceiver.class);
        mIntent.putExtra(AppBroadcastReceiver.EXTRA,AppBroadcastReceiver.ACTION_SETUP);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(getApplicationContext(),90,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
               AlarmManager.INTERVAL_DAY,pendingIntent);
        System.out.println("db setup");}
    }






    @Override
    public void onClick(View v) {
        int id = v.getId();
            switch (id){
                case R.id.log_button:
                    goToLogActivity();
                    break;
                case R.id.chart_button:
                    goTolChartActivity();
                    break;
                case R.id.outline_button:
                goToOutlineActivity();
                    break;
                case R.id.setting_button:
                    goToSettingActivity();
                    break;
                case R.id.add_drink_button:
                  showAddDialog();
                    break;

    }
}


    private void  updateView(){
      int perValue= db.getConsumedPercentage();
       if(perValue>=100)
       { circleProgress.setProgress(100);
       }
       else
       {  circleProgress.setProgress(db.getConsumedPercentage());}
          choosenAmountTv.setText(String.valueOf(db.geConsumedWaterForToadyDateLog()+" of "+ getWaterNeedPrefs()+" ml"));
       }

    private void showAddDialog() {
        AddDialog a = new AddDialog(this, db);
        a.show();
    }




    private void goToSettingActivity() {
        startActivityForResult(new Intent(this, SettingsActivity.class),0);
    }

    private void goToOutlineActivity() {
        Intent intent2 = new Intent(getApplicationContext(), OutlineActivity.class);
        startActivity(intent2);
    }

    private void goTolChartActivity() {
        Intent intent3 = new Intent(getApplicationContext(), ChartActivity.class);
        startActivity(intent3);
    }

    private void goToLogActivity() {
        Intent intent4 = new Intent(getApplicationContext(), DateLogActivity.class);
        startActivity(intent4);
    }



    public void  initializeViews(){

        logButton =(ImageButton)findViewById(R.id.log_button);
        chartButton =(ImageButton)findViewById(R.id.chart_button);
        settingButton =(ImageButton)findViewById(R.id.setting_button);
        outlineButton =(ImageButton)findViewById(R.id.outline_button);
        choosenAmountTv=(TextView) findViewById(R.id.choosen_drink_text);
        addDrinkButton = (Button) findViewById(R.id.add_drink_button);
        logButton.setOnClickListener(this);
        chartButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        outlineButton.setOnClickListener(this);
        addDrinkButton.setOnClickListener(this);
    }

    private void registerUIBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.update.view.action");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
            }
        };
        registerReceiver(updateUIReciver,filter);
    }

}
