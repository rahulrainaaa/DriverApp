package com.lugmity.driverapp.activity.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.lugmity.driverapp.utils.AlertDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Freelance Driver: Confirm Accept Order (before pickup confirmation).
 */
public class FreelanceOrderAcceptActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

    //View Objects - Global declaration
    TextView rName, rAddress, rEmail, rMobile;
    TextView cName, cAddress, cMobile, cEmail, cOrderID;
    Button btnConfirm;

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

        Restaurant restaurant = Constants.restaurant;
        rName.setText("" + restaurant.name);
        rAddress.setText("" + restaurant.address);
        rEmail.setText("Email: " + restaurant.email);
        rMobile.setText("Contact No: " + restaurant.mobile);

        Order order = Constants.order;
        cName.setText("" + order.name);
        cAddress.setText("" + order.address);
        cMobile.setText("Mob: " + order.mobile);
        cEmail.setText("Email: " + order.email);
        cOrderID.setText("OrderNo: " + order.orderId);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        if (order.pickupTime != null) {
            AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.pickup_time), order.pickupTime);
        }

    }

    @Override
    public void onClick(View view) {
        Snackbar.make(view, getResources().getString(R.string.are_you_sure_q), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.accept_q), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Restaurant restaurant = Constants.restaurant;
                    Order order = Constants.order;
                    JSONObject json = new JSONObject();
                    json.put("from", restaurant.google.trim());
                    json.put("to", order.google.trim());
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(FreelanceOrderAcceptActivity.this, FreelanceOrderAcceptActivity.this, "POST", Network.URL_PRICING_GEO, json.toString(), 0);
                    httpTask.execute("");
                } catch (JSONException jsonE) {
                    Toast.makeText(FreelanceOrderAcceptActivity.this, "Exception while packing JSON Request", Toast.LENGTH_SHORT).show();
                }
            }
        }).show();
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        if (code == 0) {
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
                    HTTPTask httpTask = new HTTPTask();
                    String hitURL = "" + Network.LURL_ACCEPT_ORDER + Constants.order.orderId + "?" + Network.L_TOKEN_KEY;
                    httpTask.setData(FreelanceOrderAcceptActivity.this, FreelanceOrderAcceptActivity.this, "POST", hitURL, "{}", 1);
                    httpTask.execute("");
                } else if (sCode == 204) {
                    AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.no_route), getResources().getString(R.string.no_route_to_restaurant));
                } else {
                    AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), "" + sCode + "." + sMsg);
                }

            } catch (JSONException jsonE) {
                Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 1) {
            try {
                JSONObject resp = new JSONObject(data.trim());
                int sCode = resp.getInt("statusCode");
                String sMsg = resp.getString("statusMessage");
                if (sCode == 200) {
                    String done = resp.getString("data");
                    if (done.contains("done")) {
                        //Add Order to Drushti database.
                        Order order = Constants.order;
                        JSONObject json = new JSONObject();
                        json.put("orderid", order.orderId);
                        json.put("distance", "0");
                        json.put("price", "0");
                        json.put("currency", "SA");
                        json.put("points", "0");
                        json.put("status", "Pending");
                        json.put("loginid", Constants.LOGIN_ID);
                        HTTPTask httpTask = new HTTPTask();
                        httpTask.setData(this, this, "POST", Network.URL_F_ORDER_ASSIGN, json.toString(), 2);
                        httpTask.execute("");
                    } else {
                        AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.already_assigned));
                        Toast.makeText(this, "" + getResources().getString(R.string.contact_admin), Toast.LENGTH_LONG).show();
                        btnConfirm.setEnabled(false);
                    }
                } else {
                    AlertDialogUtil.showErrorDialog(this, "" + sCode, sMsg.trim());
                    return;
                }
            } catch (JSONException jsonE) {
                jsonE.printStackTrace();
                Toast.makeText(this, "JSONException: " + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (code == 2) {
            try {
                JSONObject json = new JSONObject(data.trim());
                int sCode = json.getInt("statusCode");
                String sMsg = json.getString("statusMessage");
                if (sCode == 200) {
                    Toast.makeText(this, getResources().getString(R.string.order_successfully_assigned_to_you), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, FreelanceNav2Restaurant.class));
                    finish();
                } else {
                    Toast.makeText(this, "" + sCode + "." + sMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONException: " + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), statusMessage);
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), statusMessage);
    }
}