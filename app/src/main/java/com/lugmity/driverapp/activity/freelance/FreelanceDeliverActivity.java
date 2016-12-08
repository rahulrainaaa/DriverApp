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
 * Freelance Driver: Confirm Order Delivery at Customer point - Activity.
 */
public class FreelanceDeliverActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

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
        rMobile.setText("Contact No: " + restaurant.mobile);

        cName.setText("" + order.name);
        cAddress.setText("" + order.address);
        cMobile.setText("Mob: " + order.mobile);
        cEmail.setText("Email: " + order.email);
        cOrderID.setText("OrderNo: " + order.orderId);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setText(getResources().getString(R.string.confirm_delivery));
        btnConfirm.setOnClickListener(this);
        cMobile.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_freelance_pickup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, FreelanceNav2Customer.class));
        finish();
        return true;
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

                Snackbar.make(view, getResources().getString(R.string.parcel_delivered_q), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("order_status", "");
                            json.put("payment_status", "");
                            json.put("is_delivered", "Delivered");
                            json.put("token-key", Network.API_KEY);
                            HTTPTask httpTask = new HTTPTask();
                            String url = "" + Network.LURL_ORDER_EDIT + "" + order.orderId + "?" + Network.L_TOKEN_KEY;
                            //Toast.makeText(FreelanceDeliverActivity.this, "" + url.trim(), Toast.LENGTH_SHORT).show();
                            httpTask.setData(FreelanceDeliverActivity.this, FreelanceDeliverActivity.this, "POST", url, json.toString(), 1);
                            httpTask.execute("");
                        } catch (JSONException jsonE) {
                            jsonE.printStackTrace();
                            Toast.makeText(FreelanceDeliverActivity.this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        if (code == 1) {
            try {
                JSONObject json = new JSONObject(data.trim());
                int sCode = json.getInt("statusCode");
                String sMsg = json.getString("statusMessage");
                if (sCode == 200) {
                    JSONObject jsonReq = new JSONObject();
                    jsonReq.put("orderid", order.orderId.trim());
                    jsonReq.put("distance", "");
                    jsonReq.put("price", "");
                    jsonReq.put("currency", "");
                    jsonReq.put("points", "");
                    jsonReq.put("status", "delivered");
                    jsonReq.put("loginid", Constants.LOGIN_ID.trim());
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(this, this, "POST", Network.URL_F_ORDER_UPDATE, jsonReq.toString(), 2);
                    httpTask.execute("");
                } else {
                    AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), "" + sCode + "." + sMsg.trim());
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 2) {
            try {
                JSONObject json = new JSONObject(data.trim());
                AlertDialogUtil.showAlertDialog(this, "resp", data.trim());
                int sCode = json.getInt("statusCode");
                String sMsg = json.getString("statusMessage");
                if (sCode == 200) {
                    Toast.makeText(this, getResources().getString(R.string.orders_delivered_success), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), "" + sCode + "." + sMsg.trim());
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), "" + statusCode + "." + statusMessage);
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), "" + statusMessage);
    }
}
