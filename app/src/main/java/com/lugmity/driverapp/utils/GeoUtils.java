package com.lugmity.driverapp.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Geo-Utils class for processing GPS/Map entities
 */
public class GeoUtils {

    /**
     * @param str coordinate
     * @return LatLng coordinate
     * @method parseLatLng
     * @desc Parse string coordinate format to LatLng coordinate.
     */
    public static LatLng parseLatLng(String str) {
        try {
            String[] arr = str.trim().split(",");
            double lat = Double.parseDouble(arr[0].trim());
            double lng = Double.parseDouble(arr[1].trim());
            return new LatLng(lat, lng);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
