package fr.guillaumevillena.opendnsupdater.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import fr.guillaumevillena.opendnsupdater.BuildConfig;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.Receivers.ConnectivityJob;
import fr.guillaumevillena.opendnsupdater.tasks.CheckFakePhishingSite;
import fr.guillaumevillena.opendnsupdater.tasks.CheckUsingOpenDNS;
import fr.guillaumevillena.opendnsupdater.tasks.TaskFinished;
import fr.guillaumevillena.opendnsupdater.tasks.UpdateOnlineIP;
import fr.guillaumevillena.opendnsupdater.utils.DateUtils;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;
import fr.guillaumevillena.opendnsupdater.utils.RequestCodes;
import fr.guillaumevillena.opendnsupdater.utils.SimplerCountdown;
import fr.guillaumevillena.opendnsupdater.utils.StateSwitcher;
import fr.guillaumevillena.opendnsupdater.vpnService.service.OpenDnsVpnService;
import fr.guillaumevillena.opendnsupdater.vpnService.util.server.DNSServerHelper;

import static fr.guillaumevillena.opendnsupdater.TestState.ERROR;
import static fr.guillaumevillena.opendnsupdater.TestState.RUNNING;
import static fr.guillaumevillena.opendnsupdater.TestState.SUCCESS;
import static fr.guillaumevillena.opendnsupdater.TestState.UNKNOWN;


public class MainActivity extends AppCompatActivity implements TaskFinished {


    private static final String TAG = MainActivity.class.getSimpleName();
    private StateSwitcher filterPhishingStateSwitcher;
    private StateSwitcher ipAddressUpdatedStateSwitcher;
    private StateSwitcher useOpendnsStateSwitcher;
    private StateSwitcher openDNSWebsiteStateSwitcher;

    private Switch switchEnableNotification;
    private Switch switchEnableAutoUpdate;
    private Switch switchEnableEnableOpendnsServers;

    private TextView ipValue;
    private TextView interfaceValue;
    private TextView lastUpdateDateTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lastUpdateDateTextView = findViewById(R.id.mainactivity_last_update);

