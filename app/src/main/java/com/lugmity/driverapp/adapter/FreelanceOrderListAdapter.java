package com.lugmity.driverapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.model.Order;

import java.util.ArrayList;

/**
 * @class FreelanceOrderListAdapter
 * @desc Adapter Class for handling Order List in ListView
 */
public class FreelanceOrderListAdapter extends ArrayAdapter<Order> {

    Activity activity;
    ArrayList<Order> list;
    LayoutInflater inflater;

    static class ViewHolder {
        public TextView txtOrderNumber;
        public TextView txtName;
        public TextView txtAddress;
        public TextView txtEmail;
        public TextView txtMobile;

    }

    public FreelanceOrderListAdapter(Activity activity, ArrayList<Order> list) {
        super(activity, R.layout.item_list_order_list, list);
        this.activity = activity;
        this.list = list;
        this.inflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        TextView txtOrderNumber = null;
        TextView txtName = null;
        TextView txtAddress = null;
        TextView txtEmail = null;
        TextView txtMobile = null;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_order_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtOrderNumber = (TextView) view.findViewById(R.id.txt_order_no);
            viewHolder.txtAddress = (TextView) view.findViewById(R.id.txt_address);
            viewHolder.txtEmail = (TextView) view.findViewById(R.id.txt_order_email);
            viewHolder.txtMobile = (TextView) view.findViewById(R.id.txt_order_phone);
            viewHolder.txtName = (TextView) view.findViewById(R.id.txt_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        txtOrderNumber = viewHolder.txtOrderNumber;
        txtName = viewHolder.txtName;
        txtAddress = viewHolder.txtAddress;
        txtEmail = viewHolder.txtEmail;
        txtMobile = viewHolder.txtMobile;

        Order order = list.get(position);
        txtOrderNumber.setText("Order No: " + order.orderId);
        txtName.setText("" + order.name);
        txtAddress.setText("" + order.address);
        txtEmail.setText("" + order.email);
        txtMobile.setText("" + order.mobile);

//        Linkify.addLinks(txtEmail, Linkify.EMAIL_ADDRESSES | Linkify.MAP_ADDRESSES);
//        Linkify.addLinks(txtMobile, Linkify.PHONE_NUMBERS);

        return view;
    }
}
