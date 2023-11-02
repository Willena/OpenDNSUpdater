package fr.guillaumevillena.opendnsupdater.activity.introSlide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.guillaumevillena.opendnsupdater.R;

public class IntroHowItWorks extends Fragment {

    public static IntroHowItWorks newInstance() {
        return new IntroHowItWorks();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_how_it_works, container, false);
    }


}