package net.dgistudio.guillaume.opendnsupdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Guillaume on 08/03/2015.
 */
public class onNetworkChange extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadCast", "Change");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String username  = prefs.getString("OpenDns_Username","");
        String password  = prefs.getString("OpenDns_Password","");

        Log.d("pref",""+username);
        Log.d("pref",""+password);

        /*try{
           HttpClient httpclient = new DefaultHttpClient();

           HttpGet request = new HttpGet();
           URI website = new URI("http://alanhardin.comyr.com/matt24/matt28.php");
           request.setURI(website);
           HttpResponse response = httpclient.execute(request);
           in = new BufferedReader(new InputStreamReader(
                   response.getEntity().getContent()));

           // NEW CODE
           String line = in.readLine();
           textv.append(" First line: " + line);
           // END OF NEW CODE

           textv.append(" Connected ");
       }catch(Exception e){
           Log.e("log_tag", "Error in http connection "+e.toString());
       }*/
    }
}