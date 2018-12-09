package fr.guillaumevillena.opendnsupdater.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateOnlineIP extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = UpdateOnlineIP.class.getSimpleName();

    private TaskFinished asyncEventListener;

    public UpdateOnlineIP(TaskFinished asyncEventListener) {
        this.asyncEventListener = asyncEventListener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        if (!hasRequiredSettings()) {
            Log.d(TAG, "doInBackground: Skip because not valid settings ");
            return false;
        }


        SharedPreferences prefs = OpenDnsUpdater.getPrefs();

        String username = prefs.getString(PreferenceCodes.OPENDNS_USERNAME, "");
        String password = prefs.getString(PreferenceCodes.OPENDNS_PASSWORD, "");
        String network = prefs.getString(PreferenceCodes.OPENDNS_NETWORK, "");


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://updates.opendns.com/nic/update").newBuilder();
        urlBuilder.addQueryParameter("hostname", network);
        String url = urlBuilder.build().toString();

        try {
            Request request = new Request.Builder()
                    .header("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP))
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String data = response.body().string();
                Log.d(TAG, "doInBackground: " + data);
                return data.contains("good");
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.d(TAG, "onPostExecute: UPDATE FINISHED");
        asyncEventListener.onTaskFinished(this, aBoolean);
    }

    private boolean hasRequiredSettings() {
        SharedPreferences prefs = OpenDnsUpdater.getPrefs();

        return !(prefs.getString(PreferenceCodes.OPENDNS_USERNAME, "").equals("")
                || prefs.getString(PreferenceCodes.OPENDNS_PASSWORD, "").equals("")
                || prefs.getString(PreferenceCodes.OPENDNS_NETWORK, "").equals(""))
                && prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false);
    }
}
