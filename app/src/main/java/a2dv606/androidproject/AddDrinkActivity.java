package a2dv606.androidproject;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class AddDrinkActivity extends Activity  implements NumberPicker.OnValueChangeListener{
    private static TextView tv;
    static Dialog d ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
       // tv = (TextView) findViewById(R.id);
        Button b = (Button) findViewById(R.id.other_size_button);
        b.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                show();
            }

            
        });
    }

    private void show() {

        final Dialog d = new Dialog(AddDrinkActivity.this);
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
}
