package fr.guillaumevillena.opendnsupdater.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Severity;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckFakePhishingSite extends AsyncTask<Void, Void, Boolean> {

    private TaskFinished taskFinishedListenner;
    private static final String TAG = CheckFakePhishingSite.class.getSimpleName();

    public CheckFakePhishingSite(TaskFinished taskFinishedListenner) {
        this.taskFinishedListenner = taskFinishedListenner;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // http://www.internetbadguys.com/
        // This domain is blocked

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://www.internetbadguys.com/").newBuilder();
        HttpUrl url = urlBuilder.build();

        try {

            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            Log.d(TAG, "doInBackground: " + response.toString());
            return response.isRedirect() || !response.isSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
            Bugsnag.notify(e, Severity.WARNING);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        taskFinishedListenner.onTaskFinished(this, aBoolean);
    }
}
