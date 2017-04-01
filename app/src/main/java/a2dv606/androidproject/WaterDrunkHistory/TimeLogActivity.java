package a2dv606.androidproject.WaterDrunkHistory;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainActivity;
import a2dv606.androidproject.Model.TimeLog;
import a2dv606.androidproject.R;
import a2dv606.androidproject.dialogs.TimePickerFragment;

public class TimeLogActivity extends AppCompatActivity  implements View.OnClickListener, NumberPicker.OnValueChangeListener , TimePickerFragment.OnTimePickedListener {

    private List<TimeLog> values;
    private ArrayAdapter<TimeLog> adapter;
    private ListView listView;
    private DrinkDataSource db;
    TextView waterLog;
    NumberPicker numberPicker,numberPicker2;
    Dialog addDrinkdialog, addDrinkdialog2, numberBickerDialog,numberPickerDialog2;
    Button otherSize, cancel, glassButton, bottleButton, setButton,setButton2,otherSize2, cancel2, glassButton2, bottleButton2;
    TimePicker timePicker;
    int glassSize = 240;
    int bottleSize=1500;
    TimeLog t;
    int ID=0;
    int pickerValue=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_log_activity);



        Bundle bundle = getIntent().getExtras();
        String date = bundle.get(Commands.DateLogItem).toString();
        setTitle(date);

         t = new TimeLog();

        db = new DrinkDataSource(this);
        db.open();
        values = db.getAllTimes();

        listView = (ListView) findViewById(R.id.time_log_list);
        adapter = new myListAdapter(this);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);



        addDrinkdialog = new Dialog(this);
        addDrinkdialog2 = new Dialog(this);
        numberBickerDialog = new Dialog(this);
        numberPickerDialog2 = new Dialog(this);


        initializeViews();

