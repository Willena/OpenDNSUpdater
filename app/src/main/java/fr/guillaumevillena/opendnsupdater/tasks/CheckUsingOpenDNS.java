package fr.guillaumevillena.opendnsupdater.tasks;

import android.os.AsyncTask;
import android.util.Log;


import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckUsingOpenDNS extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = CheckUsingOpenDNS.class.getSimpleName();
    private final TaskFinished asyncTaskFinishedListener;

    public CheckUsingOpenDNS(TaskFinished asyncTaskFinishedListener) {
        this.asyncTaskFinishedListener = asyncTaskFinishedListener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();


        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://welcome.opendns.com/").newBuilder();
        HttpUrl url = urlBuilder.build();

        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful())
                return response.request().url().equals(url);

        } catch (UnsupportedEncodingException | ConnectException | UnknownHostException |
                 SSLException | SocketTimeoutException ignored) {
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: {}", e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        asyncTaskFinishedListener.onTaskFinished(this, aBoolean);

    }
}
