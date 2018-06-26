package fr.guillaumevillena.opendnsupdater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import fr.guillaumevillena.opendnsupdater.backgroundJobs.BootReceiver;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private StateSwitcher filterXStateSwitcher;
    private StateSwitcher filterPhishingStateSwitcher;
    private StateSwitcher useOpendnsStateSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set scheduler to get connectivity changes
        BootReceiver.setScheduler(this);

        // Getting useful widget from the view.

        TextView ipValue = findViewById(R.id.ip_text_value);
        TextView interfaceValue = findViewById(R.id.interface_text_value);

        ImageButton settingButton = findViewById(R.id.setting_button);
        ImageButton refreshButton = findViewById(R.id.refresh_button);


        Switch switchEnableNotification = findViewById(R.id.switch_enable_notifications);
        Switch switchEnableAutoUpdate = findViewById(R.id.switch_auto_update);
        Switch switchEnableEnableOpendnsServers = findViewById(R.id.switch_enable_opendns_server);


        ProgressBar progressBarFilterX = findViewById(R.id.progressBar_filterX);
        ProgressBar progressBarFilterPhishing = findViewById(R.id.progressBar_filter_phising);
        ProgressBar progressBarUsingOpenDns = findViewById(R.id.progressBar_using_opendns);


        ImageView imgStatusFilterX = findViewById(R.id.img_status_filter_X);
        ImageView imgStatusFilterPhishing = findViewById(R.id.img_status_filter_phishing);
        ImageView imgStatusUsingOpendns = findViewById(R.id.img_status_using_opendns);

        this.filterXStateSwitcher = new StateSwitcher();
        this.filterPhishingStateSwitcher = new StateSwitcher();
        this.useOpendnsStateSwitcher = new StateSwitcher();

        initStateSwitcher(this.filterXStateSwitcher, progressBarFilterX, imgStatusFilterX);
        initStateSwitcher(this.filterPhishingStateSwitcher, progressBarFilterPhishing, imgStatusFilterPhishing);
        initStateSwitcher(this.useOpendnsStateSwitcher, progressBarUsingOpenDns, imgStatusUsingOpendns);

        this.filterPhishingStateSwitcher.setCurrentState(TestState.SUCCESS);
        this.filterXStateSwitcher.setCurrentState(TestState.RUNNING);

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

    }

    private void initStateSwitcher(StateSwitcher stateSwitcher, ProgressBar progressBar, ImageView imgStatus) {
        stateSwitcher.setDefaults(imgStatus, ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_block_grey_24dp));
        stateSwitcher.setCurrentState(TestState.Unknown);

        stateSwitcher.putDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_close_red_24dp), TestState.ERROR);
        stateSwitcher.putDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_check_green_24dp), TestState.SUCCESS);
        stateSwitcher.putDrawable(null, TestState.RUNNING);

        stateSwitcher.putView(progressBar, TestState.RUNNING);
        stateSwitcher.putView(imgStatus, TestState.ERROR);
        stateSwitcher.putView(imgStatus, TestState.SUCCESS);

    }

    private void setAutoUpdater(boolean state) {

    }

    private void setNotifications(boolean state) {

    }

    private void setOpenDnsServers(boolean state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RequestCodes.SETTIGNS:
                Log.d(TAG, "onActivityResult: " + resultCode);
        }

    }

    private void openSettings() {
        Intent intent = new Intent(this, GlobalSettingsActivity.class);
        this.startActivityForResult(intent, RequestCodes.SETTIGNS);
    }

    private void refreshOpenDnsStatus() {

    }

}
