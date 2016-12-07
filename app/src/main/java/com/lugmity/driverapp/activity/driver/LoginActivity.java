package com.lugmity.driverapp.activity.driver;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.activity.freelance.FreelanceDashbordActivity;
import com.lugmity.driverapp.activity.freelance.FreelanceRegisterActivity;
import com.lugmity.driverapp.activity.freelance.TermsActivity;
import com.lugmity.driverapp.activity.sponsored.SponsoredDashboardActivity;
import com.lugmity.driverapp.adapter.LanguageAdapter;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.responseparser.LoginResponseParser;
import com.lugmity.driverapp.utils.AppLocalization;
import com.lugmity.driverapp.utils.CacheHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Login Screen and language change - Activity.
 */
public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, HTTPCallback, RadioGroup.OnCheckedChangeListener {

    private EditText etUserName = null;
    private EditText etPassword = null;
    private TextView txtTnc = null;
    private Button btnLogin = null;
    private Spinner spLang = null;
    private LanguageAdapter adapter = null;
    private ArrayList<String> list = null;
    private RadioGroup radioGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        spLang = (Spinner) findViewById(R.id.sp_language);
        radioGroup = (RadioGroup) findViewById(R.id.rg_lang);
        txtTnc = (TextView)findViewById(R.id.txttnc);
        list = new ArrayList<String>();
        String strArabic = getResources().getString(R.string.arabic);
        String strEnglish = getResources().getString(R.string.english);
        if (Constants.strLanguage.contains("arabic")) {
            list.add(strArabic);
            list.add(strEnglish);
        } else {
            list.add(strEnglish);
            list.add(strArabic);
        }
        adapter = new LanguageAdapter(this, list);
        spLang.setAdapter(adapter);
        spLang.setOnItemSelectedListener(this);
        btnLogin.setOnClickListener(this);
        txtTnc.setOnClickListener(this);
        Constants.strUserType = "freelance";
        radioGroup.setOnCheckedChangeListener(this);
        CacheHandler.getInstance().clearCache(this);
        Constants.LOGIN_FLAG = 1;
    }

    /**
     * Spinner item selection event handling methods implemented.
     **/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (Constants.start > 0) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.changing_language), Toast.LENGTH_SHORT).show();
            Constants.strLanguage = list.get(position).toLowerCase().trim();
            AppLocalization.getInstance().changeLocale(this);
            startActivity(new Intent(this, SplashActivity.class));
            finish();
            return;
        } else {
            Constants.start = 1;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * RadioGroup check change listener method implemented.
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_freelancer:

                Constants.strUserType = "freelance";
                break;

            case R.id.radio_sponsored:

                Constants.strUserType = "sponsored";
                break;
        }
    }

    /**
     * View OnclickListener interface callback method implemented.
     **/
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.txttnc:

                startActivity(new Intent(this, TermsActivity.class));
                break;

            default:

                String strUserName = etUserName.getText().toString().trim();
                String strPassword = etPassword.getText().toString().trim();

                //check for input field validations.
                boolean chkUser = Constants.patternUsernamePassword.matcher("" + strUserName).matches();
                boolean chkPass = Constants.patternUsernamePassword.matcher("" + strPassword).matches();
                if (strUserName.isEmpty()) {
                    etUserName.setError(getResources().getString(R.string.cannot_be_empty));
                    etPassword.setError(null);
                    return;
                } else if (!chkUser) {
                    etUserName.setError(getResources().getString(R.string.enter_valid_data));
                    etPassword.setError(null);
                    return;
                } else if (strPassword.isEmpty()) {
                    etUserName.setError(null);
                    etPassword.setError(getResources().getString(R.string.cannot_be_empty));
                    return;
                } else if (!chkPass) {
                    etPassword.setError(getResources().getString(R.string.enter_valid_data));
                    etUserName.setError(null);
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("lusername", strUserName);
                    json.put("lpassword", strPassword);
                    json.put("lusertype", Constants.strUserType);
                    json.put("lpass_key", Network.AUTH_LOGIN);
                } catch (JSONException jsonE) {
                    Toast.makeText(this, "JSON request packing error", Toast.LENGTH_SHORT).show();
                }
                HTTPTask httpTask = new HTTPTask();
                httpTask.setData(this, this, "POST", Network.URL_LOGIN, json.toString(), 1);
                httpTask.execute("");
                break;
        }

    }

    /**
     * @param view View clicked
     * @method gotoRegister
     * @desc Register TextView Clicked event method.
     **/
    public void gotoRegister(View view) {
        startActivity(new Intent(LoginActivity.this, FreelanceRegisterActivity.class));
    }

    /**
     * HTTPTask Interface Callback methods.
     **/
    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        JSONObject resp = null;
        try {
            resp = new JSONObject(data.trim());
            int sCode = resp.getInt("statusCode");
            String sMsg = resp.getString("statusMessage");
            if (sCode == 200) {
                JSONObject json = resp.getJSONObject("data");

                LoginResponseParser loginResponseParser = new LoginResponseParser();
                int parseStatus = -1;
                if (Constants.strUserType.contains("freelance")) {
                    parseStatus = loginResponseParser.parserFreelancerResponse(json);
                } else if (Constants.strUserType.contains("sponsored")) {
                    parseStatus = loginResponseParser.parserSponsoredResponse(json);
                }

                if (parseStatus == 0) {
                    if (Constants.strUserType.contains("freelance")) {
                        CacheHandler.getInstance().setFreelancerCache(this);
                        startActivity(new Intent(LoginActivity.this, FreelanceDashbordActivity.class));
                        finish();
                    } else if (Constants.strUserType.contains("sponsored")) {
                        CacheHandler.getInstance().setSponsoredCache(this);
                        startActivity(new Intent(LoginActivity.this, SponsoredDashboardActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login Response Parsing Error\nERRORCODE=" + parseStatus, Toast.LENGTH_SHORT).show();
                }

            } else if (sCode == 203) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_inactive), Toast.LENGTH_SHORT).show();
            } else if (sCode == 204) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
            } else if (sCode == 400) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_request), Toast.LENGTH_SHORT).show();
            } else if (sCode == 401) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unauthorized_request), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unknown_response_status), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException jsonE) {
            Toast.makeText(getApplicationContext(), "JSONException while parsing response.", Toast.LENGTH_SHORT).show();
        }
        etPassword.setText("");
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        etPassword.setText("");
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        etPassword.setText("");
        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show();
    }
}