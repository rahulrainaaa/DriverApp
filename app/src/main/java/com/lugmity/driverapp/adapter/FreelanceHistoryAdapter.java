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
 * @desc ListView ArrayAdapter for showing order histort details for freelance drivers.
 */
public class FreelanceHistoryAdapter extends ArrayAdapter<Order> {

    private Activity activity = null;
    private LayoutInflater inflater = null;
    private ArrayList<Order> list = null;

    static class ViewHolder {
        public TextView txtOrderNumber;
        public TextView txtDistance;
        public TextView txtEarning;
        public TextView txtPoints;
        public TextView txtDate;
    }

    public FreelanceHistoryAdapter(Activity activity, ArrayList<Order> list) {
        super(activity, R.layout.item_list_freelance_history, list);
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        TextView txtOrderNumber = null;
        TextView txtDistance = null;
        TextView txtEarning = null;
        TextView txtPoints = null;
        TextView txtDate = null;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_freelance_history, null);
            viewHolder = new ViewHolder();
            viewHolder.txtOrderNumber = (TextView) view.findViewById(R.id.txt_order_no);
            viewHolder.txtDistance = (TextView) view.findViewById(R.id.txt_distance);
            viewHolder.txtEarning = (TextView) view.findViewById(R.id.txt_earnings);
            viewHolder.txtPoints = (TextView) view.findViewById(R.id.txt_points);
            viewHolder.txtDate = (TextView) view.findViewById(R.id.txt_date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        txtOrderNumber = viewHolder.txtOrderNumber;
        txtDistance = viewHolder.txtDistance;
        txtEarning = viewHolder.txtEarning;
        txtPoints = viewHolder.txtPoints;
        txtDate = viewHolder.txtDate;

        Order order = list.get(position);
        txtOrderNumber.setText("" + order.orderId);
        txtDistance.setText(order.distance + " Km");
        txtEarning.setText("" + order.price + " " + order.currency);
        txtPoints.setText("" + order.points);
        txtDate.setText(activity.getResources().getString(R.string.delivered_on_p) + " " + order.modified);

        return view;
    }
}
