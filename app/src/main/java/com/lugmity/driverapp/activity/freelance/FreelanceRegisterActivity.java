package com.lugmity.driverapp.activity.freelance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.adapter.LanguageAdapter;
import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.interfaces.HTTPCallback;
import com.lugmity.driverapp.network.HTTPTask;
import com.lugmity.driverapp.network.Network;
import com.lugmity.driverapp.utils.DateUtils;
import com.lugmity.driverapp.utils.PermissionCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Register freelance driver - Activity Class
 */
public class FreelanceRegisterActivity extends AppCompatActivity implements View.OnClickListener, HTTPCallback {

    private EditText etUserName, etPassword, etCnfPassword, etName, etMobile,  etEmail, etAddress, etIdNo;
    //private EditText etMobileCode;
    private TextView txtDob;
    private Spinner spGender;
    private Button btnUploadId, btnRegister;
    private ImageView imgIDCard;
    private LanguageAdapter adapter = null;
    private ArrayList<String> list = null;
    private DatePickerDialog datePickerDialog = null;
    private String strGender = "";
    private String strImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etCnfPassword = (EditText) findViewById(R.id.et_cnf_password);
        etName = (EditText) findViewById(R.id.et_name);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        //etMobileCode = (EditText) findViewById(R.id.et_mobile_code);
        etEmail = (EditText) findViewById(R.id.et_email);
        txtDob = (TextView) findViewById(R.id.txt_dob);
        etAddress = (EditText) findViewById(R.id.et_address);
        etIdNo = (EditText) findViewById(R.id.et_idno);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnUploadId = (Button) findViewById(R.id.btn_upload);
        spGender = (Spinner) findViewById(R.id.sp_gender);
        imgIDCard = (ImageView) findViewById(R.id.img_icard);

