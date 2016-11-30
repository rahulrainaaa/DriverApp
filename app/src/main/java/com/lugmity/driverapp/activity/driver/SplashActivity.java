package com.lugmity.driverapp.activity.driver;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.freelance.FreelanceDashbordActivity;
import com.lugmity.driverapp.activity.sponsored.SponsoredDashboardActivity;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.model.FreelanceUserData;
import com.lugmity.driverapp.model.SponsoredUserData;
import com.lugmity.driverapp.utils.AppLocalization;
import com.lugmity.driverapp.utils.CacheHandler;

import java.util.Locale;

/**
 * @class SplashActivity
 * @desc Class to handle splash screen UI Activity.
 */
public class SplashActivity extends FragmentActivity {

    private Animation animation = null;
    private ImageView imgLoading = null;
    private Class activity = LoginActivity.class;
    private Intent restartIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgLoading = (ImageView) findViewById(R.id.img_loading);
        animation = AnimationUtils.loadAnimation(this, R.anim.progress_rotate);
        imgLoading.startAnimation(animation);

        Constants.strUserType = CacheHandler.getInstance().checkLoginSession(this);
        if (Constants.strUserType == null) {
            activity = LoginActivity.class;
        } else if (Constants.strUserType.isEmpty()) {
            activity = LoginActivity.class;
        } else if (Constants.strUserType.equals("freelance")) {
            Constants.sponsoredUserData = null;
            Constants.freelanceUserData = new FreelanceUserData();
            CacheHandler.getInstance().loadFreelancerCache(this);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.already_login_as_freelance_driver), Toast.LENGTH_SHORT).show();
            activity = FreelanceDashbordActivity.class;
        } else if (Constants.strUserType.equals("sponsored")) {
            Constants.freelanceUserData = null;
            Constants.sponsoredUserData = new SponsoredUserData();
            CacheHandler.getInstance().loadSponsoredCache(this);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.already_login_as_sponsored_driver), Toast.LENGTH_SHORT).show();
            activity = SponsoredDashboardActivity.class;
        }

        //check Localization
        AppLocalization.getInstance().checkLocate(this);
        String languageToLoad = null;
        if (Constants.strLanguage.contains("arabic")) {
            languageToLoad = "ar";
        } else {
            languageToLoad = "en";
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        restartIntent = new Intent(SplashActivity.this, activity);
        restartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Constants.start = 0;
                startActivity(restartIntent);
                finish();
            }
        }, 1500);

    }

    @Override
    protected void onPause() {
        animation = null;
        imgLoading = null;
        super.onPause();
    }
}
