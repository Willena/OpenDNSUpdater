package fr.guillaumevillena.opendnsupdater.backgroundJobs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by guill on 26/06/2018.
 * With the help of stack overflow : https://stackoverflow.com/questions/46163131/android-o-detect-connectivity-change-in-background
 */

public class ConnectivityJob extends JobService {

    private static final String TAG = ConnectivityJob.class.getSimpleName();
    private ConnectivityManager.NetworkCallback networkCallback;
    private BroadcastReceiver connectivityChange;
    private ConnectivityManager connectivityManager;

    @Override
    public boolean onStartJob(JobParameters job) {


        Log.d(TAG, "onStartJob: THE JOB STARTED !!!!!!");


        connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback = new ConnectivityManager.NetworkCallback() {
                // -Snip-
            });
        } else {
            registerReceiver(connectivityChange = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleConnectivityChange(!intent.hasExtra("noConnectivity"), intent.getIntExtra("networkType", -1));
                }
            }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Log.d(TAG, "onStartJob: NO active network !");
        } else {
            Log.d(TAG, "onStartJob: The current network is "  + activeNetwork.toString());
        }
        Log.d(TAG, "onStartJob: Done ! ");
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        if (networkCallback != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            connectivityManager.unregisterNetworkCallback(networkCallback);
        else if (connectivityChange != null) unregisterReceiver(connectivityChange);
        return true;
    }

    private void handleConnectivityChange(NetworkInfo networkInfo) {
        // Calls handleConnectivityChange(boolean connected, int type)
    }

    private void handleConnectivityChange(boolean connected, int type) {
        // Calls handleConnectivityChange(boolean connected, ConnectionType connectionType)
    }

    private void handleConnectivityChange(boolean connected, ConnectionType connectionType) {
        // Logic based on the new connection
    }

    private enum ConnectionType {
        MOBILE, WIFI, VPN, OTHER;
    }
}