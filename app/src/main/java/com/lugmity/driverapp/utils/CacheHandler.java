package com.lugmity.driverapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.lugmity.driverapp.constants.Constants;

/**
 * Class responsible for handling cache (setting/removing - data). (SharedPreferences.Editor).
 */
public class CacheHandler {

    private static CacheHandler ourInstance = new CacheHandler();

    private SharedPreferences.Editor se = null;
    private SharedPreferences s = null;

    public static CacheHandler getInstance() {
        return ourInstance;
    }

    public void setFreelancerCache(Activity activity) {
        se = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE).edit();

        se.putString("respLoginId", Constants.freelanceUserData.respLoginId);
        se.putString("respUserId", Constants.freelanceUserData.respUserId);
        se.putString("respUserName", Constants.freelanceUserData.respUserName);
        se.putString("respPassword", Constants.freelanceUserData.respPassword);
        se.putString("respLastLogin", Constants.freelanceUserData.respLastLogin);
        se.putString("respIsActive", Constants.freelanceUserData.respIsActive);
        se.putString("respUserType", Constants.freelanceUserData.respUserType);
        se.putString("respAuthentication", Constants.freelanceUserData.respAuthentication);
        se.putString("respCountryCode", Constants.freelanceUserData.respAuthentication);

        se.putString("respFId", Constants.freelanceUserData.respFId);
        se.putString("respFName", Constants.freelanceUserData.respFName);
        se.putString("respFGender", Constants.freelanceUserData.respFGender);
        se.putString("respFMobile", Constants.freelanceUserData.respFMobile);
        se.putString("respFEmail", Constants.freelanceUserData.respFEmail);
        se.putString("respFAddress", Constants.freelanceUserData.respFAddress);
        se.putString("respFIdNo", Constants.freelanceUserData.respFIdNo);
        se.putString("respFDob", Constants.freelanceUserData.respFDob);
        se.putString("respFPhoto", Constants.freelanceUserData.respFPhoto);
        se.putString("respFRegistrationDate", Constants.freelanceUserData.respFRegistrationDate);
        se.putString("respFApproval", Constants.freelanceUserData.respFApproval);
        se.putString("respModified", Constants.freelanceUserData.respModified);

        se.putString("respEarnedMoney", Constants.freelanceUserData.respEarnedMoney);
        se.putString("respEarnedPoints", Constants.freelanceUserData.respEarnedPoints);
        se.putString("respTotalOrders", Constants.freelanceUserData.respTotalOrders);

        se.putString("login", "yes");
        Constants.strUserType = "freelance";
        Constants.LOGIN_ID = Constants.freelanceUserData.respLoginId.trim();

