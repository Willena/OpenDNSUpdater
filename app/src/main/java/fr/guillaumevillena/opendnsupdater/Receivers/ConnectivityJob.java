package fr.guillaumevillena.opendnsupdater.Receivers;

import android.app.Notification;
import android.app.NotificationChannel;
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
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.greenrobot.eventbus.EventBus;

import java.util.Set;

import androidx.core.app.NotificationCompat;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.event.InterfaceUpdatedEvent;
import fr.guillaumevillena.opendnsupdater.tasks.ExternalIpFinder;
import fr.guillaumevillena.opendnsupdater.tasks.TaskFinished;
import fr.guillaumevillena.opendnsupdater.tasks.UpdateOnlineIP;
import fr.guillaumevillena.opendnsupdater.utils.ConnectivityUtil;
import fr.guillaumevillena.opendnsupdater.utils.IntentUtils;
import fr.guillaumevillena.opendnsupdater.utils.Notifications;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;

/**
 * Created by guill on 26/06/2018.
 * With the help of stack overflow : https://stackoverflow.com/questions/46163131/android-o-detect-connectivity-change-in-background
 */

public class ConnectivityJob extends JobService implements TaskFinished {

    private static final String TAG = ConnectivityJob.class.getSimpleName();
    private ConnectivityManager.NetworkCallback networkCallback;
    private BroadcastReceiver connectivityChange;
    private ConnectivityManager connectivityManager;
    private static boolean jobsStarted;
    private SharedPreferences prefs;

    public static void setScheduler(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = dispatcher.newJobBuilder().setService(ConnectivityJob.class)
                .setTag("connectivity-job").setLifetime(Lifetime.FOREVER).setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setRecurring(true).setReplaceCurrent(true).setTrigger(Trigger.executionWindow(0, 0)).build();

        dispatcher.schedule(job);
    }

    public static boolean isJobsStarted() {
        return jobsStarted;
    }

    @Override
    public boolean onStartJob(JobParameters job) {

        prefs = OpenDnsUpdater.getPrefs();

        Log.d(TAG, "onStartJob: THE JOB STARTED !!!!!!");
        jobsStarted = true;

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

        jobsStarted = false;

        if (networkCallback != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            connectivityManager.unregisterNetworkCallback(networkCallback);
        else if (connectivityChange != null) unregisterReceiver(connectivityChange);
        return true;
    }

    private void handleConnectivityChange() {

        if (!prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false)) {
            Log.d(TAG, "handleConnectivityChange: Auto update disabled, ignoring event.");
            return;
        }


        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        Log.d(TAG, "handleConnectivityChange: The current network is " + activeNetwork.toString());

        if (EventBus.getDefault().hasSubscriberForEvent(InterfaceUpdatedEvent.class)) {
            EventBus.getDefault().post(new InterfaceUpdatedEvent(ConnectivityUtil.getActiveNetworkName(getApplicationContext())));
            new ExternalIpFinder().execute();
        }

        if (prefs.getBoolean(PreferenceCodes.APP_USE_BLACK_LIST, false)) {

            String netName = ConnectivityUtil.getActiveNetworkName(getApplicationContext());
            Set<String> blacklisted = prefs.getStringSet(PreferenceCodes.APP_BLACKLIST, null);


            if (blacklisted != null && (
                    blacklisted.contains(netName)
                            || (blacklisted.contains("WIFI") && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            )) {
                Log.d(TAG, "handleConnectivityChange: The netname " + netName + "is blacklisted ! ");
                Log.d(TAG, "handleConnectivityChange: Stoping service !");
                OpenDnsUpdater.deactivateService(getApplicationContext());
                return;
            }


        }


        IntentUtils.sendActionUpdateNetworkInterface(this.getApplicationContext(), activeNetwork.getTypeName());


        new UpdateOnlineIP(this).execute();

    }

    private void createTimedNotification(Boolean state) {

        Log.d(TAG, "createTimedNotification: Creating notification ");


        Context context = this.getApplicationContext();

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Notifications.CHANNEL_ID, Notifications.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, Notifications.CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setWhen(0)
                .setContentTitle(getResources().getString(state ? R.string.title_ip_adress_updated : R.string.title_ip_update_error))
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setSmallIcon(R.drawable.ic_icon)
                .setColor(getResources().getColor(R.color.colorPrimary)) //backward compatibility
                .setOngoing(false)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getResources().getString(state ? R.string.ip_address_updated : R.string.ip_address_updated_error)))
                .setTicker(getResources().getString(state ? R.string.ip_address_updated : R.string.ip_address_updated_error));

        Notification notification = builder.build();

        manager.notify(Notifications.NOTIFICATION_IP_UPDATED, notification);
    }


    @Override
    public void onTaskFinished(AsyncTask task, Boolean result) {
        final Boolean shouldCreateNotification = prefs.getBoolean(PreferenceCodes.APP_NOTIFY, false);
        if (shouldCreateNotification)
            createTimedNotification(result);

        if (result)
            prefs.edit().putLong(PreferenceCodes.OPENDNS_LAST_UPDATE, System.currentTimeMillis()).apply();
    }
}