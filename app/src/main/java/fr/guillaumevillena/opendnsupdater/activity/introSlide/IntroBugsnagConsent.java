package fr.guillaumevillena.opendnsupdater.activity.introSlide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;

public class IntroBugsnagConsent extends Fragment {

    public static IntroBugsnagConsent newInstance() {
        return new IntroBugsnagConsent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bugsnag_consent, container, false);

        SwitchCompat switchCompat = root.findViewById(R.id.fragment_intro_bugsnag_consent_switch);
        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> OpenDnsUpdater.getPrefs().edit().putBoolean(PreferenceCodes.BUGSNAG_ACTIVATED, b).apply());

        OpenDnsUpdater.getPrefs().edit().putBoolean(PreferenceCodes.BUGSNAG_ACTIVATED, switchCompat.isChecked()).apply();

        return root;
    }


}