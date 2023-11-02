package fr.guillaumevillena.opendnsupdater.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.model.SliderPage;

import fr.guillaumevillena.opendnsupdater.R;

public class CreateAccountWizard extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SliderPage createAccountSlide = new SliderPage();
        createAccountSlide.setTitle(getResources().getString(R.string.intro_account_creation_1_title));
        createAccountSlide.setDescription(getResources().getString(R.string.intro_account_creation_1_main_text));
        createAccountSlide.setBackgroundColorRes(R.color.colorPrimary);
        createAccountSlide.setImageDrawable(R.drawable.opendns_1_singup);
        addSlide(AppIntroFragment.newInstance(createAccountSlide));

        SliderPage loginSlide = new SliderPage();
        loginSlide.setTitle(getResources().getString(R.string.intro_account_creation_2_title));
        loginSlide.setDescription(getResources().getString(R.string.intro_account_creation_2_main_text));
        loginSlide.setBackgroundColorRes(R.color.colorPrimary);
        loginSlide.setImageDrawable(R.drawable.opendns_2_login);
        addSlide(AppIntroFragment.newInstance(loginSlide));

        SliderPage dashboardSlide = new SliderPage();
        dashboardSlide.setTitle(getResources().getString(R.string.intro_account_creation_3_title));
        dashboardSlide.setDescription(getResources().getString(R.string.intro_account_creation_3_main_text));
        dashboardSlide.setBackgroundColorRes(R.color.colorPrimary);
        dashboardSlide.setImageDrawable(R.drawable.opendns_3_dashboard);
        addSlide(AppIntroFragment.newInstance(dashboardSlide));

        SliderPage networkSlide = new SliderPage();
        networkSlide.setTitle(getResources().getString(R.string.intro_account_creation_4_title));
        networkSlide.setDescription(getResources().getString(R.string.intro_account_creation_4_main_text));
        networkSlide.setBackgroundColorRes(R.color.colorPrimary);
        networkSlide.setImageDrawable(R.drawable.opendns_4_addnet);
        addSlide(AppIntroFragment.newInstance(networkSlide));

        SliderPage nameNetSlide = new SliderPage();
        nameNetSlide.setTitle(getResources().getString(R.string.intro_account_creation_5_title));
        nameNetSlide.setDescription(getResources().getString(R.string.intro_account_creation_5_main_text));
        nameNetSlide.setBackgroundColorRes(R.color.colorPrimary);
        nameNetSlide.setImageDrawable(R.drawable.opendns_5_namenet);
        addSlide(AppIntroFragment.newInstance(nameNetSlide));

        SliderPage dashboradListNetSlide = new SliderPage();
        dashboradListNetSlide.setTitle(getResources().getString(R.string.intro_account_creation_6_title));
        dashboradListNetSlide.setDescription(getResources().getString(R.string.intro_account_creation_6_main_text));
        dashboradListNetSlide.setBackgroundColorRes(R.color.colorPrimary);
        dashboradListNetSlide.setImageDrawable(R.drawable.opendns_6_network_ok);
        addSlide(AppIntroFragment.newInstance(dashboradListNetSlide));

        SliderPage configureFilterSlide = new SliderPage();
        configureFilterSlide.setTitle(getResources().getString(R.string.intro_account_creation_7_title));
        configureFilterSlide.setDescription(getResources().getString(R.string.intro_account_creation_7_main_text));
        configureFilterSlide.setBackgroundColorRes(R.color.colorPrimary);
        configureFilterSlide.setImageDrawable(R.drawable.opendns_7_configurefilter);
        addSlide(AppIntroFragment.newInstance(configureFilterSlide));

//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        setSkipButtonEnabled(false);
        setButtonsEnabled(true);
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
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
