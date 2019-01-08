package fr.guillaumevillena.opendnsupdater.activity.introSlide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.tasks.TaskFinished;
import fr.guillaumevillena.opendnsupdater.tasks.UpdateOnlineIP;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;
import fr.guillaumevillena.opendnsupdater.utils.StateSwitcher;

import static fr.guillaumevillena.opendnsupdater.TestState.ERROR;
import static fr.guillaumevillena.opendnsupdater.TestState.RUNNING;
import static fr.guillaumevillena.opendnsupdater.TestState.SUCCESS;
import static fr.guillaumevillena.opendnsupdater.TestState.UNKNOWN;

public class IntroMainActionAccountCredentialFragment extends Fragment implements TaskFinished {

    private View root;
    private StateSwitcher ipAddressUpdatedStateSwitcher;
    private EditText openDnsNetwork;
    private EditText openDnsPassword;
    private EditText openDnsUsername;

    public static IntroMainActionAccountCredentialFragment newInstance() {
        return new IntroMainActionAccountCredentialFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.intro_credentials_account_fragment, container, false);


        openDnsPassword = root.findViewById(R.id.editOpenDnsPassword);
        openDnsUsername = root.findViewById(R.id.editOpenDnsUsername);
        openDnsNetwork = root.findViewById(R.id.editOpenDnsNetwork);

        ipAddressUpdatedStateSwitcher = new StateSwitcher();
        initStateSwitcher(ipAddressUpdatedStateSwitcher, root.findViewById(R.id.progressBar_ip_updated), root.findViewById(R.id.img_status_ip_updated));

        AppCompatButton testbtn = root.findViewById(R.id.testCredentials);
        testbtn.setOnClickListener(view -> testConfiguration());

        return root;
    }

    private void initStateSwitcher(StateSwitcher stateSwitcher, ProgressBar progressBar, AppCompatImageView imgStatus) {


        stateSwitcher.setDefaults(imgStatus, R.drawable.ic_block_grey_24dp);
        stateSwitcher.setCurrentState(UNKNOWN);

        stateSwitcher.putDrawable(R.drawable.ic_close_red_24dp, ERROR);
        stateSwitcher.putDrawable(R.drawable.ic_check_green_24dp, SUCCESS);
        stateSwitcher.putDrawable(-1, RUNNING);

        stateSwitcher.putView(progressBar, RUNNING);
        stateSwitcher.putView(imgStatus, ERROR);
        stateSwitcher.putView(imgStatus, SUCCESS);

    }

    private void testConfiguration() {
        ipAddressUpdatedStateSwitcher.setCurrentState(RUNNING);
        new UpdateOnlineIP(this).execute();
    }

    @Override
    public void onTaskFinished(AsyncTask task, Boolean result) {
        if (task instanceof UpdateOnlineIP) {
            ipAddressUpdatedStateSwitcher.setCurrentState(result ? SUCCESS : ERROR);
            if (result) {
                OpenDnsUpdater.getPrefs().edit()
                        .putString(PreferenceCodes.OPENDNS_USERNAME, this.openDnsUsername.getText().toString())
                        .putString(PreferenceCodes.OPENDNS_PASSWORD, this.openDnsPassword.getText().toString())
                        .putString(PreferenceCodes.OPENDNS_NETWORK, this.openDnsNetwork.getText().toString())
                        .apply();
            }
        }
    }
}