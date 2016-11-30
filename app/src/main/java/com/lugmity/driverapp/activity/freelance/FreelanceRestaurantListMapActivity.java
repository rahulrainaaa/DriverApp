package com.lugmity.driverapp.activity.freelance;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Restaurant;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.GeoUtils;
import com.lugmity.driverapp.utils.PermissionCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Freelance Driver: Showing restaurants points on map for for restaurant pickup - Map Activity.
 */
public class FreelanceRestaurantListMapActivity extends FragmentActivity implements OnMapReadyCallback, HTTPCallback, GoogleMap.OnMapLongClickListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker currentMarker = null;
    private LatLng currentLatLng = null;
    private LocationManager locationManager = null;
    private JSONArray jarray = null;
    private ArrayList<Marker> markerList = new ArrayList<Marker>();
    private boolean locationUpdates = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list_map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Ensure the GPS is ON and location permission enabled for the application.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!PermissionCheck.getInstance().checkGPSPermission(this, locationManager)) {
            //GPS not enabled for the application.
        } else if (!PermissionCheck.getInstance().checkLocationPermission(this)) {
            //Location permission not given.
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Toast.makeText(this, getResources().getString(R.string.fetching_location), Toast.LENGTH_SHORT).show();
            try {
                locationUpdates = true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
            } catch (Exception e) {
                Toast.makeText(this, "ERROR: Cannot start location listener", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (locationUpdates) {
                locationManager.removeUpdates(this);
                locationUpdates = false;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        currentMarker.setPosition(latLng);
        currentLatLng = latLng;
        Toast.makeText(this, "Lat,Lng:\n" + latLng.latitude + ",\n" + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(25));
        if (marker.getTitle().trim().equalsIgnoreCase(getResources().getString(R.string.origin))) {
            return false;
        }
        final String markerTitle = marker.getTitle();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.show_orders));
        alertDialogBuilder.setMessage(marker.getTitle());
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Call method for handling search for address queried.
                        String[] arr = markerTitle.trim().split("\\.");
                        int pos = Integer.parseInt(arr[0].trim());
                        pos--;
                        try {
                            JSONObject jsonRest = jarray.getJSONObject(pos);
                            Restaurant restaurant = new Restaurant();
                            restaurant.id = jsonRest.getString("restro_uniqueId");
                            restaurant.name = jsonRest.getString("restro_name");
                            restaurant.google = "" + jsonRest.getString("restro_latitude") + "," + jsonRest.getString("restro_longitude");
                            restaurant.email = jsonRest.getString("restro_email");
                            restaurant.phone = "";//jsonRest.getString("phone");
                            restaurant.mobile = jsonRest.getString("restro_phoneno");
                            String tempAddress = jsonRest.getString("restro_address");
                            String tempZipCode = jsonRest.getString("restro_pin");
                            restaurant.address = "" + tempAddress + "-" + tempZipCode;
                            Constants.restaurant = restaurant;
                            startActivity(new Intent(FreelanceRestaurantListMapActivity.this, FreelanceViewOrderListActivity.class));
                            finish();
                        } catch (JSONException jsonE) {
                            jsonE.printStackTrace();
                            Toast.makeText(FreelanceRestaurantListMapActivity.this, "JSONException: Unable to parse response array", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentLatLng = new LatLng(lat, lng);
        currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title(getResources().getString(R.string.origin)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
        HTTPTask httpTask = new HTTPTask();
        httpTask.setData(this, this, "POST", Network.URL_GET_ALL_REST, "{}", 1);
        httpTask.execute();
        //mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        Iterator<Marker> iterator = markerList.iterator();
        Marker tempMarker = null;
        while (iterator.hasNext()) {
            tempMarker = iterator.next();
            tempMarker.remove();
            iterator.remove();
        }
        markerList.clear();
        try {
            JSONObject json = new JSONObject(data);
            int apiCode = json.getInt("statusCode");
            String apiMsg = json.getString("statusMessage");
            if (apiCode == 200) {
                //Parse the data
                jarray = json.getJSONArray("data");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject j = jarray.getJSONObject(i);
                    String name = j.getString("restro_name");
                    String google = "" + j.getString("restro_latitude") + "," + j.getString("restro_longitude");
                    markerList.add(mMap.addMarker(new MarkerOptions().position(GeoUtils.parseLatLng(google)).title((i + 1) + ". " + name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinr))));
                    builder.include(GeoUtils.parseLatLng(google));
                }
                if (jarray.length() > 0) {
                    LatLngBounds bounds = builder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                } else {
                    Toast.makeText(this, "" + getResources().getString(R.string.no_rest_fnd), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "" + apiMsg, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException jsonE) {
            Toast.makeText(this, "JSONException while parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }
}