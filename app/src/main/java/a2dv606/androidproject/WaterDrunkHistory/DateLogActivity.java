package a2dv606.androidproject.WaterDrunkHistory;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.R;

public class DateLogActivity extends AppCompatActivity {
  ArrayList<DateLog> values = new ArrayList<DateLog>();
    ArrayAdapter<DateLog> adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_log_activity);
        setTitle("History");
        values.add(new DateLog(3000,1000,getDate()));
        values.add(new DateLog(3000,2500,getDate()));
        values.add(new DateLog(3000,2000,getDate()));


        listView = (ListView) findViewById(R.id.log_list);
        adapter = new myListAdapter();
        listView.setAdapter(adapter);


    }

    private class myListAdapter extends ArrayAdapter<DateLog> {

        public myListAdapter() {
            super(DateLogActivity.this, R.layout.date_log_raw, values);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.date_log_raw, parent, false);
            }
            final DateLog dateLog = values.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TimeLogActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Commands.DateLogItem,dateLog.getDate());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }

            });
            ImageButton shareButton= (ImageButton)itemView.findViewById(R.id.forward);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            TextView date = (TextView) itemView
                    .findViewById(R.id.date);
           date.setText(dateLog.getDate());

            TextView waterLog = (TextView) itemView
                    .findViewById(R.id.water_drunk);

           int waterDrunk= dateLog.getWaterDrunk();
            int waterNeed= dateLog.getWaterNeed();
            waterLog.setText(dateLog.getWaterInLiter(waterDrunk)+"/"+ dateLog.getWaterInLiter(waterNeed)+"L");
            return itemView;
        }
    }



    private String getDate(){
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        Date today = Calendar.getInstance().getTime();
       return df.format(today);
    }
}
