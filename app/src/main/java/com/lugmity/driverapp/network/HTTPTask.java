package com.lugmity.driverapp.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.interfaces.HTTPCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @class HTTPTask
 * @desc Class for handling network task- Request/Response (REST-HTTP)
 */
public class HTTPTask extends AsyncTask<String, String, String> {
    //Objects for Request.
    public int code = -1;
    public String URL = "http://...";
    public String METHOD = "POST";
    public String JREQUEST = null;
    public HTTPCallback httpCallback = null;
    private ProgressDialog progressDialog = null;
    private Activity activity = null;

    //Objects for Response.
    public int STATUS_CODE = -1;
    public String STATUS_MESSAGE = "EXCEPTION";

    public void setData(HTTPCallback httpCallback, Activity activity, String METHOD, String URL, String JREQUEST, int code) {
        this.httpCallback = httpCallback;
        this.URL = URL;
        this.METHOD = METHOD;
        this.JREQUEST = JREQUEST;
        this.code = code;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getResources().getString(R.string.connecting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("HttpTask", JREQUEST.toString());
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            //Create HTTP URL Connection.
            URL url = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod(METHOD);

            //Create output stream and write data-request.
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(JREQUEST.toString());
            writer.flush();
            writer.close();
            os.close();

            //Check the connectivity and get Response.
            urlConnection.connect();
            STATUS_CODE = urlConnection.getResponseCode();
            STATUS_MESSAGE = urlConnection.getResponseMessage();
            if (STATUS_CODE == 200) {
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    dta.append(chunks);
                }
                return dta.toString();
            } else {
                return null;
            }
        } catch (ProtocolException pE) {
            pE.printStackTrace();
            this.STATUS_CODE = -5;
            this.STATUS_MESSAGE = "Protocol Exception";
            return null;
        } catch (UnsupportedEncodingException usE) {
            usE.printStackTrace();
            this.STATUS_CODE = -4;
            this.STATUS_MESSAGE = "Unsupported Encoding Exception";
            return null;
        } catch (MalformedURLException murlE) {
            murlE.printStackTrace();
            this.STATUS_CODE = -3;
            this.STATUS_MESSAGE = "Malformed URL Exception";
            return null;
        } catch (IOException ioE) {
            ioE.printStackTrace();
            this.STATUS_CODE = -2;
            this.STATUS_MESSAGE = "Connection Failed";
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            this.STATUS_CODE = -1;
            this.STATUS_MESSAGE = e.getMessage().trim();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String data) {
        if (data != null) {
            Log.d("Server API DATA", data);
            Log.d("Server Response Code", "" + STATUS_CODE);
            Log.d("Server Response Message", STATUS_MESSAGE);
        }
        progressDialog.dismiss();
        progressDialog = null;
        if (STATUS_CODE == 200)         //Server Success response
        {
            this.httpCallback.onSuccess(STATUS_CODE, STATUS_MESSAGE, data, code);
        } else if (STATUS_CODE < 0)     //Exceptions while processing
        {
            this.httpCallback.onError(STATUS_CODE, STATUS_MESSAGE, code);
        } else                          //Other Response from server
        {
            this.httpCallback.onFailure(STATUS_CODE, STATUS_MESSAGE, code);
        }
        super.onPostExecute(STATUS_MESSAGE);
    }

    @Override
    protected void finalize() throws Throwable {
        URL = null;
        METHOD = null;
        JREQUEST = null;
        httpCallback = null;
        STATUS_MESSAGE = null;
        super.finalize();
        System.gc();
    }
}
