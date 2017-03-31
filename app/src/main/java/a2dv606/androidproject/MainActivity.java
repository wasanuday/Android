package a2dv606.androidproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.Calendar;

import a2dv606.androidproject.WaterDrunkHistory.DateLogActivity;
import a2dv606.androidproject.Settings.SettingsActivity;
import a2dv606.androidproject.OutlinesFragments.OutlineActivity;

public class MainActivity extends Activity  implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    ImageButton logButton, chartButton, settingButton,outlineButton;
    Button addDrink, otherSize,cancel,glassButton,bottleButton, setButton;
    Dialog addDrinkdialog, numberBickerDialog;
    LinearLayout mainLayout;
    NumberPicker numberPicker;
    private AppService service = null;

    private ServiceConnection connection = new ServiceConnection() {
        //@Override  // Called when connection is made
        public void onServiceConnected(ComponentName cName, IBinder binder) {
            service = ((AppService.AppBinder)binder).getService();
        }
        //@Override   //
        public void onServiceDisconnected(ComponentName cName) {
            service = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        addDrinkdialog = new Dialog(MainActivity.this);
        numberBickerDialog =new Dialog(MainActivity.this);
        addDrinkdialog.setContentView(R.layout.add_drink_dialog);
        numberBickerDialog.setContentView(R.layout.number_picker_dialog);
        numberPicker=(NumberPicker) numberBickerDialog.findViewById(R.id.numberPicker);
        setButton = (Button) numberBickerDialog.findViewById(R.id.set_button);

        setNumberPickerFormat();
        mainLayout= (LinearLayout) findViewById(R.id.main_view);
        logButton =(ImageButton)findViewById(R.id.log_button);
        chartButton =(ImageButton)findViewById(R.id.chart_button);
        settingButton =(ImageButton)findViewById(R.id.setting_button);
        outlineButton =(ImageButton)findViewById(R.id.outline_button);
        addDrink =(Button)findViewById(R.id.add_drink_button);
        otherSize = (Button) addDrinkdialog.findViewById(R.id.other_size_button);
        cancel = (Button) addDrinkdialog.findViewById(R.id.cancel_button);
         bottleButton= ( Button)addDrinkdialog.findViewById(R.id.water_bottle_button);
        glassButton=  (Button) addDrinkdialog.findViewById(R.id.water_glass_button);

        bottleButton.setOnClickListener(this);
        glassButton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        otherSize.setOnClickListener(this);
        logButton.setOnClickListener(this);
        chartButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        outlineButton.setOnClickListener(this);
        addDrink.setOnClickListener(this);
        setButton.setOnClickListener(this);
        checkAppFirstTimeRun();
      }

    private void checkAppFirstTimeRun() {

        // get shared preferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // first time run?
        if (pref.getBoolean("first_time_run", true)) {
            System.out.println("first time lunched");
            // start the preferences activity
            setupAppDB();
            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            //get the preferences editor
            SharedPreferences.Editor editor = pref.edit();
            // avoid for next run
            editor.putBoolean("first_time_run", false);
            editor.commit();
        }


    }


    private void setupAppDB() {

        Calendar calendar = Calendar.getInstance();
        /*  set calendar time the beganing of  day    */
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
            /* broadcast to db broadcast receiver   */
        Intent mIntent = new Intent(getApplicationContext(),DBBroadcastReceiver.class);
        /* put extra to setup app (0 am) */
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(getApplicationContext(),90,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        /*repeat alarm every day at 0.1 am clock */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        System.out.println("db alarm setup");
    }


    private void bindToService(){
        Intent intent = new Intent(getApplicationContext(),AppService.class);
        MainActivity.this.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        System.out.println("Binding to SlowCountService");
    }
    private  void stopNotificationAlarm(){
        if(service!=null){
            service.stopNotificationAlarm();
        }
    }


    private void setNumberPickerFormat() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            value =value*5;
            return  value+" ml";
        }
    };
        numberPicker.setFormatter(formatter);
        numberPicker.setMaxValue(300);numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);
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
                    showAddDrinkDialog();
                    break;
                case R.id.water_bottle_button:
                    addDrink();
                    break;
                case R.id.water_glass_button:
                    addDrink();
                case R.id.cancel_button:
                    addDrinkdialog.dismiss();
                    break;
                case R.id.other_size_button:
                    addDrinkdialog.dismiss();
                    showNumberPickerDialog();
                    break;
                case R.id.set_button:
                    numberBickerDialog.dismiss();
    }
}

    private void addDrink() {
    }

    private void showAddDrinkDialog() {
        addDrinkdialog.setTitle("select container");
        addDrinkdialog.show();
    }

    private void showNumberPickerDialog() {
        numberBickerDialog.setTitle("select size");
        numberBickerDialog.show();
    }

    private void goToSettingActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void goToOutlineActivity() {
        Intent intent2 = new Intent(getApplicationContext(), OutlineActivity.class);
        startActivityForResult(intent2, 0);
    }

    private void goTolChartActivity() {
        stopNotificationAlarm();
    }

    private void goToLogActivity() {
        Intent intent4 = new Intent(getApplicationContext(), DateLogActivity.class);
        startActivityForResult(intent4, 0);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
    }

}
