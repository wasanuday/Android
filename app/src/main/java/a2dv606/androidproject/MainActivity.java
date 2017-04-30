package a2dv606.androidproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import a2dv606.androidproject.Database.DrinkDataSource;


import java.util.Calendar;
import java.util.Date;

import a2dv606.androidproject.Chart.ChartActivity;
import a2dv606.androidproject.Settings.PreferenceKey;
import a2dv606.androidproject.WaterDrankHistory.DateLogActivity;
import a2dv606.androidproject.Settings.SettingsActivity;
import a2dv606.androidproject.OutlinesFragments.OutlineActivity;

public class MainActivity extends Activity  implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    private ImageButton logButton, chartButton, settingButton,outlineButton;
    private Button addDrink, otherSize,cancel,glassButton,bottleButton, setButton;
    private Dialog addDrinkdialog, numberBickerDialog,congratulationDialog;
    private LinearLayout mainLayout;
    private NumberPicker numberPicker;
    public static  DonutProgress circleProgress;
    public static  TextView choosenAmountTv;
    private int waterNeed=2500;
    private int glassSize;
    private int bottleSize;
    private DrinkDataSource db;
    private int pickerValue=0;
    private BroadcastReceiver updateUIReciver;
    private final String GLASS="glass";
    private final String BOTTLE="bottle";
    private final String OTHER= "other";
    private boolean soundEnable;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new DrinkDataSource(this);
        db.open();
        checkAppFirstTimeRun();
        setContentView(R.layout.main_page);
        mainLayout= (LinearLayout) findViewById(R.id.main_view);
        addDrinkdialog = new Dialog(MainActivity.this);
        numberBickerDialog =new Dialog(MainActivity.this);
        congratulationDialog = new Dialog(MainActivity.this);
        congratulationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        circleProgress = (DonutProgress) findViewById(R.id.donut_progress);
        mediaPlayer= MediaPlayer.create(this, R.raw.splash_water);
        initializeViews();
        setNumberPickerFormat();

        loadContainerSizePrefs();
        loadWaterNeedPrefs();
        registerUIBroadcastReceiver();
      }

    private void checkAppFirstTimeRun() {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (pref.getBoolean("first_time_run", true)) {
            db.createDateLog(0,waterNeed,DateHandler.getCurrentDate());
            setupAppAlarm();
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
                loadContainerSizePrefs();
                loadWaterNeedPrefs();
                db.updateWaterNeedForTodayDateLog(waterNeed);
                loadNotificationsPrefs();
                break;
        }

    }

    private void loadNotificationsPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isNotEnable= prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED,true);
        soundEnable= prefs.getBoolean(PreferenceKey.PREF_SOUND,false);
        Intent myIntent= new Intent(getApplicationContext(),AppBroadcastReceiver.class);
        if (isNotEnable){
            myIntent.putExtra(AppBroadcastReceiver.EXTRA,AppBroadcastReceiver.ACTION_SCHEDULE);
            sendBroadcast(myIntent);
        }else {
            myIntent.putExtra(AppBroadcastReceiver.EXTRA,AppBroadcastReceiver.ACTION_STOP);
            sendBroadcast(myIntent);
        }


    }

    private void loadContainerSizePrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String glassSizeV=  prefs.getString(PreferenceKey.PREF_GLASS_SIZE,"250");
        String bottleSizeV=  prefs.getString(PreferenceKey.PREF_BOTTLE_SIZE, "1500");
        glassSize = Integer.valueOf(glassSizeV);
        bottleSize = Integer.valueOf(bottleSizeV);
        glassButton.setText(glassSizeV+ " ml");
        bottleButton.setText(bottleSizeV+ " ml");


    }
    private void loadWaterNeedPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        waterNeed= prefs.getInt(PreferenceKey.PREF_WATER_NEED,0);
         updateView();

    }
    private void setupAppAlarm() {
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
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
               AlarmManager.INTERVAL_DAY,pendingIntent);
        System.out.println("db setup");
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
                case R.id.imageView2:
                    congratulationDialog.dismiss();
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
        db.createTimeLog(pickerValue,OTHER,DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
        db.updateConsumedWaterForTodayDateLog(pickerValue);
         updateView();
        playSound();
        numberBickerDialog.dismiss();
    }

    private void playSound() {
        if (soundEnable)
            mediaPlayer.start();
    }

    private void addGlass(){
        db.createTimeLog(glassSize,GLASS,DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
        db.updateConsumedWaterForTodayDateLog(glassSize);
        updateView();
        playSound();
        addDrinkdialog.dismiss();
    }

    private void addBottle() {
        db.createTimeLog(bottleSize,BOTTLE,DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
        db.updateConsumedWaterForTodayDateLog(bottleSize);
         updateView();
         playSound();
        addDrinkdialog.dismiss();
    }
   private void  updateView(){
      int perValue= db.getConsumedPercentage();
       if(perValue>=100)
       { circleProgress.setProgress(100);
           if (getCongDialogPrefs()) {
               congratulationDialog.show();
               setCongDialogPrefs(false);
           }
       }
       else
       {  circleProgress.setProgress(db.getConsumedPercentage());}
       choosenAmountTv.setText(String.valueOf(db.geConsumedWaterForToadyDateLog()+" of "+waterNeed+" ml"));
       }

    public boolean getCongDialogPrefs()
    {   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean showDialog = preferences.getBoolean("show_dialog", true);
        return showDialog;
    }
    public  void setCongDialogPrefs(boolean value)
    {   SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("show_dialog", value);
        editor.commit();
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
        pickerValue= picker.getValue()*5;
    }


    public void  initializeViews(){
        addDrinkdialog.setContentView(R.layout.add_drink_dialog);
        congratulationDialog.setContentView(R.layout.congratulation_dialog);
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
     //   addDrinkTv=(TextView) findViewById(R.id.add_drink_text);
        choosenAmountTv=(TextView) findViewById(R.id.choosen_drink_text);
        ImageView CongCancel = (ImageView) congratulationDialog.findViewById(R.id.imageView2);

        bottleButton.setOnClickListener(this);
        glassButton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        CongCancel.setOnClickListener(this);
        otherSize.setOnClickListener(this);
        logButton.setOnClickListener(this);
        chartButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        outlineButton.setOnClickListener(this);
        addDrink.setOnClickListener(this);
        setButton.setOnClickListener(this);
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
