package com.lugmity.driverapp.activity.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.driver.ChangePasswordActivity;
import com.lugmity.driverapp.activity.driver.LoginActivity;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.model.Restaurant;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.AlertDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @class FreelanceDashboardActivity
 * @desc Activity class to handle freelancer dashboard.
 */
public class FreelanceDashbordActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

    private Button btnOrder, btnOrderHistory, btnPointsCollected, btnMyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelance_dashbord);

        btnOrder = (Button) findViewById(R.id.btn_order);
        btnOrderHistory = (Button) findViewById(R.id.btn_order_history);
        btnPointsCollected = (Button) findViewById(R.id.btn_points_collected);
        btnMyDetails = (Button) findViewById(R.id.btn_my_details);

        btnOrder.setOnClickListener(this);
        btnOrderHistory.setOnClickListener(this);
        btnPointsCollected.setOnClickListener(this);
        btnMyDetails.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_freelance_dashbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_home:

                break;

            case R.id.action_order_history:

                startActivity(new Intent(this, FreelanceHistoryActivity.class));
                break;

            case R.id.action_points_collected:

                startActivity(new Intent(this, FreelancePointsActivity.class));
                break;

            case R.id.action_my_details:

                startActivity(new Intent(this, FreelanceDetailsActivity.class));
                break;

            case R.id.action_change_password:

                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            case R.id.action_logout:

                Constants.start = 0;
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_order:

                checkOrderStatus();
                break;
            case R.id.btn_order_history:

                startActivity(new Intent(this, FreelanceHistoryActivity.class));
                break;
            case R.id.btn_points_collected:

                startActivity(new Intent(this, FreelancePointsActivity.class));
                break;
            case R.id.btn_my_details:

                startActivity(new Intent(this, FreelanceDetailsActivity.class));
                break;
        }
    }

    /**
     * @desc Method check for any pending delivery for freelance driver and navigate accordingly.
     */
    private void checkOrderStatus() {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("loginid", Constants.LOGIN_ID.trim());
            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.URL_CHECK_F_ORDER, jsonRequest.toString(), 1);
            httpTask.execute("");
        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
            Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        try {
            Log.d("Check order", data.trim());
            JSONObject json = new JSONObject(data.trim());
            int sCode = json.getInt("statusCode");
            String sMsg = json.getString("statusMessage");
            if (sCode == 200) {
                JSONArray jarray = json.getJSONArray("data");
                int length = jarray.length();
                if (length > 0) {
                    JSONObject jdata = jarray.getJSONObject(0);
                    String ordid = jdata.getString("ordid");
                    String orderid = jdata.getString("orderid");
                    String ordertitle = jdata.getString("ordertitle");
                    String custname = jdata.getString("custname");
                    String address = jdata.getString("address");
                    String geo = jdata.getString("geo");
                    String mob = jdata.getString("mob");
                    String email = jdata.getString("email");
                    String currency = jdata.getString("currency");
                    String status = jdata.getString("status");
                    String loginid = jdata.getString("loginid");
                    String restro_uniqueId = jdata.getString("restro_uniqueId");
                    String restro_name = jdata.getString("restro_name");
                    String restro_address = jdata.getString("restro_address");
                    String restro_latitude = jdata.getString("restro_latitude");
                    String restro_longitude = jdata.getString("restro_longitude");
                    String restro_mobile = jdata.getString("restro_mobile");
                    String restro_email = jdata.getString("restro_email");

                    Order order = new Order();
                    order.ordid = ordid.trim();
                    order.orderId = orderid.trim();
                    order.title = ordertitle.trim();
                    order.name = custname.trim();
                    order.address = address.trim();
                    order.google = geo.trim();
                    order.mobile = mob.trim();
                    order.email = email.trim();
                    order.currency = currency.trim();
                    order.status = status.trim();
                    order.loginid = loginid.trim();

                    Restaurant restaurant = new Restaurant();
                    restaurant.name = restro_name.trim();
                    restaurant.address = restro_address.trim();
                    restaurant.google = "" + restro_latitude + "," + restro_longitude;
                    restaurant.id = restro_uniqueId.trim();
                    restaurant.mobile = restro_mobile.trim();
                    restaurant.email = restro_email.trim();

                    Constants.order = order;
                    Constants.restaurant = restaurant;

                    if (status.equalsIgnoreCase("pending")) {
                        Toast.makeText(this, getResources().getString(R.string.order_is_already_pending_for_you), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, FreelanceNav2Restaurant.class));
                    } else if (status.equalsIgnoreCase("picked")) {
                        Toast.makeText(this, getResources().getString(R.string.order_is_already_for_delivery), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, FreelanceNav2Customer.class));
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.no_pending_orders), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (sCode == 204) {
                    startActivity(new Intent(this, FreelanceRestaurantListMapActivity.class));
                }
                if (sCode == 203) {
                    Toast.makeText(this, getResources().getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                    Constants.start = 0;
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    return;
                }
            }
        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
            Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.failed), "" + statusCode + "." + statusMessage + "");
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.error), "" + statusMessage + "");
    }
}