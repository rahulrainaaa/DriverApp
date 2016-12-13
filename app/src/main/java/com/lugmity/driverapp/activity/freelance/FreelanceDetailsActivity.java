package com.lugmity.driverapp.activity.freelance;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
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
 * Show freelance driver details. (My Details Page with profile image)
 */
public class FreelanceDetailsActivity extends AppCompatActivity implements HTTPCallback {

    TextView txtName, txtAddress, txtGender, txtMobile, txtEmail, txtDob;
    ImageView img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);

        txtName = (TextView) findViewById(R.id.my_details_name);
        txtAddress = (TextView) findViewById(R.id.my_details_address);
        txtGender = (TextView) findViewById(R.id.my_details_gender);
        txtMobile = (TextView) findViewById(R.id.my_details_mobile);
        txtEmail = (TextView) findViewById(R.id.my_details_email);
        txtDob = (TextView) findViewById(R.id.my_details_dob);
        img = (ImageView) findViewById(R.id.img_icard);

        txtName.setText(Constants.freelanceUserData.respUserName);
        txtAddress.setText(Constants.freelanceUserData.respFAddress);
        txtGender.setText(Constants.freelanceUserData.respFGender);
        txtMobile.setText(Constants.freelanceUserData.respCountryCode + "" + Constants.freelanceUserData.respFMobile);
        txtEmail.setText(Constants.freelanceUserData.respFEmail);
        txtDob.setText(Constants.freelanceUserData.respFDob);

//        try {
//            JSONObject json = new JSONObject();
//            json.put("fid", "" + Constants.freelanceUserData.respFId);
//            HTTPTask httpGetImage = new HTTPTask();
//            httpGetImage.setData(this, this, "POST", Network.URL_GET_IMG, json.toString(), 1);
//            httpGetImage.execute("");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error while packing getImage request", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        try {
            JSONObject json = new JSONObject(data);
            int apiCode = json.getInt("statusCode");
            String apiMsg = json.getString("statusMessage");
            if (apiCode == 200) {
                String encodedImage = json.getString("profilepic");
                byte[] imageAsBytes = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
                img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            } else {
                AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.unable_to_fetch_image));
            }
        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
            Toast.makeText(this, "" + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            if (e.getMessage().contains("base-64")) {
                Toast.makeText(this, "" + getResources().getString(R.string.image_decode_exception), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }
}