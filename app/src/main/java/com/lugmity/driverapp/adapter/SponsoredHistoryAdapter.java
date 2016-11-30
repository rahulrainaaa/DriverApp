package com.lugmity.driverapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.model.Order;

import java.util.ArrayList;

/**
 * @class FreelanceHistoryAdapter
 * @desc ListView ArrayAdapter for showing order history details for freelance drivers.
 */
public class SponsoredHistoryAdapter extends ArrayAdapter<Order> {

    private Activity activity = null;
    private LayoutInflater inflater = null;
    private ArrayList<Order> list = null;

    static class ViewHolder {
        public TextView txtOrderNumber;
        public TextView txtName;
        public TextView txtAddress;
        public TextView txtDeliver;
    }

    public SponsoredHistoryAdapter(Activity activity, ArrayList<Order> list) {
        super(activity, R.layout.item_list_sponsored_history, list);
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        TextView txtOrderNumber = null;
        TextView txtName = null;
        TextView txtAddress = null;
        TextView txtDeliver = null;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_sponsored_history, null);
            viewHolder = new ViewHolder();
            viewHolder.txtOrderNumber = (TextView) view.findViewById(R.id.txt_order_no);
            viewHolder.txtName = (TextView) view.findViewById(R.id.txt_name);
            viewHolder.txtAddress = (TextView) view.findViewById(R.id.txt_address);
            viewHolder.txtDeliver = (TextView) view.findViewById(R.id.text_delivered);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        txtOrderNumber = viewHolder.txtOrderNumber;
        txtName = viewHolder.txtName;
        txtAddress = viewHolder.txtAddress;
        txtDeliver = viewHolder.txtDeliver;

        Order order = list.get(position);

        txtOrderNumber.setText("" + order.orderId);
        txtName.setText("" + order.name);
        txtAddress.setText("" + order.address);
        txtDeliver.setText("Delivered on: " + order.modified);

        return view;
    }
}
