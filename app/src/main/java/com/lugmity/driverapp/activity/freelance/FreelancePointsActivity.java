package com.lugmity.driverapp.activity.freelance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.AlertDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Show Points status for freelancer driver.
 */
public class FreelancePointsActivity extends AppCompatActivity implements HTTPCallback {

    TextView txtPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        txtPoints = (TextView) findViewById(R.id.points);

        try {
            JSONObject json = new JSONObject();
            json.put("loginid", Constants.LOGIN_ID.trim());
            HTTPTask httpTask = new HTTPTask();
            httpTask.setData(this, this, "POST", Network.URL_GET_POINTS, json.toString(), 1);
            httpTask.execute("");
        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
            Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        try {
            JSONObject json = new JSONObject(data.trim());
            int sCode = json.getInt("statusCode");
            String sMsg = json.getString("statusMessage");
            if (sCode == 200) {
                int totalPoints = json.getInt("points");
                txtPoints.setText("" + totalPoints);
            } else {
                AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), "" + sCode + ":" + sMsg + "");
            }

        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
            Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.failed), "" + statusCode + ":" + statusMessage + "");
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.error), "" + statusMessage + "");
    }
}