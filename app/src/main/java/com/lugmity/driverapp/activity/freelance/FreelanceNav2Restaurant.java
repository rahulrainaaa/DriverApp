package com.lugmity.driverapp.activity.freelance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.model.Restaurant;
import com.lugmity.driverapp.utils.GeoUtils;
import com.lugmity.driverapp.utils.PermissionCheck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Freelance Driver: navigate to Restaurant - Map Activity.
 */
public class FreelanceNav2Restaurant extends FragmentActivity implements OnMapReadyCallback, LocationListener, RoutingListener {

    private GoogleMap mMap;
    private Marker currentMarker = null;
    private LatLng currentLatLng = null;
    private LocationManager locationManager = null;
    private boolean locationUpdates = false;
    private FloatingActionButton fab = null;
    private TextView txtDistance, txtTime;
    private Order order = null;
    private Restaurant restaurant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        txtTime = (TextView) findViewById(R.id.txt_time);
        txtDistance = (TextView) findViewById(R.id.txt_distance);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        order = Constants.order;
        restaurant = Constants.restaurant;
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
    public void onBackPressed() {
        startActivity(new Intent(this, FreelancerPickupActivity.class));
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.addMarker(new MarkerOptions().position(GeoUtils.parseLatLng(restaurant.google.trim())).title(getResources().getString(R.string.restaurant)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinr)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GeoUtils.parseLatLng(restaurant.google.trim())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentLatLng = new LatLng(lat, lng);
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Me"));
            try {
                //Do Routing
                Toast.makeText(this, getResources().getString(R.string.fetching_route), Toast.LENGTH_SHORT).show();
                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(this)
                        .waypoints(currentLatLng, GeoUtils.parseLatLng(restaurant.google.trim()))
                        .build();
                routing.execute();
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.unable_to_find_route), Toast.LENGTH_SHORT).show();
            }
        } else {
            currentMarker.setPosition(currentLatLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

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
     * @desc Routing Listener interface methods implemented.
     **/

    @Override
    public void onRoutingFailure(RouteException e) {
    }

    @Override
    public void onRoutingStart() {
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
            Polyline line = mMap.addPolyline(options);

            //Show distance and duration.
            txtDistance.setText("" + list.get(0).getDistanceText());
            txtTime.setText("" + list.get(0).getDurationText());

            //Focus on map bounds
            mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(0).getLatLgnBounds().getCenter()));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLatLng);
            builder.include(GeoUtils.parseLatLng(restaurant.google.trim()));
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        } catch (Exception e) {
            Toast.makeText(this, "EXCEPTION: Cannot parse routing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {
    }
}