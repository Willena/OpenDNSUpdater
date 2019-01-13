package fr.guillaumevillena.opendnsupdater.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.guillaumevillena.opendnsupdater.R;

public class CreateAccountWizard extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SliderPage createAccountSlide = new SliderPage();
        createAccountSlide.setTitle("1. Create your account");
        createAccountSlide.setDescription("First go to signup.opendns.com/homefree and  fill in the form");
        createAccountSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
        createAccountSlide.setImageDrawable(R.drawable.opendns_1_singup);
        addSlide(AppIntroFragment.newInstance(createAccountSlide));

        SliderPage loginSlide = new SliderPage();
        loginSlide.setTitle("2. Log into your account");
        loginSlide.setDescription("Then login to dashboard.opendns.com");
        loginSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
        loginSlide.setImageDrawable(R.drawable.opendns_2_login);
        addSlide(AppIntroFragment.newInstance(loginSlide));

        SliderPage dashboardSlide = new SliderPage();
        dashboardSlide.setTitle("3. Add a network");
        dashboardSlide.setDescription("Then click on add a network");
        dashboardSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
        dashboardSlide.setImageDrawable(R.drawable.opendns_3_dashboard);
        addSlide(AppIntroFragment.newInstance(dashboardSlide));

        SliderPage networkSlide = new SliderPage();
        networkSlide.setTitle("4. Validate the network creation");
        networkSlide.setDescription("Click on Add this network. The ip address should already be filled");
        networkSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
        networkSlide.setImageDrawable(R.drawable.opendns_4_addnet);
        addSlide(AppIntroFragment.newInstance(networkSlide));

        SliderPage nameNetSlide = new SliderPage();
        nameNetSlide.setTitle("5. Name the network");
        nameNetSlide.setDescription("Add a name to your network");
        nameNetSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
        nameNetSlide.setImageDrawable(R.drawable.opendns_5_namenet);
        addSlide(AppIntroFragment.newInstance(nameNetSlide));

        SliderPage dashboradListNetSlide = new SliderPage();
        dashboradListNetSlide.setTitle("6. The network is ready to use");
        dashboradListNetSlide.setDescription("Your network is listed in the list on the dashboard");
        dashboradListNetSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
        dashboradListNetSlide.setImageDrawable(R.drawable.opendns_6_network_ok);
        addSlide(AppIntroFragment.newInstance(dashboradListNetSlide));

        SliderPage configureFilterSlide = new SliderPage();
        configureFilterSlide.setTitle("7. Configure your filters");
        configureFilterSlide.setDescription("Click on your ip to configure how website are filtered");
        configureFilterSlide.setBgColor(getResources().getColor(R.color.colorPrimary));
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
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
