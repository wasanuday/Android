package a2dv606.androidproject.WaterDrunkHistory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.Database.DrinkDbHelper;
import a2dv606.androidproject.MainActivity;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.Model.TimeLog;
import a2dv606.androidproject.R;

public class DateLogActivity extends AppCompatActivity {
  static List<DateLog> values ;
    int waterDrunk;
    static ArrayAdapter<DateLog> adapter;
    DateLog dateLog;
    TextView waterLog,dateTv;
    static ListView listView;
    private static DrinkDataSource db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_log_activity);
        setTitle("History");


        db = new DrinkDataSource(this);
        db.open();

        dateLog= new DateLog();
        values = db.getAllDates();

        listView = (ListView) findViewById(R.id.log_list);
        adapter = new myListAdapter();
        listView.setAdapter(adapter);


    }

    private class myListAdapter extends ArrayAdapter<DateLog> {

        public myListAdapter() {
            super(DateLogActivity.this, R.layout.date_log_raw, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.date_log_raw, parent, false);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TimeLogActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Commands.DateLogItem,dateFormat(values.get(position).getDate()));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }

            });
            ImageButton shareButton = (ImageButton) itemView.findViewById(R.id.forward);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

             dateTv = (TextView) itemView.findViewById(R.id.date);
             dateTv.setText(dateFormat(values.get(position).getDate()));

            waterLog = (TextView) itemView.findViewById(R.id.water_drunk);
            waterDrunk = values.get(position).getWaterDrunk();
            int waterNeed =values.get(position).getWaterNeed();
            waterLog.setText(dateLog.getWaterInLiter(waterDrunk) + "/" + dateLog.getWaterInLiter(waterNeed) + "L");
            return itemView;
        }
    }
    public static void reloadAdapter() {
        adapter.clear();
        adapter.addAll(db.getAllDates());
        listView.setAdapter(adapter);
    }

    public String dateFormat(String date)  {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(d);
    }

    }