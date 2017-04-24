package a2dv606.androidproject.WaterDrankHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;

import java.util.List;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.DateHandler;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.R;

public class DateLogActivity extends AppCompatActivity {
    static List<DateLog> values ;
    private int waterDrank, waterNeed;
    static ArrayAdapter<DateLog> adapter;
    private DateLog dateLog;
    private TextView waterLog,dateTv;
    private CircleProgress circleProgress;
    static ListView listView;

    private static DrinkDataSource db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.date_log_activity);
        setTitle("History");
        System.out.println("hereeeee in 2");

        db = new DrinkDataSource(this);
        db.open();

        dateLog= new DateLog();
        values = db.getAllDates();

        listView = (ListView) findViewById(R.id.log_list);

        adapter = new myListAdapter();
        listView.setAdapter(adapter);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private class myListAdapter extends ArrayAdapter<DateLog> {

        public myListAdapter() {
            super(DateLogActivity.this, R.layout.date_log_raw, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

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
                    bundle.putString(Commands.DateLogItem,DateHandler.getDateFormat(values.get(position).getDate()));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }

            });
            ImageButton shareButton = (ImageButton) itemView.findViewById(R.id.forward);


            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text =  "I've just been reminded to drink " +dateLog.getWaterInLiter(waterDrank)
                            +  " of " + dateLog.getWaterInLiter(waterNeed) + "L by Daily Water Tracker!";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,text);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
            circleProgress = (CircleProgress) itemView.findViewById(R.id.circle_progress);
             dateTv = (TextView) itemView.findViewById(R.id.date);
            dateTv.setText(DateHandler.dateFormat(values.get(position).getDate()));

            waterLog = (TextView) itemView.findViewById(R.id.water_drunk);
            waterDrank = values.get(position).getWaterDrunk();
            waterNeed =values.get(position).getWaterNeed();

            circleProgress.setProgress(waterDrank*100/waterNeed);
            waterLog.setText(dateLog.getWaterInLiter(waterDrank) + "/" + dateLog.getWaterInLiter(waterNeed) + "L");
            return itemView;
        }
    }
    public static void reloadAdapter() {
        adapter.clear();
        adapter.addAll(db.getAllDates());
        listView.setAdapter(adapter);
    }

    }