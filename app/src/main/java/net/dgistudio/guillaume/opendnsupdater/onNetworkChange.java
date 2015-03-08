package net.dgistudio.guillaume.opendnsupdater;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by Guillaume on 08/03/2015.
 */
public class onNetworkChange extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        //Log.d("BroadCast", "Change");

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("info", "We are connected");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            final String username = prefs.getString("OpenDns_Username", "");
            final String password = prefs.getString("OpenDns_Password", "");

            //Log.d("pref",""+username);
            //Log.d("pref",""+password);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpGet request = new HttpGet();
                            URI website = new URI("https://updates.opendns.com/nic/update?hostname=Home");
                            request.setHeader("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP));
                            request.setURI(website);
                            HttpResponse response = httpclient.execute(request);
                            String result = EntityUtils.toString(response.getEntity());
                            Log.d("res", result);
                            if (result.split(" ")[0].equals("good")) {
                                final NotificationManager mNotification = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                                //  final Intent launchNotifiactionIntent = new Intent(this, TutoNotificationHomeActivity.class);
                                //final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                // context.REQUEST_CODE, launchNotifiactionIntent,
                                // PendingIntent.FLAG_ONE_SHOT);

                                Notification.Builder builder = new Notification.Builder(context)
                                        .setWhen(System.currentTimeMillis())
                                        .setTicker("Yeah")
                                        .setSmallIcon(R.drawable.icon)
                                        .setContentTitle(context.getString(R.string.app_name))
                                        .setContentText(context.getString(R.string.noty_content));

                                mNotification.notify(0, builder.build());

                                Runnable task = new Runnable() {
                                    public void run() {
                                        mNotification.cancel(0);
                                    }
                                };
                                final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
                                worker.schedule(task, 5, TimeUnit.SECONDS);
                            }

                        } catch (Exception e) {
                            Log.e("log_tag", "Error in http connection " + e.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            //TODO : check if it is the firs time -> do nothing and show noty asking to change config
            //TODO : If bad config -> noty + ask to change cfg
        }
    }
}