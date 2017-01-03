package com.lugmity.driverapp.activity.sponsored;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import com.lugmity.driverapp.database.SQLiteHandler;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.AlertDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Sponsored Driver - Dashboard Activity.
 */
public class SponsoredDashboardActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

    private Button btnNewOrders, btnAssignedOrders, btnDeliveredOrders, btnPendingOrders, btnReverseOrders, btnOrderHistory;
    private ArrayList<Order> list = new ArrayList<Order>();

    private JSONObject jsonIds = new JSONObject();
    private JSONArray arrIds = new JSONArray();
    private JSONArray jarray = new JSONArray();

    private boolean flgLogout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsored_dashboad);
        btnNewOrders = (Button) findViewById(R.id.btn_new_orders);
        btnAssignedOrders = (Button) findViewById(R.id.btn_assigned_orders);
        btnDeliveredOrders = (Button) findViewById(R.id.btn_delivered_orders);
        btnPendingOrders = (Button) findViewById(R.id.btn_pending_orders);
        btnReverseOrders = (Button) findViewById(R.id.btn_reverse_orders);
        btnOrderHistory = (Button) findViewById(R.id.btn_order_history);

        btnNewOrders.setOnClickListener(this);
        btnAssignedOrders.setOnClickListener(this);
        btnDeliveredOrders.setOnClickListener(this);
        btnPendingOrders.setOnClickListener(this);
        btnReverseOrders.setOnClickListener(this);
        btnOrderHistory.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jsonIds = null;
        arrIds = null;
        jarray = null;

        //Create Sponsored Driver Database.
        SQLiteHandler.getInstance().setDatabase(this, Constants.DB);
        SQLiteHandler.getInstance().create(Constants.SQL_CREATE);
        if (Constants.LOGIN_FLAG == 1) {
            Constants.LOGIN_FLAG = 0;
            restoreOrderStatus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sponsored_dashbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sync:

                Toast.makeText(this, getResources().getString(R.string.sync_in_progress), Toast.LENGTH_SHORT).show();
                doSyncAction();
                break;

            case R.id.action_change_password:

                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            case R.id.action_logout:

                Constants.start = 0;
                flgLogout = true;
                doSyncAction();
                break;
        }
        return true;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_new_orders:

                startActivity(new Intent(this, SponsoredOpenOrderListActivity.class));
                break;

            case R.id.btn_assigned_orders:

                startActivity(new Intent(this, SponsoredAssignedListActivity.class));
                break;

            case R.id.btn_delivered_orders:

                startActivity(new Intent(this, SponsoredDeliveredListActivity.class));
                break;

            case R.id.btn_reverse_orders:

                startActivity(new Intent(this, SponsoredReverseListActivity.class));
                break;

            case R.id.btn_pending_orders:

                startActivity(new Intent(this, SponsoredPendingListActivity.class));
                break;

            case R.id.btn_order_history:

                //Test - to generate log of all rows in SQLite.
                Cursor cursor = SQLiteHandler.getInstance().query(Constants.SQL_QUERY + ";");
                while (cursor.moveToNext()) {
                    Order order = new Order();
                    order.id = cursor.getInt(0);
                    order.ordid = cursor.getString(1);
                    order.orderId = cursor.getString(2);
                    order.title = cursor.getString(3);
                    order.name = cursor.getString(4);
                    order.address = cursor.getString(5);
                    order.google = cursor.getString(6);
                    order.mobile = cursor.getString(7);
                    order.email = cursor.getString(8);
                    order.currency = cursor.getString(12);
                    order.status = cursor.getString(13);
                    order.sync = cursor.getString(14);
                    Log.d("SQLite ROW:", order.id + "**" + order.ordid + "**" + order.orderId + "**" + order.title + "**" + order.name + "**" + order.address + "**" + order.google + "**" + order.mobile + "**" + order.email + "**" + order.status + "**" + order.sync);
                }
                startActivity(new Intent(this, SponsoredTripHistoryActivity.class));
                break;
        }
    }

    /**
     * @method doSyncAction
     * @desc Sync the offline delivered/reverse orders with cloud database.
     */
    private void doSyncAction() {
        //fetch delivered/reverse orders from SQLite
        Cursor cursor = SQLiteHandler.getInstance().query(Constants.SQL_QUERY + " WHERE status = 'delivered' OR status = 'reverse';");
        list.clear();
        jsonIds = new JSONObject();     //make these ids delivered in lugmety.com
        arrIds = new JSONArray();
        jarray = new JSONArray();       //change status of array of orders on DB.
        try {
            while (cursor.moveToNext()) {
                Order order = new Order();
                //id, ordid, orderno, ordername, customer, address, geo, mob, email, distance, price, points, cur, status, sync
                order.id = cursor.getInt(0);
                order.ordid = cursor.getString(1);
                order.orderId = cursor.getString(2);
                order.title = cursor.getString(3);
                order.name = cursor.getString(4);
                order.address = cursor.getString(5);
                order.google = cursor.getString(6);
                order.mobile = cursor.getString(7);
                order.email = cursor.getString(8);
                //order.distance = cursor.getString(9);
                //order.price = cursor.getString(10);
                //order.points = cursor.getString(11);
                order.currency = cursor.getString(12);
                order.status = cursor.getString(13);
                order.sync = cursor.getString(14);
                list.add(order);

                JSONObject json = new JSONObject();
                json.put("orderid", "" + order.orderId.trim());
                json.put("loginid", Constants.LOGIN_ID.trim());
                json.put("status", "" + order.status.trim());
                json.put("distance", "");
                json.put("price", "");
                json.put("currency", "");
                json.put("points", "");
                jarray.put(json);

                if (order.status.contains("delivered")) {
                    arrIds.put(Integer.parseInt(order.orderId.trim()));
                }
            }
            jsonIds.put("order_id", arrIds);
        } catch (JSONException jsonE) {
            Toast.makeText(this, "JSONException:" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (jarray.length() == 0) {
            if (flgLogout == true) {
                SQLiteHandler.getInstance().execute("DELETE FROM orders;");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.nothing_to_sync), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("Initiating Sync Process", "Total Orders to sync: " + jarray.length());
            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.LURL_SET_COMPLETED, jsonIds.toString(), 1);
            httpTask.execute("");
        }
    }

    /**
     * @method restoreOrderStatus
     * @desc Method to restore all pending into SQLite from cloud DB.
     */
    private void restoreOrderStatus() {
        try {
            JSONObject jsonReq = new JSONObject();
            jsonReq.put("loginid", Constants.LOGIN_ID.trim());
            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.URL_CHECK_F_ORDER, jsonReq.toString(), 3);
            httpTask.execute("");
        } catch (JSONException jsonE) {
            Toast.makeText(this, "JSONException: " + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        if (code == 1) { //Make delivered in Lugemity.com
            try {
                JSONObject jsonResp = new JSONObject(data.trim());
                int apiCode = jsonResp.getInt("statusCode");
                String apiMsg = jsonResp.getString("statusMessage");
                if (apiCode == 200) {
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(this, this, "POST", Network.URL_SYNC_DONE_ORDERS, jarray.toString(), 2);
                    httpTask.execute("");
                } else {
                    Toast.makeText(this, "" + apiCode + "." + apiMsg + "{" + code + "}", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONException" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 2) { //Update status in Cloud DB - SYNC.
            try {
                JSONObject jsonResp = new JSONObject(data.trim());
                int apiCode = jsonResp.getInt("statusCode");
                String apiMsg = jsonResp.getString("statusMessage");
                if (apiCode == 200) {
                    SQLiteHandler.getInstance().execute("DELETE FROM orders WHERE status = 'delivered' OR status = 'reverse'");
                    Toast.makeText(this, getResources().getString(R.string.sync_process_completed_successfully), Toast.LENGTH_SHORT).show();
                    if (flgLogout == true) {
                        SQLiteHandler.getInstance().execute("DELETE FROM orders;");
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "" + apiCode + "." + apiMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONException:" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Exception:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 3) { //Get Pending List from cloud DB.
            try {
                JSONObject jsonResp = new JSONObject(data.trim());
                int apiCode = jsonResp.getInt("statusCode");
                String apiMsg = jsonResp.getString("statusMessage");
                if (apiCode == 200) {
                    JSONArray arrData = jsonResp.getJSONArray("data");
                    for (int i = 0; i < arrData.length(); i++) {
                        JSONObject jdata = arrData.getJSONObject(i);
                        //ordid, orderno, ordername, customer, address, geo, mob, email, distance, price, points, cur, status, sync
                        SQLiteDatabase sqlite = SQLiteHandler.getInstance().getDatabase();
                        SQLiteStatement stmt = sqlite.compileStatement(Constants.SQL_INSERT_ORDER.trim());
                        stmt.bindString(1, jdata.getString("ordid").trim()); //ordid
                        stmt.bindString(2, jdata.getString("orderid").trim()); //orderno
                        stmt.bindString(3, jdata.getString("ordertitle").trim()); //ordername
                        stmt.bindString(4, jdata.getString("custname").trim()); //customer
                        stmt.bindString(5, jdata.getString("address").trim()); //address
                        stmt.bindString(6, jdata.getString("geo").trim()); //geo
                        stmt.bindString(7, jdata.getString("mob").trim()); //mob
                        stmt.bindString(8, jdata.getString("email").trim()); //email
                        stmt.bindString(9, ""); //distance
                        stmt.bindString(10, ""); //price
                        stmt.bindString(11, ""); //points
                        stmt.bindString(12, ""); //cur
                        stmt.bindString(13, jdata.getString("status").trim()); //status
                        stmt.bindString(14, ""); //sync
                        int sql = stmt.executeUpdateDelete();
                    }
                    Toast.makeText(this, getResources().getString(R.string.total_orders_restored) + " " + arrData.length(), Toast.LENGTH_SHORT).show();
                } else if (apiCode == 203) {
                    Toast.makeText(this, "" + getResources().getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    //No Pending list in cloud DB.
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONExeception: " + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.failed), statusMessage);
        if (code == 1 || code == 2) {
            Toast.makeText(this, getResources().getString(R.string.sync_failed), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.sync_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.error), statusMessage);
        if (code == 1 || code == 2) {
            Toast.makeText(this, getResources().getString(R.string.sync_failed), Toast.LENGTH_SHORT).show();
        }
    }
}