package com.lugmity.driverapp.activity.sponsored;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.driver.Current2CustMapActivity;
import com.lugmity.driverapp.adapter.FreelanceOrderListAdapter;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.database.SQLiteHandler;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.AlertDialogUtil;
import com.lugmity.driverapp.utils.GeoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Sponsored Driver- Assigned List from database (Respond: Accept/Reject)
 */
public class SponsoredAssignedListActivity extends AppCompatActivity implements HTTPCallback, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private TextView txtName;
    private FreelanceOrderListAdapter adapter;
    private ArrayList<Order> list = new ArrayList<>();      //List of all orders
    private Order order = null;                             //Selected Order from list.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsored_order_list);
        txtName = (TextView) findViewById(R.id.txtrestname);
        listView = (ListView) findViewById(R.id.listView1);
        adapter = new FreelanceOrderListAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        txtName.setText("" + Constants.sponsoredUserData.respRestroName.trim());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    /**
     * @method refreshList
     * @desc Fetches the list of all assigned orders.
     */
    private void refreshList() {
        try {
            list.clear();
            JSONObject json = new JSONObject();
            json.put("loginid", "" + Constants.LOGIN_ID.trim());
            json.put("status", "assigned");
            json.put("orderid", "");
            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.URL_GET_ORDERS, json.toString(), 1);
            httpTask.execute("");
        } catch (JSONException jsonE) {
            Toast.makeText(this, "JSONException" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.prompt));
        builder.setMessage(getResources().getString(R.string.respond_for_order));
        builder.setIcon(android.R.drawable.ic_menu_agenda);

        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonReq = new JSONObject();
                try {
                    order = list.get(position);
                    jsonReq.put("orderid", order.orderId.trim());
                    jsonReq.put("loginid", Constants.LOGIN_ID.trim());
                    jsonReq.put("status", "pending");
                    jsonReq.put("points", "");
                    jsonReq.put("price", "");
                    jsonReq.put("currency", "");
                    jsonReq.put("distance", "");
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(SponsoredAssignedListActivity.this, SponsoredAssignedListActivity.this, "POST", Network.URL_RESPOND_TO_ORDER, jsonReq.toString(), 2);
                    httpTask.execute("");
                } catch (JSONException jsonE) {
                    Toast.makeText(SponsoredAssignedListActivity.this, "JSONException" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.reject), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonReq = new JSONObject();
                try {
                    order = list.get(position);
                    jsonReq.put("orderid", order.orderId.trim());
                    jsonReq.put("loginid", Constants.LOGIN_ID.trim());
                    jsonReq.put("status", "reject");
                    jsonReq.put("points", "");
                    jsonReq.put("price", "");
                    jsonReq.put("currency", "");
                    jsonReq.put("distance", "");
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(SponsoredAssignedListActivity.this, SponsoredAssignedListActivity.this, "POST", Network.URL_RESPOND_TO_ORDER, jsonReq.toString(), 3);
                    httpTask.execute("");
                } catch (JSONException jsonE) {
                    Toast.makeText(SponsoredAssignedListActivity.this, "JSONException" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Constants.POINT_DEST = GeoUtils.parseLatLng(list.get(position).google.trim());
        Snackbar.make(view, getResources().getString(R.string.show_on_map_q), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.show), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SponsoredAssignedListActivity.this, Current2CustMapActivity.class));
            }
        }).show();
        return true;
    }


    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        if (code == 1) { //get assigned orders
            try {
                JSONObject jsonResp = new JSONObject(data);
                int apiCode = jsonResp.getInt("statusCode");
                String apiMsg = jsonResp.getString("statusMessage");
                if (apiCode == 200) {
                    JSONArray jarray = jsonResp.getJSONArray("data");
                    if (jarray.length() == 0) {
                        Toast.makeText(this, getResources().getString(R.string.empty_order_list), Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    int total = jarray.length();
                    for (int i = 0; i < total; i++) {
                        JSONObject jsonOrder = jarray.getJSONObject(i);
                        Order order = new Order();
                        order.ordid = jsonOrder.getString("ordid");
                        order.orderId = jsonOrder.getString("orderid");
                        order.title = jsonOrder.getString("ordertitle");
                        order.email = jsonOrder.getString("email");
                        order.mobile = jsonOrder.getString("mob");
                        order.google = jsonOrder.getString("geo");
                        order.name = jsonOrder.getString("custname");
                        order.address = jsonOrder.getString("address");
                        list.add(order);
                    }
                    adapter.notifyDataSetChanged();
                } else if (apiCode == 204) {
                    Toast.makeText(this, getResources().getString(R.string.no_assigned_order), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.response), "" + apiCode + "." + apiMsg);
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONException" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 2) { //accept-order
            try {
                JSONObject json = new JSONObject(data.trim());
                int apiCode = json.getInt("statusCode");
                String apiMsg = json.getString("statusMessage");
                if (apiCode == 200) {
                    //ordid, orderno, ordername, customer, address, geo, mob, email, distance, price, points, cur, status, sync
                    SQLiteDatabase sqlite = SQLiteHandler.getInstance().getDatabase();
                    SQLiteStatement stmt = sqlite.compileStatement(Constants.SQL_INSERT_ORDER.trim());
                    stmt.bindString(1, order.ordid.trim()); //ordid
                    stmt.bindString(2, order.orderId.trim()); //orderno
                    stmt.bindString(3, order.title.trim()); //ordername
                    stmt.bindString(4, order.name.trim()); //customer
                    stmt.bindString(5, order.address.trim()); //address
                    stmt.bindString(6, order.google.trim()); //geo
                    stmt.bindString(7, order.mobile.trim()); //mob
                    stmt.bindString(8, order.email.trim()); //email
                    stmt.bindString(9, ""); //distance
                    stmt.bindString(10, ""); //price
                    stmt.bindString(11, ""); //points
                    stmt.bindString(12, ""); //cur
                    stmt.bindString(13, "pending"); //status
                    stmt.bindString(14, ""); //sync
                    int sql = stmt.executeUpdateDelete();

                    //Update picked up status in Lugemity's portal.
                    HTTPTask httpTask = new HTTPTask();
                    String hitURL = "" + Network.LURL_ACCEPT_ORDER + Constants.order.orderId + "?" + Network.L_TOKEN_KEY;
                    httpTask.setData(this, this, "POST", hitURL, "{}", 4);
                    httpTask.execute("");
                } else if (apiCode == 204) {
                    refreshList();
                    Toast.makeText(this, getResources().getString(R.string.unable_toaccept_order), Toast.LENGTH_SHORT).show();
                } else {
                    refreshList();
                    Toast.makeText(this, "" + apiCode + "." + apiMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException jsonE) {
                refreshList();
                Toast.makeText(this, "JSONException:" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 3) { //reject-order
            try {
                JSONObject json = new JSONObject(data.trim());
                int apiCode = json.getInt("statusCode");
                String apiMsg = json.getString("statusMessage");
                if (apiCode == 200) {
                    Toast.makeText(this, getResources().getString(R.string.order_rejected), Toast.LENGTH_SHORT).show();
                } else if (apiCode == 204) {
                    Toast.makeText(this, getResources().getString(R.string.no_order_to_reject), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + apiCode + "." + apiMsg, Toast.LENGTH_SHORT).show();
                }
                refreshList();
            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONException:" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (code == 4) { //lugemity picked status
            try {
                JSONObject json = new JSONObject(data.trim());
                int apiCode = json.getInt("statusCode");
                String apiMsg = json.getString("statusMessage");
                if (apiCode == 200) {
                    String stData = json.getString("data");
                    if (stData.contains("done")) {
                        Toast.makeText(this, getResources().getString(R.string.orders_assigned), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.contact_owner), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), "" + apiCode + "." + apiMsg);
                }

            } catch (JSONException jsonE) {
                Toast.makeText(this, "JSONException:" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            }
            refreshList();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, "" + statusMessage, Toast.LENGTH_SHORT).show();
        if (code == 1) {
            finish();
        }
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, "" + statusMessage, Toast.LENGTH_SHORT).show();
        if (code == 1) {
            finish();
        }
    }
}