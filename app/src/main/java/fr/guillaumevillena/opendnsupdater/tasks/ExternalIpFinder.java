package fr.guillaumevillena.opendnsupdater.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import fr.guillaumevillena.opendnsupdater.event.IpUpdatedEvent;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExternalIpFinder extends AsyncTask<Void, Void, Void> {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
