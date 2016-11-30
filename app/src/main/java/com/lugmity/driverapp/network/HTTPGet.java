package com.lugmity.driverapp.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.interfaces.HTTPCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;


/**
 * @class HTTPGet
 * @desc Class for handling HTTP work - Request/Response (Simple-HTTP).
 */
public class HTTPGet extends AsyncTask<String, String, String> {
    //Objects for Request.
    public int code = -1;
    public String URL = "http://...";
    public String METHOD = "GET";
    public HTTPCallback httpCallback = null;
    private ProgressDialog progressDialog = null;
    private Activity activity = null;

    //Objects for Response.
    public int STATUS_CODE = -1;
    public String STATUS_MESSAGE = "EXCEPTION";

    public void setData(HTTPCallback httpCallback, Activity activity, String METHOD, String URL, int code) {
        this.httpCallback = httpCallback;
        this.URL = URL;
        this.METHOD = METHOD;
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
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection;
        InputStream inputStream = null;
        try {
            java.net.URL url = new java.net.URL(this.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setRequestMethod("GET");
            connection.connect();
            STATUS_CODE = connection.getResponseCode();
            STATUS_MESSAGE = connection.getResponseMessage();
            if (STATUS_CODE == 200) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            } else {
                return null;
            }

        } catch (SocketTimeoutException iioE) {
            iioE.printStackTrace();
            this.STATUS_CODE = -7;
            this.STATUS_MESSAGE = "Socket Timeout";
            return null;
        } catch (InterruptedIOException iioE) {
            iioE.printStackTrace();
            this.STATUS_CODE = -6;
            this.STATUS_MESSAGE = "Connection Timeout";
            return null;
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
        httpCallback = null;
        STATUS_MESSAGE = null;
        super.finalize();
        System.gc();
    }
}