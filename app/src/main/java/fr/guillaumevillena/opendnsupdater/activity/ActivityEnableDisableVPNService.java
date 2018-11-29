package fr.guillaumevillena.opendnsupdater.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;

public class ActivityEnableDisableVPNService extends AppCompatActivity {

    public static final String LAUNCH_ACTION = "fr.guillaumevillena.opendnsupdater.activityenabledisablevpnservice.launch.action";
    public static final String LAUNCH_ACTION_DEACTIVATE = "fr.guillaumevillena.opendnsupdater.activityenabledisablevpnservice.launch.action.deactivate";
    public static final String LAUNCH_ACTION_ACTIVATE = "fr.guillaumevillena.opendnsupdater.activityenabledisablevpnservice.launch.action.activate";
    public static final String LAUNCH_ACTION_TOGGLE = "fr.guillaumevillena.opendnsupdater.activityenabledisablevpnservice.launch.action.toggle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        String action = i.getExtras().getString(LAUNCH_ACTION);
        if (action != null) {
            if (action.equals(LAUNCH_ACTION_ACTIVATE)) {
                OpenDnsUpdater.activateService(this);
            } else if (action.equals(LAUNCH_ACTION_DEACTIVATE)) {
                OpenDnsUpdater.deactivateService(this);
            } else if (action.equals(LAUNCH_ACTION_TOGGLE)) {
                OpenDnsUpdater.switchService();
            }
        }

        finish();
    }

}
