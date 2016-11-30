package com.lugmity.driverapp.activity.freelance;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.utils.GeoUtils;

/**
 * Scrolling Activity: Show Freelance delivered orders with map point.
 */
public class FreelanceOrderHistoryDetail extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Order order = Constants.order;
    TextView id, name, address, mobile, email, distance, price, points, accepted, delivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelance_order_history_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        id = (TextView) findViewById(R.id.txt_order);
        name = (TextView) findViewById(R.id.txt_name);
        address = (TextView) findViewById(R.id.txt_address);
        mobile = (TextView) findViewById(R.id.txt_mobile);
        email = (TextView) findViewById(R.id.txt_email);
        distance = (TextView) findViewById(R.id.txt_distance);
        price = (TextView) findViewById(R.id.txt_price);
        points = (TextView) findViewById(R.id.txt_points);
        accepted = (TextView) findViewById(R.id.txt_accepted);
        delivered = (TextView) findViewById(R.id.txt_delivered);

        id.setText("Order ID: " + order.orderId);
        name.setText("" + order.name);
        address.setText("" + order.address);
        mobile.setText("Mob: " + order.mobile);
        email.setText("Email: " + order.email);
        distance.setText("Distance: " + order.distance);
        price.setText("Earned: " + order.price + " " + order.currency);
        points.setText("Points: " + order.points);
        accepted.setText("Accepted on: " + order.created);
        delivered.setText("Delivered on: " + order.modified);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.addMarker(new MarkerOptions().position(GeoUtils.parseLatLng(order.google)).title(getResources().getString(R.string.orders_delivered)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pindone)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GeoUtils.parseLatLng(order.google)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
