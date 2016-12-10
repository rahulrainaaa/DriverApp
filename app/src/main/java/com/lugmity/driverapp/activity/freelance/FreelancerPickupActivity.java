package com.lugmity.driverapp.activity.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.model.Restaurant;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.ActivityUtils;
import com.lugmity.driverapp.utils.AlertDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Freelance Driver: Confirm Order Pickup from restaurant - Activity.
 */
public class FreelancerPickupActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

    private TextView rName, rAddress, rEmail, rMobile;
    private TextView cName, cAddress, cMobile, cEmail, cOrderID;
    private Button btnConfirm;
    private Order order = Constants.order;
    private Restaurant restaurant = Constants.restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        rName = (TextView) findViewById(R.id.rest_name);
        rAddress = (TextView) findViewById(R.id.rest_add);
        rEmail = (TextView) findViewById(R.id.rest_email);
        rMobile = (TextView) findViewById(R.id.rest_mob);

        cName = (TextView) findViewById(R.id.cust_name);
        cAddress = (TextView) findViewById(R.id.cust_add);
        cMobile = (TextView) findViewById(R.id.cust_mobile);
        cEmail = (TextView) findViewById(R.id.cust_email);
        cOrderID = (TextView) findViewById(R.id.order_id);

        rName.setText("" + restaurant.name);
        rAddress.setText("" + restaurant.address);
        rEmail.setText("Email: " + restaurant.email);
        rMobile.setText("Mob: " + restaurant.mobile);

        cName.setText("" + order.name);
        cAddress.setText("" + order.address);
        cMobile.setText("Mob: " + order.mobile);
        cEmail.setText("Email: " + order.email);
        cOrderID.setText("OrderNo: " + order.orderId);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setText("Confirm Pickup");
        btnConfirm.setOnClickListener(this);
        cMobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cust_mobile:
                if (Constants.order.mobile.trim() == null) {
                    return;
                }
                ActivityUtils.startDialer(this, Constants.order.mobile.trim());
                break;

            default:
                Snackbar.make(view, "Picking order from Restaurant ?", Snackbar.LENGTH_LONG).setAction("Confirm", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("from", restaurant.google.trim());
                            json.put("to", order.google.trim());
                            HTTPTask httpTask = new HTTPTask();
                            httpTask.setData(FreelancerPickupActivity.this, FreelancerPickupActivity.this, "POST", Network.URL_PRICING_GEO, json.toString(), 1);
                            httpTask.execute("");
                        } catch (JSONException jsonE) {
                            Toast.makeText(FreelancerPickupActivity.this, "Exception while packing JSON Request", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_freelance_pickup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, FreelanceNav2Restaurant.class));
        finish();
        return true;
    }

    /**
     * HttpCallback Methods
     */
    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {

        if (code == 1) {
            try {
                JSONObject json = new JSONObject(data.trim());
                int sCode = json.getInt("statusCode");
                String sMsg = json.getString("statusMessage");
                if (sCode == 200) {
                    //parse here the distance parameters.
                    JSONObject jsonRequest = new JSONObject();
                    JSONObject jsonData = json.getJSONObject("data");
                    double price = jsonData.getDouble("Price");
                    String currency = jsonData.getString("Currency");
                    int time = jsonData.getInt("Time");
                    int points = jsonData.getInt("Points");
                    double distance = jsonData.getDouble("Distance");
                    order.distance = distance;
                    order.time = time;
                    order.points = points;
                    order.currency = currency;
                    order.price = price;
                    jsonRequest.put("orderid", order.orderId);
                    jsonRequest.put("distance", distance);
                    jsonRequest.put("price", "");
                    jsonRequest.put("currency", "");
                    jsonRequest.put("points", points);
                    jsonRequest.put("status", "picked");
                    jsonRequest.put("loginid", Constants.LOGIN_ID);
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(FreelancerPickupActivity.this, FreelancerPickupActivity.this, "POST", Network.URL_F_ORDER_UPDATE, jsonRequest.toString(), 2);
                    httpTask.execute("");
                } else if (sCode == 204) {
                    AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.no_route), getResources().getString(R.string.no_route_to_restaurant));
                } else {
                    AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), "" + sCode + "." + sMsg);
                }

            } catch (JSONException jsonE) {
                Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 2) {
            try {
                JSONObject json = new JSONObject(data.trim());
                int sCode = json.getInt("statusCode");
                String sMsg = json.getString("statusMessage");
                if (sCode == 200) {
                    Toast.makeText(this, getResources().getString(R.string.order_picked_from_restaurant), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, FreelanceNav2Customer.class));
                    finish();
                    return;
                } else {
                    AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), "" + statusCode + "." + statusMessage.trim());
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.failed), "" + statusCode + "." + statusMessage.trim());
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.error), "" + statusMessage.trim());
    }
}