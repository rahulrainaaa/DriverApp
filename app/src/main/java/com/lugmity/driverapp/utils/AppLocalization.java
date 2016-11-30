package com.lugmity.driverapp.utils;

import android.app.Activity;

import com.lugmity.driverapp.constants.Constants;

/**
 * Singleton-Class for Application localization.
 */
public class AppLocalization {
    private static AppLocalization ourInstance = new AppLocalization();

    public static AppLocalization getInstance() {
        return ourInstance;
    }

    public void checkLocate(Activity activity) {
        Constants.strLanguage = CacheHandler.getInstance().getValue(activity, "language", "english").trim();
        if (Constants.strLanguage == null) {
            Constants.strLanguage = "english";
        } else if (!Constants.strLanguage.contains("arabic")) {
            Constants.strLanguage = "english";
        }
    }

    public void changeLocale(Activity activity) {
        CacheHandler.getInstance().setValue(activity, "language", Constants.strLanguage.trim().toLowerCase());
    }
}
