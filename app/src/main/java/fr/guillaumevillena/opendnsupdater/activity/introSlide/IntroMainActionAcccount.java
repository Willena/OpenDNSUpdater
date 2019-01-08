package fr.guillaumevillena.opendnsupdater.activity.introSlide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.guillaumevillena.opendnsupdater.R;

public class IntroMainActionAcccount extends Fragment {

    private static final String TAG = IntroMainActionAcccount.class.getSimpleName();
    private View root;

    public static IntroMainActionAcccount newInstance() {
        return new IntroMainActionAcccount();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.intro_main_content_account, container, false);

        return root;
    }

    public void visible(boolean hasAccount) {

        Log.d(TAG, "visible: hasAccount " + hasAccount);

        getChildFragmentManager().beginTransaction().replace(R.id.fragment_viewer,
                (hasAccount) ? IntroMainActionAccountCredentialFragment.newInstance() : IntroMainActionAccountCreateFragment.newInstance())
                .addToBackStack(null).commit();
    }
}