package a2dv606.androidproject.WaterDrunkHistory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainActivity;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.Model.TimeLog;
import a2dv606.androidproject.R;
import a2dv606.androidproject.dialogs.TimePickerFragment;

public class TimeLogActivity extends AppCompatActivity  implements View.OnClickListener, NumberPicker.OnValueChangeListener , TimePickerFragment.OnTimePickedListener {

    private List<TimeLog> values;
    private ArrayAdapter<TimeLog> adapter;
    private ListView listView;
    private DrinkDataSource db;
    TextView waterLog, dateTv;
    NumberPicker numberPicker,numberPicker2;
    Dialog addDrinkdialog, addDrinkdialog2, numberpickerDialog,numberPickerDialog2;
    Button otherSize, cancel, glassButton, bottleButton, setButton,setButton2,otherSize2, cancel2, glassButton2, bottleButton2;
    TimePicker timePicker;
    int glassSize = 240;
    int bottleSize=1500;
    int removedValue=0;
    int ID=0;
    int pickerValue;
    String sortBy;
    int amountToInsert=0;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_log_activity);



        Bundle bundle = getIntent().getExtras();
        date = bundle.get(Commands.DateLogItem).toString();
        setTitle(date);


        db = new DrinkDataSource(this);
        db.open();

        values = db.getAllTimes( db.sortByTimeDesc(),date);

        listView = (ListView) findViewById(R.id.time_log_list);
        adapter = new myListAdapter(this);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);



        addDrinkdialog = new Dialog(this);
        addDrinkdialog2 = new Dialog(this);
        numberpickerDialog = new Dialog(this);
        numberPickerDialog2 = new Dialog(this);


        initializeViews();

        setNumberPickerFormat();
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
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);




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
        pickerValue=picker.getValue()*5;
                Log.i("value is", "" + newVal);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.water_bottle_button:
               addBottle();
                break;
            case R.id.water_bottle_button2:
            addBottle2();
                break;
            case R.id.water_glass_button:
              addGlass();
                break;
            case R.id.water_glass_button2:
                addGlass2();
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
            addFromNumberPiker();
                break;
            case R.id.set_button2:
               addFromNumberPiker2();
                break;
        }

    }


    private void  addFromNumberPiker() {
        amountToInsert=pickerValue;
        numberpickerDialog.dismiss();
        showTimePickerDialog();

    }

    private void addGlass(){
        amountToInsert=glassSize;
        addDrinkdialog.dismiss();
        showTimePickerDialog();

}

    private void addBottle() {
        amountToInsert=bottleSize;
        addDrinkdialog.dismiss();
        showTimePickerDialog();

    }

    private void  addFromNumberPiker2() {
        db.updateDrinkingAmount(db.getDrinkingAmount(), 0- removedValue,date);
        db.update(ID,pickerValue);
        db.updateDrinkingAmount(db.getDrinkingAmount(), pickerValue,date);
        numberPickerDialog2.dismiss();
        intiTextViews();
    }

    private void addGlass2(){
        db.updateDrinkingAmount(db.getDrinkingAmount(), 0- removedValue,date);
        db.update(ID,glassSize);
        db.updateDrinkingAmount(db.getDrinkingAmount(), glassSize,date);
        addDrinkdialog.dismiss();
        intiTextViews();

    }
    private void addBottle2() {
        db.updateDrinkingAmount(db.getDrinkingAmount(), 0- removedValue,date);
        db.update(ID,bottleSize);
        db.updateDrinkingAmount(db.getDrinkingAmount(), bottleSize,date);
        addDrinkdialog.dismiss();
        intiTextViews();

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
        numberpickerDialog.setTitle("select size");
        numberpickerDialog.show();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa", Locale.getDefault());

        Calendar now = Calendar.getInstance();

        if(calendar.after(now)){

            AlertDialog alertDialog = new AlertDialog.Builder(TimeLogActivity.this).create();
            alertDialog.setTitle("warning !");
            alertDialog.setMessage("This chosen time is after than the current time, please chose earlier time !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            String addedTime = dateFormat.format(calendar.getTime());
            db.createTime(amountToInsert, date, addedTime);
            db.updateDrinkingAmount(db.getDrinkingAmount(), amountToInsert, date);
            intiTextViews();
            amountToInsert = 0;
        }


    }


    private class myListAdapter extends ArrayAdapter<TimeLog> {


        public myListAdapter(Context context) {
            super(context, R.layout.time_log_raw, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            removedValue=  values.get(position).getAmount();

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.time_log_raw, parent, false);
            }


            dateTv = (TextView) itemView.findViewById(R.id.time);
            dateTv.setText(values.get(position).getTime());

            waterLog = (TextView) itemView
                    .findViewById(R.id.amount);
            waterLog.setText(values.get(position).getAmount() + " ml");

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
                            int removedAmount= 0 - time.getAmount();
                            db.updateDrinkingAmount(db.getDrinkingAmount(), removedAmount,date);
                            db.delete(time);
                            adapter.remove(time);
                            MainActivity.addDrinkTv.setText(String.valueOf(db.getDrinkingAmount()));
                            DateLogActivity.reloadAdapter();
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
                sortBy= db.sortByAmountAsc();
                break;

            case R.id.sort_by_amount_desc:
                sortBy=  db.sortByAmountDesc();
                break;

            case R.id.sort_by_time_ASC:
                sortBy=   db.sortByTimeAsc();
                break;

            case R.id.sort_by_time_desc:
                sortBy=   db.sortByTimeDesc();
                break;
        }
        reloadAdapter();
        return super.onOptionsItemSelected(item);

    }
    private void reloadAdapter() {
        adapter.clear();
        adapter.addAll(db.getAllTimes(sortBy,date));
        listView.setAdapter(adapter);
    }

    private void intiTextViews(){
        MainActivity.addDrinkTv.setText(String.valueOf(db.getTotalDrink())+"%");
        MainActivity.choosenAmountTv.setText(String.valueOf(db.getDrinkingAmount()+" of "+ DateLog.getWaterNeed()));
        adapter.clear();
        adapter.addAll(db.getAllTimes(db.sortByTimeDesc(),date));
        listView.setAdapter(adapter);
        DateLogActivity.reloadAdapter();
    }

    private void initializeViews() {
        addDrinkdialog.setContentView(R.layout.add_drink_dialog);
        addDrinkdialog2.setContentView(R.layout.update_drink_dialog);
        numberpickerDialog.setContentView(R.layout.number_picker_dialog);
        numberPicker = (NumberPicker) numberpickerDialog.findViewById(R.id.numberPicker);
        numberPickerDialog2.setContentView(R.layout.numberpicker_dialog_update);
        numberPicker2 = (NumberPicker) numberPickerDialog2.findViewById(R.id.numberPicker2);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        setButton = (Button) numberpickerDialog.findViewById(R.id.set_button);
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