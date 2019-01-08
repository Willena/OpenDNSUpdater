package fr.guillaumevillena.opendnsupdater.activity.introSlide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.guillaumevillena.opendnsupdater.R;

public class IntroAccountFragment extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;
    private boolean hasAccount;

    public static IntroAccountFragment newInstance() {
        return new IntroAccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasAccount = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.intro_account, container, false);

        ((RadioGroup) root.findViewById(R.id.hasAccountToogle)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                //True if the selected button is hasAccount
                hasAccount = id == R.id.hasAccount;
            }
        });

        return root;
    }

    public boolean hasAccount() {
        return hasAccount;
    }
}