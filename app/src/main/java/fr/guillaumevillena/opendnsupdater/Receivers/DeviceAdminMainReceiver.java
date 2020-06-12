package fr.guillaumevillena.opendnsupdater.Receivers;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;

public class DeviceAdminMainReceiver extends android.app.admin.DeviceAdminReceiver {

    private static final String TAG = DeviceAdminMainReceiver.class.getSimpleName();

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.d(TAG, "onEnabled: Ok");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Log.d(TAG, "onDisableRequested: Ok");
        return "Disable has been requested ! ";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.d(TAG, "onDisabled: Ok");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle userHandle) {
        Log.d(TAG, "onPasswordChanged: Ok");
    }

}
