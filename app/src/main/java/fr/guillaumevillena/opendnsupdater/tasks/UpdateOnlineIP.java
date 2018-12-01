package fr.guillaumevillena.opendnsupdater.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateOnlineIP extends AsyncTask<Void, Void, Boolean> {

    private TaskFinished asyncEventListener;

    public UpdateOnlineIP(TaskFinished asyncEventListener) {
        this.asyncEventListener = asyncEventListener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        if (!hasRequiredSettings())
            return false;

        SharedPreferences prefs = OpenDnsUpdater.getPrefs();

        String username = prefs.getString(PreferenceCodes.OPENDNS_USERNAME, null);
        String password = prefs.getString(PreferenceCodes.OPENDNS_PASSWORD, null);
        String network = prefs.getString(PreferenceCodes.OPENDNS_NETWORK, null);

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

            if (response.isSuccessful())
                return response.body().string().contains("good");

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
        asyncEventListener.onTaskFinished(this, aBoolean);
    }

    private boolean hasRequiredSettings() {
        SharedPreferences prefs = OpenDnsUpdater.getPrefs();

        return prefs.getString(PreferenceCodes.OPENDNS_USERNAME, null) != null
                && prefs.getString(PreferenceCodes.OPENDNS_PASSWORD, null) != null
                && prefs.getString(PreferenceCodes.OPENDNS_NETWORK, null) != null
                && prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false);
    }
}
