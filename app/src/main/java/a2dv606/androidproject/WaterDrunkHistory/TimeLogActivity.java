package a2dv606.androidproject.WaterDrunkHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import a2dv606.androidproject.Model.TimeLog;
import a2dv606.androidproject.R;

public class TimeLogActivity extends AppCompatActivity {

    ArrayList<TimeLog> values = new ArrayList<TimeLog>();
    ArrayAdapter<TimeLog> adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_log_activity);
        Bundle bundle= getIntent().getExtras();
        String date =bundle.get(Commands.DateLogItem).toString();
        setTitle(date);
        values.add(new TimeLog("1:00 pm",330,getDate()));
        values.add(new TimeLog("3:30 pm",100,getDate()));
        values.add(new TimeLog("5:15 pm",240,getDate()));


        listView = (ListView) findViewById(R.id.time_log_list);
        adapter = new myListAdapter();
        listView.setAdapter(adapter);


    }

    private class myListAdapter extends ArrayAdapter<TimeLog> {

        public myListAdapter() {
            super(getApplicationContext(), R.layout.time_log_raw, values);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.time_log_raw, parent, false);
            }
            TimeLog timeLog = values.get(position);

            TextView date = (TextView) itemView
                    .findViewById(R.id.time);
            date.setText(timeLog.getTime());

            TextView waterLog = (TextView) itemView
                    .findViewById(R.id.amount);
            waterLog.setText(timeLog.getAmount()+" ml");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimeLogActivity.this);
                    builder.setMessage("Select action");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            return itemView;
        }
    }

    private String getDate(){
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_log, menu);
        return true;
    }
}
