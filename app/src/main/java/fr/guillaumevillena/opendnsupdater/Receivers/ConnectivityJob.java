package fr.guillaumevillena.opendnsupdater.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.guillaumevillena.opendnsupdater.AsyncTasks.AsyncTaskFinished;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.IpCheckTask;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.IpUpdateTask;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.ResultItem;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.Utils.IntentUtils;
import fr.guillaumevillena.opendnsupdater.Utils.PreferenceCodes;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {

                    Log.d(TAG, "onAvailable: The network is " + network.toString());
                    handleConnectivityChange();

                }
            });
        } else {
            registerReceiver(connectivityChange = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleConnectivityChange();
                }
            }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

    private void handleConnectivityChange() {


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        if (!prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false)) {
            Log.d(TAG, "handleConnectivityChange: Auto update disabled, ignoring event.");
            return;
        }

        // Calls handleConnectivityChange(boolean connected, int type)

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        Log.d(TAG, "handleConnectivityChange: The current network is " + activeNetwork.toString());


        IntentUtils.sendActionUpdateNetworkInterface(this.getApplicationContext(), activeNetwork.getTypeName());

        final String username = prefs.getString(PreferenceCodes.OPENDNS_USERNAME, null);
        final String password = prefs.getString(PreferenceCodes.OPENDNS_PASSWORD, null);
        final String network = prefs.getString(PreferenceCodes.OPENDNS_NETWORK, null);

        final Boolean shouldCreateNotification = prefs.getBoolean(PreferenceCodes.APP_NOTIFY, false);

        if (username == null || password == null || network == null) {
            Log.w(TAG, "handleConnectivityChange: Ignoring empty value", null);
            return;
        }

        final IpUpdateTask ipUpdateTask = new IpUpdateTask();
        ipUpdateTask.setFinishListener(new AsyncTaskFinished() {
            @Override
            public void onFinished(ResultItem item) {
                Log.d(TAG, "onFinished: UpdateFinished" + item);

                if (item.getState()) {
                    if (shouldCreateNotification)
                        createTimedNotification(item.getState());

                }


            }
        });
        ipUpdateTask.executeOnExecutor(THREAD_POOL_EXECUTOR, username, password, network);


        IpCheckTask ipCheckTask = new IpCheckTask();
        ipCheckTask.setFinishListener(new AsyncTaskFinished() {
            @Override
            public void onFinished(ResultItem item) {
                Log.d(TAG, "onFinished: We have finished getting the ip :D ");
                if (item.getState()) {
                    IntentUtils.sendActionUpdateNetworkIP(ConnectivityJob.this.getApplicationContext(), (String) item.getResultValue());
                }
            }
        });

        ipCheckTask.executeOnExecutor(THREAD_POOL_EXECUTOR);

    }

    private void createTimedNotification(Boolean state) {

        Log.d(TAG, "createTimedNotification: Creating notification ");


        Context context = this.getApplicationContext();

        final NotificationManager mNotification = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);


        Notification.Builder builder = new Notification.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setTicker(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString((state) ? R.string.ip_address_updated : R.string.ip_address_updated_error));
        mNotification.notify(0, builder.build());


        Runnable task = new Runnable() {
            public void run() {
                mNotification.cancel(0);
            }
        };
        final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        worker.schedule(task, 5, TimeUnit.SECONDS);


    }


}