package a2dv606.androidproject.WaterDrankHistory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import a2dv606.androidproject.AppBroadcastReceiver;
import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.DateHandler;
import a2dv606.androidproject.MainActivity;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.Model.TimeLog;
import a2dv606.androidproject.R;
import a2dv606.androidproject.Settings.PreferenceKey;
import a2dv606.androidproject.dialogs.TimePickerFragment;

public class TimeLogActivity extends AppCompatActivity  implements View.OnClickListener, NumberPicker.OnValueChangeListener , TimePickerFragment.OnTimePickedListener {

    private List<TimeLog> values;
    private ArrayAdapter<TimeLog> adapter;
    private ListView listView;
    private DrinkDataSource db;
    TextView waterLog, dateTv;
    NumberPicker numberPicker,updateNumberPicker;
    Dialog addDrinkdialog, updateDrinkDialog, numberpickerDialog,updateNumberPickerDialog;
    Button otherSize, cancel, glassButton, bottleButton, setButton,setButtonInUpdateDialog,updateToOtherSize, cancelUpdateDialog, glassButtonInUpdateDialog, bottleButtonInUpdateDialog;
    TimePicker timePicker;
    int glassSize;
    int bottleSize;
    String containerTyp=null;
    int removedValue=0;
    int ID=0;
    int pickerValue;
    String sortBy;
    int amountToInsert=0;
    String date;
    private boolean soundEnable;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        mediaPlayer= MediaPlayer.create(this, R.raw.splash_water);



        addDrinkdialog = new Dialog(this);
        updateDrinkDialog = new Dialog(this);
        numberpickerDialog = new Dialog(this);
        updateNumberPickerDialog = new Dialog(this);


        initializeViews();
        loadContainerSizePrefs();
        loadNotificationsPrefs();

