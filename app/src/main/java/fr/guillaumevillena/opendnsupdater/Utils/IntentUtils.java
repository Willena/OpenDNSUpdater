package fr.guillaumevillena.opendnsupdater.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by guill on 29/06/2018.
 */

public class IntentUtils {
    public static final String ACTION_UPDATE_NETWORK_INTERFACE = "fr.guillaumevillena.opendnsupdater.update.network.interface";
    public static final String ACTION_UPDATE_NETWORK_IP = "fr.guillaumevillena.opendnsupdater.update.network.ip";

    public static void sendActionUpdateNetworkInterface(Context context, String networkInterface){
        Intent i = getActionIntent(ACTION_UPDATE_NETWORK_INTERFACE);
        i.putExtra("interface", networkInterface);

        sendIntent(i, context);
    }

    public static IntentFilter getIntentFilterFor(String action){
        return new IntentFilter(action);
    }

    public static void sendActionUpdateNetworkIP(Context context, String resultValue){
        Intent i = getActionIntent(ACTION_UPDATE_NETWORK_IP);
        i.putExtra("ip", resultValue);

        sendIntent(i, context);
    }

    private static void sendActionIntent(String action, Context context){
        sendIntent(getActionIntent(action), context);
    }

    private static Intent getActionIntent(String action){
        return new Intent(action);
    }

    private static void sendIntent(Intent i, Context context){
        getBroadcasManager(context).sendBroadcast(i);
    }

    public static LocalBroadcastManager getBroadcasManager(Context context){
        return LocalBroadcastManager.getInstance(context);
    }

}
