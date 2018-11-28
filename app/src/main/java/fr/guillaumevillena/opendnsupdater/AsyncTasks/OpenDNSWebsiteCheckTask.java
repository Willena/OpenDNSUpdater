package fr.guillaumevillena.opendnsupdater.AsyncTasks;

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
 * Created by guill on 30/06/2018.
 */

public class OpenDNSWebsiteCheckTask extends BasicAsyncTask<Void, Void> {
    private static final String OPEN_DNS_CHECK_URL = "http://welcome.opendns.com/";
    private static final String TAG = OpenDNSWebsiteCheckTask.class.getSimpleName();

    @Override
    protected ResultItem doInBackground(Void... voids) {
        HttpsURLConnection urlConnection = null;
        ResultItem resultItem = new ResultItem();

        InputStream inputStream = null;
        try {
            URL url = new URL(OPEN_DNS_CHECK_URL);

            urlConnection = (HttpsURLConnection) url.openConnection();
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

            if (buffer.toString().contains("Oops!")){
                Log.d(TAG, "doInBackground: The page contain Oops!");
                resultItem.putSucessState(Boolean.FALSE);
            }
            else {
                Log.d(TAG, "doInBackground: It does not contain the Oops! string");
                resultItem.putSucessState(Boolean.TRUE);
            }

            resultItem.putResultValue(buffer.toString());

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
