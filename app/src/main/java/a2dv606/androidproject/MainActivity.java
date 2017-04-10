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
import android.widget.TextView;

import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.R;
import a2dv606.androidproject.Database.DrinkDataSource;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import a2dv606.androidproject.Chart.ChartActivity;
import a2dv606.androidproject.Settings.PreferenceKey;
import a2dv606.androidproject.WaterDrunkHistory.DateLogActivity;
import a2dv606.androidproject.Settings.SettingsActivity;
import a2dv606.androidproject.OutlinesFragments.OutlineActivity;

public class MainActivity extends Activity  implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    ImageButton logButton, chartButton, settingButton,outlineButton;
    Button addDrink, otherSize,cancel,glassButton,bottleButton, setButton;
    Dialog addDrinkdialog, numberBickerDialog;
    LinearLayout mainLayout;
    NumberPicker numberPicker;
    public static  TextView addDrinkTv,choosenAmountTv;
    private int glassSize;
    private int bottleSize;
    DrinkDataSource db;
    int pickerValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        addDrinkTv=(TextView) findViewById(R.id.add_drink_text);
        choosenAmountTv=(TextView) findViewById(R.id.choosen_drink_text);
        mainLayout= (LinearLayout) findViewById(R.id.main_view);
        addDrinkdialog = new Dialog(MainActivity.this);
        numberBickerDialog =new Dialog(MainActivity.this);
        db= new DrinkDataSource(this);
        db.open();
        initializeViews();
        loadContainerSizePrefs();
        LoadWaterNeedPrefs();
        setNumberPickerFormat();
        checkAppFirstTimeRun();

      }

    private int getWaterNeedFromPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int waterNeed= prefs.getInt(PreferenceKey.PREF_WATER_RECOM,0);
        return waterNeed;

    }
    private int LoadWaterNeedPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int waterNeed= prefs.getInt(PreferenceKey.PREF_WATER_RECOM,0);
        // update water need in  db.
        choosenAmountTv.setText(String.valueOf(db.getDrinkingAmount()+" of "+waterNeed+ " ml"));
        return waterNeed;

    }


    private void checkAppFirstTimeRun() {

        // get shared preferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // first time run?
        if (pref.getBoolean("first_time_run", true)) {
            System.out.println("first time lunched");
            db.create(0,getWaterNeedFromPrefs(),new Date().toString());
            setupAppDBBroadcastReciever();

            startActivityForResult(new Intent(getBaseContext(), SettingsActivity.class),0);
            //get the preferences editor
            SharedPreferences.Editor editor = pref.edit();
            // avoid for next run
            editor.putBoolean("first_time_run", false);
            editor.commit();
        }





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        switch (requestCode) {
            case 0:
                loadContainerSizePrefs();
                db.updateWaterNeed(getWaterNeedFromPrefs());
                break;
        }

    }

    private void loadContainerSizePrefs() {
        // db prefs
   // update water need db.

        // glass size prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String glassSizeV=  prefs.getString(PreferenceKey.PREF_GLASS_SIZE,"");
        String bottleSizeV=  prefs.getString(PreferenceKey.PREF_BOTTLE_SIZE, "");
        glassSize = Integer.valueOf(glassSizeV);
        bottleSize = Integer.valueOf(bottleSizeV);
        glassButton.setText(glassSizeV+ " ml");
        bottleButton.setText(bottleSizeV+ " ml");


    }

    private void setupAppDBBroadcastReciever() {

        Calendar calendar = Calendar.getInstance();
        /*  set calendar time the beganing of  day
         *  */
     //       calendar.add(Calendar.DAY_OF_YEAR, 1);
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
        System.out.println("db alarm setup"+ new Date());
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
        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(0);
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
                   addBottle();
                    break;
                case R.id.water_glass_button:
                    addGlass();
                    break;
                case R.id.cancel_button:
                    addDrinkdialog.dismiss();
                    break;
                case R.id.other_size_button:
                    addDrinkdialog.dismiss();
                    showNumberPickerDialog();
                    break;
                case R.id.set_button:
                    addFromNumberPiker();
                    break;
    }
}
    private void  addFromNumberPiker() {
        db.createTime(pickerValue,getCurrentDate(),getCurrentTime());
        db.updateCurrentDrinkingAmount(db.getDrinkingAmount(),pickerValue);
        addDrinkTv.setText(String.valueOf(db.getTotalDrink())+"%");
        choosenAmountTv.setText(String.valueOf(db.getDrinkingAmount()+" of "+ DateLog.getWaterNeed()));
        numberBickerDialog.dismiss();
    }

    private void addGlass(){
       db.createTime(glassSize,getCurrentDate(),getCurrentTime());
      // db.create(240,3300,getCurrentDate());
        db.updateCurrentDrinkingAmount(db.getDrinkingAmount(),glassSize);
        addDrinkTv.setText(String.valueOf(db.getTotalDrink())+"%");
        choosenAmountTv.setText(String.valueOf(db.getDrinkingAmount()+" of "+ DateLog.getWaterNeed()));
        addDrinkdialog.dismiss();
    }

    private void addBottle() {
        db.createTime(bottleSize,getCurrentDate(),getCurrentTime());
        db.updateCurrentDrinkingAmount(db.getDrinkingAmount(),bottleSize);
        addDrinkTv.setText(String.valueOf(db.getTotalDrink())+"%");
        choosenAmountTv.setText(String.valueOf(db.getDrinkingAmount()+" of "+ DateLog.getWaterNeed()));
        addDrinkdialog.dismiss();
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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
        pickerValue= picker.getValue()*5;
    }

    public String getCurrentDate(){
        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
      //  Date today = Calendar.getInstance().getTime();
      return df.format(new Date());
    }
    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm a", Locale.getDefault());

        return dateFormat.format(new Date());
    }

    public void  initializeViews(){
        addDrinkdialog.setContentView(R.layout.add_drink_dialog);
        numberBickerDialog.setContentView(R.layout.number_picker_dialog);
        numberPicker=(NumberPicker) numberBickerDialog.findViewById(R.id.numberPicker);
        setButton = (Button) numberBickerDialog.findViewById(R.id.set_button);
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
    }

}
