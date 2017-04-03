package a2dv606.androidproject.WaterDrunkHistory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.R;

public class ChartActivity extends AppCompatActivity {
    BarChart barChart;
    List<DateLog> values;
    DrinkDataSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Weekly graph");

        setContentView(R.layout.activity_chart);
        barChart = (BarChart) findViewById(R.id.chart);

        db = new DrinkDataSource(this);
        db.open();
        values= db.getAllDates();

        BarDataSet barDataSet = new BarDataSet(getYAxisValues(),"Water consuming");
        BarData barData = new BarData(getXAxisValues(),barDataSet);
        barChart.setData(barData);
        barChart.setDescription("Weekly history of water drinking");
    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=0;i< values.size();i++) {
            xAxis.add(values.get(i).getDate());
        }
        return xAxis;
    }
    private ArrayList<BarEntry> getYAxisValues() {
        ArrayList<BarEntry> yAxis = new ArrayList<>();
        for(int i=0;i< values.size();i++) {
        BarEntry v1e1 = new BarEntry(values.get(i).getWaterDrunk(), i);
            yAxis.add(v1e1);
        }
        return yAxis;
    }




}

