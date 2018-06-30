package fr.guillaumevillena.opendnsupdater.AsyncTasks;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by guill on 27/06/2018.
 */

public class IpCheckTask extends BasicAsyncTask<Void, Void> {


    private static final String IP_CHECL_URL = "https://api.ipify.org/";
    private static final String TAG = IpCheckTask.class.getSimpleName();

    @Override
    protected ResultItem doInBackground(Void... voids) {
        //Here we do the http call :D

        //First get the username, the password, the network.

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;


        ResultItem resultItem = new ResultItem();

        try {
            URL url = new URL(IP_CHECL_URL);

            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.connect();

            int response = urlConnection.getResponseCode();
            Log.d(TAG, "doInBackground: The reponse for IP is " + response);

            resultItem.put("errorCode", response);

            if (response != 200) {

                resultItem.putSucessState(false);
                return resultItem;
            }

            inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                resultItem.putSucessState(false);
                return resultItem;
            }



            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }


            Log.d(TAG, "doInBackground: Print the buffer " + buffer.toString());
            resultItem.putResultValue(buffer.toString());
            resultItem.putSucessState(true);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return resultItem;
    }

}