        setNumberPickerFormat();
        setNumberPickerFormatInUpdateDialog();


    }

    private void loadNotificationsPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isNotEnable = prefs.getBoolean(PreferenceKey.PREF_IS_ENABLED, true);
        soundEnable = prefs.getBoolean(PreferenceKey.PREF_SOUND, false);
        Intent myIntent = new Intent(getApplicationContext(), AppBroadcastReceiver.class);
        if (isNotEnable) {
            myIntent.putExtra("action", "schedule_notifications");
            sendBroadcast(myIntent);
        } else {
            myIntent.putExtra("action", "stop_notifications");
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
        glassButtonInUpdateDialog.setText(glassSizeV+ " ml");
        bottleButtonInUpdateDialog.setText(bottleSizeV+ " ml");


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    private void setNumberPickerFormatInUpdateDialog() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                value = value * 5;
                return value + " ml";
            }
        };


        updateNumberPicker.setFormatter(formatter);
        updateNumberPicker.setMaxValue(300);
        updateNumberPicker.setMinValue(1);
        updateNumberPicker.setWrapSelectorWheel(false);
        updateNumberPicker.setOnValueChangedListener(this);

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
                updateToBottle();
                break;
            case R.id.water_glass_button:
              addGlass();
                break;
            case R.id.water_glass_button2:
                updateToGlass();
                break;
            case R.id.cancel_button:
                addDrinkdialog.dismiss();
                break;
            case R.id.cancel_button2:
                updateDrinkDialog.dismiss();
                break;
            case R.id.other_size_button:
                addDrinkdialog.dismiss();
                showNumberPickerDialog();
                break;
            case R.id.other_size_button2:
                updateDrinkDialog.dismiss();
                showNumberPickerDialogToUpdate();
                break;
            case R.id.set_button:
            addFromNumberPiker();
                break;
            case R.id.set_button2:
                updateFromNumberPiker();
                break;
        }

    }


    private void  addFromNumberPiker() {
        amountToInsert=pickerValue;
        containerTyp="other";
        numberpickerDialog.dismiss();
        showTimePickerDialog();

    }

    private void addGlass(){
        amountToInsert=glassSize;
        containerTyp="glass";
        addDrinkdialog.dismiss();
        showTimePickerDialog();

}

    private void addBottle() {
        amountToInsert=bottleSize;
        containerTyp="bottle";
        addDrinkdialog.dismiss();
        showTimePickerDialog();

    }

    private void  updateFromNumberPiker() {
        db.updateDrinkingAmount(db.getDrinkingAmount(date), 0- removedValue,date);
        db.updateDrink(ID,pickerValue,"other");
        db.updateDrinkingAmount(db.getDrinkingAmount(date), pickerValue,date);
        if (soundEnable)
            mediaPlayer.start();
        updateNumberPickerDialog.dismiss();
        intiTextViews();
    }

    private void updateToGlass(){
        db.updateDrinkingAmount(db.getDrinkingAmount(date), 0- removedValue,date);
        db.updateDrink(ID,glassSize,"glass");
        db.updateDrinkingAmount(db.getDrinkingAmount(date), glassSize,date);
        if (soundEnable)
            mediaPlayer.start();
        updateDrinkDialog.dismiss();
        intiTextViews();


    }
    private void updateToBottle() {
        db.updateDrinkingAmount(db.getDrinkingAmount(date), 0- removedValue,date);
        db.updateDrink(ID,bottleSize,"bottle");
        db.updateDrinkingAmount(db.getDrinkingAmount(date), bottleSize,date);
        if (soundEnable)
            mediaPlayer.start();
        updateDrinkDialog.dismiss();
        intiTextViews();

    }




    private void showAddDrinkDialog() {
        addDrinkdialog.setTitle("select container");
        addDrinkdialog.show();
    }

    private void showUpdateDrinkDialog() {
        updateDrinkDialog.setTitle("select container");
        updateDrinkDialog.show();
    }



    private void showNumberPickerDialog() {
        numberpickerDialog.setTitle("select size");
        numberpickerDialog.show();
    }

    private void showNumberPickerDialogToUpdate() {
        updateNumberPickerDialog.setTitle("select size");
        updateNumberPickerDialog.show();
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
        if(date.equals(DateHandler.getCurrentFormedDate())&&calendar.after(now)){

            AlertDialog alertDialog = new AlertDialog.Builder(TimeLogActivity.this).create();
            alertDialog.setTitle("warning !");
            alertDialog.setMessage("This chosen time is after the current time, please chose earlier time !");
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
            db.createTime(amountToInsert,containerTyp, date, addedTime);
            db.updateDrinkingAmount(db.getDrinkingAmount(date), amountToInsert, date);
            if (soundEnable)
                mediaPlayer.start();
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

            ImageView typ = (ImageView) itemView.findViewById(R.id.containerTyp);
            if(values.get(position).getContainerTyp().equals("glass"))
            typ.setImageDrawable(getResources().getDrawable(R.drawable.water_glass));
            if (values.get(position).getContainerTyp().equals("bottle"))
                typ.setImageDrawable(getResources().getDrawable(R.drawable.water_bottle));
            if (values.get(position).getContainerTyp().equals("other"))
                typ.setImageDrawable(getResources().getDrawable(R.drawable.icon_water_drop));

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
                            showUpdateDrinkDialog();
                        }
                    });

                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TimeLog time = values.get(position);
                            int removedAmount= 0 - time.getAmount();
                            db.updateDrinkingAmount(db.getDrinkingAmount(date), removedAmount,date);
                            db.deleteDrink(time);


                            intiTextViews();
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
        MainActivity.circleProgress.setProgress(db.getTotalDrink());
        MainActivity.choosenAmountTv.setText(String.valueOf(db.getDrinkingAmount()+" of "+ DateLog.getWaterNeed()));


        adapter.clear();
        adapter.addAll(db.getAllTimes(db.sortByTimeDesc(),date));
        listView.setAdapter(adapter);
        DateLogActivity.reloadAdapter();
    }

    private void initializeViews() {
        addDrinkdialog.setContentView(R.layout.add_drink_dialog);
        updateDrinkDialog.setContentView(R.layout.update_drink_dialog);
        numberpickerDialog.setContentView(R.layout.number_picker_dialog);
        numberPicker = (NumberPicker) numberpickerDialog.findViewById(R.id.numberPicker);
        updateNumberPickerDialog.setContentView(R.layout.numberpicker_dialog_update);
        updateNumberPicker = (NumberPicker) updateNumberPickerDialog.findViewById(R.id.numberPicker2);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        setButton = (Button) numberpickerDialog.findViewById(R.id.set_button);
        setButtonInUpdateDialog = (Button) updateNumberPickerDialog.findViewById(R.id.set_button2);
        otherSize = (Button) addDrinkdialog.findViewById(R.id.other_size_button);
        cancel = (Button) addDrinkdialog.findViewById(R.id.cancel_button);
        bottleButton = (Button) addDrinkdialog.findViewById(R.id.water_bottle_button);
        glassButton = (Button) addDrinkdialog.findViewById(R.id.water_glass_button);


        updateToOtherSize = (Button) updateDrinkDialog.findViewById(R.id.other_size_button2);
        cancelUpdateDialog = (Button) updateDrinkDialog.findViewById(R.id.cancel_button2);
        bottleButtonInUpdateDialog = (Button) updateDrinkDialog.findViewById(R.id.water_bottle_button2);
        glassButtonInUpdateDialog = (Button) updateDrinkDialog.findViewById(R.id.water_glass_button2);


        bottleButton.setOnClickListener(this);
        glassButton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setButton.setOnClickListener(this);
        setButtonInUpdateDialog.setOnClickListener(this);
        otherSize.setOnClickListener(this);

        bottleButtonInUpdateDialog.setOnClickListener(this);
        glassButtonInUpdateDialog.setOnClickListener(this);
        cancelUpdateDialog.setOnClickListener(this);
        updateToOtherSize.setOnClickListener(this);
    }

}