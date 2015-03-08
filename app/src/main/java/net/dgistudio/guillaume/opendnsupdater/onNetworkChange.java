package net.dgistudio.guillaume.opendnsupdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * Created by Guillaume on 08/03/2015.
 */
public class onNetworkChange extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /*String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager conMan = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null) {
                if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    // is connected
                }
            }
        }*/

        Log.d("BroadCast", "Change");
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("guiguivil@gmail.com", "00124500gvGV".toCharArray());
            }
        });
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) new URL("https://updates.opendns.com/nic/update?hostname=Home").openConnection();
            c.setUseCaches(false);
            c.connect();

            Log.d("result", c.getResponseMessage());
            Log.d("result",""+c.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}