package com.lugmity.driverapp.activity.driver;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;

/**
 * Show Map Coordinate of customer - Activity.
 */
public class ShowCoordinateMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coordinate_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mark = Constants.POINT_DEST;
        mMap.addMarker(new MarkerOptions().position(mark).title(getResources().getString(R.string.customer_delivery)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinc)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
