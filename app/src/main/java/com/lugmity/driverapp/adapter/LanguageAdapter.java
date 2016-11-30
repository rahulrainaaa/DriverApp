package com.lugmity.driverapp.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lugmity.driverapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * @Class LanguageAdapter
 * @desc Spinner-Adapter class for language selection with spinner.
 */
public class LanguageAdapter extends ArrayAdapter<String> {

    private Activity activity = null;
    private ArrayList<String> list = null;

    public LanguageAdapter(Activity activity, ArrayList<String> list)
    {
        super(activity, R.layout.item_sp_lang, list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView textView = new TextView(activity);
        textView.setText("" + list.get(position).trim());
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent)
    {
        View row = activity.getLayoutInflater().inflate(R.layout.item_sp_lang, parent, false);
        TextView textView = (TextView)row.findViewById(R.id.text_sp_item);
        textView.setTextColor(Color.DKGRAY);
        textView.setTextSize(20f);
        textView.setGravity(Gravity.LEFT);
        textView.setPadding(20, 20, 8, 20);
        textView.setText(list.get(position).toString());
        return row;
    }

}
