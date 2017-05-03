package a2dv606.androidproject.MainWindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import a2dv606.androidproject.R;

/**
 * Created by Hussain on 5/2/2017.
 */

public class SleepModeDialog extends Dialog {
    private Context context;
    private Switch aSwitch;



    public SleepModeDialog(Context context) {
        super(context);
        this.context = context;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_mode_dialog);
         aSwitch= (Switch) findViewById(R.id.swtich);
         setTitle("Sleep mode");

        if(PrefsHelper.getSleepModePrefs(context))
         aSwitch.setChecked(true);
        else
            aSwitch.setChecked(false);



        //attach a listener to check for changes in state
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    AlarmHelper.stopNotificationsAlarm(context);
                    PrefsHelper.setSleepModePrefs(context,true);
                }else{
                   PrefsHelper.setSleepModePrefs(context,false);
                    AlarmHelper.setNotificationsAlarm(context);
                }

            }
        });



    }

}