        se.commit();
        se = null;
    }

    public void clearFreelanceCache(Activity activity) {
        se = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE).edit();

        se.remove("respLoginId");
        se.remove("respUserId");
        se.remove("respUserName");
        se.remove("respPassword");
        se.remove("respLastLogin");
        se.remove("respIsActive");
        se.remove("respUserType");
        se.remove("respAuthentication");
        se.remove("respCountryCode");

        se.remove("respFId");
        se.remove("respFName");
        se.remove("respFGender");
        se.remove("respFMobile");
        se.remove("respFEmail");
        se.remove("respFAddress");
        se.remove("respFIdNo");
        se.remove("respFDob");
        se.remove("respFPhoto");
        se.remove("respFRegistrationDate");
        se.remove("respFApproval");
        se.remove("respModified");

        se.remove("respEarnedMoney");
        se.remove("respEarnedPoints");
        se.remove("respTotalOrders");

        se.remove("login");

        se.commit();
        se = null;
    }

    public void loadFreelancerCache(Activity activity) {
        s = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE);

        Constants.freelanceUserData.respLoginId = s.getString("respLoginId", Constants.freelanceUserData.respLoginId);
        Constants.freelanceUserData.respUserId = s.getString("respUserId", Constants.freelanceUserData.respUserId);
        Constants.freelanceUserData.respUserName = s.getString("respUserName", Constants.freelanceUserData.respUserName);
        Constants.freelanceUserData.respPassword = s.getString("respPassword", Constants.freelanceUserData.respPassword);
        Constants.freelanceUserData.respLastLogin = s.getString("respLastLogin", Constants.freelanceUserData.respLastLogin);
        Constants.freelanceUserData.respIsActive = s.getString("respIsActive", Constants.freelanceUserData.respIsActive);
        Constants.freelanceUserData.respUserType = s.getString("respUserType", Constants.freelanceUserData.respUserType);
        Constants.freelanceUserData.respAuthentication = s.getString("respAuthentication", Constants.freelanceUserData.respAuthentication);
        Constants.freelanceUserData.respCountryCode = s.getString("respCountryCode", Constants.freelanceUserData.respCountryCode);

        Constants.freelanceUserData.respFId = s.getString("respFId", Constants.freelanceUserData.respFId);
        Constants.freelanceUserData.respFName = s.getString("respFName", Constants.freelanceUserData.respFName);
        Constants.freelanceUserData.respFGender = s.getString("respFGender", Constants.freelanceUserData.respFGender);
        Constants.freelanceUserData.respFGender = s.getString("respFMobile", Constants.freelanceUserData.respFMobile);
        Constants.freelanceUserData.respFMobile = s.getString("respFEmail", Constants.freelanceUserData.respFEmail);
        Constants.freelanceUserData.respFAddress = s.getString("respFAddress", Constants.freelanceUserData.respFAddress);
        Constants.freelanceUserData.respFIdNo = s.getString("respFIdNo", Constants.freelanceUserData.respFIdNo);
        Constants.freelanceUserData.respFDob = s.getString("respFDob", Constants.freelanceUserData.respFDob);
        Constants.freelanceUserData.respFPhoto = s.getString("respFPhoto", Constants.freelanceUserData.respFPhoto);
        Constants.freelanceUserData.respFRegistrationDate = s.getString("respFRegistrationDate", Constants.freelanceUserData.respFRegistrationDate);
        Constants.freelanceUserData.respFApproval = s.getString("respFApproval", Constants.freelanceUserData.respFApproval);
        Constants.freelanceUserData.respModified = s.getString("respModified", Constants.freelanceUserData.respModified);

        Constants.freelanceUserData.respEarnedMoney = s.getString("respEarnedMoney", Constants.freelanceUserData.respEarnedMoney);
        Constants.freelanceUserData.respEarnedPoints = s.getString("respEarnedPoints", Constants.freelanceUserData.respEarnedPoints);
        Constants.freelanceUserData.respTotalOrders = s.getString("respTotalOrders", Constants.freelanceUserData.respTotalOrders);

        Constants.strUserType = "freelance";
        Constants.LOGIN_ID = Constants.freelanceUserData.respLoginId.trim();

        s = null;
    }

    public void setSponsoredCache(Activity activity) {

        se = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE).edit();

        se.putString("respLoginId", Constants.sponsoredUserData.respLoginId);
        se.putString("respUserId", Constants.sponsoredUserData.respUserId);
        se.putString("respUserName", Constants.sponsoredUserData.respUserName);
        se.putString("respPassword", Constants.sponsoredUserData.respPassword);
        se.putString("respLastLogin", Constants.sponsoredUserData.respLastLogin);
        se.putString("respIsActive", Constants.sponsoredUserData.respIsActive);
        se.putString("respUserType", Constants.sponsoredUserData.respUserType);
        se.putString("respAuthentication", Constants.sponsoredUserData.respAuthentication);
        //se.putString("respCountryCode", Constants.sponsoredUserData.respCountryCode);

        se.putString("sid", Constants.sponsoredUserData.respSID);
        se.putString("sname", Constants.sponsoredUserData.respSName);
        se.putString("saddress", Constants.sponsoredUserData.respSAddress);
        se.putString("scity", Constants.sponsoredUserData.respSCity);
        se.putString("smobile", Constants.sponsoredUserData.respSMobile);
        se.putString("sregistration_date", Constants.sponsoredUserData.respSRegistrationDate);
        se.putString("sapproval", Constants.sponsoredUserData.respSApproval);
        se.putString("isActive", Constants.sponsoredUserData.respIsActive);

        se.putString("respRestoId", Constants.sponsoredUserData.respRestoId);
        se.putString("respRestroName", Constants.sponsoredUserData.respRestroName);
        se.putString("respRestroUID", Constants.sponsoredUserData.respRestroUID);


        se.putString("login", "yes");

        Constants.strUserType = "sponsored";
        Constants.LOGIN_ID = Constants.sponsoredUserData.respLoginId.trim();

        se.commit();
        se = null;
    }

    public void clearSponsoredCache(Activity activity) {
        se = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE).edit();

        se.remove("respLoginId");
        se.remove("respUserId");
        se.remove("respUserName");
        se.remove("respPassword");
        se.remove("respLastLogin");
        se.remove("respIsActive");
        se.remove("respUserType");
        se.remove("respAuthentication");
        //se.remove("respCountryCode");

        se.remove("sid");
        se.remove("sname");
        se.remove("saddress");
        se.remove("scity");
        se.remove("smobile");
        se.remove("sregistration_date");
        se.remove("sapproval");
        se.remove("isActive");

        se.remove("respRestoId");
        se.remove("respRestroName");
        se.remove("respRestroUID");

        se.remove("login");

        se.commit();
        se = null;
    }

    public void loadSponsoredCache(Activity activity) {
        s = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE);

        Constants.sponsoredUserData.respLoginId = s.getString("respLoginId", Constants.sponsoredUserData.respLoginId);
        Constants.sponsoredUserData.respUserId = s.getString("respUserId", Constants.sponsoredUserData.respUserId);
        Constants.sponsoredUserData.respUserName = s.getString("respUserName", Constants.sponsoredUserData.respUserName);
        Constants.sponsoredUserData.respPassword = s.getString("respPassword", Constants.sponsoredUserData.respPassword);
        Constants.sponsoredUserData.respLastLogin = s.getString("respLastLogin", Constants.sponsoredUserData.respLastLogin);
        Constants.sponsoredUserData.respIsActive = s.getString("respIsActive", Constants.sponsoredUserData.respIsActive);
        Constants.sponsoredUserData.respUserType = s.getString("respUserType", Constants.sponsoredUserData.respUserType);
        Constants.sponsoredUserData.respAuthentication = s.getString("respAuthentication", Constants.sponsoredUserData.respAuthentication);
        //Constants.sponsoredUserData.respCountryCode = s.getString("respCountryCode", Constants.sponsoredUserData.respCountryCode);

        Constants.sponsoredUserData.respSID = s.getString("sid", Constants.sponsoredUserData.respSID);
        Constants.sponsoredUserData.respSName = s.getString("sname", Constants.sponsoredUserData.respSName);
        Constants.sponsoredUserData.respSAddress = s.getString("saddress", Constants.sponsoredUserData.respSAddress);
        Constants.sponsoredUserData.respSCity = s.getString("scity", Constants.sponsoredUserData.respSCity);
        Constants.sponsoredUserData.respSMobile = s.getString("smobile", Constants.sponsoredUserData.respSMobile);
        Constants.sponsoredUserData.respSRegistrationDate = s.getString("sregistration_date", Constants.sponsoredUserData.respSRegistrationDate);
        Constants.sponsoredUserData.respSApproval = s.getString("sapproval", Constants.sponsoredUserData.respSApproval);
        Constants.sponsoredUserData.respIsActive = s.getString("isActive", Constants.sponsoredUserData.respIsActive);

        Constants.sponsoredUserData.respRestoId = s.getString("respRestoId", Constants.sponsoredUserData.respRestoId);
        Constants.sponsoredUserData.respRestroName = s.getString("respRestroName", Constants.sponsoredUserData.respRestroName);
        Constants.sponsoredUserData.respRestroUID = s.getString("respRestroUID", Constants.sponsoredUserData.respRestroUID);

        Constants.strUserType = "sponsored";
        Constants.LOGIN_ID = Constants.sponsoredUserData.respLoginId.trim();

        s = null;
    }

    public void clearCache(Activity activity) {
        if (Constants.strUserType.contains("freelance")) {
            clearFreelanceCache(activity);
        } else if (Constants.strUserType.contains("sponsored")) {
            clearSponsoredCache(activity);
        }
        activity = null;
    }

    public String checkLoginSession(Activity activity) {
        s = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE);
        String status = s.getString("login", "").trim();
        if (status.contains("yes")) {
            return s.getString("respUserType", "").trim();
        } else {
            return "";
        }
    }

    public String getValue(Activity activity, String key, String defaultValue) {
        s = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE);
        defaultValue = s.getString(key, defaultValue);
        s = null;
        return defaultValue;
    }

    public void setValue(Activity activity, String key, String value) {
        se = activity.getSharedPreferences(Constants.CACHE_DATA.trim(), Context.MODE_PRIVATE).edit();
        se.putString(key, value);
        se.commit();
        se = null;
    }
}
