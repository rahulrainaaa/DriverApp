package com.lugmity.driverapp.activity.sponsored;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.driver.ShowCoordinateMapActivity;
import com.lugmity.driverapp.adapter.SponsoredHistoryAdapter;
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
 * Sponsored Driver: History of all delivered orders for given time bounds - Show List Activity
 */
public class SponsoredHistoryActivity extends FragmentActivity implements HTTPCallback, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView listView = null;
    private ArrayList<Order> list = new ArrayList<Order>();
    private SponsoredHistoryAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frelance_history);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new SponsoredHistoryAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        HTTPTask httpTask = new HTTPTask();
        httpTask.setData(this, this, "POST", Network.URL_GET_TRIP_HISTORY, Constants.httpJsonRequest.toString().trim(), 1);
        httpTask.execute("");
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        Constants.httpJsonRequest = null;
        list.clear();
        adapter.notifyDataSetChanged();
        try {
            JSONObject jsonResp = new JSONObject(data.trim());
            int apiCode = jsonResp.getInt("statusCode");
            String apiMsg = jsonResp.getString("statusMessage");
            if (apiCode == 200) {
                JSONArray array = jsonResp.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Order order = new Order();
                    order.orderId = json.getString("orderid");
                    order.ordid = json.getString("ordid");
                    order.title = json.getString("ordertitle");
                    order.name = json.getString("custname");
                    order.address = json.getString("address");
                    order.google = json.getString("geo");
                    order.mobile = json.getString("mob");
                    order.email = json.getString("email");
                    order.distance = Double.parseDouble("0" + json.getString("distance").trim());
                    order.price = Double.parseDouble("0" + json.getString("price").trim());
                    order.currency = json.getString("currency");
                    order.points = Integer.parseInt("0" + json.getString("points").trim());
                    order.status = json.getString("status");
                    order.loginid = json.getString("loginid");
                    order.restId = json.getString("restro_id");
                    order.created = json.getString("created");
                    order.modified = json.getString("modified");
                    list.add(order);
                }
                adapter.notifyDataSetChanged();
            } else if (apiCode == 204) {
                Toast.makeText(this, getResources().getString(R.string.empty_list), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "" + apiCode + ":" + apiMsg + "", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException jsonE) {
            Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, "" + statusMessage, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, "" + statusMessage, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Snackbar.make(view, getResources().getString(R.string.this_order_is_delivered), Snackbar.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constants.order = list.get(position);
        Constants.POINT_DEST = GeoUtils.parseLatLng(Constants.order.google.trim());
        Snackbar.make(view, getResources().getString(R.string.show_on_map_q), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.show), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SponsoredHistoryActivity.this, ShowCoordinateMapActivity.class));
            }
        }).show();
    }
}