        TextView textViewVersion = findViewById(R.id.mainactivity_app_version);
        textViewVersion.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));

        // Getting useful widget from the view.

        ipValue = findViewById(R.id.ip_text_value);
        interfaceValue = findViewById(R.id.interface_text_value);

        ImageButton settingButton = findViewById(R.id.setting_button);
        ImageButton refreshButton = findViewById(R.id.refresh_button);

        ProgressBar progressBarIpAddressUpdate = findViewById(R.id.progressBar_ip_updated);
        ProgressBar progressBarFilterPhising = findViewById(R.id.progressBar_filter_phising);
        ProgressBar progressBarUsingOpenDns = findViewById(R.id.progressBar_using_opendns);
        ProgressBar progressBarOpenDnsWebsite = findViewById(R.id.progressBar_status_website_check);

        ImageView imgStatusIpAdressUpdate = findViewById(R.id.img_status_ip_updated);
        ImageView imgStatusFilterPhishing = findViewById(R.id.img_status_filter_phishing);
        ImageView imgStatusUsingOpendns = findViewById(R.id.img_status_using_opendns);
        ImageView imgStatusOpenDNSWebiste = findViewById(R.id.img_status_website_check);

        switchEnableNotification = findViewById(R.id.switch_enable_notifications);
        switchEnableAutoUpdate = findViewById(R.id.switch_auto_update);
        switchEnableEnableOpendnsServers = findViewById(R.id.switch_enable_opendns_server);

        this.filterPhishingStateSwitcher = new StateSwitcher();
        this.ipAddressUpdatedStateSwitcher = new StateSwitcher();
        this.useOpendnsStateSwitcher = new StateSwitcher();
        this.openDNSWebsiteStateSwitcher = new StateSwitcher();

        initStateSwitcher(this.filterPhishingStateSwitcher, progressBarFilterPhising, imgStatusFilterPhishing);
        initStateSwitcher(this.ipAddressUpdatedStateSwitcher, progressBarIpAddressUpdate, imgStatusIpAdressUpdate);
        initStateSwitcher(this.useOpendnsStateSwitcher, progressBarUsingOpenDns, imgStatusUsingOpendns);
        initStateSwitcher(this.openDNSWebsiteStateSwitcher, progressBarOpenDnsWebsite, imgStatusOpenDNSWebiste);

        this.ipAddressUpdatedStateSwitcher.setCurrentState(RUNNING);
        this.filterPhishingStateSwitcher.setCurrentState(RUNNING);
        this.useOpendnsStateSwitcher.setCurrentState(RUNNING);
        this.openDNSWebsiteStateSwitcher.setCurrentState(RUNNING);

        // Connect events to switches and buttons
        switchEnableAutoUpdate.setOnCheckedChangeListener((compoundButton, state) -> setAutoUpdater(state));
        switchEnableNotification.setOnCheckedChangeListener((compoundButton, state) -> setNotifications(state));
        switchEnableEnableOpendnsServers.setOnCheckedChangeListener((compoundButton, state) -> setOpenDnsServers(state));

        settingButton.setOnClickListener(view -> openSettings());
        refreshButton.setOnClickListener(view -> refreshOpenDnsStatus());
        restoreSettings();
        refreshOpenDnsStatus();


    }

    private void restoreSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        switchEnableNotification.setChecked(prefs.getBoolean(PreferenceCodes.APP_NOTIFY, false));
        switchEnableEnableOpendnsServers.setChecked(prefs.getBoolean(PreferenceCodes.APP_DNS, false));
        switchEnableAutoUpdate.setChecked(prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false));

        long lastUpdate = OpenDnsUpdater.getPrefs().getLong(PreferenceCodes.OPENDNS_LAST_UPDATE, -1);
        lastUpdateDateTextView.setText(getString(R.string.main_activity_last_ip_update, lastUpdate != -1 ? DateUtils.getDate(this, lastUpdate) : getString(R.string.text_never)));


    }

    private void initStateSwitcher(StateSwitcher stateSwitcher, ProgressBar progressBar, ImageView imgStatus) {
        stateSwitcher.setDefaults(imgStatus, ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_block_grey_24dp));
        stateSwitcher.setCurrentState(UNKNOWN);

        stateSwitcher.putDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_close_red_24dp), ERROR);
        stateSwitcher.putDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_check_green_24dp), SUCCESS);
        stateSwitcher.putDrawable(null, RUNNING);

        stateSwitcher.putView(progressBar, RUNNING);
        stateSwitcher.putView(imgStatus, ERROR);
        stateSwitcher.putView(imgStatus, SUCCESS);

    }

    private void setAutoUpdater(boolean state) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefEditor = prefs.edit();

        prefEditor.putBoolean(PreferenceCodes.APP_AUTO_UPDATE, state);
        prefEditor.apply();
    }

    private void setNotifications(boolean state) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefEditor = prefs.edit();

        prefEditor.putBoolean(PreferenceCodes.APP_NOTIFY, state);
        prefEditor.apply();
    }

    private void setOpenDnsServers(boolean state) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefEditor = prefs.edit();

        prefEditor.putBoolean(PreferenceCodes.APP_DNS, state);
        prefEditor.apply();

        if (state)
            activateService();
        else {
            if (OpenDnsVpnService.isActivated()) {
                OpenDnsUpdater.deactivateService(this);
            }
        }

        this.refreshOpenDnsStatus();


    }

    public void activateService() {
        Intent intent = VpnService.prepare(OpenDnsUpdater.getInstance());
        if (intent != null) {
            startActivityForResult(intent, 3);
        } else {
            onActivityResult(3, Activity.RESULT_OK, null);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RequestCodes.SETTIGNS:
                restoreSettings();
                refreshOpenDnsStatus();
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    OpenDnsVpnService.primaryServer = DNSServerHelper.getAddressById(DNSServerHelper.getPrimary());
                    OpenDnsVpnService.secondaryServer = DNSServerHelper.getAddressById(DNSServerHelper.getSecondary());
                    OpenDnsUpdater.getInstance().startService(OpenDnsUpdater.getServiceIntent(getApplicationContext()).setAction(OpenDnsVpnService.ACTION_ACTIVATE));
                }
        }

    }

    private void openSettings() {
        Intent intent = new Intent(this, GlobalSettingsActivity.class);
        this.startActivityForResult(intent, RequestCodes.SETTIGNS);
    }

    private void refreshOpenDnsStatus() {

        this.ipAddressUpdatedStateSwitcher.setCurrentState(RUNNING);
        this.filterPhishingStateSwitcher.setCurrentState(RUNNING);
        this.useOpendnsStateSwitcher.setCurrentState(RUNNING);
        this.openDNSWebsiteStateSwitcher.setCurrentState(RUNNING);


        new SimplerCountdown(1500) {
            @Override
            public void onFinish() {
                useOpendnsStateSwitcher.setCurrentState(OpenDnsVpnService.isActivated() ? SUCCESS : ERROR);
            }
        };

        new SimplerCountdown(1000) {
            @Override
            public void onFinish() {
                new CheckUsingOpenDNS(MainActivity.this).execute();
                new UpdateOnlineIP(MainActivity.this).execute();
                new CheckFakePhishingSite(MainActivity.this).execute();
            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!ConnectivityJob.isJobsStarted())
            ConnectivityJob.setScheduler(this);


        Log.d(TAG, "onResume: Starting to update the UI with fresh informations ");
        this.refreshOpenDnsStatus();


    }


    @Override
    public void onTaskFinished(AsyncTask task, Boolean result) {
        if (task instanceof CheckUsingOpenDNS) {
            Log.d(TAG, "onTaskFinished: CheckUsinOpenDNS ");
            openDNSWebsiteStateSwitcher.setCurrentState(result ? SUCCESS : ERROR);
        } else if (task instanceof UpdateOnlineIP) {
            ipAddressUpdatedStateSwitcher.setCurrentState(result ? SUCCESS : ERROR);
        } else if (task instanceof CheckFakePhishingSite) {
            filterPhishingStateSwitcher.setCurrentState(result ? SUCCESS : ERROR);
        }
    }
}
