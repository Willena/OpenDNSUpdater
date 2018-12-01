package fr.guillaumevillena.opendnsupdater.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;

/**
 * Created by guill on 26/06/2018.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = BootBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (OpenDnsUpdater.getPrefs().getBoolean("settings_boot", false)) {
            OpenDnsUpdater.activateService(context);
            Log.i(TAG, "Triggered boot receiver");
        }

        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "onReceive: we got action : " + intent.getAction() + " witch is not " + Intent.ACTION_BOOT_COMPLETED);
            return;
        }

        Log.d(TAG, "onReceive: We have received a boot complete broadcast !! ");
        ConnectivityJob.setScheduler(context);

    }


}
