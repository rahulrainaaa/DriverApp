package com.lugmity.driverapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * @class PermissionCheck
 * @desc Singleton-Class for checking the various permission for application.
 */
public class PermissionCheck {
    private static PermissionCheck ourInstance = new PermissionCheck();

    public static PermissionCheck getInstance() {
        return ourInstance;
    }

    private PermissionCheck() {
    }

    /**
     * @param activity Activity to the currently running activity.
     * @method checkLocationPermission
     * @desc Method to check Location permission and load setting activity UI to enable permissions.
     * @return boolean true when permission granted/enabled, else return false.
     */
    public boolean checkLocationPermission(Activity activity)
    {
        int check1 = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int check2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((check1 != PackageManager.PERMISSION_GRANTED) || (check2 != PackageManager.PERMISSION_GRANTED))
        {
            Toast.makeText(activity, "Go to permissions and \nEnable Location permission", Toast.LENGTH_LONG).show();
            startInstalledAppDetailsActivity(activity);
            activity = null;
            return false;
        }
        else
        {
            activity = null;
            return true;
        }

    }

    /**
     * @method checkGPSPermission
     * @desc Method to check the GPS enabled on off. Also check if network available for LOCATION_PROVIDER.
     * @param context Context to the current Activity UI.
     * @param locationManager LocationManager object.
     * @return boolean true when permission granted/enabled, else return false.
     */
    public boolean checkGPSPermission(Activity context, LocationManager locationManager)
    {
        //Check if GPS is enabled or not.
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
        {
            if (!locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) )
            {
                Toast.makeText(context, "WARNING: Internet or GPS needed for location provider.", Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                context = null;
                locationManager = null;
                return false;
            }
            else
            {
                context = null;
                locationManager = null;
                return true;
            }

        }
        else if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
        {
            //GPS is providing data.
            context = null;
            locationManager = null;
            return true;
        }
        else
        {
            context = null;
            locationManager = null;
            return true;
        }
    }

    /**
     * @param activity Activity to the currently running activity.
     * @method checkStorageAccessPermission
     * @desc Method to check External SDCard permission and load setting activity UI to enable permissions.
     * @return boolean true when permission granted/enabled, else return false.
     */
    public boolean checkStorageAccessPermission(Activity activity)
    {
        int check1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int check2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if ((check1 != PackageManager.PERMISSION_GRANTED) || (check2 != PackageManager.PERMISSION_GRANTED))
        {
            Toast.makeText(activity, "Go to permissions and \nEnable Storage permission", Toast.LENGTH_LONG).show();
            startInstalledAppDetailsActivity(activity);
            activity = null;
            return false;
        }
        else
        {
            activity = null;
            return true;
        }

    }

    /**
     * @method startInstalledAppDetailsActivity
     * @desc Method to start setting activity UI to enable specific permission.
     * @param context Context of the currently calling Activity UI.
     */
    public static void startInstalledAppDetailsActivity(Activity context)
    {
        if (context == null)
        {
            return;
        }
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
        context = null;
        i = null;
    }


}
