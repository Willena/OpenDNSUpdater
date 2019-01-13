package fr.guillaumevillena.opendnsupdater.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Severity;

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
    private Configurator configurator;

    public UpdateOnlineIP(TaskFinished asyncEventListener) {
        this(asyncEventListener, null);
    }

    private TaskFinished asyncEventListener;

    public UpdateOnlineIP(TaskFinished asyncEventListener, Configurator configurator) {
        this.asyncEventListener = asyncEventListener;
        this.configurator = configurator;
        if (configurator == null) {
            SharedPreferences prefs = OpenDnsUpdater.getPrefs();

            String username = prefs.getString(PreferenceCodes.OPENDNS_USERNAME, "");
            String password = prefs.getString(PreferenceCodes.OPENDNS_PASSWORD, "");
            String network = prefs.getString(PreferenceCodes.OPENDNS_NETWORK, "");
            Boolean makeUpdate = prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false);

            this.configurator = new Configurator();
            this.configurator.setNetwork(network);
            this.configurator.setPassword(password);
            this.configurator.setUsername(username);
            this.configurator.makeUpdate(makeUpdate);

        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        if (!hasRequiredSettings()) {
            Log.d(TAG, "doInBackground: Skip because not valid settings ");
            return false;
        }


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://updates.opendns.com/nic/update").newBuilder();
        urlBuilder.addQueryParameter("hostname", this.configurator.getNetwork());
        String url = urlBuilder.build().toString();

        try {
            Request request = new Request.Builder()
                    .header("Authorization", "Basic " + Base64.encodeToString((this.configurator.getUsername() + ":" + this.configurator.getPassword()).getBytes("UTF-8"), Base64.NO_WRAP))
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String data = response.body().string();
                Log.d(TAG, "doInBackground: " + data);
                return data.contains("good") || data.contains("abuse");
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Bugsnag.notify(e, Severity.WARNING);

        } catch (IOException e) {
            e.printStackTrace();
            Bugsnag.notify(e, Severity.WARNING);
        }

        return false;

    }

    private boolean hasRequiredSettings() {
        return !(this.configurator.getUsername().equals("")
                || this.configurator.getPassword().equals("")
                || this.configurator.getNetwork().equals(""))
                && this.configurator.getMakeUpdate();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.d(TAG, "onPostExecute: UPDATE FINISHED");
        asyncEventListener.onTaskFinished(this, aBoolean);
    }

    public static class Configurator {
        private String username = "", password = "", network = "";
        private Boolean makeUpdate = false;

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void makeUpdate(Boolean makeUpdate) {
            this.makeUpdate = makeUpdate;
        }

        public Boolean getMakeUpdate() {
            return makeUpdate;
        }
    }
}
