package com.lugmity.driverapp.activity.driver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.utils.PermissionCheck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Map routing for given two locations: DEST_POINT and START_POINT.
 */
public class GivenPositionMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, RoutingListener {

    private GoogleMap mMap = null;
    private LocationManager locationManager = null;
    private FloatingActionButton fab = null;
    private TextView txtDistance, txtTime;

    //Global UI Map markers
    private Marker currentMarker = null;
    private Marker destMarker = null;
    private LatLng currentLatLng = null;
    private Polyline line = null;

    //Global flags
    private boolean firstRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txtDistance = (TextView) findViewById(R.id.txt_distance);
        txtTime = (TextView) findViewById(R.id.txt_time);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setClickable(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GivenPositionMapActivity.this.getRoutingPath();
                fab.setClickable(false);
                Snackbar.make(v, getResources().getString(R.string.fetching_route), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstRefresh = true;
        //Ensure the GPS is ON and location permission enabled for the application.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!PermissionCheck.getInstance().checkGPSPermission(this, locationManager)) {
            //GPS not enabled for the application.
        } else if (!PermissionCheck.getInstance().checkLocationPermission(this)) {
            //Location permission not given.
        } else {
            Toast.makeText(GivenPositionMapActivity.this, getResources().getString(R.string.fetching_location), Toast.LENGTH_SHORT).show();
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
            } catch (Exception e) {
                Toast.makeText(GivenPositionMapActivity.this, "ERROR: Cannot start location listener", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        if (locationManager != null) {
            //Check needed in case of  API level 23.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            try {
                locationManager.removeUpdates(this);
            } catch (Exception e) {
            }
        }
        locationManager = null;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
        destMarker = mMap.addMarker(new MarkerOptions().position(Constants.POINT_DEST).title(getResources().getString(R.string.customer)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinc)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.POINT_DEST));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * @desc LocationListener Interface Methods implemented.
     */

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentLatLng = new LatLng(lat, lng);
        if (firstRefresh) {
            //Add Start Marker.
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title(getResources().getString(R.string.current)));
            firstRefresh = false;
            getRoutingPath();
        } else {
            currentMarker.setPosition(currentLatLng);

        }
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

    /**
     * @desc MapMarker Interface Methods Implemented.
     */

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().contains(getResources().getString(R.string.destination))) {
            //Do some task on dest pin click
        } else if (marker.getTitle().contains(getResources().getString(R.string.origin))) {
            //Do some task on current pin click
        }
        return false;
    }

    /**
     * @desc Routing Listener interface methods implemented.
     **/
    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(GivenPositionMapActivity.this, getResources().getString(R.string.routing_failed), Toast.LENGTH_SHORT).show();
        fab.setClickable(true);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(this, getResources().getString(R.string.fetching_route), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> list, int i) {
        try {
            //Get all points and plot the polyLine route.
            List<LatLng> listPoints = list.get(0).getPoints();
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            Iterator<LatLng> iterator = listPoints.iterator();
            while (iterator.hasNext()) {
                LatLng data = iterator.next();
                options.add(data);
            }

            //If line not null then remove old polyline routing.
            if (line != null) {
                line.remove();
            }
            line = mMap.addPolyline(options);

            //Show distance and duration.
            txtDistance.setText("Distance: " + list.get(0).getDistanceText());
            txtTime.setText("Duration: " + list.get(0).getDurationText());

            //Focus on map bounds
            mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(0).getLatLgnBounds().getCenter()));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLatLng);
            builder.include(Constants.POINT_DEST);
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        } catch (Exception e) {
            Toast.makeText(GivenPositionMapActivity.this, "EXCEPTION: Cannot parse routing response", Toast.LENGTH_SHORT).show();
        }
        fab.setClickable(true);
    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(GivenPositionMapActivity.this, getResources().getString(R.string.routing_cancelled), Toast.LENGTH_SHORT).show();
        fab.setClickable(true);
    }

    /**
     * @method getRoutingPath
     * @desc Method to draw the google routed path.
     */
    private void getRoutingPath() {
        try {
            //Do Routing
            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(currentLatLng, Constants.POINT_DEST)
                    .build();
            routing.execute();
        } catch (Exception e) {
            Toast.makeText(GivenPositionMapActivity.this, getResources().getString(R.string.unable_to_find_route), Toast.LENGTH_SHORT).show();
        }
    }
}