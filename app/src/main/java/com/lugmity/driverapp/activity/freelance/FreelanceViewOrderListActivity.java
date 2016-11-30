package com.lugmity.driverapp.activity.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.driver.Current2Rest2CustMapActivity;
import com.lugmity.driverapp.adapter.FreelanceOrderListAdapter;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.GeoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Freelance Driver: Show orders of a restaurant to accept by the freelance driver- Activity
 */
public class FreelanceViewOrderListActivity extends AppCompatActivity implements HTTPCallback, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private TextView txtName;
    FreelanceOrderListAdapter adapter;
    ArrayList<Order> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        txtName = (TextView) findViewById(R.id.txtrestname);
        listView = (ListView) findViewById(R.id.listView1);
        adapter = new FreelanceOrderListAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        txtName.setText("" + Constants.restaurant.name.trim());

        try {
            String id = Constants.restaurant.id;
            JSONObject jsonReq = new JSONObject();
            jsonReq.put("orderid", "");
            jsonReq.put("status", "open");
            jsonReq.put("restro_id", id.trim());
            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.URL_GET_SEL_F_ORDERS, jsonReq.toString(), 1);
            httpTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constants.order = list.get(position);
        Snackbar.make(view, "Are you sure ?", Snackbar.LENGTH_LONG).setAction("Accept", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreelanceViewOrderListActivity.this, FreelanceOrderAcceptActivity.class));
                finish();
            }
        }).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        Snackbar.make(view, "See delivery point ?", Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.show_map_q), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.POINT_START = GeoUtils.parseLatLng(Constants.restaurant.google.trim());
                Constants.POINT_DEST = GeoUtils.parseLatLng(list.get(position).google.trim());
                FreelanceViewOrderListActivity.this.startActivity(new Intent(FreelanceViewOrderListActivity.this, Current2Rest2CustMapActivity.class));
            }
        }).show();

        return true;
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        list.clear();
        try {
            JSONObject json = new JSONObject(data);
            int apiCode = json.getInt("statusCode");
            String apiMsg = json.getString("statusMessage");

            if (apiCode == 200) {
                //Parse the data
                JSONArray jarray = json.getJSONArray("data");
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
                    order.email = jsonOrder.getString("email");
                    order.mobile = jsonOrder.getString("mob");
                    order.google = jsonOrder.getString("geo");
                    order.name = jsonOrder.getString("custname");
                    order.address = jsonOrder.getString("address");
                    list.add(order);
                }
                adapter.notifyDataSetChanged();

            } else if (apiCode == 204) {
                Toast.makeText(this, getResources().getString(R.string.no_orders_found), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "" + apiMsg, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException jsonE) {
            Toast.makeText(this, "JSONException while parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        list.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        list.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }
}