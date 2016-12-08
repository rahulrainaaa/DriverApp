package com.lugmity.driverapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Utility Class to start Activity from intent.
 */

public class ActivityUtils {

    /**
     * @method startDialer
     * @desc Start dialer application from intent for calling.
     * @param activity of the calling Activity.
     * @param string number to dial.
     */
    public static void startDialer(Activity activity, String string) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "Your Phone_number"));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Please enable the calling permission.", Toast.LENGTH_LONG).show();
            PermissionCheck.getInstance().startInstalledAppDetailsActivity(activity);
            return;
        }
        activity.startActivity(intent);
    }

}
