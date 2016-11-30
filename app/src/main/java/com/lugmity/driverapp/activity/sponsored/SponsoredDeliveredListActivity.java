package com.lugmity.driverapp.activity.sponsored;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.driver.ShowCoordinateMapActivity;
import com.lugmity.driverapp.adapter.FreelanceOrderListAdapter;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.database.SQLiteHandler;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.utils.GeoUtils;

import java.util.ArrayList;

/**
 * Sponsored Driver: Show all delivered orders from SQLite DB.
 */
public class SponsoredDeliveredListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private TextView txtName;
    FreelanceOrderListAdapter adapter;
    ArrayList<Order> list = new ArrayList<>();

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
        doRefreshList();
    }

    /**
     * @method doRefreshList
     * @desc Method to refresh the list. Fetch data from SQLite and populate in listView.
     */
    private void doRefreshList() {
        Cursor cursor = SQLiteHandler.getInstance().query(Constants.SQL_QUERY + " where status = 'delivered';");
        list.clear();
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
        }
        if (list.size() < 1) {
            Toast.makeText(this, getResources().getString(R.string.empty_list), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        adapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Constants.order = list.get(position);
        Snackbar.make(view, getResources().getString(R.string.this_order_is_delivered), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Constants.POINT_DEST = GeoUtils.parseLatLng(list.get(position).google.trim());
        Snackbar.make(view, getResources().getString(R.string.show_on_map_q), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.show), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SponsoredDeliveredListActivity.this, ShowCoordinateMapActivity.class));
            }
        }).show();
        return true;
    }
}