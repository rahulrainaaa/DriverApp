package com.lugmity.driverapp.activity.driver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Change password for any Driver (Sponsored or Freelance Driver)
 */
public class ChangePasswordActivity extends AppCompatActivity implements HTTPCallback {

    Button btnSave;
    EditText etNewPasswd, etCfmPasswd, etOldPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOldPasswd = (EditText) findViewById(R.id.et_oldpasswd);
        etNewPasswd = (EditText) findViewById(R.id.et_newpasswd);
        etCfmPasswd = (EditText) findViewById(R.id.et_cfmpasswd);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONObject json = null;
                if ((json = checkValidation()) != null) {
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(ChangePasswordActivity.this, ChangePasswordActivity.this, "POST", Network.URL_CHANGE_PASS, json.toString(), 3);
                    httpTask.execute("");
                }
            }
        });
    }

    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        JSONObject resp = null;
        try {
            resp = new JSONObject(data.trim());
            int sCode = resp.getInt("statusCode");
            String sMsg = resp.getString("statusMessage");
            if (sCode == 200) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                finish();
            } else if (sCode == 204) {
                Toast.makeText(getApplicationContext(), sMsg, Toast.LENGTH_SHORT).show();
            } else if (sCode == 400) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_request), Toast.LENGTH_SHORT).show();
            } else if (sCode == 401) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unauthorized_request), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unknown_response_status), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException jsonE) {
            Toast.makeText(getApplicationContext(), "JSONException while parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(getApplicationContext(), statusMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(getApplicationContext(), statusMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return JSONObject if valid data, else null.
     * @method checkValidation
     * @desc Method to check for field valications and
     */
    public JSONObject checkValidation() {
        String oldPassword = etOldPasswd.getText().toString().trim();
        String newPassword = etNewPasswd.getText().toString().trim();
        String cnfPassword = etCfmPasswd.getText().toString().trim();

        boolean chkNew = Constants.patternUsernamePassword.matcher("" + newPassword).matches();

        int flagValid = 0;

        if (oldPassword.isEmpty()) {
            etOldPasswd.setError(getResources().getString(R.string.cannot_be_empty));
            flagValid = 1;
        }
        if (newPassword.isEmpty()) {
            etNewPasswd.setError(getResources().getString(R.string.cannot_be_empty));
            flagValid = 2;
        }
        if (!newPassword.equals(cnfPassword)) {
            etCfmPasswd.setError(getResources().getString(R.string.confirm_password_not_matching));
            flagValid = 3;
        }
        if (!chkNew) {
            etNewPasswd.setError(getResources().getString(R.string.enter_valid_password));
            flagValid = 3;
        }
        if (flagValid != 0) {
            return null;
        }
        JSONObject json = new JSONObject();
        String strLoginId = "";
        if (Constants.strUserType.trim().contains("freelance")) {
            strLoginId = Constants.freelanceUserData.respLoginId;
        } else if (Constants.strUserType.trim().contains("sponsored")) {
            strLoginId = Constants.sponsoredUserData.respLoginId;
        }
        try {
            json.put("oldpassword", oldPassword);
            json.put("newpassword", newPassword);
            json.put("loggedin_id", strLoginId.trim());
            json.put("pass_key", Network.AUTH_CHANGE_PASS);
        } catch (JSONException e) {
            json = null;
            Toast.makeText(getApplicationContext(), "Exception: JSONObject request packing error.", Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception e) {
            json = null;
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return json;
    }

}
