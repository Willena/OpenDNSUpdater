package fr.guillaumevillena.opendnsupdater.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Severity;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import fr.guillaumevillena.opendnsupdater.event.IpUpdatedEvent;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExternalIpFinder extends AsyncTask<Void, Void, Void> {

    private static final String TAG = ExternalIpFinder.class.getSimpleName();

    @Override
    protected Void doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.ipify.org/").newBuilder();
        String url = urlBuilder.build().toString();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response;

            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String data = response.body().string();
                Log.d(ExternalIpFinder.class.getSimpleName(), "doInBackground: The content is '" + data + "'");
                if (EventBus.getDefault().hasSubscriberForEvent(IpUpdatedEvent.class)) {
                    EventBus.getDefault().post(new IpUpdatedEvent(data));
                }
            }
        } catch (UnsupportedEncodingException | ConnectException | UnknownHostException | SSLException | SocketTimeoutException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            Bugsnag.notify(e, Severity.WARNING);

        }
        return null;
    }
}
