package com.lugmity.driverapp.activity.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.adapter.FreelanceHistoryAdapter;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Freelance Driver: List of order delivery history.
 */
public class FreelanceHistoryActivity extends FragmentActivity implements HTTPCallback, AdapterView.OnItemClickListener {

    private ListView listView = null;
    private ArrayList<Order> list = new ArrayList<Order>();
    private FreelanceHistoryAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frelance_history);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new FreelanceHistoryAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("loginid", Constants.LOGIN_ID);
            jsonRequest.put("status", "delivered");
            jsonRequest.put("orderid", "");

            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.URL_GET_ORDERS, jsonRequest.toString(), 1);
            httpTask.execute("");
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constants.order = list.get(position);
        startActivity(new Intent(this, FreelanceOrderHistoryDetail.class));
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        JSONObject jsonResp = null;
        try {
            jsonResp = new JSONObject(data);
            int sCode = jsonResp.getInt("statusCode");
            String sMsg = jsonResp.getString("statusMessage");
            if (sCode == 200) {
                list.clear();
                JSONArray jsonDataArray = jsonResp.getJSONArray("data");
                int length = jsonDataArray.length();
                if (length == 0) {
                    Toast.makeText(this, getResources().getString(R.string.empty_list), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < length; i++) {
                    JSONObject json = jsonDataArray.getJSONObject(i);
                    Order order = new Order();
                    order.ordid = json.getString("ordid").trim();
                    order.orderId = json.getString("orderid").trim();
                    order.title = json.getString("ordertitle").trim();
                    order.name = json.getString("custname").trim();
                    order.address = json.getString("address").trim();
                    order.google = json.getString("geo").trim();
                    order.mobile = json.getString("mob").trim();
                    order.email = json.getString("email").trim();
                    order.distance = Double.parseDouble(json.getString("distance").trim());
                    order.price = Double.parseDouble(json.getString("price").trim());
                    order.currency = json.getString("currency").trim();
                    order.points = Integer.parseInt(json.getString("points").trim());
                    order.status = json.getString("status").trim();
                    order.loginid = json.getString("loginid").trim();
                    order.created = json.getString("created").trim();
                    order.modified = json.getString("modified").trim();

                    list.add(order);
                }
                adapter.notifyDataSetChanged();
            } else if (sCode == 204) {
                Toast.makeText(getApplicationContext(), sMsg.trim(), Toast.LENGTH_SHORT).show();
            } else if (sCode == 400) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_request), Toast.LENGTH_SHORT).show();
            } else if (sCode == 401) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unauthorized_request), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unknown_response_status), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception: Some internal error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, "" + statusMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, "" + statusMessage, Toast.LENGTH_SHORT).show();
    }
}