//        setNumberPickerFormat();
        setNumberPickerFormat2();


    }


    private void setNumberPickerFormat() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                value = value * 5;
                return value + " ml";
            }
        };
        numberPicker.setFormatter(formatter);
        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);

        numberPicker2.setFormatter(formatter);
        numberPicker2.setMaxValue(300);
        numberPicker2.setMinValue(1);
        numberPicker2.setWrapSelectorWheel(false);
        numberPicker2.setOnValueChangedListener(this);

    }

    private void setNumberPickerFormat2() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                value = value * 5;
                return value + " ml";
            }
        };


        numberPicker2.setFormatter(formatter);
        numberPicker2.setMaxValue(300);
        numberPicker2.setMinValue(1);
        numberPicker2.setWrapSelectorWheel(false);
        numberPicker2.setOnValueChangedListener(this);

    }



    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        pickerValue= picker.getValue();
        Log.i("value is", "" + newVal);
    }

    @Override
    public void onClick(View view) {
        int size=0;
        int id = view.getId();
        switch (id) {
            case R.id.water_bottle_button:
                addDrinkdialog.dismiss();
                showTimePickerDialog();
                db.createTime(bottleSize,t.getDate(),t.getTime());
                size=bottleSize;
                break;

            case R.id.water_bottle_button2:
                db.update(ID, bottleSize);
                size=bottleSize;
                addDrinkdialog2.dismiss();
                break;

            case R.id.water_glass_button:
                addDrinkdialog.dismiss();
                showTimePickerDialog();
                db.createTime(glassSize,t.getDate(),t.getTime());
                size=glassSize;
                break;

            case R.id.water_glass_button2:
                db.update( ID, glassSize);
                size=glassSize;
                addDrinkdialog2.dismiss();
                break;

            case R.id.cancel_button:
                addDrinkdialog.dismiss();
                break;
            case R.id.cancel_button2:
                addDrinkdialog2.dismiss();
                break;
            case R.id.other_size_button:
                addDrinkdialog.dismiss();
                showNumberPickerDialog();
                break;
            case R.id.other_size_button2:
                addDrinkdialog2.dismiss();
                showNumberPickerDialog2();
                break;

            case R.id.set_button:

                db.createTime(pickerValue,t.getDate(),t.getTime());
                size=pickerValue;
                numberBickerDialog.dismiss();
                break;
            case R.id.set_button2:

                db.update( ID, pickerValue);
                size=pickerValue;
                numberPickerDialog2.dismiss();
                break;
        }

        Intent intent = new Intent();
        intent.putExtra("add", size);
        setResult(RESULT_OK, intent);

        intent.putExtra("update", size);
        setResult(CONTEXT_INCLUDE_CODE, intent);


    }






    private void showAddDrinkDialog() {
        addDrinkdialog.setTitle("select container");
        addDrinkdialog.show();
    }

    private void showAddDrinkDialog2() {
        addDrinkdialog2.setTitle("select container");
        addDrinkdialog2.show();
    }



    private void showNumberPickerDialog() {
        numberBickerDialog.setTitle("select size");
        numberBickerDialog.show();
    }

    private void showNumberPickerDialog2() {
        numberPickerDialog2.setTitle("select size");
        numberPickerDialog2.show();
    }
    public void showTimePickerDialog() {

        Integer hour =  0;
        Integer minute = 0;
        Integer layoutId=R.layout.time_picker_dialog;

        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);

        DialogFragment fragment = new TimePickerFragment();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), Integer.toString(layoutId));
    }


    @Override
    public void onTimePicked(int textId, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        t.setTime(String.valueOf(calendar.getTimeInMillis()));

        Intent intent = new Intent();
        intent.putExtra("result1", glassSize);
        setResult(RESULT_OK, intent);

    }


    private class myListAdapter extends ArrayAdapter<TimeLog> {


        public myListAdapter(Context context) {
            super(context, R.layout.time_log_raw, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.time_log_raw, parent, false);
            }
            final TimeLog timeLog = values.get(position);

            TextView date = (TextView) itemView
                    .findViewById(R.id.time);
            date.setText(timeLog.getTime());

            waterLog = (TextView) itemView
                    .findViewById(R.id.amount);
            waterLog.setText(timeLog.getAmount() + " ml");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimeLogActivity.this);
                    builder.setMessage("Select action");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           ID= (int) values.get(position).getID();
                            showAddDrinkDialog2();
                        }
                    });

                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TimeLog time = values.get(position);
                            Intent intent = new Intent();
                            int removedAmount= 0 - time.getAmount();
                            intent.putExtra("remove",removedAmount);
                            setResult(DEFAULT_KEYS_DIALER, intent);


                            db.delete(time);
                            adapter.remove(time);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            return itemView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_drink:
                showAddDrinkDialog();
                return true;

            case R.id.sort_by_amount_asc:
                db.sortByAmountAsc();
                break;

            case R.id.sort_by_amount_desc:
                db.sortByAmountDesc();
                break;

            case R.id.sort_by_time_ASC:
                db.sortByTimeAsc();
                break;

            case R.id.sort_by_time_desc:
                db.sortByTimeDesc();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeViews() {
        addDrinkdialog.setContentView(R.layout.add_drink_dialog);
        addDrinkdialog2.setContentView(R.layout.update_drink_dialog);
        numberBickerDialog.setContentView(R.layout.number_picker_dialog);
        numberPicker = (NumberPicker) numberPickerDialog2.findViewById(R.id.numberPicker2);
        numberPickerDialog2.setContentView(R.layout.numberpicker_dialog_update);
        numberPicker2 = (NumberPicker) numberPickerDialog2.findViewById(R.id.numberPicker2);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        setButton = (Button) numberBickerDialog.findViewById(R.id.set_button);
        setButton2 = (Button) numberPickerDialog2.findViewById(R.id.set_button2);
        otherSize = (Button) addDrinkdialog.findViewById(R.id.other_size_button);
        cancel = (Button) addDrinkdialog.findViewById(R.id.cancel_button);
        bottleButton = (Button) addDrinkdialog.findViewById(R.id.water_bottle_button);
        glassButton = (Button) addDrinkdialog.findViewById(R.id.water_glass_button);


        otherSize2 = (Button) addDrinkdialog2.findViewById(R.id.other_size_button2);
        cancel2 = (Button) addDrinkdialog2.findViewById(R.id.cancel_button2);
        bottleButton2 = (Button) addDrinkdialog2.findViewById(R.id.water_bottle_button2);
        glassButton2 = (Button) addDrinkdialog2.findViewById(R.id.water_glass_button2);


        bottleButton.setOnClickListener(this);
        glassButton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setButton.setOnClickListener(this);
        setButton2.setOnClickListener(this);
        otherSize.setOnClickListener(this);

        bottleButton2.setOnClickListener(this);
        glassButton2.setOnClickListener(this);
        cancel2.setOnClickListener(this);
        otherSize2.setOnClickListener(this);
    }

}