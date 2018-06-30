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

public class IpUpdateTask extends BasicAsyncTask<String, Void> {


    private static final String IP_UPTATER_URL = "https://updates.opendns.com/nic/update?hostname=";
    private static final String TAG = IpUpdateTask.class.getSimpleName();

    @Override
    protected ResultItem doInBackground(String... strings) {
        //Here we do the http call :D

        //First get the username, the password, the network.
        final String username = strings[0];
        final String password = strings[1];
        String network = strings[2];

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;


        ResultItem resultItem = new ResultItem();

        try {
            URL url = new URL(IP_UPTATER_URL + network);

            urlConnection = (HttpsURLConnection) url.openConnection();

            String encoded = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);

            urlConnection.connect();

            int response = urlConnection.getResponseCode();
            Log.d(TAG, "doInBackground: The reponse is " + response);

            resultItem.put("errorCode", response);

            if (response != 200) {

                resultItem.putSucessState(Boolean.FALSE);
                return resultItem;
            }

            inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                resultItem.putSucessState(Boolean.FALSE);
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
            resultItem.putSucessState(Boolean.TRUE);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            Log.d(TAG, "doInBackground: An Exeption " + e.getMessage() + "   " + e.getCause());
        }
        finally {
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
