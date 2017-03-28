package a2dv606.androidproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import a2dv606.androidproject.WaterDrunkHistory.DateLogActivity;
import a2dv606.androidproject.Settings.SettingsActivity;
import a2dv606.androidproject.OutlinesFragments.OutlineActivity;

public class MainActivity extends Activity  implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    ImageButton logButton, chartButton, settingButton,outlineButton;
    Button addDrink, otherSize,cancel,glassButton,bottleButton, setButton;
    Dialog addDrinkdialog, numberBickerDialog;
    LinearLayout mainLayout;
    NumberPicker numberPicker;
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