        list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                // set selected date into textview
                txtDob.setText(new StringBuilder().append(selectedDay).append("-")
                        .append(DateUtils.getMonthName(selectedMonth)).append("-").append(selectedYear));

            }
        }, 1995, 1, 1);

        adapter = new LanguageAdapter(this, list);

        spGender.setAdapter(adapter);

        //Gender selection spinner
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strGender = "" + list.get(position).trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtDob.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnUploadId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_dob:

                //datePickerDialog.show();
                DialogFragment dialogfragment = new DatePickerDialogTheme();

                dialogfragment.show(getFragmentManager(), "Theme");
                break;
            case R.id.btn_upload:

                if (!PermissionCheck.getInstance().checkStorageAccessPermission(this)) {
                    return;
                }
                pickFile();
                break;
            case R.id.btn_register:

                JSONObject json = null;
                if ((json = checkValidation(view)) != null) {
                    HTTPTask httpTask = new HTTPTask();
                    httpTask.setData(this, this, "POST", Network.URL_REGISTER, json.toString(), 1);
                    httpTask.execute("");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    strImage = getPath(selectedImageUri);
                    File file = new File(strImage);
                    int file_size = Integer.parseInt(String.valueOf(file.length() / (1024)));
                    Log.d("Image Size", "Uploaded image file size = " + file_size + "KB");
                    if (file_size > 1200) {
                        Toast.makeText(this, getResources().getString(R.string.image_file_kb), Toast.LENGTH_SHORT).show();
                        selectedImageUri = null;
                        file = null;
                        file_size = 0;
                        strImage = "";
                        return;
                    }
                    Bitmap bmp = BitmapFactory.decodeFile(strImage.trim());
                    imgIDCard.setImageBitmap(bmp);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_attached), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * @method pickFile
     * @desc Method to pick file from SDCard.
     */

    private void pickFile() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, 1);
    }

    /**
     * @method getPath
     * @desc helper method to retrieve the path of an image URI
     */
    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    /**
     * @return JSONObject request if valid data, else null.
     * @method checkValidation
     * @desc Method to check for field valications and request building.
     */
    public JSONObject checkValidation(View view) {
        //Get data from fields.
        String strUserName = etUserName.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();
        String strCnfPassword = etCnfPassword.getText().toString().trim();
        String strName = etName.getText().toString().trim();
        String strMobile = etMobile.getText().toString().trim();
        //String strMobileCode = etMobileCode.getText().toString().trim();
        String strEmail = etEmail.getText().toString().trim();
        String strDOB = txtDob.getText().toString().trim();
        String strAddress = etAddress.getText().toString().trim();
        String strIdNo = etIdNo.getText().toString().trim();

        //Reset all errors.
        clearAllErrors();
        int flgValid = 0;

        //Check username
        if (strUserName.isEmpty()) {
            flgValid = 1;
            etUserName.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (!Constants.patternUsernamePassword.matcher("" + strUserName).matches()) {
            flgValid = 1;
            etUserName.setError(getResources().getString(R.string.enter_valid_data));
        }
        //check password
        if (strPassword.isEmpty()) {
            flgValid = 2;
            etPassword.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (!Constants.patternUsernamePassword.matcher("" + strPassword).matches()) {
            flgValid = 2;
            etPassword.setError(getResources().getString(R.string.enter_valid_data));
        }
        //match confirm password
        else if (!strPassword.equals(strCnfPassword)) {
            flgValid = 3;
            etCnfPassword.setError(getResources().getString(R.string.confirm_password_not_matching));
        }
        //check name
        if (strName.isEmpty()) {
            flgValid = 3;
            etName.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (!Constants.patternFullName.matcher("" + strName).matches()) {
            flgValid = 4;
            etName.setError(getResources().getString(R.string.enter_valid_data));
        }
        //check address
        if (strAddress.trim().isEmpty()) {
            flgValid = 5;
            etAddress.setError(getResources().getString(R.string.cannot_be_empty));
        }
        //check IdNo
        if (strIdNo.trim().isEmpty()) {
            flgValid = 6;
            etIdNo.setError(getResources().getString(R.string.cannot_be_empty));
        }
        //Check mobile fields
        if (strMobile.isEmpty()) {
            flgValid = 7;
            etMobile.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (!Constants.patternMobile.matcher("" + strMobile).matches()) {
            flgValid = 7;
            etMobile.setError(getResources().getString(R.string.enter_valid_data));
        }
        else if(strMobile.length()!=10) {
            flgValid = 7;
            etMobile.setError(getResources().getString(R.string.minimum_ten));
        }

        //Check country-code fields
        /*if (strMobileCode.isEmpty()) {
            flgValid = 8;
            etMobileCode.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (!Constants.patternCountryCode.matcher("" + strMobileCode).matches()) {
            flgValid = 8;
            etMobileCode.setError(getResources().getString(R.string.enter_valid_data));
        }*/

        //other validation checks
        if (strDOB.isEmpty()) {
            flgValid = 9;
            txtDob.setError(getResources().getString(R.string.cannot_be_empty));
        }
        if (strEmail.isEmpty()) {
            flgValid = 10;
            etEmail.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (!Constants.patternEmail.matcher("" + strEmail).matches()) {
            flgValid = 10;
            etEmail.setError(getResources().getString(R.string.enter_valid_data));
        }
        //check image
        if (strImage.trim().isEmpty() && (flgValid == 0)) {
            //flgValid = 11;
            Snackbar.make(view, getResources().getString(R.string.id_proof_image_required), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.add_q), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickFile();
                    //Toast.makeText(getApplicationContext(), "Upload under development.", Toast.LENGTH_SHORT).show();
                }
            }).show();
            flgValid = 11;
        }
        //Check if any validation error then return null.
        if (flgValid != 0) {
            return null;
        }

        //Prepare the request
        JSONObject json = new JSONObject();
        try {
            File file = new File(strImage.trim());
            FileInputStream fin = new FileInputStream(file);
            byte[] bFile = new byte[(int) file.length()];
            fin.read(bFile);
            String encodedImage = new String(Base64.encode(bFile, Base64.DEFAULT));
            fin.close();
            json.put("pass_key", Network.AUTH_REGISTER);
            json.put("fname", strName);
            json.put("fgender", strGender);
            json.put("fmobile", strMobile.trim());
            json.put("countrycode", "");
            json.put("femail", strEmail);
            json.put("faddress", strAddress);
            json.put("fidno", strIdNo);
            json.put("fdob", strDOB);
            json.put("fphoto", "");
            json.put("username", strUserName);
            json.put("password", strPassword);
            json.put("usertype", "freelance");
            json.put("profilepic", encodedImage.toString());
        } catch (JSONException jsonE) {
            json = null;
            Toast.makeText(getApplicationContext(), "JSONException while request packing", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException ioE) {
            json = null;
            Toast.makeText(getApplicationContext(), "IOException: File Handling Error", Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception e) {
            json = null;
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return json;
    }

    /**
     * @method clearAllErrors
     * @desc Method to clear all editText Error messages.
     */
    private void clearAllErrors() {
        etUserName.setError(null);
        etPassword.setError(null);
        etCnfPassword.setError(null);
        etName.setError(null);
        etMobile.setError(null);
        etEmail.setError(null);
        txtDob.setError(null);
        etAddress.setError(null);
        etIdNo.setError(null);
    }

    /**
     * HttpCallback from HttpTask- Interface methods implemented.
     */
    @Override
    public void onSuccess(int statusCode, String statusMessage, String data, int code) {
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            int sCode = json.getInt("statusCode");
            String sMsg = json.getString("statusMessage");
            if (sCode == 200) {
                Toast.makeText(this, "Successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else if (sCode == 204) {
                Toast.makeText(getApplicationContext(), sMsg.trim(), Toast.LENGTH_SHORT).show();
            } else if (sCode == 400) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_request), Toast.LENGTH_SHORT).show();
            } else if (sCode == 401) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.unauthorized_request), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "" + statusMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int statusCode, String statusMessage, int code) {
        Toast.makeText(FreelanceRegisterActivity.this, statusMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int statusCode, String statusMessage, int code) {
        Toast.makeText(FreelanceRegisterActivity.this, statusMessage, Toast.LENGTH_SHORT).show();
    }

            public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener{

             @Override
             public Dialog onCreateDialog(Bundle savedInstanceState){
             final Calendar calendar = Calendar.getInstance();
             int year = calendar.get(Calendar.YEAR);
             int month = calendar.get(Calendar.MONTH);
             int day = calendar.get(Calendar.DAY_OF_MONTH);



         DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
             AlertDialog.THEME_HOLO_LIGHT,this,1995,1,1);




             return datepickerdialog;
             }

             public void onDateSet(DatePicker view, int year, int month, int day){

             TextView textview = (TextView)getActivity().findViewById(R.id.txt_dob);

                 textview.setText(day + "-" + (DateUtils.getMonthName(month)) + "-" + year);

             }
             }
}