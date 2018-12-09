package fr.guillaumevillena.opendnsupdater;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;
import fr.guillaumevillena.opendnsupdater.vpnService.service.OpenDnsVpnService;
import fr.guillaumevillena.opendnsupdater.vpnService.util.server.DNSServer;
import fr.guillaumevillena.opendnsupdater.vpnService.util.server.DNSServerHelper;
import io.fabric.sdk.android.Fabric;


public class OpenDnsUpdater extends Application {

    public static final List<DNSServer> DNS_SERVERS = new ArrayList<DNSServer>() {{
        add(new DNSServer("208.67.222.222", R.string.server_opendns_primary));
        add(new DNSServer("208.67.220.220", R.string.server_opendns_secondary));
    }};
    private static final String SHORTCUT_ID_ACTIVATE = "shortcut_activate";

    private static OpenDnsUpdater instance = null;
    private SharedPreferences prefs;

    public static SharedPreferences getPrefs() {
        return getInstance().prefs;
    }

    public static Intent getServiceIntent(Context context) {
        return new Intent(context, OpenDnsVpnService.class);
    }

    public static boolean switchService() {
        if (OpenDnsVpnService.isActivated()) {
            deactivateService(instance);
            return false;
        } else {
            activateService(instance);
            return true;
        }
    }

    public static boolean activateService(Context context) {
        Intent intent = VpnService.prepare(context);
        if (intent != null) {
            return false;
        } else {
            OpenDnsVpnService.primaryServer = DNSServerHelper.getAddressById(DNSServerHelper.getPrimary());
            OpenDnsVpnService.secondaryServer = DNSServerHelper.getAddressById(DNSServerHelper.getSecondary());
            context.startService(OpenDnsUpdater.getServiceIntent(context).setAction(OpenDnsVpnService.ACTION_ACTIVATE));
            return true;
        }
    }

    public static void deactivateService(Context context) {
        if (OpenDnsVpnService.isActivated()) {
            context.startService(getServiceIntent(context).setAction(OpenDnsVpnService.ACTION_DEACTIVATE));
            context.stopService(getServiceIntent(context));
        }

    }


    public static OpenDnsUpdater getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(
                new Fabric.Builder(this)
                        .kits(new Crashlytics())
                        .appIdentifier(BuildConfig.APPLICATION_ID)
                        .build()
        );

        instance = this;
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(PreferenceCodes.FIRST_TIME, true))
            prefs.edit().putString(PreferenceCodes.OPENDNS_NETWORK, "")
                    .putString(PreferenceCodes.OPENDNS_PASSWORD, "")
                    .putString(PreferenceCodes.OPENDNS_USERNAME, "")
                    .putBoolean(PreferenceCodes.FIRST_TIME, false).apply();
    }

    @Override
    public void onTerminate() {
        Log.d("OpenDnsUpdater", "onTerminate");
        super.onTerminate();

        instance = null;
        prefs = null;
    }
}
