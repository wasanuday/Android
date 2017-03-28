package a2dv606.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import a2dv606.androidproject.WaterDrunkHistory.DateLogActivity;
import a2dv606.androidproject.WaterDrunkHistory.OutlineActivity;

public class MainActivity extends Activity  implements View.OnClickListener {

    ImageButton logButton, chartButton, settingButton,outlineButton;
    Button addDrink;
    LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        mainLayout= (LinearLayout) findViewById(R.id.main_view);
        logButton =(ImageButton)findViewById(R.id.log_button);
        chartButton =(ImageButton)findViewById(R.id.chart_button);
        settingButton =(ImageButton)findViewById(R.id.setting_button);
        outlineButton =(ImageButton)findViewById(R.id.outline_button);
        addDrink =(Button)findViewById(R.id.add_drink);
        logButton.setOnClickListener(this);
        chartButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        outlineButton.setOnClickListener(this);
        addDrink.setOnClickListener(this);







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
                case R.id.add_drink:
                    goToAddDrinkActivity();
                    break;
    }
}

    private void goToAddDrinkActivity() {
        Intent intent0 = new Intent(getApplicationContext(), AddDrinkActivity.class);
        startActivityForResult(intent0, 0);
    }

    private void goToSettingActivity() {
    }

    private void goToOutlineActivity() {
        Intent intent2 = new Intent(getApplicationContext(), OutlineActivity.class);
        startActivity(intent2);
    }

    private void goTolChartActivity() {
    }

    private void goToLogActivity() {
        Intent intent4 = new Intent(getApplicationContext(), DateLogActivity.class);
        startActivityForResult(intent4, 0);
    }
    }
