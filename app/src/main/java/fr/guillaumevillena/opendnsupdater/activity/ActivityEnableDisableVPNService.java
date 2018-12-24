package fr.guillaumevillena.opendnsupdater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;

public class ActivityEnableDisableVPNService extends AppCompatActivity {

    public static final String LAUNCH_ACTION = "fr.guillaumevillena.opendnsupdater.activity.ActivityEnableDisableVPNService.LAUNCH_ACTION";
    public static final String LAUNCH_ACTION_DEACTIVATE = "fr.guillaumevillena.opendnsupdater.activity.ActivityEnableDisableVPNService.launch.action.deactivate";
    public static final String LAUNCH_ACTION_ACTIVATE = "fr.guillaumevillena.opendnsupdater.activity.ActivityEnableDisableVPNService.launch.action.activate";
    public static final String LAUNCH_ACTION_TOGGLE = "fr.guillaumevillena.opendnsupdater.activity.ActivityEnableDisableVPNService.launch.action.toggle";
    private static final String TAG = ActivityEnableDisableVPNService.class.getSimpleName();
    private Intent incoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.incoming = getIntent();

        String toto = this.incoming.getStringExtra(LAUNCH_ACTION);

        Log.d(TAG, "onCreate: Receievd ! " + toto + " ");

        if (toto == null) {
            finish();
            return;
        }


        String action = incoming.getStringExtra(LAUNCH_ACTION);
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
