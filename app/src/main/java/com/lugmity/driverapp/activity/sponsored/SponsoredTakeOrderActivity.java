package com.lugmity.driverapp.activity.sponsored;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.driver.GivenPositionMapActivity;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.database.SQLiteHandler;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.AlertDialogUtil;
import com.lugmity.driverapp.utils.GeoUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Sponsored Driver: Pick the open order for deliver and make it pending to same freelance driver for delivery.
 * Also save this order offline in SQLite.
 */
public class SponsoredTakeOrderActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

    private TextView txtName, txtAddress, txtMobile, txtEmail;
    private Button btnMap, btnPick;
    private Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsored_take_order);
        txtName = (TextView) findViewById(R.id.cust_name);
        txtAddress = (TextView) findViewById(R.id.cust_add);
        txtMobile = (TextView) findViewById(R.id.cust_mobile);
        txtEmail = (TextView) findViewById(R.id.cust_email);
        btnPick = (Button) findViewById(R.id.btn_pick);
        btnMap = (Button) findViewById(R.id.btn_map);

        order = Constants.order;
        txtName.setText("" + order.name.trim());
        txtAddress.setText("" + order.address.trim());
        txtMobile.setText("" + order.mobile.trim());
        txtEmail.setText("" + order.email.trim());

        btnPick.setOnClickListener(this);
        btnMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pick:

                Snackbar.make(v, getResources().getString(R.string.are_you_sure_q), Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.pick_order), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HTTPTask httpTask = new HTTPTask();
                        String hitURL = "" + Network.LURL_ACCEPT_ORDER + Constants.order.orderId + "?" + Network.L_TOKEN_KEY;
                        httpTask.setData(SponsoredTakeOrderActivity.this, SponsoredTakeOrderActivity.this, "POST", hitURL, "{}", 1);
                        httpTask.execute("");
                    }
                }).show();
                break;

            case R.id.btn_map:

                Constants.POINT_DEST = GeoUtils.parseLatLng(order.google.trim());
                startActivity(new Intent(this, GivenPositionMapActivity.class));
                break;

            default:
                Toast.makeText(this, "Unhandled onClickEvent", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        if (code == 1) {
            try {
                JSONObject resp = new JSONObject(data.trim());
                int sCode = resp.getInt("statusCode");
                String sMsg = resp.getString("statusMessage");
                if (sCode == 200) {
                    String done = resp.getString("data");
                    if (done.contains("done")) {
                        //Add Order to Drushti database.
                        Order order = Constants.order;
                        JSONObject json = new JSONObject();
                        json.put("orderid", order.orderId);
                        json.put("distance", "0");
                        json.put("price", "0");
                        json.put("currency", "SA");
                        json.put("points", "0");
                        json.put("status", "Pending");
                        json.put("loginid", Constants.LOGIN_ID);
                        HTTPTask httpTask = new HTTPTask();
                        httpTask.setData(this, this, "POST", Network.URL_F_ORDER_ASSIGN, json.toString(), 2);
                        httpTask.execute("");
                    } else {
                        AlertDialogUtil.showAlertDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.contact_owner));
                    }
                } else {
                    AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.alert), "" + sCode + ":" + sMsg.trim());
                    return;
                }
            } catch (JSONException jsonE) {
                jsonE.printStackTrace();
                Toast.makeText(this, "Exception: " + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (code == 2) {
            try {
                JSONObject json = new JSONObject(data.trim());
                int sCode = json.getInt("statusCode");
                String sMsg = json.getString("statusMessage");
                if (sCode == 200) {
                    Toast.makeText(this, getResources().getString(R.string.order_successfully_assigned_to_you), Toast.LENGTH_SHORT).show();
                    //ordid, orderno, ordername, customer, address, geo, mob, email, distance, price, points, cur, status, sync
                    SQLiteDatabase sqlite = SQLiteHandler.getInstance().getDatabase();
                    SQLiteStatement stmt = sqlite.compileStatement(Constants.SQL_INSERT_ORDER.trim());
                    stmt.bindString(1, order.ordid.trim()); //ordid
                    stmt.bindString(2, order.orderId.trim()); //orderno
                    stmt.bindString(3, order.title.trim()); //ordername
                    stmt.bindString(4, order.name.trim()); //customer
                    stmt.bindString(5, order.address.trim()); //address
                    stmt.bindString(6, order.google.trim()); //geo
                    stmt.bindString(7, order.mobile.trim()); //mob
                    stmt.bindString(8, order.email.trim()); //email
                    stmt.bindString(9, ""); //distance
                    stmt.bindString(10, ""); //price
                    stmt.bindString(11, ""); //points
                    stmt.bindString(12, ""); //cur
                    stmt.bindString(13, "pending"); //status
                    stmt.bindString(14, ""); //sync
                    int sql = stmt.executeUpdateDelete();
                    Toast.makeText(this, getResources().getString(R.string.order_updated), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "" + sCode + "." + sMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException jsonE) {
                Toast.makeText(this, "Exception: " + jsonE.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.failed), "" + statusMessage);
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        AlertDialogUtil.showErrorDialog(this, getResources().getString(R.string.error), "" + statusMessage);
    }
}
