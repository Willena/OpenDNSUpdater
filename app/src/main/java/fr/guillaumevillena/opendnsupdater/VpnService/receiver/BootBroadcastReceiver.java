package fr.guillaumevillena.opendnsupdater.VpnService.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = BootBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (OpenDnsUpdater.getPrefs().getBoolean("settings_boot", false)) {
            OpenDnsUpdater.activateService(context);
            Log.i(TAG, "Triggered boot receiver");
        }
    }
}
