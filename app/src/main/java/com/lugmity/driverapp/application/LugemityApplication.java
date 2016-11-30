package com.lugmity.driverapp.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.database.SQLiteHandler;

import java.util.Locale;

/**
 * @class LugemityApplication
 * @desc Application class for Lugemity DriverApp. Handle App Localization and other configuration.
 */
public class LugemityApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String languageToLoad;
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
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        SQLiteHandler.getInstance().releaseDB();
        super.onTerminate();
        System.gc();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}