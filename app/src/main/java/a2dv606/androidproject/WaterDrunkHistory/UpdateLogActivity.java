package a2dv606.androidproject.WaterDrunkHistory;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import a2dv606.androidproject.AddDrinkActivity;
import a2dv606.androidproject.R;

public class UpdateLogActivity extends Activity implements View.OnClickListener , NumberPicker.OnValueChangeListener{
    private static TextView tv;
    Button otherSizeButton, cancelButton,_100MlButton,_150MlButton,_200MlButton,_240MlButton,
    _330Button,_1500MlButton;
    static Dialog d ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_log);
        setTitle("Update log");
        _100MlButton = (Button) findViewById(R.id.hundred_ml_button);
        _150MlButton=(Button)findViewById(R.id.hundred_fifty_ml_button);
        _200MlButton= (Button) findViewById(R.id.two_hundred_ml_button);
        _240MlButton=  (Button) findViewById(R.id.two_hundred_forty_ml_button);
        _330Button=   (Button) findViewById(R.id.three_hundred_thirty_ml_button);
        _1500MlButton=(Button) findViewById(R.id.thousand_five_hundred);
        otherSizeButton = (Button) findViewById(R.id.other_size_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        _100MlButton.setOnClickListener(this);
        _200MlButton.setOnClickListener(this);
        _150MlButton.setOnClickListener(this);
        _330Button.setOnClickListener(this);
        _240MlButton.setOnClickListener(this);
        _1500MlButton.setOnClickListener(this);
        otherSizeButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void show() {
        final Dialog d = new Dialog(UpdateLogActivity.this);
        d.setTitle("select size");
        d.setContentView(R.layout.number_picker_dialog);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 50;
                return  temp+" ml";
            }
        };
        np.setFormatter(formatter);
        np.setMaxValue(60);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.hundred_ml_button:
                break;
            case R.id.hundred_fifty_ml_button:
                break;
            case R.id.two_hundred_ml_button:
                break;
            case R.id.two_hundred_forty_ml_button:
                break;
            case R.id.three_hundred_thirty_ml_button:
                break;
            case R.id.thousand_five_hundred:
                break;
            case R.id.other_size_button:
                show();
                break;
            case R.id.cancel_button:
                UpdateLogActivity.this.finish();
                break;

        }

    }
}
