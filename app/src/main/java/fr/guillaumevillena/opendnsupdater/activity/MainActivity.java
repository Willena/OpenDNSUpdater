package fr.guillaumevillena.opendnsupdater.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.AsyncTaskFinished;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.DnsUsageCheckerTask;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.IpCheckTask;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.OpenDNSWebsiteCheckTask;
import fr.guillaumevillena.opendnsupdater.AsyncTasks.ResultItem;
import fr.guillaumevillena.opendnsupdater.BuildConfig;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.Receivers.BootReceiver;
import fr.guillaumevillena.opendnsupdater.Utils.IntentUtils;
import fr.guillaumevillena.opendnsupdater.Utils.PreferenceCodes;
import fr.guillaumevillena.opendnsupdater.Utils.RequestCodes;
import fr.guillaumevillena.opendnsupdater.Utils.StateSwitcher;
import fr.guillaumevillena.opendnsupdater.VpnService.service.OpenDnsVpnService;
import fr.guillaumevillena.opendnsupdater.VpnService.util.server.DNSServerHelper;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static fr.guillaumevillena.opendnsupdater.TestState.ERROR;
import static fr.guillaumevillena.opendnsupdater.TestState.RUNNING;
import static fr.guillaumevillena.opendnsupdater.TestState.SUCCESS;
import static fr.guillaumevillena.opendnsupdater.TestState.UNKNOWN;
import static fr.guillaumevillena.opendnsupdater.Utils.IntentUtils.ACTION_UPDATE_NETWORK_INTERFACE;
import static fr.guillaumevillena.opendnsupdater.Utils.IntentUtils.ACTION_UPDATE_NETWORK_IP;
import static fr.guillaumevillena.opendnsupdater.Utils.IntentUtils.getIntentFilterFor;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private StateSwitcher filterXStateSwitcher;
    private StateSwitcher filterPhishingStateSwitcher;
    private StateSwitcher useOpendnsStateSwitcher;
    private StateSwitcher openDNSWebsiteStateSwitcher;

    private Switch switchEnableNotification;
    private Switch switchEnableAutoUpdate;
    private Switch switchEnableEnableOpendnsServers;

    private TextView ipValue;
    private TextView interfaceValue;

    /*

      if (OpenDnsVpnService.isActivated()) {
                Daedalus.deactivateService(getActivity().getApplicationContext());
            } else {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .putExtra(MainActivity.LAUNCH_ACTION, MainActivity.LAUNCH_ACTION_ACTIVATE));
            }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set scheduler to get connectivity changes
        BootReceiver.setScheduler(this);

        TextView textViewVersion = findViewById(R.id.mainactivity_app_version);
        textViewVersion.setText("v" + BuildConfig.VERSION_NAME);

        // Getting useful widget from the view.

        ipValue = findViewById(R.id.ip_text_value);
        interfaceValue = findViewById(R.id.interface_text_value);

        ImageButton settingButton = findViewById(R.id.setting_button);
        ImageButton refreshButton = findViewById(R.id.refresh_button);


        switchEnableNotification = findViewById(R.id.switch_enable_notifications);
        switchEnableAutoUpdate = findViewById(R.id.switch_auto_update);
        switchEnableEnableOpendnsServers = findViewById(R.id.switch_enable_opendns_server);


        ProgressBar progressBarFilterX = findViewById(R.id.progressBar_filterX);
        ProgressBar progressBarFilterPhishing = findViewById(R.id.progressBar_filter_phising);
        ProgressBar progressBarUsingOpenDns = findViewById(R.id.progressBar_using_opendns);
        ProgressBar progressBarOpenDnsWebsite = findViewById(R.id.progressBar_status_website_check);


        ImageView imgStatusFilterX = findViewById(R.id.img_status_filter_X);
        ImageView imgStatusFilterPhishing = findViewById(R.id.img_status_filter_phishing);
        ImageView imgStatusUsingOpendns = findViewById(R.id.img_status_using_opendns);
        ImageView imgStatusOpenDNSWebiste = findViewById(R.id.img_status_website_check);

        this.filterXStateSwitcher = new StateSwitcher();
        this.filterPhishingStateSwitcher = new StateSwitcher();
        this.useOpendnsStateSwitcher = new StateSwitcher();
        this.openDNSWebsiteStateSwitcher = new StateSwitcher();

        initStateSwitcher(this.filterXStateSwitcher, progressBarFilterX, imgStatusFilterX);
        initStateSwitcher(this.filterPhishingStateSwitcher, progressBarFilterPhishing, imgStatusFilterPhishing);
        initStateSwitcher(this.useOpendnsStateSwitcher, progressBarUsingOpenDns, imgStatusUsingOpendns);
        initStateSwitcher(this.openDNSWebsiteStateSwitcher, progressBarOpenDnsWebsite, imgStatusOpenDNSWebiste);

        this.filterPhishingStateSwitcher.setCurrentState(SUCCESS);
        this.filterXStateSwitcher.setCurrentState(RUNNING);
        this.useOpendnsStateSwitcher.setCurrentState(RUNNING);
        this.openDNSWebsiteStateSwitcher.setCurrentState(RUNNING
        );

        // Connect events to switches and buttons

        switchEnableAutoUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                setAutoUpdater(state);
            }
        });

        switchEnableNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                setNotifications(state);
            }
        });

        switchEnableEnableOpendnsServers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                setOpenDnsServers(state);
            }
        });


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshOpenDnsStatus();
            }
        });

        restoreSettings();


    }

    private void restoreSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        switchEnableNotification.setChecked(prefs.getBoolean(PreferenceCodes.APP_NOTIFY, false));
        switchEnableEnableOpendnsServers.setChecked(prefs.getBoolean(PreferenceCodes.APP_DNS, false));
        switchEnableAutoUpdate.setChecked(prefs.getBoolean(PreferenceCodes.APP_AUTO_UPDATE, false));

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
                Log.d(TAG, "onActivityResult: " + resultCode);
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

    }


    @Override
    protected void onPause() {
        super.onPause();


        Log.d(TAG, "onResume: Unsubscribe to broadcast listener updates");
        IntentUtils.getBroadcasManager(this).unregisterReceiver(networkInterfaceUpdateReceiver);
        IntentUtils.getBroadcasManager(this).unregisterReceiver(networkIPUpdateReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: Subscribe to broadcast listener updates");
        IntentUtils.getBroadcasManager(this).registerReceiver(networkInterfaceUpdateReceiver, getIntentFilterFor(ACTION_UPDATE_NETWORK_INTERFACE));
        IntentUtils.getBroadcasManager(this).registerReceiver(networkIPUpdateReceiver, getIntentFilterFor(ACTION_UPDATE_NETWORK_IP));


        Log.d(TAG, "onResume: Starting to update the UI with fresh informations ");
        this.refreshUIInformations();


    }

    private void refreshUIInformations() {


        //First set all stateSwitcher to indeterminate...

        this.filterPhishingStateSwitcher.setCurrentState(RUNNING);
        this.filterXStateSwitcher.setCurrentState(RUNNING);
        this.useOpendnsStateSwitcher.setCurrentState(RUNNING);
        this.openDNSWebsiteStateSwitcher.setCurrentState(RUNNING);


        //Refresh the current interface.
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        IntentUtils.sendActionUpdateNetworkInterface(this.getApplicationContext(), activeNetwork.getTypeName());


        //Refresh the IP
        IpCheckTask ipCheckTask = new IpCheckTask();
        ipCheckTask.setFinishListener(new AsyncTaskFinished() {
            @Override
            public void onFinished(ResultItem item) {
                Log.d(TAG, "onFinished: We have finished getting the ip :D ");
                if (item.getState()) {
                    IntentUtils.sendActionUpdateNetworkIP(MainActivity.this.getApplicationContext(), (String) item.getResultValue());
                }
            }
        });

        ipCheckTask.executeOnExecutor(THREAD_POOL_EXECUTOR);


        //Doing all available tests...
        //TODO : Tests OPENDNS status.

        DnsUsageCheckerTask dnsUsageCheckerTask = new DnsUsageCheckerTask();
        dnsUsageCheckerTask.setFinishListener(new AsyncTaskFinished() {
            @Override
            public void onFinished(ResultItem item) {
                Log.d(TAG, "onFinished: We have a result ! for DNS ! ");
                MainActivity.this.useOpendnsStateSwitcher.setCurrentState((item.getState()) ? SUCCESS : ERROR);
            }
        });

        dnsUsageCheckerTask.executeOnExecutor(THREAD_POOL_EXECUTOR, this);


        OpenDNSWebsiteCheckTask openDNSWebsiteCheckTask = new OpenDNSWebsiteCheckTask();
        openDNSWebsiteCheckTask.setFinishListener(new AsyncTaskFinished() {
            @Override
            public void onFinished(ResultItem item) {
                Log.d(TAG, "onFinished: We have the result ");
                MainActivity.this.openDNSWebsiteStateSwitcher.setCurrentState((item.getState()) ? SUCCESS : ERROR);
            }
        });

        openDNSWebsiteCheckTask.executeOnExecutor(THREAD_POOL_EXECUTOR);

    }


    /*
        LIST OF BROADCAST RECEIVER HERE !!
     */

    BroadcastReceiver networkInterfaceUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {

                String _interface = intent.getExtras().getString("interface", "");
                Log.d(TAG, "onReceive: Interface : " + _interface);

                if (!_interface.equals("")) {
                    MainActivity.this.interfaceValue.setText(_interface);
                }


            }
        }
    };

    BroadcastReceiver networkIPUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {

                String _ip = intent.getExtras().getString("ip", "");
                Log.d(TAG, "onReceive: IP : " + _ip);

                if (!_ip.equals("")) {
                    MainActivity.this.ipValue.setText(_ip);
                }


            }
        }
    };

}
