package fr.guillaumevillena.opendnsupdater.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.activity.introSlide.IntroAccountFragment;
import fr.guillaumevillena.opendnsupdater.activity.introSlide.IntroMainActionAcccount;

public class ApplicationWizard extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle(getString(R.string.app_intro_welcom_title));
        sliderPage.setDescription("Welcome to OpenDNS Updater\nThe following steps will help you to configure the application");
        sliderPage.setImageDrawable(R.drawable.cellphone_settings_variant);
        sliderPage.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
        addSlide(IntroAccountFragment.newInstance());
        addSlide(IntroMainActionAcccount.newInstance());

//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

        if (oldFragment instanceof IntroAccountFragment && newFragment instanceof IntroMainActionAcccount) {
            IntroAccountFragment hasAccountFragment = (IntroAccountFragment) oldFragment;
            IntroMainActionAcccount accountActionFragment = (IntroMainActionAcccount) newFragment;
            accountActionFragment.visible(hasAccountFragment.hasAccount());

        }
    }
}
