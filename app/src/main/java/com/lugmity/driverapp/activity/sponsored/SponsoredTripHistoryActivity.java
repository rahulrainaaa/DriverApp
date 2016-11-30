package com.lugmity.driverapp.activity.sponsored;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Sponsored Driver: Trip History Date Select Activity.
 */
public class SponsoredTripHistoryActivity extends AppCompatActivity {

    private TextView fromDate = null;
    private TextView toDate = null;
    private DatePickerDialog datePickerDialog = null;
    private DatePickerDialog datePickerDialog2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        fromDate = (TextView) findViewById(R.id.from_date);
        toDate = (TextView) findViewById(R.id.to_date);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat df = new SimpleDateFormat("dd,MM,yyyy");
        String formattedDate = df.format(c.getTime());
        System.out.println(formattedDate);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String strMonthOfYear = String.format(Locale.ENGLISH, "%02d", monthOfYear + 1);
                String strDayOfMonth = String.format(Locale.ENGLISH, "%02d", dayOfMonth);
                fromDate.setText(new StringBuilder().append(year).append("-").append(strMonthOfYear).append("-").append(strDayOfMonth));
            }
        }, year, month, day);

        datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String strMonthOfYear = String.format(Locale.ENGLISH, "%02d", monthOfYear + 1);
                String strDayOfMonth = String.format(Locale.ENGLISH, "%02d", dayOfMonth);
                toDate.setText(new StringBuilder().append(year).append("-").append(strMonthOfYear).append("-").append(strDayOfMonth));
            }
        }, year, month, day);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fromDateS, toDateS;
                fromDateS = fromDate.getText().toString();
                toDateS = toDate.getText().toString();
                if (fromDateS.length() == 0) {
                    Toast.makeText(SponsoredTripHistoryActivity.this, getResources().getString(R.string.pick_from_date), Toast.LENGTH_SHORT).show();
                } else if (toDateS.length() == 0) {
                    Toast.makeText(SponsoredTripHistoryActivity.this, getResources().getString(R.string.pick_to_date), Toast.LENGTH_SHORT).show();
                } else {
                    Constants.httpJsonRequest = new JSONObject();
                    try {
                        Constants.httpJsonRequest.put("startdate", "" + fromDateS.trim());
                        Constants.httpJsonRequest.put("enddate", "" + toDateS.trim());
                        Constants.httpJsonRequest.put("status", "delivered");
                        Constants.httpJsonRequest.put("loginid", Constants.LOGIN_ID.trim());
                        startActivity(new Intent(SponsoredTripHistoryActivity.this, SponsoredHistoryActivity.class));
                    } catch (JSONException jsonE) {
                        Toast.makeText(SponsoredTripHistoryActivity.this, "JSONException while request packing", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
