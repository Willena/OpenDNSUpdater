package fr.guillaumevillena.opendnsupdater.backgroundJobs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by guill on 26/06/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {


        if (! intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.d(TAG, "onReceive: we got action : " + intent.getAction() + " witch is not " + Intent.ACTION_BOOT_COMPLETED);
            return;
        }

        Log.d(TAG, "onReceive: We have received a boot complete broadcast !! ");
        setScheduler(context);

    }

    public static void setScheduler(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = dispatcher.newJobBuilder().setService(ConnectivityJob.class)
                .setTag("connectivity-job").setLifetime(Lifetime.FOREVER).setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setRecurring(true).setReplaceCurrent(true).setTrigger(Trigger.executionWindow(0, 0)).build();

        dispatcher.schedule(job);
    }
}
